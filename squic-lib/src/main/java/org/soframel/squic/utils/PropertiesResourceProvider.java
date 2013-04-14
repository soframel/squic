package org.soframel.squic.utils;

import java.io.InputStream;

public interface PropertiesResourceProvider {
	public InputStream getPropertiesInputStream(String resourceName);
}
