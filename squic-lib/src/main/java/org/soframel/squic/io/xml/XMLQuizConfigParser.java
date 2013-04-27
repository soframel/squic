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
package org.soframel.squic.io.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.soframel.squic.quiz.question.initializable.*;
import org.soframel.squic.io.QuizConfigParser;
import org.soframel.squic.quiz.automatic.Operator;
import org.soframel.squic.utils.ResourceProvider;
import org.soframel.squic.utils.SquicLogger;
import org.soframel.squic.quiz.Level;
import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.action.ReadResultAction;
import org.soframel.squic.quiz.action.ResultAction;
import org.soframel.squic.quiz.action.SpeechResultAction;
import org.soframel.squic.quiz.action.TextToSpeechResultAction;
import org.soframel.squic.quiz.automatic.CalculationQuestions;
import org.soframel.squic.quiz.media.Color;
import org.soframel.squic.quiz.media.SoundFile;
import org.soframel.squic.quiz.mode.GameMode;
import org.soframel.squic.quiz.mode.GameModeCountPoints;
import org.soframel.squic.quiz.mode.GameModeRetry;
import org.soframel.squic.quiz.question.MultipleChoiceQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceSpokenQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextQuestion;
import org.soframel.squic.quiz.question.MultipleChoiceTextToSpeechQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.question.TextQuestion;
import org.soframel.squic.quiz.response.ColorResponse;
import org.soframel.squic.quiz.response.ImageResponse;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.TextResponse;
import org.soframel.squic.quiz.reward.IntentReward;
import org.soframel.squic.quiz.reward.Reward;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


/**
 * Parser for XML Quizzes. 
 * This parser is implemented in plain old JAXP, because it must work in Android apps. 
 * @author sophie.ramel
 *
 */
public class XMLQuizConfigParser implements QuizConfigParser {
	
	//private static final String NAMESPACE_QUIZ="org.soframel.android.kidsquiz.quiz";
	private static final String NAMESPACE_XSI="http://www.w3.org/2001/XMLSchema-instance";
	
	private SquicLogger logger;
	private ResourceProvider propertiesProvider;
	
	public XMLQuizConfigParser(SquicLogger logger, ResourceProvider propertiesProvider){
		this.logger=logger;
		this.propertiesProvider=propertiesProvider;
	}
	
	public Quiz parseQuizConfig(InputStream in){
		DocumentBuilderFactory fact=DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc=null;
		try {
			builder = fact.newDocumentBuilder();
			doc=builder.parse(in);
		} catch (ParserConfigurationException e) {
			logger.warn("ParserConfigurationException while creating new DocumentBuilder", e);
		} catch (SAXException e) {
			logger.warn("SAXException while loading InputStream", e);
		} catch (IOException e) {
			logger.warn("IOException while loading InputStream", e);
		}
		
		if(doc!=null)
			return this.loadQuiz(doc);
		else
			return null;
	}
	
	private Quiz loadQuiz(Document doc){
		Element quizEl=doc.getDocumentElement();		
		Quiz quiz=new Quiz();
		String quizName=quizEl.getAttribute("name");
		quiz.setName(quizName);
		String id=quizEl.getAttribute("id");
		quiz.setId(id);
		String language=quizEl.getAttribute("language");
		if(language!=null && !language.equals("")){
			quiz.setLanguage(this.getLocaleFromLanguage(language));
		}
		String icon=quizEl.getAttribute("icon");
		if(icon!=null && !icon.equals("")){
			quiz.setIcon(icon);
		}
		String ratioS=quizEl.getAttribute("widthToHeightResponsesRatio");
		if(ratioS!=null && !ratioS.equals("")){
			try{
				float ratio=Float.parseFloat(ratioS);
				if(ratio>0){
					logger.debug( "Setting widthToHeightResponsesRatio to "+ratioS);
					quiz.setWidthToHeightResponsesRatio(ratio);
				}
			}catch(NumberFormatException ex){
				logger.warn("NumberFormatException while reading widthToHeightResponsesRatio: "+ratioS);
			}
		}
		
		//game mode
		GameMode mode=this.loadGameMode(quizEl);
		quiz.setGameMode(mode);
		
		//result actions
		this.loadResultActions(quiz, quizEl);
		
		//responses
		Map<String,MultipleChoiceResponse> responses=this.loadResponses(quizEl);
		ArrayList<MultipleChoiceResponse> responsesList=new ArrayList<MultipleChoiceResponse>(responses.size());
		responsesList.addAll(responses.values());
		quiz.setResponses(responsesList);
		
		//questions
		NodeList readingQuestions=quizEl.getElementsByTagName("readingQuestions");
		NodeList genreQuestions=quizEl.getElementsByTagName("genreQuestions");
		NodeList writingQuestions=quizEl.getElementsByTagName("writingQuestions");
		if(readingQuestions!=null && readingQuestions.getLength()>0){
			this.loadReadingQuestions((Element)readingQuestions.item(0), quiz);
		}
		else if(genreQuestions!=null && genreQuestions.getLength()>0){
			this.loadGenreQuestions((Element)genreQuestions.item(0), quiz);
		}
		else if(writingQuestions!=null && writingQuestions.getLength()>0){
			this.loadWritingQuestions((Element)writingQuestions.item(0), quiz);
		}
		else{
			this.loadQuestions(quizEl, quiz, responses);
		}
		
		logger.debug( "Loaded Quiz:"+quiz.toString());
		
		return quiz;
	}
	
	
	
