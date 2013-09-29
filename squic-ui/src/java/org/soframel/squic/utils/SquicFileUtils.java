package org.soframel.squic.utils;

import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.user.User;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * User: sophie
 * Date: 22/9/13
 */
public class SquicFileUtils {

    public final static String resourcesDirectory="/opt/squic/resources";


    public static File getUserDirectory(User user) throws FileManagementException {
        File userDir=null;
        if(user!=null){
            //user directory
            userDir=new File(resourcesDirectory, user.getId());
            if(!userDir.exists()){
                if(!userDir.mkdirs()){
                    throw new FileManagementException("Could not create user directory");
                }
            }
        }
        return userDir;
    }

    public static File getAndCreateQuizDirectory(User user, Quiz quiz) throws FileManagementException {
        File userDir=SquicFileUtils.getUserDirectory(user);
        File quizDir=null;
        if(userDir!=null){
            quizDir=new File(userDir, quiz.getId());
            if(!quizDir.exists() && !quizDir.mkdirs()){
                throw new FileManagementException("Could not create quiz directory");
            }
        }
        return quizDir;
    }
}
