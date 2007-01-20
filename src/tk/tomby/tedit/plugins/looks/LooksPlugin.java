package tk.tomby.tedit.plugins.looks;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import tk.tomby.tedit.plugins.IPlugin;
import tk.tomby.tedit.services.WorkspaceManager;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class LooksPlugin implements IPlugin {
	
	public LooksPlugin() {
		UIManager.put("ClassLoader", LooksPlugin.class.getClassLoader());
		UIManager.put(Options.NO_MARGIN_KEY, Boolean.TRUE);
		UIManager.put(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);
		UIManager.put(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
		try {
			UIManager.setLookAndFeel(new PlasticLookAndFeel());
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