	/**
	 * convert between an xsd:language and a locale language. 
	 * only supports English, French, German, Italian. 
	 * @param language
	 * @return
	 */
	private Locale getLocaleFromLanguage(String language){
		if(language.equals("fr"))
			return Locale.FRENCH;
		else if(language.equals("en"))
			return Locale.ENGLISH;
		else if(language.equals("de"))
			return Locale.GERMAN;
		else if(language.equals("it"))
			return Locale.ITALIAN;
		else
			return null;
	}
	
	private void loadResultActions(Quiz quiz, Element quizEl){
		Element resultEl=(Element) quizEl.getElementsByTagName("resultAction").item(0);
		//good result
		Element goodResultEl=(Element) resultEl.getElementsByTagName("goodResultAction").item(0);
		String xsitype=goodResultEl.getAttributeNS(NAMESPACE_XSI, "type");
		if(xsitype!=null && xsitype.equals("speechResultAction")){
			quiz.setGoodResultAction(this.loadSpeechResultAction(quiz, goodResultEl));
		}
		else if(xsitype!=null && xsitype.equals("readResultAction")){
			quiz.setGoodResultAction(this.loadReadResultAction(quiz, goodResultEl));
		}
		else if(xsitype!=null && xsitype.equals("textToSpeechResultAction")){
			quiz.setGoodResultAction(this.loadTextToSpeechResultAction(quiz, goodResultEl));
		}
		//Bad result
		Element badResultEl=(Element) resultEl.getElementsByTagName("badResultAction").item(0);
		String xsitype2=badResultEl.getAttributeNS(NAMESPACE_XSI, "type");
		if(xsitype2!=null && xsitype2.equals("speechResultAction")){
			quiz.setBadResultAction(this.loadSpeechResultAction(quiz, badResultEl));
		}
		else if(xsitype!=null && xsitype.equals("readResultAction")){
			quiz.setBadResultAction(this.loadReadResultAction(quiz, badResultEl));
		}
		else if(xsitype2!=null && xsitype2.equals("textToSpeechResultAction")){
			quiz.setBadResultAction(this.loadTextToSpeechResultAction(quiz, badResultEl));
		}
		
		//Quiz finished
		Element finishedResultEl=(Element) resultEl.getElementsByTagName("quizFinishedAction").item(0);
		String xsitype3=finishedResultEl.getAttributeNS(NAMESPACE_XSI, "type");
		if(xsitype3!=null && xsitype3.equals("speechResultAction")){
			quiz.setQuizFinishedAction(this.loadSpeechResultAction(quiz, finishedResultEl));
		}
		else if(xsitype!=null && xsitype.equals("readResultAction")){
			quiz.setQuizFinishedAction(this.loadReadResultAction(quiz, finishedResultEl));
		}
		else if(xsitype3!=null && xsitype3.equals("textToSpeechResultAction")){
			quiz.setQuizFinishedAction(this.loadTextToSpeechResultAction(quiz, finishedResultEl));
		}
	}
	
