import java.util.Properties;

public class ConfigReader {

	private static final Properties prop = new Properties();
	static {
		try (var stream = ConfigReadear.class
				.getClassLoader()
				.getResourceAsStream("config.properties")) {
			prop.load(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
