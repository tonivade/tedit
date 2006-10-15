package tk.tomby.tedit.services;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadManager {
	
	private static Executor pool = 
		new ThreadPoolExecutor(5, 20, 1000, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue());
	
	
	public static void execute(Runnable work) {
		pool.execute(work);
	}

}
