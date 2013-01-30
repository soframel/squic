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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.soframel.android.squic.media.ISoundFilePlayer;
import org.soframel.android.squic.media.SoundMediaPlayer;
import org.soframel.android.squic.media.TextToSpeechManager;
import org.soframel.android.squic.points.PointsManager;
import org.soframel.android.squic.points.ShowPointsActivity;
import org.soframel.android.squic.quiz.ColorResponse;
import org.soframel.android.squic.quiz.GameModeCountPoints;
import org.soframel.android.squic.quiz.GameModeRetry;
import org.soframel.android.squic.quiz.ImageResponse;
import org.soframel.android.squic.quiz.Question;
import org.soframel.android.squic.quiz.Quiz;
import org.soframel.android.squic.quiz.ReadResultAction;
import org.soframel.android.squic.quiz.Response;
import org.soframel.android.squic.quiz.ResultAction;
import org.soframel.android.squic.quiz.SpeechResultAction;
import org.soframel.android.squic.quiz.SpokenQuestion;
import org.soframel.android.squic.quiz.TextQuestion;
import org.soframel.android.squic.quiz.TextResponse;
import org.soframel.android.squic.quiz.TextToSpeechQuestion;
import org.soframel.android.squic.quiz.TextToSpeechResultAction;
import org.soframel.android.squic.quiz.TextQuestionImpl;
import org.soframel.android.squic.view.ResponsesLayout;
import org.soframel.android.squic.view.QuizViewManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Activity to play a quiz. 
 * This class has the role of a controller (MVC), and should thus not contain graphical/view logic
 * but only quiz logic, and delegate the rest.
 * @author sophie
 *
 */
