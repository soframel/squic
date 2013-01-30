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

public class TouchResponseView extends ImageButton {

	private static final String TAG = "TouchResponseView";
	
	//private int nbButtons = 0;

	//public TouchResponseView(Context context, int nbButtons, String image, String colorCode) {
	public TouchResponseView(Context context, String image, String colorCode) {
		super(context);
		//this.nbButtons=nbButtons;
		if(colorCode!=null)
			this.setColor(colorCode);		
	}
	
	private void setColor(String colorCode){
		int color=Color.parseColor(colorCode);
		super.setBackgroundColor(color);
	}

	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		//int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		int width=parentWidth / (nbButtons+1);
		Log.d(TAG,"Calculating width: parent width="+parentWidth+", nb="+nbButtons+", width="+width);
		
		this.setMeasuredDimension(width, width);
	}*/
}
