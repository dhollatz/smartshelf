package de.haw.smartshelf.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ReaderConfig {
	@Attribute
	private int id;

	@Element(name = "type")
	private String type = "0";

	@Element(name = "position")
	private int position = 0;

	@Element(name = "port")
	private String port = "0";

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public int getPosition() {
		return position;
	}

	public String getPort() {
		return port;
	}
}
