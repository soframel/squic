package org.soframel.squic.quiz.question.initializable;

import org.soframel.squic.quiz.question.MultipleChoiceTextQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.TextResponse;
import org.soframel.squic.xml.DictionaryLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: sophie.ramel
 * Date: 22/4/13
 */
public class GenreQuestions extends WordQuestions{

    private List<MultipleChoiceResponse> responses;

    @Override
    public List<Question> createQuestions() {
        List<Question> questions=new ArrayList<Question>(this.getDictionaryLines().size());
        responses=new ArrayList<MultipleChoiceResponse>(this.getDictionaryLines().size());
        Map<String, String> genres=new HashMap<String, String>();
        int i=0;
        for(DictionaryLine line: this.getDictionaryLines()){
            //question
            MultipleChoiceTextQuestion question=new MultipleChoiceTextQuestion(line.getName());
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

        return questions;
    }

    @Override
    public List<MultipleChoiceResponse> getResponses() throws Exception {
       return responses;
    }
}
