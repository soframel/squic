package org.soframel.android.squic.points;

import org.soframel.android.squic.R;
import org.soframel.android.squic.SquicApplication;
import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.mode.GameModeCountPoints;
import org.soframel.squic.quiz.reward.IntentReward;
import org.soframel.squic.quiz.reward.Reward;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowPointsActivity extends Activity{ 
	
	private static final String TAG = "Squic_ShowPointsActivity";
	
	public final static String POINTS_EXTRA="points";
	public final static String QUIZ_ID="quizId";
	
	private Reward reward;
	private int points;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.showpoints);        
	        
	        points=this.getIntent().getIntExtra(POINTS_EXTRA, 0);         
	        TextView pointsView= (TextView) findViewById(R.id.points);
	        pointsView.setText(String.valueOf(points));
	        
	        String id=this.getIntent().getStringExtra(QUIZ_ID);
	        Quiz quiz=((SquicApplication)this.getApplication()).getQuizzes().get(id);	
	        if(quiz!=null && quiz.getGameMode()!=null
	        	&& quiz.getGameMode() instanceof GameModeCountPoints
	        	&& ((GameModeCountPoints)quiz.getGameMode()).getReward()!=null){
	        	reward=((GameModeCountPoints)quiz.getGameMode()).getReward();
	        
	        	if(reward.getText()!=null && points>=reward.getPointsRequired()){
	        		TextView rewardView= (TextView) findViewById(R.id.reward);
	        		rewardView.setText(reward.getText());
	        	}
	        	else
	        		Log.d(TAG, "Reward has no text, or not enough points");
	        }
	        else
	        	Log.d(TAG, "no Reward?");
	    }

	public void clickOk(View view){
		if(reward!=null && points>=reward.getPointsRequired())
			this.playReward(reward);
			
			
		this.finish();
	}
		 
	 private void playReward(Reward reward){
		if(reward instanceof IntentReward){
			IntentReward iReward=(IntentReward) reward;
			Intent i=new Intent(iReward.getAction());
			i.setData(Uri.parse(iReward.getUri()));
			startActivity(i);
		} 
	 }
	 
}
