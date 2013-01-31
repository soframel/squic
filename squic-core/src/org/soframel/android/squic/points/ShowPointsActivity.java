package org.soframel.android.squic.points;

import org.soframel.android.squic.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowPointsActivity extends Activity{ 
	
	public final static String POINTS_EXTRA="points";
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.showpoints);        
	        
	        int points=this.getIntent().getIntExtra(POINTS_EXTRA, 0);         
	        TextView pointsView= (TextView) findViewById(R.id.points);
	        pointsView.setText(String.valueOf(points));
	    }

	public void clickOk(View view){
		this.finish();
	}
	 
}
