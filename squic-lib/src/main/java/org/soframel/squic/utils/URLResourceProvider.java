package org.soframel.squic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This resource provider reads stream from an URL, given as resourceName
 */
public class URLResourceProvider implements
        ResourceProvider {

	public InputStream getPropertiesInputStream(String resourceName) {

        try {
            URL url=new URL(resourceName);
            return url.openStream();
        } catch (IOException e) {
            System.out.println("Error while reading URL "+resourceName+": "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
