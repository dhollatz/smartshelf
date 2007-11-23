package de.haw.smartshelf.config;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

public class ConfigurationUtil {
	public static void main(String[] args) {
		writeEmptyConfig();
	}

	private static void writeEmptyConfig() {
		Serializer serializer = new Persister();
		ShelfConfig shelfConfig = new ShelfConfig();
		shelfConfig.createDummy();
		File result = new File("emptyShelfProperties.xml");
		try {
			serializer.write(shelfConfig, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