public class PlayQuizActivity extends Activity implements 
OnClickListener, MediaPlayer.OnCompletionListener, OnUtteranceCompletedListener {
	
	private static final String TAG = "PlayQuizActivity";
	private static final String FINISH_ID="finish";
		
	//data
	private Quiz quiz;
	private List<Question> remainingQuestions;
	private List<Question> alreadyPlayed;
	private int idGoodResultSound=-1;
	private int idBadResultSound=-1;
	private int idFinishResultSound=-1;	 
	private Question currentQuestion;
	private boolean responseCorrect=false;
	private MediaPlayer finishMP=null;
	
	private PointsManager pointsManager=new PointsManager();
	
	//helpers/managers
	private QuizViewManager viewHelper;		
	private ISoundFilePlayer soundPlayer=null;
	private TextToSpeechManager ttsManager=null;
	private Random rand=new Random();
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.quiz);        
		 Log.d(TAG, "PlayQuizActivity created");
		 		 
		 pointsManager.resetPoints();
		 
		 String id=this.getIntent().getStringExtra("quizId");
		 
		 //read list of quizzes from application context
		 quiz=((SquicApplication)this.getApplication()).getQuizzes().get(id);		 
		 if(quiz==null)
				 Log.d(TAG, "PlayQuizActivity: no quiz found for id="+id);
		 else{
			 Log.d(TAG, "PlayQuizActivity: found quiz id="+id+", name="+quiz.getName());

			 //get questions
			 remainingQuestions=new ArrayList<Question>(quiz.getQuestions());
			 alreadyPlayed=new ArrayList<Question>();
			 		 		 
			 //initialize sound
			 //soundPlayer=new SoundPoolPlayer(this);
			 soundPlayer=new SoundMediaPlayer(this);
			 this.loadSounds();
			
			 //initialize layout
			 ResponsesLayout responsesLayout= (ResponsesLayout) findViewById(R.id.responsesLayout);
			 LinearLayout questionLayout= (LinearLayout) findViewById(R.id.questionLayout);
			 viewHelper=new QuizViewManager(this, responsesLayout, questionLayout); 
			 
			 //play first question
			 this.playNextQuestion();
		 }
	 }

	 
	 private void loadSounds(){
		 if(quiz.getGoodResultAction()!=null && quiz.getGoodResultAction() instanceof SpeechResultAction){
			 String file=((SpeechResultAction)quiz.getGoodResultAction()).getSpeechFile().getFile();
			 file=quiz.getResPrefix()+file;
			 Log.d(TAG, "loading good result sound: file "+file);
			 this.idGoodResultSound=soundPlayer.loadFile(file);
			 Log.d(TAG, " -> id="+idGoodResultSound);
		 }
		 if(quiz.getBadResultAction()!=null && quiz.getBadResultAction() instanceof SpeechResultAction){
			 String file=((SpeechResultAction)quiz.getBadResultAction()).getSpeechFile().getFile();
			 file=quiz.getResPrefix()+file;
			 Log.d(TAG, "loading bad result sound: file "+file);
			 this.idBadResultSound=soundPlayer.loadFile(file);
			 Log.d(TAG, " -> id="+idBadResultSound);
		 }
		 if(quiz.getQuizFinishedAction()!=null && quiz.getQuizFinishedAction() instanceof SpeechResultAction){
			 String file=((SpeechResultAction)quiz.getQuizFinishedAction()).getSpeechFile().getFile();
			 file=quiz.getResPrefix()+file;
			 Log.d(TAG, "loading finish result sound: file "+file);
			 this.idFinishResultSound=soundPlayer.loadFile(file);
			 Log.d(TAG, " -> id="+idFinishResultSound);
		 }
		 
	 }
	 
	 /**
	  * play the next question of the quiz
	  */
	 private void playNextQuestion(){
		 viewHelper.emptyLayout();
		 
		 if(remainingQuestions.isEmpty()
				 || (quiz.getNbQuestions()>0 && alreadyPlayed.size()>=quiz.getNbQuestions())
				 ){
			 //game is finished -> congratulate, return to home
			 this.congratulate();
		 }
		 else{	 //play question	 
			 Question q=this.chooseQuestion();
			 currentQuestion=q;
			 remainingQuestions.remove(q);
			 alreadyPlayed.add(q);			
			 
			 if(q instanceof TextQuestionImpl){
				 //show question
				 viewHelper.showQuestion((TextQuestionImpl) currentQuestion);
			 }
			 
			 this.showResponses(q);
			 viewHelper.adaptLayout();
			 this.playQuestion(q);
		 }
	 }
	 

	 /**
	  * choose a question to play among the remaining questions
	  * @return
	  */
	 private Question chooseQuestion(){		 
		 int index=rand.nextInt(remainingQuestions.size());
		 return remainingQuestions.get(index);
	 }
	 
	 /**
	  * display the possible responses of the question
	  * @param question
	  */
	 private void showResponses(Question question){
		 List<Response> responses=question.getResponsesWithRandom(quiz);
		 Collections.shuffle(responses);
	
		 //draw responses
		 for(int i=0;i<responses.size();i++){
			 Response resp=responses.get(i);
			 if(resp instanceof ColorResponse)
				 viewHelper.showColorResponse((ColorResponse)resp);
			 else if(resp instanceof TextResponse)
				 viewHelper.showTextResponse((TextResponse)resp);
			 else if(resp instanceof ImageResponse)
				 viewHelper.showImageResponse((ImageResponse)resp);
		 }		 
	 }	 
	
	 /**
	  * play the question
	  * @param question
	  */
	 private void playQuestion(Question question){		 		 
		 if(question instanceof SpokenQuestion){
			 String file=((SpokenQuestion)question).getSpeechFile().getFile();
			 file=quiz.getResPrefix()+file;
			 int id=soundPlayer.loadFile(file);
			 Log.d(TAG, "Loaded question sound file: "+file+" in id="+id);
			 soundPlayer.playFile(id);
		 }
		 else if(question instanceof TextToSpeechQuestion){
			 String text=((TextToSpeechQuestion)question).getText();
			 Log.d(TAG, "Loaded text to speech question text:"+text);
			 
			 if(ttsManager==null){
				//initialize it the first time, with initialize text
				 this.initializeTTS(text);
			 }
			 else{
				 ttsManager.playText(text);
			 }		
		 }	
		viewHelper.setEnableAll(true);
	 }
	 
	 private void initializeTTS(String text){
		 ttsManager=new TextToSpeechManager(this, quiz.getLanguage(), text, this);
	 }
	
	@Override
	public void onClick(View v) {
		viewHelper.setEnableAll(false);
		String id=(String) v.getTag();
		Log.d(TAG, "Button clicked: "+v.getTag());
		if(id!=null && currentQuestion.getCorrectIds().contains(id)){
			Log.d(TAG, "correct answer");
			responseCorrect=true;
			correctAnswer(id);
		}
		else{
			Log.d(TAG, "incorrect answer");
			responseCorrect=false;
			incorrectAnswer(id);
		}		
		//after: called by callback when sound has finished playing
	}
	
	/**
	 * play the action specified when a good/bad answer is given, or when quiz is finished
	 * @param resultAction
	 * @param goodResult
	 * @param finish
	 * @param resultId
	 */
	private void playResult(ResultAction resultAction, boolean goodResult, boolean finish, String resultId){
		
		if(resultAction instanceof SpeechResultAction){
			if(goodResult && idGoodResultSound>-1)
				soundPlayer.playFileSynchronous(idGoodResultSound, this);
			else if(finish && idFinishResultSound>-1){
				finishMP=((SoundMediaPlayer) soundPlayer).getMediaPlayer(idFinishResultSound);
				soundPlayer.playFileSynchronous(idFinishResultSound, this);
			}
			else if(!goodResult && idBadResultSound>-1)
				soundPlayer.playFileSynchronous(idBadResultSound, this);
		}
		else if(resultAction instanceof TextToSpeechResultAction
				|| resultAction instanceof ReadResultAction){
			
			//build text in case of ReadResultAction
			if(resultAction instanceof ReadResultAction){
				ReadResultAction rrAction=(ReadResultAction) resultAction;
				Log.d(TAG, "ReadResulTAction");
				//find response text
				String responseText="";
				List<Response> responses=this.currentQuestion.getPossibleResponses();
				Iterator<Response> it=responses.iterator();
				boolean responseFound=false;
				while(it.hasNext() && !responseFound){
					Response resp=it.next();
					if(resp.getId().equals(resultId) && resp instanceof TextResponse){
						responseFound=true;
						responseText=((TextResponse)resp).getText();
						Log.d(TAG, "Response found, text="+responseText);
					}
				}
				//find question text
				String questionText="";
				if(currentQuestion instanceof TextQuestion)
					questionText=((TextQuestion)currentQuestion).getText();
				
				//find good response
				String goodResponse="";
				if(goodResult)
					goodResponse=responseText;
				else{
					List<Response> goods=this.currentQuestion.findGoodResponses();
					if(goods.size()>0){
						Response goodR=goods.get(0);
						if(goodR instanceof TextResponse){
							goodResponse=((TextResponse)goodR).getText();
						}
					}
				}
				
				rrAction.setText(rrAction.buildText(questionText, responseText, goodResponse));
				Log.d(TAG, "ReadResponse, text="+responseText);
			}
			
			String text=((TextToSpeechResultAction)resultAction).getText();
			String id=(Integer.valueOf(text.hashCode())).toString();
			if(finish)
				id=FINISH_ID;
			if(ttsManager==null){
				this.initializeTTS(text);
				//simulate synchronous call
				this.onUtteranceCompleted(id);
			}
			else{								
				ttsManager.playSynchronousText(text, id);
			}
		}
	}
	
	/**
	 * called by listeners once the result has been played
	 */
	public void afterResultPlayed(){
		if(quiz.getGameMode()==null || quiz.getGameMode() instanceof GameModeRetry){
			if(responseCorrect)
				this.playNextQuestion();
			else
				this.playQuestion(currentQuestion);
		}
		else{ // if(quiz.getGameMode() instanceof GameModeCountPoints){
			this.playNextQuestion();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if(mp.equals(finishMP))
			this.finishQuiz();
		else
			this.afterResultPlayed();
	}

	@Override
	public void onUtteranceCompleted(final String utteranceId) {
		//Warning, OnUtteranceCompletedListener requires "runOnUiThread" !
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	Log.d(TAG, "onUttereanceCompleted called");
            	if(utteranceId.equals(FINISH_ID))
            		finishQuiz();
            	else
            		afterResultPlayed();
            }
        });

	}
	
	private void correctAnswer(String id){
		if(quiz.getGameMode()!=null && quiz.getGameMode() instanceof GameModeCountPoints){
			pointsManager.calculatePointsForAnswer(true, (GameModeCountPoints) quiz.getGameMode());
		}
		
		this.playResult(quiz.getGoodResultAction(), true, false, id);
	}
	private void incorrectAnswer(String id){
		if(quiz.getGameMode()!=null && quiz.getGameMode() instanceof GameModeCountPoints){
			pointsManager.calculatePointsForAnswer(false, (GameModeCountPoints) quiz.getGameMode());
		}
		
		this.playResult(quiz.getBadResultAction(), false, false, id);
	}
	
	private void congratulate(){
		 //congratulate
		 this.playResult(quiz.getQuizFinishedAction(), false, true, null);
	}
	
	 private void finishQuiz(){
		 Log.d(TAG, "Finishing quiz");
		 
		 if(ttsManager!=null)
			 ttsManager.finish();
		 
		 if(quiz.getGameMode()!=null && quiz.getGameMode() instanceof GameModeCountPoints){		
			 int points=this.pointsManager.getTotalPoints();
			 Log.d(TAG, "Game finished, total points="+points);
			 
			 Intent i = new Intent(this, ShowPointsActivity.class);
			 i.putExtra("points", points);
			 this.startActivity(i);
		 }
		 
		 this.finish();
	 }
	 
	
	@Override
	protected void onDestroy() {
		this.finishQuiz();
		super.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(TAG, "PlayQuizActivity: configuration changed: "+newConfig);
		
		super.onConfigurationChanged(newConfig);

		//do not call viewHelper.adaptLayout(); : widht/height of layout not changed yet,
		//and ViewTreeObserver will be called when they are anyway
		/*if(viewHelper!=null){
			viewHelper.adaptLayout();
		}*/
		
	}

	public Question getCurrentQuestion(){
		return currentQuestion;
	}
	public Quiz getCurrentQuiz(){
		return quiz;
	}

}
