package de.haw.smartshelf.reader;

import org.apache.log4j.Logger;

import de.haw.smartshelf.config.ReaderConfig;

public class ReaderFactory {

	private static final Logger LOG = Logger.getLogger(ReaderFactory.class);

	private static ReaderFactory instance = null;

	private ReaderFactory() {

	}

	public static ReaderFactory getInstance() {
		if (instance == null) {
			instance = new ReaderFactory();
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public ShelfReader createReader(ReaderConfig aReaderConfig)
			throws NoSuchReaderException {
		// TODO evtl. Dependency Injection mit Spring?!
		// http://www.theserverside.com/tt/articles/article.tss?l=IOCBeginners
		try {
			Class readerClass = Class.forName(aReaderConfig.getClassname());
			LOG.debug("Creating reader for class: " + readerClass + " ...");
			if (ShelfReader.class.isAssignableFrom(readerClass)) {
				return (ShelfReader) readerClass.newInstance();
			}
			throw new NoSuchReaderException("Reader not supported: "
					+ readerClass.getCanonicalName());
		} catch (ClassNotFoundException e) {
			throw new NoSuchReaderException(e);
		} catch (InstantiationException e) {
			throw new NoSuchReaderException("Problem creating an instance", e);
		} catch (IllegalAccessException e) {
			throw new NoSuchReaderException(e);
		}

	};

}
