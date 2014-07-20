import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MainProgram {

	public static void testPostHttpClient() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://targethost/login");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "vip"));
		nvps.add(new BasicNameValuePair("password", "secret"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response2 = httpclient.execute(httpPost);
		try {
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			EntityUtils.consume(entity2);
		} finally {
			response2.close();
		}
	}

	public static void testGetHttpClient() throws ClientProtocolException, IOException {
		String url = "http://localhost/test.csv";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);

		try {
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			InputStream content = entity1.getContent();
			String msg = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(content));
			while ((msg = br.readLine()) != null) {
				System.out.println(msg);
			}

			EntityUtils.consume(entity1);
			System.out.println(entity1.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response1.close();
		}
	}

	public static void testConnectMongo() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		List<String> dbs = mongoClient.getDatabaseNames();
		for (String db : dbs) {
			System.out.println(db);
		}
		DB db = mongoClient.getDB("test");
		DBCollection coll = db.getCollection("testCollection");
		BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("type", "database").append("count", 1)
				.append("info", new BasicDBObject("x", 203).append("y", 102));
		coll.insert(doc);
	}

	public static void insertDB(List<DBObject> list) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("test");
		DBCollection coll = db.getCollection("testCollection");
		coll.insert(list);

	}

	public static BasicDBObject genDBObject(List<String> headers, String s) {
		BasicDBObject tmp = new BasicDBObject();
		List<String> data = Arrays.asList(s.split(","));
		for (int i = 0; i < data.size(); i++) {
			tmp.append(headers.get(i), data.get(i));
		}
		return tmp;
	}

	public static void execute() throws Exception {
		String url = "http://localhost/test.csv";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpGet);

		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();

			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
			String s = br.readLine();
			List<String> headers = Arrays.asList(s.split(","));
			List<DBObject> list = new ArrayList<DBObject>();
			int i = 0;
			while ((s = br.readLine()) != null) {
				i++;
				DBObject dbObj = genDBObject(headers, s);
				list.add(dbObj);
				if (i % 100 == 0) {
					insertDB(list);
					list = new ArrayList<DBObject>();
				}
			}

			insertDB(list);
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
		}
	}

	public void start() {
		try {
			
			if (!getConfig().isConfigOK()) {
				genConfigGUI = new GenConfigGUI(this, false);
				genConfigGUI.frame.setVisible(true);
			} else {
				schedulerGUI = new SchedulerGUI(this);
				schedulerGUI.frame.setVisible(true);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Configuration config;
	private GenConfigGUI genConfigGUI;
	private SchedulerGUI schedulerGUI;

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
}
