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

import org.soframel.android.squic.PlayQuizActivity;
import org.soframel.android.squic.quiz.ColorResponse;
import org.soframel.android.squic.quiz.ImageResponse;
import org.soframel.android.squic.quiz.Question;
import org.soframel.android.squic.quiz.TextResponse;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView.ScaleType;

/**
 * Manages the views and layout of a quiz
 * 
 * @author sophie
 * 
 */
public class QuizViewManager {
	private static final String TAG = "QuizViewManager";
	
	private PlayQuizActivity activity;
	private QuestionLayout myLayout;

	private int itemWidth;
	private int itemHeight;
	private int spaceDimensionV;
	private int spaceDimensionH;
	private float textSize;

	public QuizViewManager(PlayQuizActivity activity, QuestionLayout layout) {
		this.activity = activity;		
		this.myLayout=layout;
		myLayout.setManager(this);
	}


	/**
	 * remove elements from the layout
	 */
	public void emptyLayout() {		
		myLayout.removeAllViews();
	}

	/**
	 * calculate layout as if screen is horizontal
	 * in other case, inverse results...
	 */
	public void adaptLayout() {
		float widthToHeightRatio=activity.getCurrentQuiz().getWidthToHeightResponsesRatio();
		Log.d(TAG, "widthToHeightRatio="+widthToHeightRatio);
		
		Question question=activity.getCurrentQuestion();
		if(question!=null){
			int nbResponses = question.getNumberOfResponses();
			
			// Rows: fixed to 2 for now
			int nbRows=2;
			
			//get dimensions of layout, more exact (- status bar, title, etc) than dimension of window
			int width=myLayout.getWidth();
			int height=myLayout.getHeight();
			boolean vertical=false;
			if(width<height){	
				//vertical: simulate horizontal screen as values will be switched after
				width=myLayout.getHeight();
				height=myLayout.getWidth();
				vertical=true;
				//inverse ratio as widthToHeight will always be widthToHeight even if vertical
				widthToHeightRatio=1/widthToHeightRatio;
			}
			
			Log.d(TAG, "Display width=" + width + ", height=" + height);			
	
			// number of columns
			int nbColumns = nbResponses / nbRows;
			if (nbResponses % nbRows > 0)
				nbColumns++;
			Log.d(TAG, "number of  columns: " + nbColumns + ", total number of responses: " + nbResponses);
	
			// set nb columns / rows
			myLayout.setRowCount(nbRows);
			myLayout.setColumnCount(nbColumns);				
	
			// size of each element of layout:
			//calculate size of spaces with 2 columns
			//spaces are around each item (so between 2 items there are 2 spaces)
			int spaceToItemRatioFixed=3;
			
			//Option A: get space dimension vertically + deduce item dimension
			float spaceDimensionV_A=height/((2*nbRows)+nbRows*spaceToItemRatioFixed);
			float itemHeight_A=spaceDimensionV_A*spaceToItemRatioFixed;
			float itemWidth_A=itemHeight_A*widthToHeightRatio;
			float spaceDimensionH_A=(width-nbColumns*itemWidth_A)/(2*nbColumns);
			
			//Option B: check if itemDimension would be bigger when based on horizontal
			float spaceDimensionH_B=width/((2*nbColumns)+nbColumns*spaceToItemRatioFixed);
			float itemWidth_B=spaceDimensionH_B*spaceToItemRatioFixed;
			float itemHeight_B=itemWidth_B/widthToHeightRatio;
			float spaceDimensionV_B=(height-nbRows*itemHeight_B)/(2*nbRows);
			
			//Choose option
			float fitemWidth;
			float fitemHeight;
			float fspaceDimensionH;
			float fspaceDimensionV;			
			if(spaceDimensionH_A<=0 || 
					(itemWidth_B>itemWidth_A && spaceDimensionV_B>0)){
				Log.d(TAG, "Taking item dimension from horizontal (Option B)");
				fitemWidth=itemWidth_B;
				fitemHeight=itemHeight_B;
				fspaceDimensionH=spaceDimensionH_B;
				fspaceDimensionV=spaceDimensionV_B;
			}else{
				Log.d(TAG, "Taking item dimension from vertical (Option A)");
				fitemWidth=itemWidth_A;
				fitemHeight=itemHeight_A;
				fspaceDimensionH=spaceDimensionH_A;
				fspaceDimensionV=spaceDimensionV_A;
			}			
			//convert floats to ints
			itemWidth=(new Float(fitemWidth)).intValue();
			itemHeight=(new Float(fitemHeight)).intValue();
			spaceDimensionH=(new Float(fspaceDimensionH)).intValue();
			spaceDimensionV=(new Float(fspaceDimensionV)).intValue();
						
			Log.d(TAG, "item width="+itemWidth+", height="+itemHeight+", space dimension vertically="+spaceDimensionV+", horizontally="+spaceDimensionH);
			
			//If screen is vertical: adapt dimensions
			if (vertical) {
				int swap=spaceDimensionV;
				spaceDimensionV=spaceDimensionH;
				spaceDimensionH=swap;
				swap=itemWidth;
				itemWidth=itemHeight;
				itemHeight=swap;
				myLayout.setRowCount(nbColumns);
				myLayout.setColumnCount(nbRows);
				Log.d(TAG, "vertical screen: switching dimensions");
			}
			
			int nbChildren=myLayout.getChildCount();
			
			//if text, calculate text size
			if(myLayout.getChildAt(0) instanceof TextResponseView){
				// find the maximum number of characters of a TextResponse
				int maxChars=0;
				for(int i=0;i<nbChildren;i++){
					View child=myLayout.getChildAt(i);
					if(child instanceof TextResponseView){
						CharSequence text=((TextResponseView) child).getText();
						int nbChars=text.length();
						if(nbChars>maxChars)
							maxChars=nbChars;
					}
				}	
				Log.d(TAG, "Text responses. Max number of characters="+maxChars);
				//max chars correspond minimum size
				textSize=(itemWidth/(maxChars+2));
			}			
			
			//adapt layouts of buttons			
			for(int i=0;i<nbChildren;i++){
				View child=myLayout.getChildAt(i);
				if(child instanceof ResponseView){
					this.adaptButtonLayoutParams(child);
				}
			}
		}
	}

