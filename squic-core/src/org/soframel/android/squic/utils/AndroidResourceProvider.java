package org.soframel.android.squic.utils;

import java.io.InputStream;

import org.soframel.squic.utils.ResourceProvider;

import android.app.Activity;

public class AndroidResourceProvider implements
        ResourceProvider {

	private Activity activity;
	public AndroidResourceProvider(Activity activity){
		this.activity=activity;
	}
	
	@Override
	public InputStream getPropertiesInputStream(String resourceName) {
		int dicoId=activity.getResources().getIdentifier(resourceName , "raw", activity.getPackageName());
		return activity.getResources().openRawResource(dicoId);
	}

}
