package org.soframel.android.squic.automatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.soframel.squic.quiz.automatic.AutomaticQuestions;
import org.soframel.squic.quiz.automatic.CalculationQuestions;
import org.soframel.squic.quiz.automatic.IntWithDivisors;
import org.soframel.squic.quiz.automatic.Operator;
import org.soframel.squic.quiz.question.MultipleChoiceQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.TextResponse;

import android.util.Log;

/**
 * Simple arithmetic operations
 * @author sophie
 *
 */
public class CalculationQuestionsManager extends AutomaticQuestionsManager {

	private static final String TAG = "CalculationQuestions";
	
	//Computed fields
	private Random random;
	private int minResultValue;
	private int maxResultValue;
	
	//Fields copied from question
	private int nbRandom;
	private int minValue;
	private int maxValue;
	private int nbOperands;
	private Operator operator;		
	
	public CalculationQuestionsManager(CalculationQuestions questions){
		this.setAutomaticQuestions(questions);
		this.nbRandom=questions.getNbRandom();
		this.minValue=questions.getMinValue();
		this.maxValue=questions.getMaxValue();
		this.nbOperands=questions.getNbOperands();
		this.operator=questions.getOperator();
		Log.d(TAG, "new CalculationQuestions with operator="+operator+", nbOperands="+nbOperands+", minValue="+minValue+", maxValue="+maxValue+" and nbRandom="+nbRandom);
		
		random = new Random();		
		this.findMinMaxResultValue();
	}

	
	@Override
	public List<Question> initializeQuestions() {
		List<Question> questions=new ArrayList<Question>();
		for(int i=0;i<this.getAutomaticQuestions().getNbQuestions();i++){
			String text="";
			List<Integer> operandValues=new ArrayList<Integer>(nbOperands);			
			IntWithDivisors previousDivisor=null;
			for(int j=0;j<nbOperands;j++){
				int value=0;
				if(operator.equals(Operator.divide)){
					previousDivisor=this.getNextDivisionOperand(previousDivisor, minValue, maxValue, (j==nbOperands-1));
					value=previousDivisor.getN();
				}
				else
					value=this.getRandomValue(minValue,maxValue);
				operandValues.add(value);
				text=text+(new Integer(value)).toString();
				if(j+1<nbOperands){
					text=text+" "+operator.getCode()+" ";
				}
			}			
			text=text+" = ?";
			MultipleChoiceTextQuestion question=new MultipleChoiceTextQuestion(text);
			questions.add(question);
			
			//get correct response
			int response=this.calculateResponse(operandValues);
			TextResponse correctResponse=new TextResponse();
			correctResponse.setId("0");
			correctResponse.setText((new Integer(response)).toString());
			List<String> correctIds=new ArrayList<String>();
			correctIds.add("0");
			question.setCorrectIds(correctIds);
			List<MultipleChoiceResponse> responses=new ArrayList<MultipleChoiceResponse>();
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
	
	/**
	 * find a random value for division, which is a divisor of the previousValue, first in the minValue,maxValue range, 
	 * and itself has divisors except if lastOperand=true;
	 * @param previousValue
	 * @param minValue
	 * @param maxValue
	 * @param lastOperand
	 * @return
	 */
	public IntWithDivisors getNextDivisionOperand(IntWithDivisors previousValue, int minValue, int maxValue, boolean lastOperand){
		if(previousValue==null){
			boolean hasDivisors=true;		
			int i;
			List<Integer> divisors;
			do{
				i=this.getRandomValue(minValue, maxValue);
				divisors=this.getDivisors(i);
				hasDivisors=(!divisors.isEmpty());
			}while(!hasDivisors);
			return new IntWithDivisors(i, divisors);
		}
		else{
			List<Integer> prevDivisors=previousValue.getDivisors();
			List<Integer> nextDivisors=null;
			int divisor;
			if(prevDivisors.size()==1){
				//only one divisor in the list, there is not choice...
				divisor=prevDivisors.get(0);
				int nextVal=previousValue.getN()/divisor;
				nextDivisors=this.getDivisors(nextVal);
			}
			else{
				do{
					int index=this.getRandomValue(0, prevDivisors.size()-1);
					divisor=prevDivisors.get(index);
					if(!lastOperand){
						int nextVal=previousValue.getN()/divisor;
						nextDivisors=this.getDivisors(nextVal);
						if(previousValue.getN()%divisor!=0)
							Log.w(TAG, "Problem with division: result is not round: n="+previousValue.getN()+", divisor="+divisor);
					}
				}while(!lastOperand && nextDivisors.isEmpty());
			}
			return new IntWithDivisors(divisor, nextDivisors);
		}
	}
	
	/**
	 * For division: get the list of all the divisors of a number n
	 * @param n
	 * @return
	 */
	private List<Integer> getDivisors(int n){
		List<Integer> divisors=new ArrayList<Integer>();
		for(int i=1;i<n;i++){
			if(n%i==0) //this is a divisor
				divisors.add(i);
		}
		return divisors;
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
