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

import org.soframel.android.squic.automatic.AutomaticQuestionsManager;
import org.soframel.android.squic.automatic.CalculationQuestionsManager;
import org.soframel.android.squic.media.ISoundFilePlayer;
import org.soframel.android.squic.media.SoundMediaPlayer;
import org.soframel.android.squic.media.TextToSpeechManager;
import org.soframel.android.squic.points.PointsManager;
import org.soframel.android.squic.points.ShowPointsActivity;
import org.soframel.android.squic.view.MultipleChoiceQuizViewManager;
import org.soframel.android.squic.view.ResponseDialogFragment;
import org.soframel.android.squic.view.ResponsesLayout;
import org.soframel.android.squic.view.ViewManager;
import org.soframel.android.squic.view.WritingViewManager;
import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.action.ReadResultAction;
import org.soframel.squic.quiz.action.ResultAction;
import org.soframel.squic.quiz.action.SpeechResultAction;
import org.soframel.squic.quiz.action.TextToSpeechResultAction;
import org.soframel.squic.quiz.automatic.CalculationQuestions;
import org.soframel.squic.quiz.mode.GameModeCountPoints;
import org.soframel.squic.quiz.mode.GameModeRetry;
import org.soframel.squic.quiz.question.MultipleChoiceQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceSpokenQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextToSpeechQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.question.TextQuestion;
import org.soframel.squic.quiz.question.TextToSpeechQuestion;
import org.soframel.squic.quiz.question.WritingQuestion;
import org.soframel.squic.quiz.response.ColorResponse;
import org.soframel.squic.quiz.response.ImageResponse;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.TextResponse;

import android.app.Activity;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Activity to play a quiz. This class has the role of a controller (MVC), and
 * should thus not contain graphical/view logic but only quiz logic, and
 * delegate the rest.
 * 
 * @author sophie
 * 
 */