	private ResultAction loadSpeechResultAction(Quiz quiz, Element resultEl){
		SpeechResultAction resultA=new SpeechResultAction();
		SoundFile sfile=this.loadSoundFile(resultEl);
		resultA.setSpeechFile(sfile);
		return resultA;
	}
	private ResultAction loadTextToSpeechResultAction(Quiz quiz, Element resultEl){
		TextToSpeechResultAction result=new TextToSpeechResultAction();
		String s=this.loadTextValueChild(resultEl);
		result.setText(s);
		return result;
	}
	private ResultAction loadReadResultAction(Quiz quiz, Element resultEl){
		ReadResultAction result=new ReadResultAction();		

		NodeList nodes=resultEl.getChildNodes();
		List items=new ArrayList();
		for(int i=0;i<nodes.getLength();i++){
			Node n=nodes.item(i);
			if(n instanceof Element){
				Element el=(Element)n;
				if(el.getLocalName().equals("question"))
					items.add(ReadResultAction.specialAction.QUESTION);
				else if(el.getLocalName().equals("response"))
					items.add(ReadResultAction.specialAction.RESPONSE);
				else if(el.getLocalName().equals("goodResponse"))
					items.add(ReadResultAction.specialAction.GOODRESPONSE);
				else if(el.getLocalName().equals("text"))
					items.add(el.getTextContent());
				else
					logger.warn("ReadResultAction element not recognized: "+el.getNodeName());
			}
		}
		result.setItems(items);
		
		String showDialog=resultEl.getAttribute("showResponseDialog");
		if(showDialog!=null && showDialog.equals("true"))
			result.setShowResponseDialog(true);
		else
			result.setShowResponseDialog(false);
		
		return result;
	}
	
	private Map<String,MultipleChoiceResponse> loadResponses(Element quizEl){
		HashMap<String,MultipleChoiceResponse> responses=new HashMap<String,MultipleChoiceResponse>();
		NodeList responsesList=quizEl.getElementsByTagName("responses");
		if(responsesList.getLength()>0){
			Element responsesEl=(Element) responsesList.item(0);
			NodeList responseList=responsesEl.getElementsByTagName("response");
			int nbQ=responseList.getLength();
			for(int i=0;i<nbQ;i++){
				Element responseEl=(Element)responseList.item(i);
				MultipleChoiceResponse response=null;
				String xsitype=responseEl.getAttributeNS(NAMESPACE_XSI, "type");
				if(xsitype.endsWith("colorResponse")){
					response=new ColorResponse();
					Element colorEl=(Element) responseEl.getElementsByTagName("color").item(0);
					String colorCode=colorEl.getAttribute("colorCode");
					Color color=new Color();
					color.setColorCode(colorCode);
					((ColorResponse)response).setColor(color);
				}
				else if(xsitype.endsWith("textResponse")){
					response=new TextResponse();
					Element textEl=(Element) responseEl.getElementsByTagName("text").item(0);
					Text text=(Text) textEl.getFirstChild();
					String textS=text.getNodeValue();
					((TextResponse)response).setText(textS);
				}
				else if(xsitype.endsWith("imageResponse")){
					response=new ImageResponse();
					Element imageEl=(Element) responseEl.getElementsByTagName("imageFile").item(0);
					String imageFile=imageEl.getAttribute("file");
					((ImageResponse)response).setImageFile(imageFile);
				}
				else
					response=new MultipleChoiceResponse();
				
				String id=responseEl.getAttribute("id");
				response.setId(id);
				
				responses.put(id, response);
			}
		}
		return responses;
	}

