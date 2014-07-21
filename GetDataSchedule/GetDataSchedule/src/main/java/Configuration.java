import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {
	public static boolean isRelease = false;
	public String url;
	public int runTimeHour;
	public int runTimeMin;
	public String configFileLocation;
	private boolean isConfigOK;
	
	private int intervalRun;

	public Configuration() {
		this.loadConfig();
	}

	public void loadConfig() {
		if (isRelease) {
			configFileLocation = "C:\\config.properties";
			this.setIntervalRun(1440);
		} else {
			configFileLocation = "/data/config.properties";
			this.setIntervalRun(1);
		}
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(configFileLocation);
			prop.load(input);
			url = prop.getProperty("URL");
			runTimeHour = Integer.parseInt(prop.getProperty("RunTimeHour"));
			runTimeMin = Integer.parseInt(prop.getProperty("RunTimeMinute"));
			setConfigOK(true);
		} catch (IOException ex) {
			ex.printStackTrace();
			setConfigOK(false);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateConfig(String url, int hour, int minute) {

		Properties prop = new Properties();
		OutputStream output = null;
		try {
			if (isRelease) {
				configFileLocation = "C:\\config.properties";
			} else {
				configFileLocation = "/data/config.properties";
			}
			output = new FileOutputStream(configFileLocation);

			prop.setProperty("URL", url);
			prop.setProperty("RunTimeHour", String.valueOf(hour));
			prop.setProperty("RunTimeMinute", String.valueOf(minute));

			// save properties to project root folder
			prop.store(output, null);
			this.loadConfig();
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isConfigOK() {
		return isConfigOK;
	}

	public void setConfigOK(boolean isConfigOK) {
		this.isConfigOK = isConfigOK;
	}

	public int getIntervalRun() {
		return intervalRun;
	}

	public void setIntervalRun(int intervalRun) {
		this.intervalRun = intervalRun;
	}

}
