package xyz.poncio.TrayMessageNotify.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	private static PropertiesUtils instance;
	private Properties prop = new Properties();
	private static final String LOCAL_FILE = "./config.properties";

	private PropertiesUtils() throws Exception {
		this.prop = loadProperties();
	}

	public static PropertiesUtils getInstance() throws Exception {
		if (instance == null)
			instance = new PropertiesUtils();
		return instance;
	}

	public <T> T readProperty(String key, Class<T> clazz) throws Exception {
		return clazz.cast(this.prop.getProperty(key));
	}

	public void setProperty(String key, String value) {
		try {
			File confFile = new File(LOCAL_FILE);
			if (!confFile.exists())
				confFile.createNewFile();
			this.prop.setProperty(key, value);
			this.prop.store(new FileOutputStream(new File(LOCAL_FILE)), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Properties loadProperties() throws Exception {
		try {
			boolean makeFile = false;

			InputStream input = null;
			File confFile = new File(LOCAL_FILE);
			if (!confFile.exists())
				makeFile = true;
			else
				input = new FileInputStream(confFile);

			if (input == null)
				makeFile = true;

			if (makeFile) {
				setProperty("mqtt.host", "");
				setProperty("mqtt.user", "");
				setProperty("mqtt.pass", "");
				setProperty("tela.usuario", "");
				setProperty("tela.topico", "");
				throw new Exception("Sorry, unable to find config.properties");
			}

			// load a properties file from class path, inside static method
			Properties prop = new Properties();
			prop.load(input);
			prop.list(System.out);
			return prop;
		} catch (Exception ex) {
			throw ex;
		}
	}

}
