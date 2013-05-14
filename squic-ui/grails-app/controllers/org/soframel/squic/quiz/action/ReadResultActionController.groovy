package org.soframel.squic.quiz.action

import org.springframework.dao.DataIntegrityViolationException

class ReadResultActionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [readResultActionInstanceList: ReadResultAction.list(params), readResultActionInstanceTotal: ReadResultAction.count()]
    }

    def create() {
        [readResultActionInstance: new ReadResultAction(params)]
    }

    def save() {
        def readResultActionInstance = new ReadResultAction(params)
        if (!readResultActionInstance.save(flush: true)) {
            render(view: "create", model: [readResultActionInstance: readResultActionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), readResultActionInstance.id])
        redirect(action: "show", id: readResultActionInstance.id)
    }

    def show(Long id) {
        def readResultActionInstance = ReadResultAction.get(id)
        if (!readResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), id])
            redirect(action: "list")
            return
        }

        [readResultActionInstance: readResultActionInstance]
    }

    def edit(Long id) {
        def readResultActionInstance = ReadResultAction.get(id)
        if (!readResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), id])
            redirect(action: "list")
            return
        }

        [readResultActionInstance: readResultActionInstance]
    }

    def update(Long id, Long version) {
        def readResultActionInstance = ReadResultAction.get(id)
        if (!readResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (readResultActionInstance.version > version) {
                readResultActionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'readResultAction.label', default: 'ReadResultAction')] as Object[],
                          "Another user has updated this ReadResultAction while you were editing")
                render(view: "edit", model: [readResultActionInstance: readResultActionInstance])
                return
            }
        }

        readResultActionInstance.properties = params

        if (!readResultActionInstance.save(flush: true)) {
            render(view: "edit", model: [readResultActionInstance: readResultActionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), readResultActionInstance.id])
        redirect(action: "show", id: readResultActionInstance.id)
    }

    def delete(Long id) {
        def readResultActionInstance = ReadResultAction.get(id)
        if (!readResultActionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), id])
            redirect(action: "list")
            return
        }

        try {
            readResultActionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'readResultAction.label', default: 'ReadResultAction'), id])
            redirect(action: "show", id: id)
        }
    }
}
