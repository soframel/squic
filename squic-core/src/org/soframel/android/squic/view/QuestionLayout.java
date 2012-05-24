package org.soframel.android.squic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

public class QuestionLayout extends GridLayout{

	private QuizViewManager manager;

	public QuizViewManager getManager() {
		return manager;
	}

	public void setManager(QuizViewManager manager) {
		this.manager = manager;
	}

	public QuestionLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);		
		manager.adaptLayout();
	}

	
}
