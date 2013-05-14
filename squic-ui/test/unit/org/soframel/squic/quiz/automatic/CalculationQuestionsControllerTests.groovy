package org.soframel.squic.quiz.automatic



import org.junit.*
import grails.test.mixin.*

@TestFor(CalculationQuestionsController)
@Mock(CalculationQuestions)
class CalculationQuestionsControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/calculationQuestions/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.calculationQuestionsInstanceList.size() == 0
        assert model.calculationQuestionsInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.calculationQuestionsInstance != null
    }

    void testSave() {
        controller.save()

        assert model.calculationQuestionsInstance != null
        assert view == '/calculationQuestions/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/calculationQuestions/show/1'
        assert controller.flash.message != null
        assert CalculationQuestions.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/calculationQuestions/list'

        populateValidParams(params)
        def calculationQuestions = new CalculationQuestions(params)

        assert calculationQuestions.save() != null

        params.id = calculationQuestions.id

        def model = controller.show()

        assert model.calculationQuestionsInstance == calculationQuestions
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/calculationQuestions/list'

        populateValidParams(params)
        def calculationQuestions = new CalculationQuestions(params)

        assert calculationQuestions.save() != null

        params.id = calculationQuestions.id

        def model = controller.edit()

        assert model.calculationQuestionsInstance == calculationQuestions
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/calculationQuestions/list'

        response.reset()

        populateValidParams(params)
        def calculationQuestions = new CalculationQuestions(params)

        assert calculationQuestions.save() != null

        // test invalid parameters in update
        params.id = calculationQuestions.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/calculationQuestions/edit"
        assert model.calculationQuestionsInstance != null

        calculationQuestions.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/calculationQuestions/show/$calculationQuestions.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        calculationQuestions.clearErrors()

        populateValidParams(params)
        params.id = calculationQuestions.id
        params.version = -1
        controller.update()

        assert view == "/calculationQuestions/edit"
        assert model.calculationQuestionsInstance != null
        assert model.calculationQuestionsInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/calculationQuestions/list'

        response.reset()

        populateValidParams(params)
        def calculationQuestions = new CalculationQuestions(params)

        assert calculationQuestions.save() != null
        assert CalculationQuestions.count() == 1

        params.id = calculationQuestions.id

        controller.delete()

        assert CalculationQuestions.count() == 0
        assert CalculationQuestions.get(calculationQuestions.id) == null
        assert response.redirectedUrl == '/calculationQuestions/list'
    }
}
