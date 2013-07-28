package org.soframel.squic.quiz.question.initializable.word;

import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.question.initializable.InitializableQuestions;
import org.soframel.squic.utils.ResourceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Word questions are questions based on an external dictionary resource
 * User: sophie.ramel
 * Date: 22/4/13
 */
public abstract class WordQuestions extends InitializableQuestions {

    //definition fields
    private String dictionaryResource;
    private DictionaryType dictionaryType;

    //runtime fields
    private ResourceProvider propertiesProvider;
    private List<DictionaryLine> dictionaryLines;

    public WordQuestions(){

    }
    public WordQuestions(String dictionaryResource, ResourceProvider propertiesProvider){
        this.dictionaryResource = dictionaryResource;
        this.propertiesProvider=propertiesProvider;
    }

    public abstract List<Question> createQuestions();

    @Override
    public List<Question> initialize() throws IOException {
        InputStream in=propertiesProvider.getPropertiesInputStream(dictionaryResource);
        InputStreamReader reader=new InputStreamReader(in);
        BufferedReader breader=new BufferedReader(reader);
        dictionaryLines=new ArrayList<DictionaryLine>();
        String line=null;
        do{
            line=breader.readLine();
            if(line!=null){
                DictionaryLine dl=this.parseDictionaryLine(line);
                dictionaryLines.add(dl);
            }
        }while(line!=null);
        return this.createQuestions();
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

    public String getDictionaryResource() {
        return dictionaryResource;
    }

    public void setDictionaryResource(String dictionaryResource) {
        this.dictionaryResource = dictionaryResource;
    }

    public List<DictionaryLine> getDictionaryLines() {
        return dictionaryLines;
    }

    public ResourceProvider getPropertiesProvider() {
        return propertiesProvider;
    }

    public void setPropertiesProvider(ResourceProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    public DictionaryType getDictionaryType() {
        return dictionaryType;
    }

    public void setDictionaryType(DictionaryType dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

}
