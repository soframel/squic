package org.soframel.squic.utils;

import java.io.InputStream;

public class FilePropertiesResourceProvider implements
		PropertiesResourceProvider {

	private String folder;
	
	public InputStream getPropertiesInputStream(String resourceName) {
		return this.getClass().getResourceAsStream(folder+"/"+resourceName);
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

}
