package org.soframel.android.squic.view;

import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.response.Response;

public interface ViewManager {
	public void emptyLayout();
	public void showQuestion(Question question);
	public void adaptLayout();
	public void showResponse(Response resp);
	public void setEnableAll(boolean enabled);
}