	/**
	 * get the text value from a child <text>...</text> element
	 * @param parent
	 * @return
	 */
	private String loadTextValueChild(Element parent){
		Element textEl=(Element) parent.getElementsByTagName("text").item(0);
		Text text=(Text) textEl.getFirstChild();
		String textS=text.getNodeValue();
		return textS;
	}
	
	
	private void loadQuestions(Element quizEl, Quiz quiz, Map<String,MultipleChoiceResponse> responses){				
		NodeList questionsList=quizEl.getElementsByTagName("questions");
		if(questionsList.getLength()>0){
			Element questionsEl=(Element) questionsList.item(0);
			
			this.setNbQuestions(questionsEl, quiz);
		
			String questionsType=questionsEl.getAttributeNS(NAMESPACE_XSI, "type");
			if(questionsType.endsWith("calculationQuestions")){
				this.loadCalculationQuestions(questionsEl, quiz);
			}
            else if(questionsType.endsWith("readingQuestions")){
                this.loadReadingQuestions(questionsEl, quiz);
            }
            else if(questionsType.endsWith("writingQuestions")){
                this.loadWritingQuestions(questionsEl, quiz);
            }
            else if(questionsType.endsWith("genreQuestions")){
                this.loadGenreQuestions(questionsEl, quiz);
            }
			else{ //normal questions
				
				ArrayList<Question> questions=new ArrayList<Question>();
				NodeList questionList=questionsEl.getElementsByTagName("question");
				int nb=questionList.getLength();
				for(int i=0;i<nb;i++){
					Element questionEl=(Element)questionList.item(i);
					MultipleChoiceQuestion question=null;
					String xsitype=questionEl.getAttributeNS(NAMESPACE_XSI, "type");
					if(xsitype.endsWith("spokenQuestion")){
						question=new MultipleChoiceSpokenQuestion();
						SoundFile sfile=this.loadSoundFile(questionEl);
						((MultipleChoiceSpokenQuestion)question).setSpeechFile(sfile);
					}
					else if(xsitype.endsWith("textToSpeechQuestion")){
						question=new MultipleChoiceTextToSpeechQuestion();
						String textS=this.loadTextValueChild(questionEl);
						((MultipleChoiceTextToSpeechQuestion)question).setText(textS);
					}
					else if(xsitype.endsWith("textQuestion")){
						question=new MultipleChoiceTextQuestion(xsitype);
						String textS=this.loadTextValueChild(questionEl);
						((TextQuestion)question).setText(textS);
					}
					else
						question=new MultipleChoiceQuestion();
					
					question.setId(questionEl.getAttribute("id"));
					String level=questionEl.getAttribute("level");
					question.setLevel(Level.fromValue(level));
					
					//set responses references
					List<MultipleChoiceResponse> possibleResps=new ArrayList<MultipleChoiceResponse>();
					List<String> correctIds=new ArrayList<String>();
					Element possibleRespEl=(Element) questionEl.getElementsByTagName("possibleResponses").item(0);
					NodeList refs=possibleRespEl.getElementsByTagName("responseRef");
					int nbR=refs.getLength();
					for(int j=0;j<nbR;j++){
						Element ref=(Element) refs.item(j);
						String respId=ref.getAttribute("id");
						String correct=ref.getAttribute("correct");
						if(correct!=null && correct.equals("true"))
							correctIds.add(respId);
						MultipleChoiceResponse resp=responses.get(respId);
						if(resp!=null)
							possibleResps.add(resp);
						else
							logger.warn("Response reference not found: "+respId);
					}
					question.setPossibleResponses(possibleResps);
					question.setCorrectIds(correctIds);			
					//also load random responses
					NodeList randomList=possibleRespEl.getElementsByTagName("randomResponses");
					if(randomList!=null && randomList.getLength()>0){
						Element random=(Element) randomList.item(0);
						String nbRandomS=random.getAttribute("nb");
						int nbRandom=Integer.parseInt(nbRandomS);
						if(nbRandom>0)
							question.setNbRandomResponses(nbRandom);
					}
					
					questions.add(question);
				}
				quiz.setQuestions(questions);
			}
		}
		
	}
	
	private void loadCalculationQuestions(Element questionsEl, Quiz quiz){		
		String nbRandomS=questionsEl.getAttribute("nbRandom");
		int nbRandom=Integer.parseInt(nbRandomS);
		
		String minValueS=questionsEl.getAttribute("minValue");
		int minValue=Integer.parseInt(minValueS);
		
		String maxValueS=questionsEl.getAttribute("maxValue");
		int maxValue=Integer.parseInt(maxValueS);
		
		String nbOperandsS=questionsEl.getAttribute("nbOperands");
		int nbOperands=Integer.parseInt(nbOperandsS);
		
		String operatorS=questionsEl.getAttribute("operator");
		Operator operator=Operator.fromString(operatorS);
		
		CalculationQuestions questions=new CalculationQuestions(quiz.getNbQuestions(), nbRandom, minValue, maxValue, nbOperands, operator);
		quiz.setAutomaticQuestions(questions);
	}

