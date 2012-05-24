package org.soframel.android.squic.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

public class TextResponseView extends Button implements ResponseView {

	private static final String TAG = "ResponseView";
	
	public TextResponseView(Context context, String text) {
		super(context);
		super.setBackgroundColor(Color.WHITE);
		super.setTextColor(Color.BLACK);
		if(text!=null)
			this.setText(text);		
	}
	
	private void setText(String text){		
		super.setText(text);
	}
}