	/**
	 * adapt button to question layout and add it to layout
	 * 
	 * @param button
	 */
	public void addButtonLayout(View button) {
		Log.d(TAG, "adding button to view");
		myLayout.addView(button);
	}

	private void adaptButtonLayoutParams(View button) {
		GridLayout.LayoutParams responsesLayoutParams = (GridLayout.LayoutParams) button.getLayoutParams();
		responsesLayoutParams.setMargins(spaceDimensionH, spaceDimensionV,spaceDimensionH, spaceDimensionV);
		responsesLayoutParams.setGravity(Gravity.LEFT);
		responsesLayoutParams.height = itemHeight;
		responsesLayoutParams.width = (new Float(itemWidth)).intValue();
		
		if(button instanceof TextResponseView){
			TextResponseView tButton=(TextResponseView)button;
			tButton.setTextSize(textSize);
		}
		else if(button instanceof ImageResponseView){
			((ImageResponseView) button).setScaleType(ScaleType.CENTER_INSIDE);
		}
	}
	
	
	public void setEnableAll(boolean enabled){
		myLayout.setEnabled(enabled);
		int nbChildren=myLayout.getChildCount();
		for(int i=0;i<nbChildren;i++){
			View child=myLayout.getChildAt(i);
			if(child instanceof ResponseView){
				child.setEnabled(enabled);
				//Log.d(TAG, "Change enablement state of "+child.getTag()+" to "+enabled);
			}
		}
	}
	
	/**
	 * show one response of type ColorResponse
	 * @param resp
	 */
	public void showColorResponse(ColorResponse resp){
		//Color
		String colorS=resp.getColor().getColorCode();
		
		//Button
		ColorResponseView button=new ColorResponseView(activity, colorS);
		button.setTag(resp.getId());
		button.setOnClickListener(activity);
		button.setEnabled(false);
		//Layout
		this.addButtonLayout(button);
		
		button.requestLayout();
	}
	
	/**
	 * show one response of type ColorResponse
	 * @param resp
	 */
	public void showTextResponse(TextResponse resp){		
		//Button
		TextResponseView button=new TextResponseView(activity, resp.getText());
		button.setTag(resp.getId());
		button.setOnClickListener(activity);
		button.setEnabled(false);
		//Layout
		this.addButtonLayout(button);
		
		button.requestLayout();
	}
	
	/**
	 * show one response of type ColorResponse
	 * @param resp
	 */
	public void showImageResponse(ImageResponse resp){
		//image
		String image=resp.getImageFile();
		image=activity.getCurrentQuiz().getResPrefix()+image;
		
		//Button
		ImageResponseView button=new ImageResponseView(activity, image);
		button.setTag(resp.getId());
		button.setOnClickListener(activity);
		button.setEnabled(false);
		//Layout
		this.addButtonLayout(button);
		
		button.requestLayout();
	}
	
}
