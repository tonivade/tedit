package tk.tomby.tedit.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tk.tomby.tedit.messages.StatusMessage;
import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.ScriptingManager;
import tk.tomby.tedit.services.TaskManager;
import tk.tomby.tedit.services.WorkspaceManager;

public class RunScriptAction extends AbstractAction {
	
	public RunScriptAction() {
		super("run-script");
	}

	public void actionPerformed(ActionEvent evt) {
		MessageManager.sendMessage(MessageManager.STATUS_GROUP_NAME,
                new StatusMessage(this, "RunScriptAction"));

		JFileChooser chooser = new JFileChooser();
		int retVal           = chooser.showOpenDialog(WorkspaceManager.getMainFrame());
		
		if (retVal == JFileChooser.APPROVE_OPTION) {
			try {
				File script = chooser.getSelectedFile();
				final InputStream in = new FileInputStream(script);
				final String lang = ScriptingManager.getLanguage(script.getName());

				if (lang != null) {
					TaskManager.execute(new TaskManager.Task(0, 100, 10) {
			            public void work() {
			                ScriptingManager.exec(lang, in, WorkspaceManager.getCurrentBuffer());
			            }
			        });
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(WorkspaceManager.getMainFrame(), "File not found", "File not found", JOptionPane.ERROR_MESSAGE);
			}			
		}
	}

}
