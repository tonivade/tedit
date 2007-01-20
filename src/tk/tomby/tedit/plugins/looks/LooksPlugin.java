package tk.tomby.tedit.plugins.looks;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import tk.tomby.tedit.plugins.IPlugin;
import tk.tomby.tedit.services.WorkspaceManager;

public class LooksPlugin implements IPlugin {
	
	public LooksPlugin() {
		UIManager.put("ClassLoader", LooksPlugin.class.getClassLoader());
		try {
			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		SwingUtilities.updateComponentTreeUI(WorkspaceManager.getMainFrame());
	}
	
	public void save() {
		// Nothing to do
	}

}
