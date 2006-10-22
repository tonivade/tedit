package tk.tomby.tedit.services;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.messages.StatusMessage;


public class TaskManager {
	
	private static Log log = LogFactory.getLog(TaskManager.class);
	
	private static ProgressMonitor monitor;
	
	private static Executor pool = 
		new ThreadPoolExecutor(5, 20, 1000, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue());
	
	
	public static void execute(Runnable work) {
		pool.execute(work);
	}
	
	public static void initProgress(int minimun, int maximun, String message, String note) {
		monitor = new ProgressMonitor(WorkspaceManager.getMainFrame(), message, note, minimun, maximun);
		monitor.setMillisToDecideToPopup(0);
		monitor.setMillisToPopup(10);
	}
	
	public static void setProgress(final int progress) {
		if (monitor != null) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						monitor.setProgress(progress);
					}
				});
			} catch (InterruptedException e) {
				log.warn(e);
			} catch (InvocationTargetException e) {
				log.warn(e);
			}
		}
	}
	
	public static abstract class Task implements Runnable {
		private int current;
		private int maximun;
		private int minimun;
		private String message;
		private String note;
		
		public Task(int minimun, int maximun) {
			this.current = 0;
			this.maximun = maximun;
			this.minimun = minimun;
			this.message = "Progress";
			this.note = "Work in progress";
		}
		
		public Task(int minimun, int maximun, String message, String note) {
			this.current = 0;
			this.maximun = maximun;
			this.minimun = minimun;
			this.message = message;
			this.note = note;
		}
		
		public void init() {
			TaskManager.initProgress(minimun, maximun, message, note);
		}
		
		public boolean isDone() {
			return current >= maximun;
		}
		
		public int getProgress() {
			return current;
		}
		
		public void run() {
			init();
			work();
			setProgress(maximun);
		}
		
		public void setProgress(int progress) {
			this.current = progress;
			TaskManager.setProgress(progress);
			MessageManager.sendMessage(MessageManager.STATUS_GROUP_NAME, 
					new StatusMessage(this, note + " at " + computePercent() + " %"));
		}

		private int computePercent() {
			return (int)((double)current / (double)maximun) * 100;
		}
		
		public abstract void work();
	}

}
