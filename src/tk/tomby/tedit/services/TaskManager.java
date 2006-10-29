package tk.tomby.tedit.services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

import tk.tomby.tedit.messages.StatusMessage;


public class TaskManager {
	
	private static Log log = LogFactory.getLog(TaskManager.class);
	
	private static ProgressMonitor monitor;
	
	private static Timer timer;
	
	private static Executor pool = 
		new ThreadPoolExecutor(5, 20, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
	
	public static void execute(Runnable work) {
		pool.execute(work);
	}
	
	public static void initProgress(final Task work) {
		monitor = new ProgressMonitor(WorkspaceManager.getMainFrame(), 
				work.getMessage(), work.getNote(), work.getMinimun(), work.getMaximun());
		monitor.setMillisToDecideToPopup(0);
		monitor.setMillisToPopup(100);
		
		timer = new Timer(500, new ActionListener() {
			private Update update = new Update(work);
			
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(update);
			};
		});
		timer.start();
	}
	
	public static synchronized void setProgress(final int progress) {
		if (monitor != null) {
			if (SwingUtilities.isEventDispatchThread()) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						monitor.setProgress(progress);
					}
				});
			} else {
				monitor.setProgress(progress);
			}
		}
	}
	
	public static class Update implements Runnable {
		
		private Task work;
		private int counter;
		private int step;
		private int maximum;
		
		public Update(Task work) {
			this.work = work;
			this.counter = work.getMinimun();
			this.maximum = work.getMaximun();
			this.step = work.getStep();
		}
		
		public void run() {
			if (monitor != null) {
				if (monitor.isCanceled()) {
					monitor.close();
					timer.stop();
				} else if (!work.isDone()) {
					counter += step;
					if (counter < maximum) {
						work.setProgress(counter);
					}
				} else {
					timer.stop();
				}
			}
		}
	}
	
	public static abstract class Task implements Runnable {
		protected int current;
		protected int maximun;
		protected int minimun;
		protected int step;
		protected String message;
		protected String note;
		
		public Task(int minimun, int maximun, int step) {
			this(minimun, maximun, step, "Task", "Work in progress");
		}

		public Task(int minimun, int maximun, int step, String message, String note) {
			this.current = 0;
			this.maximun = maximun;
			this.minimun = minimun;
			this.step = step;
			this.message = message;
			this.note = note;
		}
		
		public void init() {
			TaskManager.initProgress(this);
			setProgress(minimun);
		}
		
		public boolean isDone() {
			return current >= maximun;
		}
		
		public int getProgress() {
			return current;
		}
		
		public int getMaximun() {
			return maximun;
		}
		
		public int getMinimun() {
			return minimun;
		}
		
		public int getStep() {
			return step;
		}
		
		public String getMessage() {
			return message;
		}
		
		public String getNote() {
			return note;
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
			super(0, 100, 10);
			this.worker = worker;
		}

		public SwingWorkerTask(String message, String note, SwingWorker worker) {
			super(0, 100, 10, message, note);
			this.worker = worker;
		}
		
		public void init() {
			TaskManager.initProgress(this);
			worker.addPropertyChangeListener(this);
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
