/*******************************************************************************
 * Copyright (c) 2012 soframel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     soframel - initial API and implementation
 ******************************************************************************/
package org.soframel.android.squic.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

public class ImageResponseView extends ImageButton implements ResponseView {

	private static final String TAG = "ResponseView";
	
	public ImageResponseView(Context context, String imageFile) {
		super(context);
		//this.setScaleType(ScaleType.FIT_START);
		if(imageFile!=null){
			int id=this.getResources().getIdentifier(imageFile, "drawable",context.getApplicationInfo().packageName);
			Log.d(TAG, "showing image "+imageFile+" with id "+id);
			
			this.setBackgroundResource(id);
		}
	}
	
}
