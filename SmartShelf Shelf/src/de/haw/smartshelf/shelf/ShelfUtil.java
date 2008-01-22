package de.haw.smartshelf.shelf;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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

		URL defaultResource = null;
		try {
			defaultResource = new File(configuredTags.getDefaultImageURL())
					.toURI().toURL();
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage(), e);
		}
		for (RFIDTag configuredTag : configuredTags.getTags()) {
			if (configuredTag.getId().equals(tag.getId())) {
				URL resource = null;
				try {
					resource = new File(configuredTag.getImageURL()).toURI()
							.toURL();
				} catch (MalformedURLException e) {
					LOG.error(e.getMessage(), e);
				}
				if (resource == null) {
					return defaultResource != null ? new ImageIcon(
							defaultResource) : null;
				}
				return new ImageIcon(resource);
			}
		}

		return defaultResource != null ? new ImageIcon(defaultResource) : null;
	}

}
