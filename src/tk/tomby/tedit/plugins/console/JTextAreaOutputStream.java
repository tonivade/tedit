package tk.tomby.tedit.plugins.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JTextAreaOutputStream extends OutputStream {
	
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
	
	private JTextArea area = null;
	
	public JTextAreaOutputStream(JTextArea area) {
		this.area = area;
	}

	@Override
	public void write(int b) throws IOException {
		buffer.write(b);
		if (b == '\n') {
			flushBuffer();
		}
	}
	
	private void flushBuffer() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				area.append(new String(buffer.toByteArray()));
				area.setCaretPosition(area.getDocument().getLength());
				buffer.reset();
			}
		});
	}

}
