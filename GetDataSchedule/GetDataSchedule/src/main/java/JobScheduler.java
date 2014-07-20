import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobScheduler {
	public static void main(String[] args) throws InterruptedException {
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

		// schedule to run after sometime
		System.out.println("Current Time = " + new Date());
		WorkerThread worker = new WorkerThread("do heavy processing");
		scheduledThreadPool.scheduleAtFixedRate(worker, 0, 5, TimeUnit.SECONDS);

		// add some delay to let some threads spawn by scheduler
		Thread.sleep(30000);

		scheduledThreadPool.shutdown();
		while (!scheduledThreadPool.isTerminated()) {
			// wait for all tasks to finish
		}
		System.out.println("Finished all threads");
	}
}

class WorkerThread implements Runnable {

	private String command;

	public WorkerThread(String s) {
		this.command = s;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());

	}

	@Override
	public String toString() {
		return this.command;
	}
}