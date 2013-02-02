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
package org.soframel.android.squic.xml;

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

import org.soframel.android.squic.quiz.Color;
import org.soframel.android.squic.quiz.ColorResponse;
import org.soframel.android.squic.quiz.GameMode;
import org.soframel.android.squic.quiz.GameModeCountPoints;
import org.soframel.android.squic.quiz.GameModeRetry;
import org.soframel.android.squic.quiz.ImageResponse;
import org.soframel.android.squic.quiz.IntentReward;
import org.soframel.android.squic.quiz.Level;
import org.soframel.android.squic.quiz.Question;
import org.soframel.android.squic.quiz.Quiz;
import org.soframel.android.squic.quiz.ReadResultAction;
import org.soframel.android.squic.quiz.Response;
import org.soframel.android.squic.quiz.ResultAction;
import org.soframel.android.squic.quiz.Reward;
import org.soframel.android.squic.quiz.SoundFile;
import org.soframel.android.squic.quiz.SpeechResultAction;
import org.soframel.android.squic.quiz.SpokenQuestion;
import org.soframel.android.squic.quiz.TextQuestion;
import org.soframel.android.squic.quiz.TextQuestionImpl;
import org.soframel.android.squic.quiz.TextResponse;
import org.soframel.android.squic.quiz.TextToSpeechQuestion;
import org.soframel.android.squic.quiz.TextToSpeechResultAction;
import org.soframel.android.squic.quiz.automatic.CalculationQuestions;
import org.soframel.android.squic.quiz.automatic.Operator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.util.Log;

public class XMLQuizConfigParser {
	
	private static final String TAG = "XMLQuizConfigParser";
	
	//private static final String NAMESPACE_QUIZ="org.soframel.android.kidsquiz.quiz";
	private static final String NAMESPACE_XSI="http://www.w3.org/2001/XMLSchema-instance";
	
	private Activity activity;
	public XMLQuizConfigParser(Activity activity){
		this.activity=activity;
	}
	
	public Quiz parseXMLQuizConfig(InputStream in){
		DocumentBuilderFactory fact=DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc=null;
		try {
			builder = fact.newDocumentBuilder();
			doc=builder.parse(in);
		} catch (ParserConfigurationException e) {
			Log.w(TAG, "ParserConfigurationException while creating new DocumentBuilder", e);
		} catch (SAXException e) {
			Log.w(TAG, "SAXException while loading InputStream", e);
		} catch (IOException e) {
			Log.w(TAG, "IOException while loading InputStream", e);
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
				int ratio=Integer.parseInt(ratioS);
				if(ratio>0){
					Log.d(TAG, "Setting widthToHeightResponsesRatio to "+ratioS);
					quiz.setWidthToHeightResponsesRatio(ratio);
				}
			}catch(NumberFormatException ex){
				Log.w(TAG, "NumberFormatException while reading widthToHeightResponsesRatio: "+ratioS);
			}
		}
		
		//game mode
		GameMode mode=this.loadGameMode(quizEl);
		quiz.setGameMode(mode);
		
		//result actions
		this.loadResultActions(quiz, quizEl);
		
		//responses
		Map<String,Response> responses=this.loadResponses(quizEl);
		ArrayList<Response> responsesList=new ArrayList<Response>(responses.size());
		responsesList.addAll(responses.values());
		quiz.setResponses(responsesList);
		
		//questions
		NodeList readingQuestions=quizEl.getElementsByTagName("readingQuestions");
		NodeList genreQuestions=quizEl.getElementsByTagName("genreQuestions");
		if(readingQuestions!=null && readingQuestions.getLength()>0){
			this.loadReadingQuestions((Element)readingQuestions.item(0), quiz);
		}
		else if(genreQuestions!=null && genreQuestions.getLength()>0){
			this.loadGenreQuestions((Element)genreQuestions.item(0), quiz);
		}
		else{
			this.loadQuestions(quizEl, quiz, responses);
			
		}
		
