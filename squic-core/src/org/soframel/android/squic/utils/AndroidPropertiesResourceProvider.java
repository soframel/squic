package org.soframel.android.squic.utils;

import java.io.InputStream;

import org.soframel.squic.utils.PropertiesResourceProvider;

import android.app.Activity;

public class AndroidPropertiesResourceProvider implements
		PropertiesResourceProvider {

	private Activity activity;
	public AndroidPropertiesResourceProvider(Activity activity){
		this.activity=activity;
	}
	
	@Override
	public InputStream getPropertiesInputStream(String resourceName) {
		int dicoId=activity.getResources().getIdentifier(resourceName , "raw", activity.getPackageName());
		return activity.getResources().openRawResource(dicoId);
	}

}
