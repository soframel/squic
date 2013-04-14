		package org.soframel.squic.xml;

import java.io.OutputStream;
import java.util.List;

import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.action.ResultAction;
import org.soframel.squic.quiz.action.SpeechResultAction;
import org.soframel.squic.quiz.action.TextToSpeechResultAction;
import org.soframel.squic.quiz.question.MultipleChoiceQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceSpokenQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextToSpeechQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.question.TextQuestion;
import org.soframel.squic.quiz.question.TextToSpeechQuestion;
import org.soframel.squic.quiz.question.WritingQuestion;
import org.soframel.squic.quiz.response.ColorResponse;
import org.soframel.squic.quiz.response.ImageResponse;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.TextResponse;
import org.soframel.squic.utils.SquicLogger;

/**
 * Serializer for XML Quizzes. 
 * This serializer is implemented in plain old java, because it must work in Android apps. 
 * @author sophie.ramel
 *
 */
public class XMLQuizConfigSerializer implements QuizConfigSerializer{

	private final static String NEWLINE="\n";
	
	private SquicLogger logger;
		
	public XMLQuizConfigSerializer(SquicLogger logger){
		this.logger=logger;
	}
	
	/**
	 * Serializes quiz. 
	 * Header in the form: 
	 * <?xml version="1.0" encoding="UTF-8"?>
		<quiz id="2" name="SpokenQuiz" icon="couleursfacile"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" >
	 */
	public String serializeQuizConfig(Quiz quiz) {
		StringBuilder s=new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>"+NEWLINE+"<quiz ");
		
		String id=quiz.getId();
		s.append(" id='"+id+"'");
		
		String name=quiz.getName();
		s.append(" name='"+name+"'");
		
		String icon=quiz.getIcon();
		s.append(" icon='"+icon+"'");
		
		s.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"+NEWLINE);
		
		this.serializeActions(quiz, s);
		this.serializeQuestions(quiz, s);
		this.serializeResponses(quiz, s);
		
		s.append("</quiz>");
		
		return s.toString();
	}
	
	/** serializes actions like: 
	 *  <resultAction>
		<goodResultAction xsi:type="speechResultAction">
			<speechFile file="bravo"/>
		</goodResultAction>
		<badResultAction xsi:type="textToSpeechResultAction">
			<text>Essaie encore</text>
		</badResultAction>
		<quizFinishedAction xsi:type="speechResultAction">
			<speechFile file="fini"/>
		</quizFinishedAction>
	</resultAction>
	 * @param quiz
	 * @param s
	 */
	private void serializeActions(Quiz quiz, StringBuilder s){
		s.append("<resultAction>"+NEWLINE);
		s.append("<goodResultAction ");
		this.serializeAction(quiz.getGoodResultAction(), s);
		s.append("</goodResultAction>"+NEWLINE);
		s.append("<badResultAction ");
		this.serializeAction(quiz.getBadResultAction(), s);
		s.append("</badResultAction>"+NEWLINE);
		s.append("<quizFinishedAction ");
		this.serializeAction(quiz.getQuizFinishedAction(), s);
		s.append("</quizFinishedAction>"+NEWLINE);
		s.append("</resultAction>"+NEWLINE);
	}
	private void serializeAction(ResultAction a, StringBuilder s){
		if(a instanceof SpeechResultAction)
			this.serializeSpeechResultAction((SpeechResultAction)a, s);
		else if(a instanceof TextToSpeechResultAction)
			this.serializeTextToSpeechResultAction((TextToSpeechResultAction)a, s);
		else
			logger.warn("ResultAction not supported: "+a.getClass().getName());
	}
	
	private void serializeSpeechResultAction(SpeechResultAction action, StringBuilder s){
		s.append("xsi:type='speechResultAction'>"+NEWLINE);
		s.append("<speechFile file='");
		s.append(action.getSpeechFile().getFile());
		s.append("'/>"+NEWLINE);		
	}
	private void serializeTextToSpeechResultAction(TextToSpeechResultAction action, StringBuilder s){
		s.append("xsi:type='textToSpeechResultAction'>"+NEWLINE);
		s.append("<text>");
		s.append(action.getText());
		s.append("</text>"+NEWLINE);		
	}

	/** serializes questions like: 
	 *  <questions>
    	<question id="1" level="easy" xsi:type="spokenQuestion">
    	<possibleResponses>
    		<responseRef id="red" correct="true"/>
    		<responseRef id="blue"/>
    		<responseRef id="green"/>
    		<responseRef id="yellow"/>
    	</possibleResponses>
    	<speechFile file="choisisrouge"/>
    	</question>  
    	...  
	 * @param quiz
	 * @param s
	 */
	private void serializeQuestions(Quiz quiz, StringBuilder s){
		List<Question> questions=quiz.getQuestions();
		s.append("<questions>"+NEWLINE);
		for(Question q: questions){
			s.append("<question xsi:type='");
			s.append(this.getQuestionType(q));
			s.append("'");
			if(q instanceof MultipleChoiceQuestion){
				this.serializeMultipleChoiceQuestion((MultipleChoiceQuestion) q, s);
			}
			s.append("</question>"+NEWLINE);
		}
		s.append("</questions>"+NEWLINE);
	}
	
