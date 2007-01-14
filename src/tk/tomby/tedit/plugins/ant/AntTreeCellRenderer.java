package tk.tomby.tedit.plugins.ant;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

public class AntTreeCellRenderer extends JLabel implements TreeCellRenderer {

	//	~ Constructors *******************************************************************************

    /**
     * Creates a new AntTreeCellRenderer object.
     */
    public AntTreeCellRenderer() {
        setOpaque(false);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param selected DOCUMENT ME!
     * @param expanded DOCUMENT ME!
     * @param leaf DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTreeCellRendererComponent(JTree   tree,
                                                  Object  value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int     row,
                                                  boolean hasFocus) {
        Object obj = ((DefaultMutableTreeNode) value).getUserObject();
        
        if (obj instanceof Project) {
        	renderProject(tree, (Project) obj, selected, expanded, leaf, row, hasFocus);
        } else if (obj instanceof Target) {
        	renderTarget(tree, (Target) obj, selected, expanded, leaf, row, hasFocus);
        }

        if (selected) {
            setOpaque(true);
            setBackground(UIManager.getColor("Tree.selectionBackground"));
            setForeground(UIManager.getColor("Tree.selectionForeground"));
        } else {
            setOpaque(false);
            setBackground(UIManager.getColor("Tree.background"));
            setForeground(UIManager.getColor("Tree.foreground"));
        }

        return this;
    }
    
    private void renderProject(JTree   tree,
					    	   Project project,
					           boolean selected,
					           boolean expanded,
					           boolean leaf,
					           int     row,
					           boolean hasFocus) {
    	setText(project.getName());
    	
    	if (expanded) {
            setIcon(UIManager.getIcon("Tree.openIcon"));
        } else {
            setIcon(UIManager.getIcon("Tree.closedIcon"));
        }
	}
    
    private void renderTarget(JTree   tree,
				   	   		  Target  target,
				   	   		  boolean selected,
				              boolean expanded,
				              boolean leaf,
				              int     row,
				              boolean hasFocus) {
    	String defaultTarget = target.getProject().getDefaultTarget();
    	if (defaultTarget != null && target.getName().equals(defaultTarget)) {
    		setText(target.getName() + " (default)");
    	} else {
    		setText(target.getName());
    	}
    	
    	setIcon(UIManager.getIcon("Tree.leafIcon"));
	}
}
