package de.haw.smartshelf.eha;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class EventHeapAdapterConfig implements IEventHeapAdapterConfig
{
	private String _configFile;
	private Properties _properties;
	
	private EventHeapAdapterConfig()
	{
		
	}
	
	public EventHeapAdapterConfig(String configFile) throws FileNotFoundException, IOException
	{
		_configFile = configFile;
		init();
	}
	
	private void init() throws FileNotFoundException, IOException
	{
		_properties = new Properties();
		_properties.load(new FileInputStream(_configFile));
	}
	
	public String getEventHeapURL()
	{
		return _properties.getProperty("eventheap.url");
	}

	public String getClientName()
	{
		return _properties.getProperty("eventheap.clientname");
	}
}