	private void setNbQuestions(Element questionsEl, Quiz quiz){
		String nbQ=questionsEl.getAttribute("nbQuestions");
		if(nbQ!=null && !nbQ.equals("")){
			try{
				int nb=Integer.parseInt(nbQ);
				quiz.setNbQuestions(nb);
			}
			catch(NumberFormatException ex){
				logger.warn("Could not load nbQuestions as an int: "+nbQ);
			}
		}
	}
	
	/**
	 * load readingQuestions by reading automatically different possible questions and responses
	 * @param readingQuestions
	 * @param quiz
	 */
	private void loadReadingQuestions(Element readingQuestions, Quiz quiz){
		this.setNbQuestions(readingQuestions, quiz);
		String qPrefix=readingQuestions.getAttribute("questionPrefix");
		String qSuffix=readingQuestions.getAttribute("questionSuffix");
		String nbRandomS=readingQuestions.getAttribute("nbRandom");
		int nbRandom=0;
		try{
			nbRandom=Integer.parseInt(nbRandomS);
		}catch(NumberFormatException ex){
			logger.error( "NumberFormatException while reading nbRandom="+nbRandom);
		}


        ReadingQuestions questions=new ReadingQuestions();
        questions.setNbRandom(nbRandom);
        questions.setQuestionPrefix(qPrefix);
        questions.setQuestionSuffix(qSuffix);

        this.loadDictionaryElement(readingQuestions, questions);

        quiz.setInitializableQuestions(questions);

	}
	
	/**
	 * load genreQuestions by reading automatically different possible questions and responses
	 * @param readingQuestions
	 * @param quiz
	 */
	private void loadGenreQuestions(Element readingQuestions, Quiz quiz){
		this.setNbQuestions(readingQuestions, quiz);

        GenreQuestions questions=new GenreQuestions();
        this.loadDictionaryElement(readingQuestions, questions);

        quiz.setInitializableQuestions(questions);
	}
	
	/**
	 * load genreQuestions by reading automatically different possible questions and responses
	 * @param quiz
	 */
	private void loadWritingQuestions(Element writingQuestions, Quiz quiz){
		this.setNbQuestions(writingQuestions, quiz);

        String prefix=writingQuestions.getAttribute("questionPrefix");
        String suffix=writingQuestions.getAttribute("questionSuffix");

        WritingQuestions questions=new WritingQuestions();
        questions.setQuestionSuffix(suffix);
        questions.setQuestionPrefix(prefix);
        this.loadDictionaryElement(writingQuestions, questions);

        quiz.setInitializableQuestions(questions);

	}

    private void loadDictionaryElement(Element questionsEl, WordQuestions questions){
        Element dictionaryEl=(Element) questionsEl.getElementsByTagName("dictionary").item(0);
        String dicoRes=dictionaryEl.getTextContent();

        questions.setDictionaryResource(dicoRes);
        questions.setPropertiesProvider(propertiesProvider);
        if(dictionaryEl.hasAttribute("type")){
            String type=dictionaryEl.getAttribute("type");
            if(type!=null && type.equals("url"))
                questions.setDictionaryType(WordQuestions.DictionaryType.url);
            else
                questions.setDictionaryType(WordQuestions.DictionaryType.file);
        }
        else
            questions.setDictionaryType(WordQuestions.DictionaryType.file);
    }

    private List<DictionaryLine> parseDictionary(String dicoRes) throws IOException{
        //int dicoId=activity.getResources().getIdentifier(dicoRes , "raw", activity.getPackageName());
        //InputStream in=activity.getResources().openRawResource(dicoId);
        InputStream in=propertiesProvider.getPropertiesInputStream(dicoRes);
        InputStreamReader reader=new InputStreamReader(in);
        BufferedReader breader=new BufferedReader(reader);
        List<DictionaryLine> lines=new ArrayList<DictionaryLine>();
        String line=null;
        do{
            line=breader.readLine();
            if(line!=null){
                DictionaryLine dl=this.parseDictionaryLine(line);
                lines.add(dl);
            }
        }while(line!=null);

        return lines;
    }
	private DictionaryLine parseDictionaryLine(String line){
		String[] els=line.split(";");
		DictionaryLine dl=new DictionaryLine();
		dl.setGenre(els[0].trim());
		dl.setName(els[1].trim());
		if(els.length>2)
			dl.setImageRef(els[2].trim());
		
		return dl;
	}
	