	private String getQuestionType(Question q){
		if(q instanceof MultipleChoiceQuestion){
			if(q instanceof MultipleChoiceSpokenQuestion)
				return "spokenQuestion";
			else if(q instanceof MultipleChoiceTextQuestion)
				return "textQuestion";
			else if(q instanceof MultipleChoiceTextToSpeechQuestion)
				return "textToSpeechQuestion";
			else
				logger.warn("Unsupported question type: "+q.getClass().getName());
		} 
		else
			logger.warn("Unsupported question type: "+q.getClass().getName());
		return null;
	}
	
	/**
	 * Reference of the form: 
	 * <responseRef id="red" correct="true"/>
	 * @param q
	 * @param s
	 */
	private void serializeMultipleChoiceQuestion(MultipleChoiceQuestion q, StringBuilder s){
		s.append(" id='");
		s.append(q.getId());
		s.append("' level='");
		s.append(q.getLevel().value());
		s.append("'>"+NEWLINE);
		
		List<MultipleChoiceResponse> responses=q.getPossibleResponses();
		for(MultipleChoiceResponse resp: responses){
			this.serializeMultipleChoiceResponseReference((MultipleChoiceResponse)resp, s, q.getCorrectIds());
		}
		
		if(q instanceof MultipleChoiceSpokenQuestion)
			this.serializeMultipleChoiceSpokenQuestion((MultipleChoiceSpokenQuestion) q, s);
		else if(q instanceof MultipleChoiceTextQuestion)
			this.serializeMultipleChoiceTextQuestion((MultipleChoiceTextQuestion)q, s);
		else if(q instanceof MultipleChoiceTextToSpeechQuestion)
			this.serializeMultipleChoiceTextToSpeechQuestion((MultipleChoiceTextToSpeechQuestion)q, s);
		else
			logger.warn("Question type not supported: "+q.getClass().getName());
	}
	private void serializeMultipleChoiceResponseReference(MultipleChoiceResponse resp, StringBuilder s, List<String> correctIds){
		s.append("<responseRef id='");
		s.append(resp.getId());
		s.append("'");
		if(correctIds.contains(resp.getId()))
			s.append(" correct='true'");
		s.append("/>"+NEWLINE);
	}
	/**
	 * example: <speechFile file="choisisrouge"/>
	 * @param q
	 * @param s
	 */
	private void serializeMultipleChoiceSpokenQuestion(MultipleChoiceSpokenQuestion q, StringBuilder s){
		s.append("<speechFile file='");
		s.append(q.getSpeechFile().getFile());
		s.append("' />"+NEWLINE);
	}
	private void serializeMultipleChoiceTextQuestion(MultipleChoiceTextQuestion q, StringBuilder s){
		s.append("<text>");
		s.append(q.getText());
		s.append("</text>"+NEWLINE);
	}
	private void serializeMultipleChoiceTextToSpeechQuestion(MultipleChoiceTextToSpeechQuestion q, StringBuilder s){
		s.append("<text>");
		s.append(q.getText());
		s.append("</text>"+NEWLINE);
	}
	
	/**
	 * example: 
	 *  <responses>   
		    <response id="cat" xsi:type="imageResponse">
		    	<imageFile file="cat"/>
		    </response> 
	 * @param quiz
	 * @param s
	 */
	private void serializeResponses(Quiz quiz, StringBuilder s){
		s.append("<responses>"+NEWLINE);
		for(MultipleChoiceResponse r: quiz.getResponses()){
			this.serializeMultipleChoiceResponse(r, s);
		}
		s.append("</responses>");
	}
	private void serializeMultipleChoiceResponse(MultipleChoiceResponse r, StringBuilder s){
		s.append("<response id='");
		s.append(r.getId());
		s.append("' xsi:type='");
		s.append(this.getResponseType(r));
		s.append("'>"+NEWLINE);
		if(r instanceof ColorResponse)
			this.serializeColorResponse((ColorResponse) r, s);
		else if(r instanceof ImageResponse)
			this.serializeImageResponse((ImageResponse) r, s);
		else if(r instanceof TextResponse)
			this.serializeTextResponse((TextResponse) r, s);
		s.append("</response>"+NEWLINE);
	}
	private String getResponseType(MultipleChoiceResponse r){
		if(r instanceof ColorResponse)
			return "colorResponse";
		else if(r instanceof ImageResponse)
			return "imageResponse";
		else if(r instanceof TextResponse)
			return "textResponse";
		else
			logger.warn("Response not supported: "+r.getClass().getName());
		return null;
	}
	
	/**
	 * <color colorCode="red"/>
	 * @param c
	 * @param s
	 */
	private void serializeColorResponse(ColorResponse r, StringBuilder s){
		s.append("<color colorCode='");
		s.append(r.getColor().getColorCode());
		s.append("' />"+NEWLINE);
	}
	/**
	 * <imageFile file="cat"/>
	 * @param c
	 * @param s
	 */
	private void serializeImageResponse(ImageResponse r, StringBuilder s){
		s.append("<imageFile file='");
		s.append(r.getImageFile());
		s.append("' />"+NEWLINE);
	}
	/**
	 * <text>Gilles</text>
	 * @param c
	 * @param s
	 */
	private void serializeTextResponse(TextResponse r, StringBuilder s){
		s.append("<text>");
		s.append(r.getText());
		s.append("</text>"+NEWLINE);
	}
}
