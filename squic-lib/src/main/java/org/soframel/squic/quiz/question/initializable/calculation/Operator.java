package org.soframel.squic.quiz.question.initializable.calculation;

public enum Operator {
	plus("+"), minus("-"), multiply("x"), divide("\\");
	
	private String code;
	Operator(String code){
		this.code=code;
	}
	public String getCode(){
		return code;
	}
	
	public static Operator fromString(String s) {
	    if (s != null) {
	      for (Operator o : Operator.values()) {
	        if (s.equalsIgnoreCase(o.code)) {
	          return o;
	        }
	      }
	    }
	    return null;
	  }

}
