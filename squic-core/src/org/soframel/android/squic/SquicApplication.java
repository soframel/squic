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

import java.util.Map;

import android.app.Application;

import org.soframel.android.squic.quiz.Quiz;

public class SquicApplication extends Application {
	private Map<String,Quiz> quizzes;

	public Map<String,Quiz> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(Map<String,Quiz> quizzes) {
		this.quizzes = quizzes;
	}
}
