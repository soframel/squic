package org.soframel.squic.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.soframel.squic.quiz.Level;
import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.action.SpeechResultAction;
import org.soframel.squic.quiz.action.TextToSpeechResultAction;
import org.soframel.squic.quiz.media.Color;
import org.soframel.squic.quiz.media.SoundFile;
import org.soframel.squic.quiz.question.MultipleChoiceSpokenQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextToSpeechQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.response.ColorResponse;
import org.soframel.squic.quiz.response.ImageResponse;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.TextResponse;
import org.soframel.squic.utils.SystemOutLogger;
import org.xml.sax.SAXException;

/**
 * tests cases for quiz parser. 
 * TODO: test all kinds of quizzes...
 * @author sophie.ramel
 *
 */
public class XMLQuizConfigSerializerTestCase {
	
	private XMLQuizConfigSerializer serializer;
	
	@Before
	public void configureParser(){
		SystemOutLogger logger=new SystemOutLogger();
		
		serializer=new XMLQuizConfigSerializer(logger);
	}
	
	@Test
	public void testSerializeSimpleQuiz() throws IOException, SAXException{
		Quiz quiz=new Quiz();
		quiz.setId("id");
		quiz.setName("testQuiz");
		quiz.setIcon("myIcon");
		
		//actions
		SpeechResultAction gAction=new SpeechResultAction();
		SoundFile soundFile=new SoundFile();
		soundFile.setFile("goodFile");
		gAction.setSpeechFile(soundFile);
		quiz.setGoodResultAction(gAction);
		
		TextToSpeechResultAction bAction=new TextToSpeechResultAction();
		bAction.setText("incorrect");
		quiz.setBadResultAction(bAction);
		
		TextToSpeechResultAction fAction=new TextToSpeechResultAction();
		fAction.setText("finished");
		quiz.setQuizFinishedAction(fAction);
		
		//Responses
		List<MultipleChoiceResponse> responses=new ArrayList<MultipleChoiceResponse>();
		quiz.setResponses(responses);
		ColorResponse r1=new ColorResponse();
		r1.setId("1");
		Color color=new Color();		
		color.setColorCode("blue");
		r1.setColor(color);
		responses.add(r1);
		
		ImageResponse r2=new ImageResponse();
		r2.setId("2");
		r2.setImageFile("imageFile");
		responses.add(r2);
		
		TextResponse r3=new TextResponse();
		r3.setId("3");
		r3.setText("responseText");
		responses.add(r3);
		
		//Questions
		List<Question> questions=new ArrayList<Question>();
		quiz.setQuestions(questions);
		
		MultipleChoiceSpokenQuestion q1=new MultipleChoiceSpokenQuestion();
		questions.add(q1);
		q1.setId("1");
		q1.setCorrectIds(Arrays.asList("1"));
		q1.setLevel(Level.EASY);
		q1.setNbRandomResponses(1);
		SoundFile soundFile2=new SoundFile();
		soundFile2.setFile("questionFile");
		q1.setSpeechFile(soundFile2);
		List<MultipleChoiceResponse> possibleResponses1=new ArrayList<MultipleChoiceResponse>();
		possibleResponses1.add(r1);
		possibleResponses1.add(r2);
		q1.setPossibleResponses(possibleResponses1);
		
		MultipleChoiceTextQuestion q2=new MultipleChoiceTextQuestion("textQuestion");
		questions.add(q2);
		q2.setId("2");
		q2.setCorrectIds(Arrays.asList("2"));
		q2.setLevel(Level.NORMAL);
		q2.setNbRandomResponses(2);		
		List<MultipleChoiceResponse> possibleResponses2=new ArrayList<MultipleChoiceResponse>();
		possibleResponses2.add(r1);
		possibleResponses2.add(r2);
		q2.setPossibleResponses(possibleResponses2);
		
		MultipleChoiceTextToSpeechQuestion q3=new MultipleChoiceTextToSpeechQuestion();
		questions.add(q3);
		q3.setId("3");
		q3.setCorrectIds(Arrays.asList("3"));
		q3.setLevel(Level.HARD);
		q3.setNbRandomResponses(3);	
		q3.setText("speak");
		List<MultipleChoiceResponse> possibleResponses3=new ArrayList<MultipleChoiceResponse>();
		possibleResponses3.add(r2);
		possibleResponses3.add(r3);
		q3.setPossibleResponses(possibleResponses3);
		
		String result=serializer.serializeQuizConfig(quiz);
		System.out.println("result="+result);
		InputStream refIS=this.getClass().getResourceAsStream("/simpleQuiz.xml");
		String ref=IOUtils.toString(refIS);
		System.out.println("ref="+ref);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreComments(true);
		XMLAssert.assertXMLEqual(ref, result);
	}
}
