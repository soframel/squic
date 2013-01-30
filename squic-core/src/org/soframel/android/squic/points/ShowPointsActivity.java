package org.soframel.android.squic.points;

import org.soframel.android.squic.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShowPointsActivity extends Activity implements OnClickListener{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.showpoints);        
	        
	        Button button=(Button) this.findViewById(R.id.pointsokbutton);
	        button.setOnClickListener(this);
	        
	        int points=this.getIntent().getIntExtra("points", 0);         
	        TextView pointsView= (TextView) findViewById(R.id.points);
	        pointsView.setText(String.valueOf(points));
	    }

	@Override
	public void onClick(View arg0) {
		this.finish();
	}
	 
	 
}
