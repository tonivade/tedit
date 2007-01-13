package tk.tomby.tedit.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import tk.tomby.tedit.core.IBuffer;
import tk.tomby.tedit.messages.StatusMessage;
import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.ScriptingManager;
import tk.tomby.tedit.services.TaskManager;
import tk.tomby.tedit.services.WorkspaceManager;

public class RunBufferAction extends AbstractAction {
	
	public RunBufferAction() {
		super("run-buffer");
	}

	public void actionPerformed(ActionEvent e) {
		MessageManager.sendMessage(new StatusMessage(this, "RunBufferAction"));
		
		final IBuffer buffer = WorkspaceManager.getCurrentBuffer();
		
		if (buffer != null) {
			String name = buffer.getFileName();
			
			if (name != null) {
				final String lang = ScriptingManager.getLanguage(name);
				final String script = buffer.getText();
	
				if (lang != null) {
					TaskManager.execute(new Runnable() {
			            public void run() {
			                ScriptingManager.eval(lang, script, buffer);
			            }
			        });
				}
			}
		} else {
			JOptionPane.showMessageDialog(WorkspaceManager.getMainFrame(), "No buffer", "No buffer", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	

}
