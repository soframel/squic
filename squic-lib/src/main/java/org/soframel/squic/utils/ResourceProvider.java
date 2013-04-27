package org.soframel.squic.utils;

import java.io.InputStream;

public interface ResourceProvider {
	public InputStream getPropertiesInputStream(String resourceName);
}
