package de.haw.smartshelf.eha;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

import de.haw.smartshelf.config.ConfigurationException;

@Root
public class EventHeapAdapterConfig implements IEventHeapAdapterConfig {
	private String _configFile;
	private Properties _properties;

	@Attribute
	private String eventHeapUrl;
	@Attribute
	private String clientname;

	@SuppressWarnings("unused")
	private EventHeapAdapterConfig() {

	}

	public EventHeapAdapterConfig(String configFile)
			throws FileNotFoundException, IOException {
		_configFile = configFile;
		init();
	}

	private void init() throws FileNotFoundException, IOException {
		_properties = new Properties();
		_properties.load(new FileInputStream(_configFile));
	}

	public String getEventHeapURL() {
		if (eventHeapUrl==null) {
			eventHeapUrl = _properties.getProperty("eventheap.url");	
		}
		return eventHeapUrl;
	}

	public void setEventHeapURL(String eventHeapURL) {
		this.eventHeapUrl = eventHeapURL;
	}

	public void setClientName(String clientName) {
		this.clientname = clientName;
	}

	public String getClientName() {
		if (clientname==null) {
			clientname = _properties.getProperty("eventheap.clientname");
		}
		return clientname;
	}

	public static EventHeapAdapterConfig getConfig(String fileName)
			throws ConfigurationException {
		Serializer serializer = new Persister();
		File source = new File(fileName);
		EventHeapAdapterConfig ehaConfig = null;
		try {
			ehaConfig = serializer.read(EventHeapAdapterConfig.class, source);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
		return ehaConfig;
	}

	public String toString() {
		return "<EventHeap> url: " + eventHeapUrl + " - clientName: "
				+ clientname;
	}
}
