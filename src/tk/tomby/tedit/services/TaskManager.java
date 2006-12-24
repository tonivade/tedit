package tk.tomby.tedit.services;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.messages.StatusMessage;


public class TaskManager {
	
	private static final Log log = LogFactory.getLog(TaskManager.class);
	
	private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	
	private static Thread runner = new Thread(new TaskRunner(), "Task-Runner");
	
	static {
		runner.start();
	}
	
	public static void execute(Runnable work) {
		queue.add(work);
	}
	
	protected static void beforeExecute(Thread thread, Runnable task) {
		MessageManager.sendMessage(new StatusMessage(task, "Tarea iniciada"));
	}
	
	protected static void afterExecute(Runnable task, Throwable t) {
		if (t != null) {
			log.error("error", t);
		}
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
