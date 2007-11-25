package de.haw.smartshelf.config;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

public class ConfigurationUtil {
	public static void main(String[] args) {
		writeEmptyConfig();
		readConfig();
	}

	private static void readConfig() {
		Serializer serializer = new Persister();
		File source = new File("config/shelfProperties.xml");
		ShelfConfig shelfConfig = null;
		try {
			shelfConfig = serializer.read(ShelfConfig.class, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(shelfConfig);
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
