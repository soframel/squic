package org.soframel.squic.quiz.automatic

import org.springframework.dao.DataIntegrityViolationException

class CalculationQuestionsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [calculationQuestionsInstanceList: CalculationQuestions.list(params), calculationQuestionsInstanceTotal: CalculationQuestions.count()]
    }

    def create() {
        [calculationQuestionsInstance: new CalculationQuestions(params)]
    }

    def save() {
        def calculationQuestionsInstance = new CalculationQuestions(params)
        calculationQuestionsInstance.setId(null);
        if (!calculationQuestionsInstance.save(flush: true)) {
            render(view: "create", model: [calculationQuestionsInstance: calculationQuestionsInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), calculationQuestionsInstance.id])
        redirect(action: "show", id: calculationQuestionsInstance.id)
    }

    def show(Long id) {
        def calculationQuestionsInstance = CalculationQuestions.get(id)
        if (!calculationQuestionsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), id])
            redirect(action: "list")
            return
        }

        [calculationQuestionsInstance: calculationQuestionsInstance]
    }

    def edit(Long id) {
        def calculationQuestionsInstance = CalculationQuestions.get(id)
        if (!calculationQuestionsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), id])
            redirect(action: "list")
            return
        }

        [calculationQuestionsInstance: calculationQuestionsInstance]
    }

    def update(Long id, Long version) {
        def calculationQuestionsInstance = CalculationQuestions.get(id)
        if (!calculationQuestionsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (calculationQuestionsInstance.version > version) {
                calculationQuestionsInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'calculationQuestions.label', default: 'CalculationQuestions')] as Object[],
                          "Another user has updated this CalculationQuestions while you were editing")
                render(view: "edit", model: [calculationQuestionsInstance: calculationQuestionsInstance])
                return
            }
        }

        calculationQuestionsInstance.properties = params

        if (!calculationQuestionsInstance.save(flush: true)) {
            render(view: "edit", model: [calculationQuestionsInstance: calculationQuestionsInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), calculationQuestionsInstance.id])
        redirect(action: "show", id: calculationQuestionsInstance.id)
    }

    def delete(Long id) {
        def calculationQuestionsInstance = CalculationQuestions.get(id)
        if (!calculationQuestionsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), id])
            redirect(action: "list")
            return
        }

        try {
            calculationQuestionsInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'calculationQuestions.label', default: 'CalculationQuestions'), id])
            redirect(action: "show", id: id)
        }
    }
}
