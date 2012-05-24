package org.soframel.android.squic.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

public class ColorResponseView extends ImageButton implements ResponseView {

	private static final String TAG = "ResponseView";
	
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
