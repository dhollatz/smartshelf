package de.haw.smartshelf.shelf;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import de.haw.smartshelf.config.ConfigurationException;
import de.haw.smartshelf.config.TagConfig;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class ShelfUtil {

	private static final Logger LOG = Logger.getLogger(Shelf.class);

	private static volatile ShelfUtil INSTANCE;
	private static final String TAG_CONFIG = "config/tagProperties.xml";

	private TagConfig configuredTags;

	private ShelfUtil() {
	}

	public static ShelfUtil getInstance() {
		if (INSTANCE == null)
			synchronized (ShelfUtil.class) {
				if (INSTANCE == null)
					INSTANCE = new ShelfUtil();
			}
		INSTANCE.initialize();
		return INSTANCE;
	}

	private void initialize() {
		try {
			configuredTags = TagConfig.getConfig(TAG_CONFIG);
		} catch (ConfigurationException e) {
			LOG.fatal("Configuration failed " + e.getMessage());
			throw new RuntimeException("Fatal - Configuration failed", e);
		}
	}

	public Icon getImage(RFIDTag tag) {
		return getImage(tag.getId());
	}
	
	public Icon getImage(String tagId) {

		File defaultResource = new File(configuredTags.getDefaultImageURL());
		RFIDTag configuredTag = configuredTags.getTags().get(tagId);
		if (configuredTag != null) {
			File resource = new File(configuredTag.getImageURL());
			if (resource.exists()) {
				return new ImageIcon(resource.getAbsolutePath());
			}
		}

		return defaultResource.exists() ? new ImageIcon(defaultResource
				.getAbsolutePath()) : null;
	}

}
