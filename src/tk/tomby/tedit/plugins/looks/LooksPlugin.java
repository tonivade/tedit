package tk.tomby.tedit.plugins.looks;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

import tk.tomby.tedit.plugins.IPlugin;
import tk.tomby.tedit.services.WorkspaceManager;

public class LooksPlugin implements IPlugin {
	
	public void init() {
		try {
			UIManager.setLookAndFeel(new PlasticLookAndFeel());
			SwingUtilities.updateComponentTreeUI(WorkspaceManager.getMainFrame());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		// Nothing to do
	}

}