		Log.d(TAG, "Loaded Quiz:"+quiz.toString());
		
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
		/*NodeList prefixEls=resultEl.getElementsByTagName("prefix");
		if(prefixEls!=null && prefixEls.getLength()>0)
			result.setPrefix(((Element)prefixEls.item(0)).getTextContent()); 
		NodeList suffixEls=resultEl.getElementsByTagName("suffix");
		if(suffixEls!=null && suffixEls.getLength()>0)
			result.setSuffix(((Element)suffixEls.item(0)).getTextContent()); */
		
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
					Log.w(TAG, "ReadResultAction element not recognized: "+el.getNodeName());
			}
		}
		result.setItems(items);
		
		return result;
	}
	
	private Map<String,Response> loadResponses(Element quizEl){
		HashMap<String,Response> responses=new HashMap<String,Response>();
		NodeList responsesList=quizEl.getElementsByTagName("responses");
		if(responsesList.getLength()>0){
			Element responsesEl=(Element) responsesList.item(0);
			NodeList responseList=responsesEl.getElementsByTagName("response");
			int nbQ=responseList.getLength();
			for(int i=0;i<nbQ;i++){
				Element responseEl=(Element)responseList.item(i);
				Response response=null;
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
					response=new Response();
				
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
	
	
	private void loadQuestions(Element quizEl, Quiz quiz, Map<String,Response> responses){				
		NodeList questionsList=quizEl.getElementsByTagName("questions");
		if(questionsList.getLength()>0){
			Element questionsEl=(Element) questionsList.item(0);
			
			this.setNbQuestions(questionsEl, quiz);
		
			String questionsType=questionsEl.getAttributeNS(NAMESPACE_XSI, "type");
			if(questionsType.endsWith("calculationQuestions")){
				this.loadCalculationQuestions(questionsEl, quiz);
			}
			else{ //normal questions
				
				ArrayList<Question> questions=new ArrayList<Question>();
				NodeList questionList=questionsEl.getElementsByTagName("question");
				int nb=questionList.getLength();
				for(int i=0;i<nb;i++){
					Element questionEl=(Element)questionList.item(i);
					Question question=null;
					String xsitype=questionEl.getAttributeNS(NAMESPACE_XSI, "type");
					if(xsitype.endsWith("spokenQuestion")){
						question=new SpokenQuestion();
						SoundFile sfile=this.loadSoundFile(questionEl);
						((SpokenQuestion)question).setSpeechFile(sfile);
					}
					else if(xsitype.endsWith("textToSpeechQuestion")){
						question=new TextToSpeechQuestion();
						String textS=this.loadTextValueChild(questionEl);
						((TextToSpeechQuestion)question).setText(textS);
					}
					else if(xsitype.endsWith("textQuestion")){
						question=new TextQuestionImpl(xsitype);
						String textS=this.loadTextValueChild(questionEl);
						((TextQuestion)question).setText(textS);
					}
					else
						question=new Question();
					
					question.setId(questionEl.getAttribute("id"));
					String level=questionEl.getAttribute("level");
					question.setLevel(Level.fromValue(level));
					
					//set responses references
					List<Response> possibleResps=new ArrayList<Response>();
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
						Response resp=responses.get(respId);
						if(resp!=null)
							possibleResps.add(resp);
						else
							Log.w(TAG,"Response reference not found: "+respId);
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
				Log.w(TAG, "Could not load nbQuestions as an int: "+nbQ);
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
			Log.e(TAG, "NumberFormatException while reading nbRandom="+nbRandom);
		}
		Element readingTextsEl=(Element) readingQuestions.getElementsByTagName("readingTexts").item(0);
		NodeList textEls=readingTextsEl.getElementsByTagName("text");
		int nbT=textEls.getLength();
		ArrayList<Question> questions=new ArrayList<Question>(nbT);
		ArrayList<Response> responses=new ArrayList<Response>(nbT);
		for(int i=0;i<nbT;i++){
			Element textEl=(Element) textEls.item(i);
			String prefix=textEl.getAttribute("prefix");
			if(prefix==null)
				prefix="";
			String text=textEl.getTextContent();
			String questionS=qPrefix+prefix+" "+text+qSuffix;
			TextToSpeechQuestion question=new TextToSpeechQuestion();
			questions.add(question);
			question.setText(questionS);			
			question.setId((new Integer(i)).toString());
			
			TextResponse response=new TextResponse();
			((TextResponse)response).setText(text);
			response.setId((new Integer(i)).toString());
			responses.add(response);
			
			ArrayList<Response> possibleResps=new ArrayList<Response>(1);
			possibleResps.add(response);
			question.setPossibleResponses(possibleResps);
			ArrayList<String> correctIds=new ArrayList<String>(1);
			correctIds.add(response.getId());
			question.setCorrectIds(correctIds);			
			
			//also load random responses
			if(nbRandom>0)
				question.setNbRandomResponses(nbRandom);			
		}
		quiz.setResponses(responses);
		quiz.setQuestions(questions);
	}
	
	/**
	 * load genreQuestions by reading automatically different possible questions and responses
	 * @param readingQuestions
	 * @param quiz
	 */
	private void loadGenreQuestions(Element readingQuestions, Quiz quiz){
		this.setNbQuestions(readingQuestions, quiz);
		
		Element dictionnaryEl=(Element) readingQuestions.getElementsByTagName("dictionnary").item(0);
		String dicoRes=dictionnaryEl.getTextContent();
		List<DictionaryLine> dico=null;
		try {
			dico = this.parseDictionary(dicoRes);
			Log.d(TAG, "Parsed dictionary file "+dicoRes+", read "+dico.size()+" lines");
		} catch (IOException e) {
			Log.w(TAG, "IOException while reading dictionary "+dicoRes, e);
			return;
		}

		List<Question> questions=new ArrayList<Question>(dico.size());
		List<Response> responses=new ArrayList<Response>(dico.size());
		Map<String, String> genres=new HashMap<String, String>();
		int i=0;
		for(DictionaryLine line: dico){
			//question
			TextQuestionImpl question=new TextQuestionImpl(line.getName());
			questions.add(question);		
			question.setId((Integer.valueOf(i)).toString());
			
			//response
			String goodRespId=null;
			TextResponse response=new TextResponse();			
			if(!genres.containsKey(line.getGenre())){
				((TextResponse)response).setText(line.getGenre());
				goodRespId=(Integer.valueOf(i)).toString();
				response.setId(goodRespId);
				responses.add(response);
				genres.put(line.getGenre(), goodRespId);
			}
			else{
				goodRespId=genres.get(line.getGenre());
			}
			
			ArrayList<String> correctIds=new ArrayList<String>(1);
			correctIds.add(goodRespId);
			question.setCorrectIds(correctIds);		
			question.setPossibleResponses(responses);
			
			i++;
		}		
		quiz.setResponses(responses);
		quiz.setQuestions(questions);
	}
	
	private List<DictionaryLine> parseDictionary(String dicoRes) throws IOException{
		int dicoId=activity.getResources().getIdentifier(dicoRes , "raw", activity.getPackageName());
		InputStream in=activity.getResources().openRawResource(dicoId);
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
								Log.w(TAG, "Could not load intentReward: action not found");
							
							NodeList uriEls=rewardEl.getElementsByTagName("uri");
							if(uriEls!=null && uriEls.getLength()>0){
								Element uriEl=(Element) uriEls.item(0);
								((IntentReward)reward).setUri(uriEl.getTextContent());
							}
							else
								Log.w(TAG, "Could not load intentReward: action not found");
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
					Log.w(TAG, "game mode not recognized: "+modeEl.getLocalName());
				return mode;
			}
		}
	}
	private GameMode getDefaultGameMode(){
		return new GameModeRetry();
	}
	
	public Element getFirstElement(Node n){
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
