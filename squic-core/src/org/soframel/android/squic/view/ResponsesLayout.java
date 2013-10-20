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
package org.soframel.android.squic.view;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.GridLayout;
//compatibility with android <4:
//import android.support.v7.widget.GridLayout;

public class ResponsesLayout extends GridLayout{

	private MultipleChoiceQuizViewManager manager;

	public MultipleChoiceQuizViewManager getManager() {
		return manager;
	}

	public void setManager(MultipleChoiceQuizViewManager manager) {
		this.manager = manager;
	}

	public ResponsesLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/* not needed anymore because of getViewTreeObserver().addOnGlobalLayoutListener    */
	/*@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);		
		manager.adaptLayout();
	} */

	
}
