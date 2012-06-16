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
import org.soframel.android.squic.R;
import org.soframel.android.squic.quiz.ColorResponse;
import org.soframel.android.squic.quiz.ImageResponse;
import org.soframel.android.squic.quiz.Question;
import org.soframel.android.squic.quiz.TextQuestion;
import org.soframel.android.squic.quiz.TextResponse;
import org.soframel.android.squic.quiz.TextQuestionImpl;

//support android <2.2
import android.graphics.Color;
import android.support.v7.widget.GridLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Manages the views and layout of a quiz
 * 
 * @author sophie
 * 
 */
public class QuizViewManager {
	private static final String TAG = "QuizViewManager";
	
	private PlayQuizActivity activity;
	private ResponsesLayout responsesLayout;
	private LinearLayout questionLayout;

	private int itemWidth;
	private int itemHeight;
	private int spaceDimensionV;
	private int spaceDimensionH;
	private float textSize;
	private int responsesLayoutWidth=-1;
	private int responsesLayoutHeight=-1;

	public QuizViewManager(PlayQuizActivity activity, ResponsesLayout responseslayout, LinearLayout questionLayout) {
		this.activity = activity;		
		this.responsesLayout=responseslayout;
		this.questionLayout=questionLayout;
		responsesLayout.setManager(this);
		
		 //make layout adapt each time view is layed out
		 responsesLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
			@Override
			public void onGlobalLayout() {
				int width=responsesLayout.getWidth();
				int heigth=responsesLayout.getHeight();
				//no need to call adaptLayout if width/heigth not calculated yet
				if(width>0 && heigth>0){
					//if values not set yet or if they really changed -> call it
					if(responsesLayoutWidth<0
							|| responsesLayoutHeight<0 
							|| width!=responsesLayoutWidth 
							|| heigth!=responsesLayoutHeight)
						adaptLayout();
				}
			}				 
		 });
	}

	/**
	 * remove elements from the layout
	 */
	public void emptyLayout() {		
		responsesLayout.removeAllViews();
		questionLayout.removeAllViews();
	}
	
	public void setResponsesLayout(ResponsesLayout layout){
		this.responsesLayout=layout;
		responsesLayout.setManager(this);
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
			
			//get dimensions of layout, more exact (- status bar, title, etc) than dimension of window
			responsesLayoutWidth=responsesLayout.getWidth();
			responsesLayoutHeight=responsesLayout.getHeight();
			
			if(responsesLayoutWidth==0 || responsesLayoutHeight==0){
				Log.d(TAG,"width or heigth still to 0 -> doing nothing");
				return ;
			}
			
			//draw question
			if(question instanceof TextQuestion){
				this.drawTextQuestion((TextQuestion) question);
			}	
			
			//Below: calculate optimal dimensions
			
			int width=responsesLayoutWidth;
			int height=responsesLayoutHeight;
			boolean vertical=false;
			if(width<height){	
				//vertical: simulate horizontal screen as values will be switched after
				width=responsesLayout.getHeight();
				height=responsesLayout.getWidth();
				vertical=true;
				//inverse ratio as widthToHeight will always be widthToHeight even if vertical
				widthToHeightRatio=1/widthToHeightRatio;
			}
			
			Log.d(TAG, "Display width=" + width + ", height=" + height);			
	
			// Rows: fixed to 2 for now
			int nbRows=2;
			// number of columns
			int nbResponses = question.getNumberOfResponses();						
			int nbColumns = nbResponses / nbRows;
			if (nbResponses % nbRows > 0)
				nbColumns++;
			Log.d(TAG, "number of  columns: " + nbColumns + ", total number of responses: " + nbResponses);
	
			// set nb columns / rows
			responsesLayout.setRowCount(nbRows);
			responsesLayout.setColumnCount(nbColumns);				
	
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
				responsesLayout.setRowCount(nbColumns);
				responsesLayout.setColumnCount(nbRows);
				Log.d(TAG, "vertical screen: switching dimensions");
			}			
			
			//if text, calculate text size
			textSize=this.getTextResponsesTextSize();				
			
			//adapt layouts of buttons			
			for(int i=0;i<responsesLayout.getChildCount();i++){
				View child=responsesLayout.getChildAt(i);
				if(child instanceof ResponseView){
					this.adaptButtonLayoutParams(child);
				}
			}
		
		}
		else{
			responsesLayoutHeight=0;
			responsesLayoutWidth=0;
		}
	}

	private void drawTextQuestion(TextQuestion question){
		//draw question
		TextView textView=(TextView) questionLayout.getChildAt(0);
		if(textView!=null){
			LinearLayout.LayoutParams questionLayoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
			questionLayoutParams.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
			//width = width of ResponseLayout
			questionLayoutParams.width = responsesLayoutWidth;
			
			//size of text
			/*String text=((TextQuestion)question).getText();					
			float maxQuestionTextSize=this.getTextSize(responsesLayoutWidth, text.length());
			Log.d(TAG, "max question text size: "+maxQuestionTextSize);
			textView.setTextSize(maxQuestionTextSize);*/
			
			//no, use response text size? -> no, unknown yet
			//textView.setTextSize(textSize);
			
			// set height to 1/10th of total height
			LinearLayout quizLayout=(LinearLayout) activity.findViewById(R.id.quizLayout);
			int totalHeight=quizLayout.getHeight();
			int availableHeight=totalHeight/10;
			questionLayoutParams.height=availableHeight;
			
			//base text size on available height
			float qTextSize=this.getTextSizeFromHeight(availableHeight);
			textView.setTextSize(qTextSize);
		}
	}
	
	private float getTextResponsesTextSize(){
		int nbChildren=responsesLayout.getChildCount();
		float textSize=0.0f;
		//if text, calculate text size
		if(responsesLayout.getChildAt(0) instanceof TextResponseView){
			// find the maximum number of characters of a TextResponse
			int maxChars=0;
			for(int i=0;i<nbChildren;i++){
				View child=responsesLayout.getChildAt(i);
				if(child instanceof TextResponseView){
					CharSequence text=((TextResponseView) child).getText();
					int nbChars=text.length();
					if(nbChars>maxChars)
						maxChars=nbChars;
				}
			}					
			//max chars correspond minimum size
			textSize=this.getTextSizeFromWidth(itemWidth, maxChars);
			Log.d(TAG, "Text responses. Max number of characters="+maxChars+", textSize="+textSize);
		}		
		return textSize;
	}
	
	/**
	 * calculate the size of text based on the number of characters to show and the available width
	 * @param width
	 * @param nbChars
	 * @return
	 */
	private float getTextSizeFromWidth(float width, int nbChars){
		return width/(nbChars+2);
	}
	
	private float getTextSizeFromHeight(int height){
		//this is really an approximation !
		return height/2;
	}
	
	/**
	 * adapt button to question layout and add it to layout
	 * 
	 * @param button
	 */
	public void addButtonLayout(View button) {
		Log.d(TAG, "adding button to view");
		responsesLayout.addView(button);
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
		responsesLayout.setEnabled(enabled);
		int nbChildren=responsesLayout.getChildCount();
		for(int i=0;i<nbChildren;i++){
			View child=responsesLayout.getChildAt(i);
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
	
	public void showQuestion(TextQuestion question){
		TextView textView=new TextView(activity);
		textView.setTextColor(Color.BLACK);
		textView.setText(question.getText());
		textView.setGravity(Gravity.CENTER);
		questionLayout.addView(textView);
	}
}
