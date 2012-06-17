package org.soframel.android.squic.quiz.automatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.soframel.android.squic.quiz.Question;
import org.soframel.android.squic.quiz.Response;
import org.soframel.android.squic.quiz.TextQuestionImpl;
import org.soframel.android.squic.quiz.TextResponse;

import android.util.Log;

/**
 * Simple arithmetic operations
 * @author sophie
 *
 */
public class CalculationQuestions extends AutomaticQuestions {

	private static final String TAG = "CalculationQuestions";
	
	private Random random;
	
	private int nbRandom;
	private int minValue;
	private int maxValue;
	private int minResultValue;
	private int maxResultValue;
	private int nbOperands;
	private Operator operator;		
	
	public CalculationQuestions(int nbQuestions, int nbRandom, int minValue, int maxValue, int nbOperands, Operator operator){
		super(nbQuestions);
		this.nbRandom=nbRandom;
		this.minValue=minValue;
		this.maxValue=maxValue;
		this.nbOperands=nbOperands;
		this.operator=operator;
		Log.d(TAG, "new CalculationQuestions with operator="+operator+", nbOperands="+nbOperands+", minValue="+minValue+", maxValue="+maxValue+" and nbRandom="+nbRandom);
		
		random = new Random();		
		this.findMinMaxResultValue();
	}
	
	private void findMinMaxResultValue(){		
		if(operator.equals(Operator.plus)){
			maxResultValue=nbOperands*maxValue;
			minResultValue=nbOperands*minValue;
		}
		else if(operator.equals(Operator.minus)){
			maxResultValue=maxValue-((nbOperands-1)*minValue);
			minResultValue=minValue-((nbOperands-1)*maxValue);
		}
		else if(operator.equals(Operator.multiply)){
			maxResultValue=(int) Math.pow(maxValue,nbOperands);
			minResultValue=(int) Math.pow(minValue,nbOperands);
		}
		else if(operator.equals(Operator.divide)){
			maxResultValue=(new Double(maxValue/Math.pow(minValue,(nbOperands-1)))).intValue();
			minResultValue=(new Double(minValue/Math.pow(maxValue,(nbOperands-1)))).intValue();
		}
		Log.d(TAG, "minResultValue="+minResultValue+", maxResultValue="+maxResultValue);
	}
	
	@Override
	public List<Question> initializeQuestions() {
		List<Question> questions=new ArrayList<Question>();
		for(int i=0;i<this.getNbQuestions();i++){
			String text="";
			List<Integer> operandValues=new ArrayList<Integer>(nbOperands);			
			for(int j=0;j<nbOperands;j++){				
				int value=this.getRandomValue(minValue,maxValue);
				operandValues.add(value);
				text=text+(new Integer(value)).toString();
				if(j+1<nbOperands){
					text=text+" "+operator.getCode()+" ";
				}
			}			
			text=text+" = ?";
			TextQuestionImpl question=new TextQuestionImpl(text);
			questions.add(question);
			
			//get correct response
			int response=this.calculateResponse(operandValues);
			TextResponse correctResponse=new TextResponse();
			correctResponse.setId("0");
			correctResponse.setText((new Integer(response)).toString());
			List<String> correctIds=new ArrayList<String>();
			correctIds.add("0");
			question.setCorrectIds(correctIds);
			List<Response> responses=new ArrayList<Response>();
			responses.add(correctResponse);
			List<Integer> responsesValues=new ArrayList<Integer>();
			responsesValues.add(response);
			
			//fill other responses
			for(int j=1;j<=nbRandom;j++){
				int value=this.getRandomValueExcept(minResultValue, maxResultValue, responsesValues);
				responsesValues.add(value);
				TextResponse randResponse=new TextResponse();
				randResponse.setId((new Integer(j)).toString());
				randResponse.setText((new Integer(value)).toString());
				responses.add(randResponse);
			}
			
			question.setPossibleResponses(responses);
		}
		return questions;
	}
	
	private int calculateResponse(List<Integer> operandValues){			
		//first value
		int result=operandValues.get(0);
		//operation
		for(int i=1;i<operandValues.size();i++){
			int value=operandValues.get(i);
			if(operator.equals(Operator.plus))
				result=result+value;
			else if(operator.equals(Operator.minus))
				result=result-value;
			else if(operator.equals(Operator.multiply))
				result=result*value;
			else if(operator.equals(Operator.divide))
				result=result/value;
		}
		return result;
	}
	
	private int getRandomValue(int minValue, int maxValue){
		return random.nextInt(maxValue - minValue) + minValue;
	}
	private int getRandomValueExcept(int minValue, int maxValue, List<Integer> excepts){
		int value=0;
		do{
			value=this.getRandomValue(minValue, maxValue);			
		}
		while(excepts.contains(value));
		return value;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public int getNbRandom() {
		return nbRandom;
	}

	public void setNbRandom(int nbRandom) {
		this.nbRandom = nbRandom;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getNbOperands() {
		return nbOperands;
	}

	public void setNbOperands(int nbOperands) {
		this.nbOperands = nbOperands;
	}


}
