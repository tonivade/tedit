package tk.tomby.tedit.plugins.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JTextAreaOutputStream extends OutputStream {
	
	private static final int BUFFER_SIZE = 2048;
	
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream(BUFFER_SIZE);
	
	private JTextArea area = null;
	
	public JTextAreaOutputStream(JTextArea area) {
		this.area = area;
	}

	@Override
	public void write(int b) throws IOException {
		buffer.write(b);
		if (b == '\n' || buffer.size() >= BUFFER_SIZE) {
			flushBuffer();
		}
	}
	
	private void flushBuffer() {
		final byte[] b = buffer.toByteArray();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				area.append(new String(b));
				area.setCaretPosition(area.getDocument().getLength());
				
			}
		});
		buffer.reset();
	}

}
