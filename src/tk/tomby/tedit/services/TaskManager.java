package tk.tomby.tedit.services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.messages.StatusMessage;


public class TaskManager {
	
	private static final Log log = LogFactory.getLog(TaskManager.class);
	
	private static ProgressMonitor monitor;
	
	private static Timer timer;
	
	private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	
	private static Thread runner = new Thread(new TaskRunner(), "Task-Runner");
	
	static {
		runner.start();
	}
	
	public static void execute(Runnable work) {
		queue.add(work);
	}
	
	protected static void beforeExecute(final Thread thread, final Runnable task) {
		MessageManager.sendMessage(new StatusMessage(task, "Tarea iniciada"));
		
		monitor = new ProgressMonitor(WorkspaceManager.getMainFrame(), "Tarea iniciada", "Ejecutando tarea", 0, 100);
		monitor.setMillisToDecideToPopup(50);
		monitor.setMillisToPopup(500);
		
		timer = new Timer(1000, new ActionListener() {
			private int counter = 0;
			
			public void actionPerformed(ActionEvent evt) {
				if (counter < 90) {
					counter += 10;
				} else if (counter < 100) {
					counter += 1;
				}
				
				if (counter < 100) {
					MessageManager.sendMessage(new StatusMessage(task, "Tarea al " + getPercent()));
					
					monitor.setProgress(counter);
				}
			};
			
			public int getPercent() {
				return (int) (((double) counter / (double) 100) * 100);
			}
		});
		timer.start();
	}
	
	protected static void afterExecute(Runnable task, Throwable t) {
		if (t != null) {
			log.error("error", t);
		}
		
		monitor.setProgress(100);
		timer.stop();
		
		MessageManager.sendMessage(new StatusMessage(task, "Tarea finalizada"));
	}
	
	private static class TaskRunner implements Runnable {
		public void run() {
			Thread thread = Thread.currentThread();
			
			while (!thread.isInterrupted()) {
				try {
					Runnable task = queue.take();
					
					beforeExecute(thread, task);
					boolean completed = false;
					try {
						task.run();
						completed = true;
						afterExecute(task, null);
					} catch(Throwable t) {
						if (!completed) {
							afterExecute(task, t);
						} else {
							log.error("error", t);
						}
					}
				} catch (InterruptedException e) {
					log.warn("error", e);
				}	
			}
		}
	}
}
