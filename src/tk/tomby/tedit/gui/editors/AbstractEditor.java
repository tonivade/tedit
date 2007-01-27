package tk.tomby.tedit.gui.editors;

import javax.swing.JPanel;

import tk.tomby.tedit.core.preferences.IEditor;
import tk.tomby.tedit.messages.PreferenceMessage;
import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;

public abstract class AbstractEditor extends JPanel implements IEditor {
	
	//~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private String defaultValue = "";

    /** DOCUMENT ME! */
    private String key = null;
	
  	//~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param def DOCUMENT ME!
     */
    public void setDefault(String def) {
        this.defaultValue = def;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key) {
        this.key = key;

        setValue(PreferenceManager.getString(key, defaultValue));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isModified() {
        String newValue = getValue();
        String oldValue = PreferenceManager.getString(key, defaultValue);

        return !newValue.equals(oldValue);
    }

    /**
     * DOCUMENT ME!
     */
    public void commit() {
        String newValue = getValue();
        String oldValue = PreferenceManager.getString(key, defaultValue);

        if (!newValue.equals(oldValue)) {
            PreferenceMessage message = new PreferenceMessage(this, key, newValue, oldValue);

            PreferenceManager.putString(key, newValue);

            MessageManager.sendMessage(message);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restore() {
        setValue(PreferenceManager.getString(key, defaultValue));
    }
    
    protected abstract String getValue();
    
    protected abstract void setValue(String value);

}
