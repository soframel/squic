package org.soframel.squic.xml;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.action.SpeechResultAction;
import org.soframel.squic.quiz.question.MultipleChoiceQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.question.SpokenQuestion;
import org.soframel.squic.quiz.response.ColorResponse;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.utils.FilePropertiesResourceProvider;
import org.soframel.squic.utils.SystemOutLogger;

/**
 * tests cases for quiz parser. 
 * TODO: test all kinds of quizzes...
 * @author sophie.ramel
 *
 */
public class XMLQuizConfigParserTestCase {
	
	private XMLQuizConfigParser parser;
	
	@Before
	public void configureParser(){
		SystemOutLogger logger=new SystemOutLogger();
		FilePropertiesResourceProvider provider=new FilePropertiesResourceProvider();
		provider.setFolder("");
		
		parser=new XMLQuizConfigParser(logger, provider);
	}
	
	@Test
	public void testParseSpokenQuiz(){
		InputStream in=this.getClass().getResourceAsStream("/spokenQuiz.xml");
		Quiz quiz=parser.parseQuizConfig(in);
		
		//metadata
		assertEquals("SpokenQuiz", quiz.getName());
		assertEquals("2", quiz.getId());
		assertEquals("couleursfacile", quiz.getIcon());
		
		//questions & responses
		assertEquals(4, quiz.getQuestions().size());
		Question question=quiz.getQuestions().get(0);
		assertTrue(question instanceof SpokenQuestion);
		SpokenQuestion sq=(SpokenQuestion) question;
		assertEquals("choisisrouge", sq.getSpeechFile().getFile());
		assertTrue(question instanceof MultipleChoiceQuestion);
		MultipleChoiceQuestion mcq=(MultipleChoiceQuestion)question;
		assertEquals("1", mcq.getId());
		List<MultipleChoiceResponse> resps=mcq.getPossibleResponses();
		assertEquals(4, resps.size());
		MultipleChoiceResponse resp=resps.get(0);
		assertEquals("red", resp.getId());
		assertEquals("red", mcq.getCorrectIds().get(0));
		assertTrue(resp instanceof ColorResponse);
		assertEquals("red", ((ColorResponse)resp).getColor().getColorCode());
		
		assertEquals(4, quiz.getResponses().size());
		
		//actions
		assertTrue(quiz.getBadResultAction() instanceof SpeechResultAction);
		SpeechResultAction badAction=(SpeechResultAction) quiz.getBadResultAction();
		assertEquals("essaieencore", badAction.getSpeechFile().getFile());
		assertTrue(quiz.getGoodResultAction() instanceof SpeechResultAction);
		SpeechResultAction goodAction=(SpeechResultAction) quiz.getGoodResultAction();
		assertEquals("bravo", goodAction.getSpeechFile().getFile());
		assertTrue(quiz.getQuizFinishedAction() instanceof SpeechResultAction);
		SpeechResultAction finishAction=(SpeechResultAction) quiz.getQuizFinishedAction();
		assertEquals("fini", finishAction.getSpeechFile().getFile());
		
	}
}