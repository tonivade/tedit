package tk.tomby.tedit.gui.editors;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;

public class FileEditor extends AbstractEditor {
	//~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JLabel label = null;

    /** DOCUMENT ME! */
    private JTextField field = null;
    
    //~ Constructors *******************************************************************************

    /**
     * Creates a new FileEditor object.
     */
    public FileEditor() {
        super();

        setLayout(new GridLayout(0, 2, 5, 5));

        label = new JLabel();
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));
        
        field = new JTextField(15);
        field.setEditable(false);
        field.setHorizontalAlignment(JTextField.LEFT);
        field.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		JFileChooser chooser = new JFileChooser();
                int retVal           = chooser.showOpenDialog(WorkspaceManager.getMainFrame());

                if (retVal == JFileChooser.APPROVE_OPTION) {
                    File selected = chooser.getSelectedFile();
                    field.setText(selected.getAbsolutePath());
                }
        	}
        });

        add(label);
        add(field);
    }
    
    //~ Methods ************************************************************************************
    
    public void setLabel(String label) {
    	this.label.setText(ResourceManager.getProperty(label));
    }

    @Override
    protected void setValue(String value) {
    	field.setText(value);
    }
    
    @Override
    protected String getValue() {
    	return field.getText();
    }

}