public class PlayQuizActivity extends FragmentActivity implements
		OnClickListener, MediaPlayer.OnCompletionListener,
		OnUtteranceCompletedListener {

	private static final String TAG = "PlayQuizActivity";
	private static final String FINISH_ID = "finish";

	// data for current status of quiz
	private Quiz quiz;
	private List<Question> remainingQuestions;
	private List<Question> alreadyPlayed;
	private int idGoodResultSound = -1;
	private int idBadResultSound = -1;
	private int idFinishResultSound = -1;
	private Question currentQuestion;
	private boolean responseCorrect = false;
	private MediaPlayer finishMP = null;
	private ResultAction currentResultAction;
	private String currentGoodResponse;

	private PointsManager pointsManager = new PointsManager();

	// helpers/managers
	private ViewManager viewHelper;
	private ISoundFilePlayer soundPlayer = null;
	private TextToSpeechManager ttsManager = null;
	private Random rand = new Random();
	
	private AutomaticQuestionsManager automaticQuestionsManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.quiz);
		Log.d(TAG, "PlayQuizActivity created");

		pointsManager.resetPoints();

		String id = this.getIntent().getStringExtra("quizId");

		// read list of quizzes from application context
		quiz = ((SquicApplication) this.getApplication()).getQuizzes().get(id);
		if (quiz == null)
			Log.d(TAG, "PlayQuizActivity: no quiz found for id=" + id);
		else {
			Log.d(TAG, "PlayQuizActivity: found quiz id=" + id + ", name="
					+ quiz.getName());

			//automatic questions: initialize questions
			if(quiz.getAutomaticQuestions()!=null){
				if(quiz.getAutomaticQuestions()!=null && quiz.getAutomaticQuestions() instanceof CalculationQuestions)
					automaticQuestionsManager=new CalculationQuestionsManager((CalculationQuestions) quiz.getAutomaticQuestions());
				else
					Log.e(TAG, "Unsupported automatic question: "+quiz.getAutomaticQuestions());
				
				if(automaticQuestionsManager!=null){
					List<Question> questions=automaticQuestionsManager.initializeQuestions();
					quiz.setQuestions(questions);
				}
			}

            //initializable questions: idem
            if(quiz.getInitializableQuestions()!=null){
                List<Question> questions=null;
                List<MultipleChoiceResponse> responses=null;
                try {
                    questions = quiz.getInitializableQuestions().initialize();
                    responses=quiz.getInitializableQuestions().getResponses();
                } catch (Exception e) {
                    Log.w(TAG, "Could not initialize initializable question: "+e.getMessage(), e);
                }
                quiz.setQuestions(questions);
                quiz.setResponses(responses);
            }
			
			// initialize layout
			Question firstQuestion = quiz.getQuestions().get(0);
			if (firstQuestion instanceof MultipleChoiceQuestion) {
				viewHelper = new MultipleChoiceQuizViewManager(this);
			} else if (firstQuestion instanceof WritingQuestion)
				viewHelper = new WritingViewManager(this);

			// get questions
			remainingQuestions = new ArrayList<Question>(quiz.getQuestions());
			alreadyPlayed = new ArrayList<Question>();

			// initialize sound
			soundPlayer = new SoundMediaPlayer(this);
			this.loadSounds();

			// play first question
			this.playNextQuestion();
		}
	}

	private void loadSounds() {
		if (quiz.getGoodResultAction() != null
				&& quiz.getGoodResultAction() instanceof SpeechResultAction) {
			String file = ((SpeechResultAction) quiz.getGoodResultAction())
					.getSpeechFile().getFile();
			file = quiz.getResPrefix() + file;
			Log.d(TAG, "loading good result sound: file " + file);
			this.idGoodResultSound = soundPlayer.loadFile(file);
			Log.d(TAG, " -> id=" + idGoodResultSound);
		}
		if (quiz.getBadResultAction() != null
				&& quiz.getBadResultAction() instanceof SpeechResultAction) {
			String file = ((SpeechResultAction) quiz.getBadResultAction())
					.getSpeechFile().getFile();
			file = quiz.getResPrefix() + file;
			Log.d(TAG, "loading bad result sound: file " + file);
			this.idBadResultSound = soundPlayer.loadFile(file);
			Log.d(TAG, " -> id=" + idBadResultSound);
		}
		if (quiz.getQuizFinishedAction() != null
				&& quiz.getQuizFinishedAction() instanceof SpeechResultAction) {
			String file = ((SpeechResultAction) quiz.getQuizFinishedAction())
					.getSpeechFile().getFile();
			file = quiz.getResPrefix() + file;
			Log.d(TAG, "loading finish result sound: file " + file);
			this.idFinishResultSound = soundPlayer.loadFile(file);
			Log.d(TAG, " -> id=" + idFinishResultSound);
		}

	}

	/**
	 * play the next question of the quiz
	 */
	private void playNextQuestion() {
		viewHelper.emptyLayout();

		if (remainingQuestions.isEmpty()
				|| (quiz.getNbQuestions() > 0 && alreadyPlayed.size() >= quiz
						.getNbQuestions())) {
			// game is finished -> congratulate, return to home
			this.congratulate();
		} else { // play question
			Question q = this.chooseQuestion();
			currentQuestion = q;
			remainingQuestions.remove(q);
			alreadyPlayed.add(q);

			if (q instanceof MultipleChoiceTextQuestion) {
				// show question
				viewHelper.showQuestion(currentQuestion);
			}

			if (q instanceof MultipleChoiceQuestion)
				this.showResponses((MultipleChoiceQuestion) q);
			viewHelper.adaptLayout();
			this.playQuestion(q);
		}
	}

	/**
	 * choose a question to play among the remaining questions
	 * 
	 * @return
	 */
	private Question chooseQuestion() {
		int index = rand.nextInt(remainingQuestions.size());
		return remainingQuestions.get(index);
	}

	/**
	 * display the responses of the question
	 * 
	 * @param question
	 */
	private void showResponses(MultipleChoiceQuestion question) {
		List<MultipleChoiceResponse> responses = question
				.getResponsesWithRandom(quiz);
		Collections.shuffle(responses);

		// draw responses
		for (int i = 0; i < responses.size(); i++) {
			MultipleChoiceResponse resp = responses.get(i);
			viewHelper.showResponse(resp);
		}
	}

	/**
	 * play the question
	 * 
	 * @param question
	 */
	private void playQuestion(Question question) {
		if (question instanceof MultipleChoiceSpokenQuestion) {
			String file = ((MultipleChoiceSpokenQuestion) question)
					.getSpeechFile().getFile();
			file = quiz.getResPrefix() + file;
			int id = soundPlayer.loadFile(file);
			Log.d(TAG, "Loaded question sound file: " + file + " in id=" + id);
			soundPlayer.playFile(id);
		} else if (question instanceof TextToSpeechQuestion) {
			String text = ((TextToSpeechQuestion) question).getText();
			Log.d(TAG, "Loaded text to speech question text:" + text);

			if (ttsManager == null) {
				// initialize it the first time, with initialize text
				this.initializeTTS(text);
			} else {
				ttsManager.playText(text);
			}
		}
		viewHelper.setEnableAll(true);
	}

	private void initializeTTS(String text) {
		ttsManager = new TextToSpeechManager(this, quiz.getLanguage(), text,
				this);
	}

	@Override
	public void onClick(View v) {
		viewHelper.setEnableAll(false);
		String tag = (String) v.getTag();
		Log.d(TAG, "Button clicked: " + v.getTag());
		if (tag == null)
			Log.w(TAG, "Response is null");
		else if (currentQuestion instanceof MultipleChoiceQuestion) {
			if (((MultipleChoiceQuestion) currentQuestion).getCorrectIds()
					.contains(tag)) {
				Log.d(TAG, "correct answer");
				responseCorrect = true;
				correctAnswer(tag);
			} else {
				Log.d(TAG, "incorrect answer");
				responseCorrect = false;
				incorrectAnswer(tag);
			}
		} else {
			Log.d(TAG, "unknown answer type");
			responseCorrect = false;
			incorrectAnswer(tag);
		}
		// next steps are called by callback when sound has finished playing
	}

	public void writingquizOk(View view) {
		String tag = (String) view.getTag();
		if (currentQuestion instanceof WritingQuestion) {
			EditText edit = (EditText) this.findViewById(R.id.textField);
			String response = edit.getText().toString();
			Log.d(TAG, "EditText value=" + response);
			if (response != null
					&& response.toLowerCase().equals(
							((WritingQuestion) currentQuestion).getResponse()
									.toLowerCase())) {
				Log.d(TAG, "correct answer");
				responseCorrect = true;
				correctAnswer(tag);
			} else {
				Log.d(TAG, "incorrect answer");
				responseCorrect = false;
				incorrectAnswer(tag);
			}

		}
	}

	/**
	 * play the action specified when a good/bad answer is given, or when quiz
	 * is finished
	 * 
	 * @param resultAction
	 * @param goodResult
	 * @param finish
	 * @param response
	 */
	private void playResult(ResultAction resultAction, boolean goodResult,
			boolean finish, String response) {

		currentResultAction = resultAction;

		if (resultAction instanceof SpeechResultAction) {
			if (goodResult && idGoodResultSound > -1)
				soundPlayer.playFileSynchronous(idGoodResultSound, this);
			else if (finish && idFinishResultSound > -1) {
				finishMP = ((SoundMediaPlayer) soundPlayer)
						.getMediaPlayer(idFinishResultSound);
				soundPlayer.playFileSynchronous(idFinishResultSound, this);
			} else if (!goodResult && idBadResultSound > -1)
				soundPlayer.playFileSynchronous(idBadResultSound, this);
		} else if (resultAction instanceof TextToSpeechResultAction
				|| resultAction instanceof ReadResultAction) {

			// build text in case of ReadResultAction
			if (resultAction instanceof ReadResultAction) {
				ReadResultAction rrAction = (ReadResultAction) resultAction;
				Log.d(TAG, "ReadResulTAction");

				// find question text
				String questionText = "";
				if (currentQuestion instanceof TextQuestion)
					questionText = ((TextQuestion) currentQuestion).getText();

				currentGoodResponse = "";
				String responseText = "";
				if (currentQuestion instanceof MultipleChoiceQuestion) {
					MultipleChoiceQuestion mcQuestion = (MultipleChoiceQuestion) currentQuestion;

					// find response text
					List<MultipleChoiceResponse> responses = mcQuestion
							.getPossibleResponses();
					Iterator<MultipleChoiceResponse> it = responses.iterator();
					boolean responseFound = false;
					while (it.hasNext() && !responseFound) {
						MultipleChoiceResponse resp = it.next();
						// in case of MultipleChoiceQuestion, response is the id
						// of the response
						if (resp.getId().equals(response)
								&& resp instanceof TextResponse) {
							responseFound = true;
							responseText = ((TextResponse) resp).getText();
							Log.d(TAG, "Response found, text=" + responseText);
						}
					}

					// find good response
					if (goodResult)
						currentGoodResponse = responseText;
					else {
						List<MultipleChoiceResponse> goods = mcQuestion
								.findGoodResponses();
						if (goods.size() > 0) {
							MultipleChoiceResponse goodR = goods.get(0);
							if (goodR instanceof TextResponse) {
								currentGoodResponse = ((TextResponse) goodR)
										.getText();
							}
						}
					}
				} else if (currentQuestion instanceof WritingQuestion) {
					currentGoodResponse = ((WritingQuestion) currentQuestion)
							.getResponse();
					responseText = response;
				}
				rrAction.setText(rrAction.buildText(questionText, responseText,
						currentGoodResponse));
				Log.d(TAG, "ReadResponse, text=" + responseText);
			}

			//in all TextToSpeech cases: play text
			String text = ((TextToSpeechResultAction) resultAction).getText();
			String id = (Integer.valueOf(text.hashCode())).toString();
			if (finish)
				id = FINISH_ID;
			if (ttsManager == null) {
				this.initializeTTS(text);
				// simulate synchronous call
				this.onUtteranceCompleted(id);
			} else {
				ttsManager.playSynchronousText(text, id);
			}

		}
	}

	/**
	 * called by listeners once the result has been played
	 */
	public void afterResultPlayed() {
		// show response dialog if needed
		if (currentResultAction instanceof ReadResultAction
				&& (((ReadResultAction) currentResultAction)
						.isShowResponseDialog() && !responseCorrect)) {
			ResponseDialogFragment dialog = new ResponseDialogFragment();
			Bundle args=new Bundle();
			args.putString("response", currentGoodResponse);
			dialog.setArguments(args);
			dialog.show(getSupportFragmentManager(), "responseDialog");
		} else
			this.continueQuiz();
	}

	/**
	 * called after "afterResultPlayed" method: either directly called, or after the dialog has been closed
	 */
	public void continueQuiz() {
		// Play next question or retry, depending on mode
		if (quiz.getGameMode() == null
				|| quiz.getGameMode() instanceof GameModeRetry) {
			if (responseCorrect)
				this.playNextQuestion();
			else
				this.playQuestion(currentQuestion);
		} else {
			this.playNextQuestion();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mp.equals(finishMP))
			this.finishQuiz();
		else
			this.afterResultPlayed();
	}

	@Override
	public void onUtteranceCompleted(final String utteranceId) {
		// Warning, OnUtteranceCompletedListener requires "runOnUiThread" !
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "onUttereanceCompleted called");
				if (utteranceId.equals(FINISH_ID))
					finishQuiz();
				else
					afterResultPlayed();
			}
		});

	}

	private void correctAnswer(String id) {
		if (quiz.getGameMode() != null
				&& quiz.getGameMode() instanceof GameModeCountPoints) {
			pointsManager.calculatePointsForAnswer(true,
					(GameModeCountPoints) quiz.getGameMode());
		}

		this.playResult(quiz.getGoodResultAction(), true, false, id);
	}

	private void incorrectAnswer(String id) {
		if (quiz.getGameMode() != null
				&& quiz.getGameMode() instanceof GameModeCountPoints) {
			pointsManager.calculatePointsForAnswer(false,
					(GameModeCountPoints) quiz.getGameMode());
		}

		this.playResult(quiz.getBadResultAction(), false, false, id);
	}

	private void congratulate() {
		// congratulate
		this.playResult(quiz.getQuizFinishedAction(), false, true, null);
	}

	private void finishQuiz() {
		Log.d(TAG, "Finishing quiz");

		if (ttsManager != null)
			ttsManager.finish();

		//show ShowPointsActivity in case mode is GameModeCountPoints 
		if (quiz.getGameMode() != null
				&& quiz.getGameMode() instanceof GameModeCountPoints) {
			int points = this.pointsManager.getTotalPoints();
			Log.d(TAG, "Game finished, total points=" + points);

			Intent i = new Intent(this, ShowPointsActivity.class);
			i.putExtra(ShowPointsActivity.POINTS_EXTRA, points);
			i.putExtra(ShowPointsActivity.QUIZ_ID, quiz.getId());
			this.startActivity(i);
		}

		this.finish();
	}

	@Override
	protected void onDestroy() {
		if (ttsManager != null)
			ttsManager.finish();
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(TAG, "PlayQuizActivity: configuration changed: " + newConfig);

		super.onConfigurationChanged(newConfig);

		// do not call viewHelper.adaptLayout(); : widht/height of layout not
		// changed yet,
		// and ViewTreeObserver will be called when they are anyway
		/*
		 * if(viewHelper!=null){ viewHelper.adaptLayout(); }
		 */

	}
	
	/**
	 * Repeating of question requested
	 */
	public void repeatQuestion(View view){
		this.playQuestion(currentQuestion);
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	public Quiz getCurrentQuiz() {
		return quiz;
	}

}
