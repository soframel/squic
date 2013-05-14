package org.soframel.squic.quiz.action



import org.junit.*
import grails.test.mixin.*

@TestFor(SpeechResultActionController)
@Mock(SpeechResultAction)
class SpeechResultActionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/speechResultAction/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.speechResultActionInstanceList.size() == 0
        assert model.speechResultActionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.speechResultActionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.speechResultActionInstance != null
        assert view == '/speechResultAction/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/speechResultAction/show/1'
        assert controller.flash.message != null
        assert SpeechResultAction.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/speechResultAction/list'

        populateValidParams(params)
        def speechResultAction = new SpeechResultAction(params)

        assert speechResultAction.save() != null

        params.id = speechResultAction.id

        def model = controller.show()

        assert model.speechResultActionInstance == speechResultAction
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/speechResultAction/list'

        populateValidParams(params)
        def speechResultAction = new SpeechResultAction(params)

        assert speechResultAction.save() != null

        params.id = speechResultAction.id

        def model = controller.edit()

        assert model.speechResultActionInstance == speechResultAction
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/speechResultAction/list'

        response.reset()

        populateValidParams(params)
        def speechResultAction = new SpeechResultAction(params)

        assert speechResultAction.save() != null

        // test invalid parameters in update
        params.id = speechResultAction.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/speechResultAction/edit"
        assert model.speechResultActionInstance != null

        speechResultAction.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/speechResultAction/show/$speechResultAction.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        speechResultAction.clearErrors()

        populateValidParams(params)
        params.id = speechResultAction.id
        params.version = -1
        controller.update()

        assert view == "/speechResultAction/edit"
        assert model.speechResultActionInstance != null
        assert model.speechResultActionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/speechResultAction/list'

        response.reset()

        populateValidParams(params)
        def speechResultAction = new SpeechResultAction(params)

        assert speechResultAction.save() != null
        assert SpeechResultAction.count() == 1

        params.id = speechResultAction.id

        controller.delete()

        assert SpeechResultAction.count() == 0
        assert SpeechResultAction.get(speechResultAction.id) == null
        assert response.redirectedUrl == '/speechResultAction/list'
    }
}
