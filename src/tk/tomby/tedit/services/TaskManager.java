package tk.tomby.tedit.services;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

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
			if (SwingUtilities.isEventDispatchThread()) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						monitor.setProgress(progress);
					}
				});
			} else {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							monitor.setProgress(progress);
						}
					});
				} catch (InterruptedException e) {
					log.warn(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					log.warn(e.getMessage(), e);
				}
			}
		}
	}
	
	public static abstract class Task implements Runnable {
		protected int current;
		protected int maximun;
		protected int minimun;
		protected String message;
		protected String note;
		
		public Task(int minimun, int maximun) {
			this.current = 0;
			this.maximun = maximun;
			this.minimun = minimun;
			this.message = "Task";
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
			setProgress(minimun);
		}
		
		public boolean isDone() {
			return current >= maximun;
		}
		
		public int getProgress() {
			return current;
		}
		
		public void run() {
			try {
				init();
				work();
			} finally {
				clean();
			}
		}
		
		public void setProgress(int progress) {
			this.current = progress;
			TaskManager.setProgress(progress);
			MessageManager.sendMessage(MessageManager.STATUS_GROUP_NAME, 
					new StatusMessage(this, note + " at " + getPercent() + " %"));
		}

		public int getPercent() {
			return (int) (((double) current / (double) maximun) * 100);
		}
		
		public abstract void work();
		
		public void clean() {
			if (!isDone()) {
				setProgress(maximun);
			}
		}
	}
	
	public static class SwingWorkerTask extends Task implements PropertyChangeListener {

		private SwingWorker worker;
		
		public SwingWorkerTask(SwingWorker worker) {
			super(0, 100);
			this.worker = worker;			
		}

		public SwingWorkerTask(String message, String note, SwingWorker worker) {
			super(0, 100, message, note);
			this.worker = worker;
		}
		
		public void init() {
			TaskManager.initProgress(minimun, maximun, message, note);
			this.worker.addPropertyChangeListener(this);
		}
		
		public void propertyChange(PropertyChangeEvent evt) {
			if (log.isDebugEnabled()) {
				log.debug(evt.getPropertyName() + " = " + evt.getNewValue());
			}
			setProgress(worker.getProgress());
			if (worker.isDone()) {
				worker.removePropertyChangeListener(this);
			}
		}
		
		public void work() {
			worker.execute();
		}
		
		public void clean() {
		}
	}

}
