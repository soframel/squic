package org.soframel.squic.io.xml;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.action.SpeechResultAction;
import org.soframel.squic.quiz.action.TextToSpeechResultAction;
import org.soframel.squic.quiz.question.MultipleChoiceQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.question.SpokenQuestion;
import org.soframel.squic.quiz.question.initializable.ReadingQuestions;
import org.soframel.squic.quiz.question.initializable.WordQuestions;
import org.soframel.squic.quiz.response.ColorResponse;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.utils.FileResourceProvider;
import org.soframel.squic.utils.SystemOutLogger;
import org.soframel.squic.utils.URLResourceProvider;

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
		FileResourceProvider fileProvider=new FileResourceProvider();
        fileProvider.setFolder("");
        URLResourceProvider urlProvider=new URLResourceProvider();
		
		parser=new XMLQuizConfigParser(logger, fileProvider, urlProvider);
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

    @Test
    public void testParseReadingQuiz(){
        InputStream in=this.getClass().getResourceAsStream("/readingQuiz.xml");
        Quiz quiz=parser.parseQuizConfig(in);

        //metadata
        assertEquals("testQuiz", quiz.getName());
        assertEquals("id", quiz.getId());
        assertEquals("myIcon", quiz.getIcon());
        String lng=quiz.getLanguage().getLanguage();
        assertEquals("de", lng);
        assertEquals(2.0, quiz.getWidthToHeightResponsesRatio(), 0);

        //actions
        assertTrue(quiz.getBadResultAction() instanceof TextToSpeechResultAction);
        TextToSpeechResultAction badAction=(TextToSpeechResultAction) quiz.getBadResultAction();
        assertEquals("incorrect", badAction.getText());
        assertTrue(quiz.getGoodResultAction() instanceof SpeechResultAction);
        SpeechResultAction goodAction=(SpeechResultAction) quiz.getGoodResultAction();
        assertEquals("goodFile", goodAction.getSpeechFile().getFile());
        assertTrue(quiz.getQuizFinishedAction() instanceof TextToSpeechResultAction);
        TextToSpeechResultAction finishAction=(TextToSpeechResultAction) quiz.getQuizFinishedAction();
        assertEquals("finished", finishAction.getText());

        //questions & responses
        assertNotNull(quiz.getInitializableQuestions());
        ReadingQuestions questions=(ReadingQuestions) quiz.getInitializableQuestions();
        assertEquals(7, questions.getNbRandom());
        assertEquals(2, quiz.getNbQuestions());
        assertEquals("How do you spell ", questions.getQuestionPrefix());
        assertEquals("?", questions.getQuestionSuffix());
        assertEquals("dictionary_en", questions.getDictionaryResource());
        assertEquals(WordQuestions.DictionaryType.file, questions.getDictionaryType());

    }
}