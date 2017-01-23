package net.kodehawa.mantarobot.config;

import net.kodehawa.mantarobot.util.JSONUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This loads all configurations stored in the config.json file.
 * This class loads *before* everything else.
 *
 * @author Yomura
 */
public class Config {

	private volatile static Config cl = new Config();

	public static Config load() {
		return cl;
	}
	private final JSONObject objdata = new JSONObject();
	private String OS = System.getProperty("os.name").toLowerCase();
	private File config;
	private HashMap<String, Object> properties = new HashMap<>();

	private Config() {
		System.out.println("Loading config...");
		objdata.put("token", "");
		objdata.put("alsecret", "");
		objdata.put("weatherappid", "");
		objdata.put("osuapikey", "");
		objdata.put("debug", false);
		objdata.put("console", false);

		if (isWindows()) {
			this.config = new File("C:/mantaro/config/config.json");
		} else if (isUnix()) {
			this.config = new File("/home/mantaro/config/config.json");
		}
		createFile();
		setValues();
	}

	private void createFile() {
		if (!config.exists()) {
			config.getParentFile().mkdirs();
			try {
				config.createNewFile();
				writeValues(config, objdata);
			} catch (Exception ignored) {
			}
		}
	}

	private boolean isUnix() {
		return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	}

	private boolean isWindows() {
		return (OS.contains("win"));
	}

	private void setValues() {
		try {
			JSONObject data = JSONUtils.instance().getJSONObject(config);
			Iterator<?> datakeys = data.keys();

			while (datakeys.hasNext()) {
				String key = (String) datakeys.next();
				Object value = data.get(key);
				properties.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Please populate config.json file in /mantaro/config.");
			System.out.println("Exiting...");
			System.exit(-1);
		}

	}

	public HashMap<String, Object> values() {
		return properties;
	}

	private void writeValues(File file, JSONObject obj) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(objdata.toString(4));
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}