	private GameMode loadGameMode(Element quizEl){
		
		NodeList gameModes=quizEl.getElementsByTagName("gameMode");
		if(gameModes==null || gameModes.getLength()==0)
			return this.getDefaultGameMode();
		else{
			Element gameMode=(Element) gameModes.item(0);
			if(!gameMode.hasChildNodes()){
				return new GameModeRetry();
			}
			else{
				GameMode mode=null;
				
				Element modeEl=this.getFirstElement(gameMode);
				if(modeEl.getLocalName().equals("retryUntilCorrect"))
					mode=new GameModeRetry();
				else if(modeEl.getLocalName().equals("countPointsInGame")){
					GameModeCountPoints pointsMode=new GameModeCountPoints();
					mode=pointsMode;
					
					//correctPoints
					NodeList correctEls=modeEl.getElementsByTagName("correctAnswerPoints");
					int correctPoints=1;
					if(correctEls!=null && correctEls.getLength()>0){
						String correctPointsS=correctEls.item(0).getTextContent();
						correctPoints=Integer.parseInt(correctPointsS);
					}
					pointsMode.setCorrectAnswerPoints(correctPoints);
					
					//incorrectPoints
					NodeList incorrectEls=modeEl.getElementsByTagName("incorrectAnswerPoints");
					int incorrectPoints=0;
					if(incorrectEls!=null && incorrectEls.getLength()>0){
						String incorrectPointsS=incorrectEls.item(0).getTextContent();
						incorrectPoints=Integer.parseInt(incorrectPointsS);
					}
					pointsMode.setIncorrectAnswerPoints(incorrectPoints);
					
					//Rewards
					NodeList rewards=modeEl.getElementsByTagName("reward");
					if(rewards!=null && rewards.getLength()>0){
						Element rewardEl=(Element) rewards.item(0);
						Reward reward=null;
						
						String xsitype=rewardEl.getAttributeNS(NAMESPACE_XSI, "type");
						if(xsitype!=null && xsitype.equals("intentReward")){
							reward=new IntentReward();
							NodeList actionEls=rewardEl.getElementsByTagName("action");
							if(actionEls!=null && actionEls.getLength()>0){
								Element actionEl=(Element) actionEls.item(0);
								((IntentReward)reward).setAction(actionEl.getTextContent());
							}
							else
								logger.warn("Could not load intentReward: action not found");
							
							NodeList uriEls=rewardEl.getElementsByTagName("uri");
							if(uriEls!=null && uriEls.getLength()>0){
								Element uriEl=(Element) uriEls.item(0);
								((IntentReward)reward).setUri(uriEl.getTextContent());
							}
							else
								logger.warn("Could not load intentReward: action not found");
						}
						
						if(reward!=null){
							//pointsRequired
							int pointsRequired=Integer.parseInt(rewardEl.getAttribute("pointsRequired"));
							reward.setPointsRequired(pointsRequired);
							pointsMode.setReward(reward);
							
							//text
							NodeList textEls=rewardEl.getElementsByTagName("text");
							if(textEls!=null && textEls.getLength()>0){
								Element textEl=(Element) textEls.item(0);
								reward.setText(textEl.getTextContent());
							}
						}
					}
				}
				else
					logger.warn("game mode not recognized: "+modeEl.getLocalName());
				return mode;
			}
		}
	}
	private GameMode getDefaultGameMode(){
		return new GameModeRetry();
	}
	
	private Element getFirstElement(Node n){
		Element first=null;
		NodeList list=n.getChildNodes();
		for(int i=0;i<list.getLength()&&first==null;i++){
			Node child=list.item(i);
			if(child!=null && child instanceof Element){
				first=(Element)child;
			}
		}
		return first;
	}
	
	////// methods for loading resources

	private SoundFile loadSoundFile(Element parent){
		Element speechFile=(Element) parent.getElementsByTagName("speechFile").item(0);
		String file=speechFile.getAttribute("file");
		//String type=speechFile.getAttribute("type");
		SoundFile sfile=new SoundFile();
		sfile.setFile(file);
		//sfile.setType(SoundType.fromValue(type));
		return sfile;
	}
}
