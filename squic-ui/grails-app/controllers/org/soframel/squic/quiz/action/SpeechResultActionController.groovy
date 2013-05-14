package org.soframel.squic.quiz.action

import org.soframel.squic.quiz.media.SoundFile
import org.springframework.dao.DataIntegrityViolationException

class SpeechResultActionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [speechResultActionInstanceList: SpeechResultAction.list(params), speechResultActionInstanceTotal: SpeechResultAction.count()]
    }

    def create() {
        SpeechResultAction speechResultActionInstance= new SpeechResultAction(params)
        speechResultActionInstance.setSpeechFile(new SoundFile());
        [speechResultActionInstance: speechResultActionInstance]

    }

    def save() {
        def speechResultActionInstance = new SpeechResultAction(params)
        if (!speechResultActionInstance.save(flush: true)) {
            render(view: "create", model: [speechResultActionInstance: speechResultActionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), speechResultActionInstance.id])
        redirect(action: "show", id: speechResultActionInstance.id)
    }

    def show(Long id) {
        def speechResultActionInstance = SpeechResultAction.get(id)
        if (!speechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        [speechResultActionInstance: speechResultActionInstance]
    }

    def edit(Long id) {
        def speechResultActionInstance = SpeechResultAction.get(id)
        if (!speechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        [speechResultActionInstance: speechResultActionInstance]
    }

    def update(Long id, Long version) {
        def speechResultActionInstance = SpeechResultAction.get(id)
        if (!speechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (speechResultActionInstance.version > version) {
                speechResultActionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'speechResultAction.label', default: 'SpeechResultAction')] as Object[],
                          "Another user has updated this SpeechResultAction while you were editing")
                render(view: "edit", model: [speechResultActionInstance: speechResultActionInstance])
                return
            }
        }

        speechResultActionInstance.properties = params

        if (!speechResultActionInstance.save(flush: true)) {
            render(view: "edit", model: [speechResultActionInstance: speechResultActionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), speechResultActionInstance.id])
        redirect(action: "show", id: speechResultActionInstance.id)
    }

    def delete(Long id) {
        def speechResultActionInstance = SpeechResultAction.get(id)
        if (!speechResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), id])
            redirect(action: "list")
            return
        }

        try {
            speechResultActionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'speechResultAction.label', default: 'SpeechResultAction'), id])
            redirect(action: "show", id: id)
        }
    }
}
