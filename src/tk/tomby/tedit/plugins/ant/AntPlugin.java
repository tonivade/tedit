package tk.tomby.tedit.plugins.ant;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

import tk.tomby.tedit.core.IWorkspace;
import tk.tomby.tedit.gui.ShortedTreeModel;
import tk.tomby.tedit.gui.ToStringComparator;
import tk.tomby.tedit.plugins.AbstractDockablePlugin;
import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.TaskManager;
import tk.tomby.tedit.services.WorkspaceManager;

public class AntPlugin extends AbstractDockablePlugin {
	
	static {
		ResourceManager.loadCategory("ant", "tk/tomby/tedit/plugins/ant");
		PreferenceManager.loadCategory("ant", "tk/tomby/tedit/plugins/ant");
	}
	
	private static Log log = LogFactory.getLog(AntPlugin.class);
	
	private static AntProcessor processor = new AntProcessor();
	
	private JTree projectTree = null;
	
	private ShortedTreeModel projectTreeModel = null;
	
	private DefaultMutableTreeNode projectRoot = new DefaultMutableTreeNode("root");
	
	private Map<String, Project> projects = new HashMap<String, Project>();
	
	public AntPlugin() {
		super();
		
		setLayout(new BorderLayout());
		
		title.setText("Ant");
		location = PreferenceManager.getInt("ant.location", IWorkspace.PLUGIN_LEFT);
		
		projectTreeModel = new ShortedTreeModel(projectRoot, new ToStringComparator());
		projectTree = new JTree(projectTreeModel);
		projectTree.setCellRenderer(new AntTreeCellRenderer());
		projectTree.setRootVisible(false);
		projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		projectTree.setEnabled(true);
		projectTree.setEditable(false);
		projectTree.setShowsRootHandles(true);
		
		JScrollPane projectScroll = new JScrollPane(projectTree);
		projectScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		projectScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		projectScroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
		
		projectTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() >= 2) {
					DefaultMutableTreeNode node = 
						(DefaultMutableTreeNode) projectTree.getLastSelectedPathComponent();

					if (!node.isRoot()) {
						Object obj = node.getUserObject();
						
						if (obj instanceof Project) {
							Project project = (Project) obj;
							processor.execute(project, new String[] { project.getDefaultTarget() });
						} else if (obj instanceof Target) {
							Target target = (Target) obj;
							processor.execute(target.getProject(), new String[] { target.getName() });
						}
					}
				}
			}
		});
		
		DropTarget dropTarget =
	            new DropTarget(projectTree,
	                           new DropTargetAdapter() {
	                    public void drop(DropTargetDropEvent dtde) {
	                        if (log.isDebugEnabled()) {
	                            log.debug("drop start");
	                        }

	                        try {
	                            if (log.isDebugEnabled()) {
	                                log.debug(dtde.getSource());
	                            }

	                            Transferable tr      = dtde.getTransferable();
	                            DataFlavor[] flavors = tr.getTransferDataFlavors();

	                            for (int i = 0; i < flavors.length; i++) {
	                                DataFlavor flavor = flavors[i];

	                                if (log.isDebugEnabled()) {
	                                    log.debug("mime-type:" + flavor.getMimeType());
	                                }

	                                if (flavor.isMimeTypeEqual("text/plain")) {
	                                    final Object obj = tr.getTransferData(flavor);

	                                    if (log.isDebugEnabled()) {
	                                        log.debug(obj);
	                                    }

	                                    if (obj instanceof String) {
	                                        TaskManager.execute(new Runnable() {
	                                        	public void run() {
	                                                addBuildFile((String)obj);
	                                            }
	                                        });
	                                    }

	                                    dtde.dropComplete(true);

	                                    return;
	                                }
	                            }
	                        } catch (UnsupportedFlavorException e) {
	                            log.warn(e.getMessage(), e);
	                        } catch (IOException e) {
	                            log.warn(e.getMessage(), e);
	                        }

	                        dtde.rejectDrop();

	                        if (log.isDebugEnabled()) {
	                            log.debug("drop end");
	                        }
	                    }
	                });

		projectTree.setDropTarget(dropTarget);
		
		projectTree.expandRow(0);
		
		add(title, BorderLayout.NORTH);
        add(projectScroll, BorderLayout.CENTER);
	}

	public void init() {
		WorkspaceManager.addPlugin(WorkspaceManager.PLUGIN_WORKSPACE_POSITION, this);
		
		System.setProperty("ant.home", "/home/tomby/programas/apache-ant-1.6.5");
		System.setProperty("ant.library.dir", "/home/tomby/programas/apache-ant-1.6.5/lib");
	}
	
	public void addBuildFile(String file) {
		addBuildFile(new File(file));
	}
	
	public void addBuildFile(final File file) {
		SwingWorker worker = new SwingWorker<Project, Target>() {

			@Override
			protected Project doInBackground() throws Exception {
				Project project = processor.process(file);
				projects.put(project.getName(), project);
				return project;
			}
			
			@Override
			protected void done() {
				try {
					Project project = get();
					
					DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project);
					projectTreeModel.insertNodeInto(projectNode, projectRoot);
					
					Collection targets = project.getTargets().values();
					for (Iterator i = targets.iterator(); i.hasNext();) {
						Target target = (Target) i.next();
						if (target.getName() != null && !target.getName().equals("")) {
							DefaultMutableTreeNode targetNode = new DefaultMutableTreeNode(target);
							projectTreeModel.insertNodeInto(targetNode, projectNode);
						}
					}
					
					projectTreeModel.nodeStructureChanged(projectRoot);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				} catch (ExecutionException e) {
					log.error(e.getMessage(), e);
				}
			}
		};
		
		TaskManager.execute(worker);
	}

	public void save() {
		PreferenceManager.putInt("ant.location", location);
	}

}
