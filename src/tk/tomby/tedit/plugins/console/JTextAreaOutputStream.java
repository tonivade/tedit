package tk.tomby.tedit.plugins.console;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JTextAreaOutputStream extends OutputStream {
	
	private JTextArea area = null;
	
	public JTextAreaOutputStream(JTextArea area) {
		this.area = area;
	}

	@Override
	public void write(final int b) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				area.append(String.valueOf((char) b));
			}
		});
	}

}
