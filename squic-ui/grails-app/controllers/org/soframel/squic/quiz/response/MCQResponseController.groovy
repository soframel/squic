package org.soframel.squic.quiz.response

import org.soframel.squic.quiz.MenuController
import org.soframel.squic.quiz.Quiz
import org.soframel.squic.user.User
import org.soframel.squic.user.UserController
import org.soframel.squic.utils.FileManagementException
import org.soframel.squic.utils.SquicFileUtils
import org.springframework.dao.DataIntegrityViolationException
import org.soframel.squic.quiz.media.Color

import javax.persistence.Query

class MCQResponseController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        User user=session.getAttribute("user");
        if(user==null){
            //show nothing
            flash.message = "Sorry, you are not logged in.";
            redirect(controller: "user", action: "login")
            return;
        }
        Quiz quiz=session.getAttribute("quiz");
        List list=null;

        if(quiz==null){
            //list=TextResponse.findAll()
            //list.addAll(ImageResponse.findAll())
            //list.addAll(ColorResponse.findAll())
            list=MultipleChoiceResponse.findAll()
        }
        else{
            list = quiz.responses;
        }

        params.max = Math.min(max ?: 10, 100)


        [responseInstanceList: list, responseInstanceTotal: list.size()]
    }

    def create() {
        [responseInstance: null]
    }

    def editFromMenu(){
        String sid=params.get("responses")
        redirect(action: "edit", id: sid)
    }

    def deleteFromMenu(){
        String sid=params.get("responses")
        this.delete(new Long(sid))
    }

    def createFromMenu(){
        redirect(action: "create")
    }


    private Response fillWithParams(Response responseInstance){
        if(responseInstance==null){
            if(params.get("responseType")=="text"){
                responseInstance=new TextResponse();
            }
            else if(params.get("responseType")=="color"){
                responseInstance=new ColorResponse();
            }
            else if(params.get("responseType")=="image"){
                responseInstance=new ImageResponse();
            }
        }
        String id=responseInstance.getId();
        if(params.get("responseType")=="text"){
            ((TextResponse)responseInstance).setText(params.get("text"))
        }
        else if(params.get("responseType")=="color"){
            Color c=new Color()
            c.setColorCode(params.get("colorCode"))
            ((ColorResponse)responseInstance).setColor(c)
        }
        else if(params.get("responseType")=="image"){
           //do nothing, files managed in upload method
        }

        return responseInstance;
    }

    def save() {

        def responseInstance=this.fillWithParams(null)

        if (!responseInstance.save(flush: true)) {
            render(view: "create", model: [responseInstance: responseInstance])
            return
        }
        Quiz quiz=session.getAttribute("quiz")
        if(quiz!=null){
            quiz.getResponses().add(responseInstance);
            if (!quiz.save(flush: true)) {
                render(view: "create", model: [responseInstance: responseInstance])
                return
            }
        }

        if(responseInstance instanceof ImageResponse)
            this.saveFileUploads(responseInstance);

        flash.message = message(code: 'default.created.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), responseInstance.id])
        redirect(action: "edit", id: responseInstance.id, model: responseInstance)
    }

    private void saveFileUploads(ImageResponse responseInstance){
        //files uploads
        boolean resave=false;
        def imageFile = request.getFile('imageFile')
        if(imageFile!=null){
            String image=this.upload(imageFile, responseInstance);
            if(image!=null){
                responseInstance.setImageFile(image);
                //re-save
                resave=true;
            }
        }

        if(resave){
            if (!responseInstance.save(flush: true)) {
                render(view: "create", model: [responseInstance: responseInstance])
                return
            }
        }
    }
    private String upload(def file, ImageResponse responseInstance) {
        if (file.empty) {
            flash.message = 'file cannot be empty'
            return
        }
        User user=(User) session.getAttribute("user");
        Quiz quiz=(Quiz) session.getAttribute("quiz");
        if(user!=null && quiz!=null){
            File quizDir=null;
            try {
                quizDir=SquicFileUtils.getAndCreateQuizDirectory(user,quiz);
            }
            catch(FileManagementException ex){
                flash.message = 'directory for uploaded file could not be created: '+ex.getMessage();
                return
            }
            if(quizDir!=null){
                String name=file.getOriginalFilename();
                file.transferTo(new File(quizDir, name))
                return name;
            }
            else{
                flash.message = 'directory for uploaded file could not be created for un unknown reason: '+ex.getMessage();
                return
            }
        }
        else {
            flash.message = 'user must be logged in to create quiz, and quiz must be created to add files'
            return
        }
    }

    def show(Long id) {
        def responseInstance = TextResponse.get(id)
        if (!responseInstance)  {
            responseInstance=ImageResponse.get(id)
            if (!responseInstance)
                responseInstance=ColorResponse.get(id)
        }

        if (!responseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), id])
            redirect(action: "list")
            return
        }

        [responseInstance: responseInstance]
    }

    def edit(Long id) {
        def responseInstance = TextResponse.get(id)
        if (!responseInstance)  {
            responseInstance=ImageResponse.get(id)
            if (!responseInstance)
                responseInstance=ColorResponse.get(id)
        }

        if (!responseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), id])
            redirect(action: "list")
            return
        }

        [responseInstance: responseInstance]
    }

    def update(Long id, Long version) {
        def responseInstance = TextResponse.get(id)
        if (!responseInstance)  {
            responseInstance=ImageResponse.get(id)
            if (!responseInstance)
                responseInstance=ColorResponse.get(id)
        }

        if (!responseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (responseInstance.version > version) {
                responseInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'MCQResponse.label', default: 'MCQ Response')] as Object[],
                          "Another user has updated this MCQ Response while you were editing")
                render(view: "edit", model: [responseInstance: responseInstance])
                return
            }
        }

        //responseInstance.properties = params
        responseInstance=this.fillWithParams(responseInstance)

        if (!responseInstance.save(flush: true)) {
            render(view: "edit", model: [responseInstance: responseInstance])
            return
        }

        if(responseInstance instanceof ImageResponse)
            this.saveFileUploads(responseInstance);

        //reload quiz !
        MenuController.reloadQuiz(this.getSession())

        flash.message = message(code: 'default.updated.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), responseInstance.id])
        redirect(action: "edit", id: responseInstance.id)
    }

    def delete(Long id) {
        def responseInstance = TextResponse.get(id)
        if (!responseInstance)  {
            responseInstance=ImageResponse.get(id)
            if (!responseInstance)
                responseInstance=ColorResponse.get(id)
        }
        if (!responseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), id])
            redirect(action: "list")
            return
        }

        try {
            responseInstance.delete(flush: true)

            //reload quiz !
            MenuController.reloadQuiz(this.getSession())

            flash.message = message(code: 'default.deleted.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'MCQResponse.label', default: 'MCQ Response'), id])
            redirect(action: "show", id: id)
        }
    }
}
