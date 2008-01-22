package de.haw.smartshelf.config;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

import de.haw.smartshelf.eha.EventHeapAdapterConfig;

public class ConfigurationUtil {
	public static void main(String[] args) {
		writeEmptyConfig();
		// readConfig();
	}

	@SuppressWarnings("unused")
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

		source = new File("config/ehaProperties.xml");
		EventHeapAdapterConfig ehaConfig = null;
		try {
			ehaConfig = serializer.read(EventHeapAdapterConfig.class, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(ehaConfig);

		source = new File("config/ehaProperties.xml");
		TagConfig tagConfig = null;
		try {
			tagConfig = serializer.read(TagConfig.class, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(tagConfig);
	}

	@SuppressWarnings("unused")
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

//		EventHeapAdapterConfig ehaConfig = new EventHeapAdapterConfig();
//		ehaConfig.setClientName("smartshelf");
//		ehaConfig.setEventHeapURL("192.168.14.27");
//		result = new File("emptyEhaProperties.xml");
//		try {
//			serializer.write(ehaConfig, result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		TagConfig tagConfig = new TagConfig();
		tagConfig.createDummy();
		result = new File("emptyTagProperties.xml");
		try {
			serializer.write(tagConfig, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
