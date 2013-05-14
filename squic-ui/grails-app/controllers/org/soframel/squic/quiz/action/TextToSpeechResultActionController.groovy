package org.soframel.squic.quiz.action

import org.springframework.dao.DataIntegrityViolationException

class TextToSpeechResultActionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [textToSpeechResultActionInstanceList: TextToSpeechResultAction.list(params), textToSpeechResultActionInstanceTotal: TextToSpeechResultAction.count()]
    }

    def create() {
        [textToSpeechResultActionInstance: new TextToSpeechResultAction(params)]
    }

    def save() {
        def textToSpeechResultActionInstance = new TextToSpeechResultAction(params)
        if (!textToSpeechResultActionInstance.save(flush: true)) {
            render(view: "create", model: [textToSpeechResultActionInstance: textToSpeechResultActionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), textToSpeechResultActionInstance.id])
        redirect(action: "show", id: textToSpeechResultActionInstance.id)
    }

    def show(Long id) {
        def textToSpeechResultActionInstance = TextToSpeechResultAction.get(id)
        if (!textToSpeechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        [textToSpeechResultActionInstance: textToSpeechResultActionInstance]
    }

    def edit(Long id) {
        def textToSpeechResultActionInstance = TextToSpeechResultAction.get(id)
        if (!textToSpeechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        [textToSpeechResultActionInstance: textToSpeechResultActionInstance]
    }

    def update(Long id, Long version) {
        def textToSpeechResultActionInstance = TextToSpeechResultAction.get(id)
        if (!textToSpeechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (textToSpeechResultActionInstance.version > version) {
                textToSpeechResultActionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction')] as Object[],
                          "Another user has updated this TextToSpeechResultAction while you were editing")
                render(view: "edit", model: [textToSpeechResultActionInstance: textToSpeechResultActionInstance])
                return
            }
        }

        textToSpeechResultActionInstance.properties = params

        if (!textToSpeechResultActionInstance.save(flush: true)) {
            render(view: "edit", model: [textToSpeechResultActionInstance: textToSpeechResultActionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), textToSpeechResultActionInstance.id])
        redirect(action: "show", id: textToSpeechResultActionInstance.id)
    }

    def delete(Long id) {
        def textToSpeechResultActionInstance = TextToSpeechResultAction.get(id)
        if (!textToSpeechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        try {
            textToSpeechResultActionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction'), id])
            redirect(action: "show", id: id)
        }
    }
}
