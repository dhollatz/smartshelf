package de.haw.smartshelf.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

import de.haw.smartshelf.reader.tags.ICodeTag;
import de.haw.smartshelf.reader.tags.RFIDTag;

@Root
public class TagConfig {

	@Attribute
	private String defaultImageURL = "";

	@ElementMap(entry = "tag", key = "id", attribute = true, inline = true)
	private Map<String, RFIDTag> tags;

	public void createDummy() {
		// tags = new ArrayList<RFIDTag>();
		// tags.add(new ICodeTag());
		tags = new HashMap<String, RFIDTag>();
		tags.put("Tag1", new ICodeTag("Tag1_TagID"));
		tags.put("Tag2", new ICodeTag("Tag2_TagID"));
	}

	public String getDefaultImageURL() {
		return defaultImageURL;
	}

	public void setDefaultImageURL(String defaultImageURL) {
		this.defaultImageURL = defaultImageURL;
	}

	public static TagConfig getConfig(String fileName)
			throws ConfigurationException {
		Serializer serializer = new Persister();
		File source = new File(fileName);
		TagConfig tagConfig = null;
		try {
			tagConfig = serializer.read(TagConfig.class, source);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
		return tagConfig;
	}

	public Map<String, RFIDTag> getTags() {
		return tags;
	}
}
