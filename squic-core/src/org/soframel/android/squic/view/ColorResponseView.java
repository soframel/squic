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

public class ColorResponseView extends ImageButton implements ResponseView {

	private static final String TAG = "Squic_ColorResponseView";
	
	public ColorResponseView(Context context, String colorCode) {
		super(context);
		if(colorCode!=null)
			this.setColor(colorCode);		
	}
	
	private void setColor(String colorCode){
		int color=Color.parseColor(colorCode);
		super.setBackgroundColor(color);
	}
}
