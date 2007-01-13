package tk.tomby.tedit.plugins.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleInterceptor extends PrintStream {
	
	private OutputStream out = null;
	
	public ConsoleInterceptor(PrintStream ps, OutputStream out) {
		super(ps);
		this.out = out;
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		try {
			out.write(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.write(b);
	}
	
	@Override
	public void write(byte[] buf, int off, int len) {
		try {
			out.write(buf, off, len);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.write(buf, off, len);
	}
	
	@Override
	public void write(int b) {
		try {
			out.write(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.write(b);
	}

}
