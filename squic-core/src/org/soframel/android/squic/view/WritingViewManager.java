package org.soframel.android.squic.view;

import org.soframel.android.squic.PlayQuizActivity;
import org.soframel.android.squic.R;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.response.Response;

import android.widget.Button;
import android.widget.EditText;

public class WritingViewManager implements ViewManager {

	private PlayQuizActivity activity;
	public WritingViewManager(PlayQuizActivity activity){
		this.activity=activity;
		
		activity.setContentView(R.layout.writingquiz);
	}
	
	@Override
	public void emptyLayout() {
		EditText text=(EditText) activity.findViewById(R.id.textField);
		text.setText("");
	}

	@Override
	public void showQuestion(Question question) {
		//do nothing
	}

	@Override
	public void adaptLayout() {
		//do nothing
	}

	@Override
	public void showResponse(Response resp) {
		//do nothing
	}

	@Override
	public void setEnableAll(boolean enabled) {
		EditText text=(EditText) activity.findViewById(R.id.textField);
		text.setEnabled(enabled);
		Button ok=(Button) activity.findViewById(R.id.writingQuizOk);
		ok.setEnabled(enabled);
	}

}
