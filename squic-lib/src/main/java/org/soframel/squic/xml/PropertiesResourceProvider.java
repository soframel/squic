package org.soframel.squic.xml;

import java.io.InputStream;

public interface PropertiesResourceProvider {
	public InputStream getPropertiesInputStream(String resourceName);
}
