import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class MainProgram {

	public void start() {
		try {
			if (!getConfig().isConfigOK()) {
				genConfigGUI = new GenConfigGUI(this, false);
				genConfigGUI.frame.setVisible(true);
			} else {
				schedulerGUI = new SchedulerGUI(this);
				schedulerGUI.frame.setVisible(true);
				this.restartJob();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Configuration config;
	private GenConfigGUI genConfigGUI;
	private SchedulerGUI schedulerGUI;
	private ScheduledExecutorService scheduledThreadPool;
	private WorkerThread worker;

	public MainProgram() {
		this.setConfig(new Configuration());
	}

	public static void main(String[] args) {
		System.out.println("YES");
		MainProgram mc = new MainProgram();
		mc.start();
		// testConnectMongo();
		// testGetHttpClient();
		// execute();
		// System.out.println(Config.url);
	}

	public void updateConfig(int hour, int minute, String url) {
		getConfig().updateConfig(url, hour, minute);
		this.start();
	}

	public void cancelUpdateConfig() {
		if (!getConfig().isConfigOK()) {
			JOptionPane.showMessageDialog(null, "Invalid Configuration. Exit now!", "Error", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		} else {
			this.start();
		}
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public void updateEvent() {
		genConfigGUI = new GenConfigGUI(this, true);
		genConfigGUI.frame.setVisible(true);
		schedulerGUI.frame.setVisible(false);
	}

	public void restartJob() {
		this.endJob();
		this.startJob();
	}
	
	public boolean runNowJob(){
		WorkerThread tmp = new WorkerThread(this.getConfig().url);
		try {
			tmp.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void startJob() {
		scheduledThreadPool = Executors.newScheduledThreadPool(5);
		worker = new WorkerThread(this.getConfig().url);
		Calendar current = Calendar.getInstance();
		int currentByMinute = current.get(Calendar.HOUR_OF_DAY) * 60 + current.get(Calendar.MINUTE);
		int runByMinute = this.getConfig().runTimeHour * 60 + this.getConfig().runTimeMin;

		int remainMinute = runByMinute - currentByMinute;
		if (remainMinute < 0) {
			remainMinute += 1440;
		}
		if (!this.getConfig().isRelease) {
			scheduledThreadPool.scheduleWithFixedDelay(worker, 0, 10, TimeUnit.SECONDS);
		}else{
			scheduledThreadPool.scheduleWithFixedDelay(worker, remainMinute, this.getConfig().getIntervalRun(), TimeUnit.MINUTES);			
		}
	}

	public void endJob() {
		try {
			if (scheduledThreadPool != null) {
				scheduledThreadPool.shutdown();
				while (!scheduledThreadPool.isTerminated()) {			
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Finished all threads");

	}
}
