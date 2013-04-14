package org.soframel.android.squic.points;

import org.soframel.squic.quiz.mode.GameModeCountPoints;

/**
 * First version of a very simple manager to count points.
 * Next versions could manage different users and store high scores...
 **/
public class PointsManager {
	private int currentPoints=0;
	
	public void calculatePointsForAnswer(boolean correctAnswer, GameModeCountPoints gameMode){
		if(correctAnswer)
			this.currentPoints=this.currentPoints+gameMode.getCorrectAnswerPoints();
		else
			this.currentPoints=this.currentPoints+gameMode.getIncorrectAnswerPoints();
	}
	
	public int getTotalPoints(){
		return currentPoints;
	}
	public void resetPoints(){
		currentPoints=0;
	}
}
