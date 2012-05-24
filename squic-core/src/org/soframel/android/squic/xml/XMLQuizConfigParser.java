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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.soframel.android.squic.quiz.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLQuizConfigParser {
	
	private static final String TAG = "XMLQuizConfigParser";
	
	//private static final String NAMESPACE_QUIZ="org.soframel.android.kidsquiz.quiz";
	private static final String NAMESPACE_XSI="http://www.w3.org/2001/XMLSchema-instance";
	
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
		if(language!=null && !language.isEmpty()){
			quiz.setLanguage(this.getLocaleFromLanguage(language));
		}
		String icon=quizEl.getAttribute("icon");
		if(icon!=null && !icon.isEmpty()){
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
		
		//result actions
		this.loadResultActions(quiz, quizEl);
		
		//responses
		Map<String,Response> responses=this.loadResponses(quizEl);
		ArrayList<Response> responsesList=new ArrayList<Response>(responses.size());
		responsesList.addAll(responses.values());
		quiz.setResponses(responsesList);
		
		//questions
		List<Question> questions=this.loadQuestions(quizEl, quiz, responses);
		quiz.setQuestions(questions);
		
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
			SpeechResultAction goodResult=new SpeechResultAction();
			SoundFile sfile=this.loadSoundFile(goodResultEl);
			goodResult.setSpeechFile(sfile);
			quiz.setGoodResultAction(goodResult);
		}
		else if(xsitype!=null && xsitype.equals("textToSpeechResultAction")){
			TextToSpeechResultAction goodResult=new TextToSpeechResultAction();
			String s=this.loadTextValueChild(goodResultEl);
			goodResult.setText(s);
			quiz.setGoodResultAction(goodResult);
		}
		//Bad result
		Element badResultEl=(Element) resultEl.getElementsByTagName("badResultAction").item(0);
		String xsitype2=badResultEl.getAttributeNS(NAMESPACE_XSI, "type");
		if(xsitype2!=null && xsitype2.equals("speechResultAction")){
			SpeechResultAction badResult=new SpeechResultAction();
			SoundFile sfile=this.loadSoundFile(badResultEl);
			badResult.setSpeechFile(sfile);
			quiz.setBadResultAction(badResult);
		}
		else if(xsitype2!=null && xsitype2.equals("textToSpeechResultAction")){
			TextToSpeechResultAction badResult=new TextToSpeechResultAction();
			String s=this.loadTextValueChild(badResultEl);
			badResult.setText(s);
			quiz.setBadResultAction(badResult);
		}
		
		//Quiz finished
		Element finishedResultEl=(Element) resultEl.getElementsByTagName("quizFinishedAction").item(0);
		String xsitype3=finishedResultEl.getAttributeNS(NAMESPACE_XSI, "type");
		if(xsitype3!=null && xsitype3.equals("speechResultAction")){
			SpeechResultAction finishedResult=new SpeechResultAction();
			SoundFile sfile=this.loadSoundFile(finishedResultEl);
			finishedResult.setSpeechFile(sfile);
			quiz.setQuizFinishedAction(finishedResult);
		}
		else if(xsitype3!=null && xsitype3.equals("textToSpeechResultAction")){
			TextToSpeechResultAction finishedResult=new TextToSpeechResultAction();
			String s=this.loadTextValueChild(finishedResultEl);
			finishedResult.setText(s);
			quiz.setQuizFinishedAction(finishedResult);
		}
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
	
	
	private List<Question> loadQuestions(Element quizEl, Quiz quiz, Map<String,Response> responses){
		ArrayList<Question> questions=new ArrayList<Question>();		
		NodeList questionsList=quizEl.getElementsByTagName("questions");
		if(questionsList.getLength()>0){
			Element questionsEl=(Element) questionsList.item(0);
			
			String nbQ=questionsEl.getAttribute("nbQuestions");
			if(nbQ!=null && !nbQ.isEmpty()){
				try{
					int nb=Integer.parseInt(nbQ);
					quiz.setNbQuestions(nb);
				}
				catch(NumberFormatException ex){
					Log.w(TAG, "Could not load nbQuestions as an int: "+nbQ);
				}
			}
			
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
		}
		return questions;
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
