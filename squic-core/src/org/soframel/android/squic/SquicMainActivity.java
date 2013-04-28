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
package org.soframel.android.squic;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.soframel.android.squic.utils.AndroidLogger;
import org.soframel.android.squic.utils.AndroidResourceProvider;
import org.soframel.android.squic.utils.AndroidURLResourceProvider;
import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.io.xml.XMLQuizConfigParser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import org.soframel.squic.utils.URLResourceProvider;

public class SquicMainActivity extends Activity implements OnClickListener{
		
	private static final String TAG = "Squic_SquicMainActivity";
		
	XMLQuizConfigParser parser=new XMLQuizConfigParser(new AndroidLogger("XMLQuizConfigParser"), new AndroidResourceProvider(this), new AndroidURLResourceProvider(this));
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String,Quiz> quizzes=this.loadQuizzes();

        setContentView(R.layout.menu);        
        
        this.showQuizzesButtons(quizzes);
    }
    
    /**
     * load all quizzes in the directory corresponding to the language
     */
    private Map<String,Quiz> loadQuizzes(){
    	Map<String,Quiz> quizzes=new HashMap<String,Quiz>();
    	    	
    	String packageName=getPackageName();
    	
    	//load quizzes based on configuration
    	int id=getResources().getIdentifier("quizzes" ,"array", packageName);
    	Log.d(TAG, "Loading quizzes file with id "+id);
    	String[] quizzesNames=this.getResources().getStringArray(id);
    	for(String name: quizzesNames){
    		Log.d(TAG, "Reading quiz "+name);
			this.loadQuiz(quizzes, getResources().getIdentifier(name , "raw", packageName));
    	}
    	
    	//store quizzes in application context
    	((SquicApplication) this.getApplication()).setQuizzes(quizzes);
    	
    	return quizzes;
    }     
    
    private void loadQuiz(Map<String,Quiz> quizzes, int resourceId){
    	InputStream in=this.getResources().openRawResource(resourceId);
    	Quiz quiz1=parser.parseQuizConfig(in);
    	quizzes.put(quiz1.getId(), quiz1);    	
    }

    private void showQuizzesButtons(Map<String,Quiz> quizzes){    	
    	LinearLayout containerLayout= (LinearLayout) findViewById(R.id.MenuLayout);

    	containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setGravity(Gravity.CENTER_HORIZONTAL);    	
    	for(Quiz quiz: quizzes.values()){
    		this.createButton(quiz, containerLayout);
    	}
    	
    }
    
    private void createButton(Quiz quiz, LinearLayout layout){
    	View button=null;
    	
    	if(quiz.getIcon()==null || quiz.getIcon().equals("")){
    		button=new Button(this); 
    		((Button) button).setGravity(Gravity.CENTER_HORIZONTAL);
    		((Button) button).setText(quiz.getName());
    		((Button) button).setTextColor(Color.BLACK);
    	}
    	else{
    		button=new ImageButton(this);
    		String icon=quiz.getResPrefix()+quiz.getIcon();    		
        	int id=this.getResources().getIdentifier(icon, "drawable",this.getApplicationInfo().packageName);
        	Log.d(TAG, "Using icon "+icon+" with id="+id);
        	((ImageButton) button).setImageResource(id);
    	}    			    
    	button.setOnClickListener(this);
    	button.setTag(quiz.getId());
    	
    	this.addButtonBorder(button);
    	
    	LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	params.setMargins(0, 10, 0, 10);
    	button.setLayoutParams(params);    	
    	
    	layout.addView(button);    	        	
    }
    	
    
    private void addButtonBorder(View button){
    	RectShape rect=new RectShape();
    	//TODO: round angles. Default "null" values don't work on android 2.2
    	//RoundRectShape rect=new RoundRectShape(null,null,null);
    	ShapeDrawable shape=new ShapeDrawable(rect);   	
    	Paint paint=shape.getPaint();
    	paint.setStrokeWidth(5);
    	paint.setStyle(Style.STROKE);
    	paint.setColor(Color.parseColor("#00FFFFFF"));
    	paint.setAlpha(255);
    	shape.setAlpha(255);    	
    	button.setBackgroundDrawable(shape);
    }

	@Override
	public void onClick(View v) {
		String id=(String) v.getTag();
		Log.d(TAG, "Clicked on button "+v.getId()+", tag="+id);
		
		Intent i = new Intent(this, PlayQuizActivity.class);
		i.putExtra("quizId", id);
		this.startActivity(i);
	}
    
}
