package frameworkAutomate.utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	private static final Properties prop = new Properties();
	static {
		try (var stream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
			prop.load(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* ---------- Métodos de acceso ---------- */
	public static String get(String key) {
		return prop.getProperty(key);
	}
	
	 /** Devuelve el valor o el default dado si la clave falta. */
    public static String get(String key, String defaultVal) {
        return prop.getProperty(key, defaultVal);
    }

	public static int getInt(String key, int defaultVal) {
	    String raw = prop.getProperty(key);
	    return raw == null ? defaultVal : Integer.parseInt(raw);
	}
	
	public static long getLong(String key) {
		return Long.parseLong(get(key));
	}
}