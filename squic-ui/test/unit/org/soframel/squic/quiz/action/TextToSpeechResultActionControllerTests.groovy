package org.soframel.squic.quiz.action



import org.junit.*
import grails.test.mixin.*

@TestFor(TextToSpeechResultActionController)
@Mock(TextToSpeechResultAction)
class TextToSpeechResultActionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/textToSpeechResultAction/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.textToSpeechResultActionInstanceList.size() == 0
        assert model.textToSpeechResultActionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.textToSpeechResultActionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.textToSpeechResultActionInstance != null
        assert view == '/textToSpeechResultAction/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/textToSpeechResultAction/show/1'
        assert controller.flash.message != null
        assert TextToSpeechResultAction.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/textToSpeechResultAction/list'

        populateValidParams(params)
        def textToSpeechResultAction = new TextToSpeechResultAction(params)

        assert textToSpeechResultAction.save() != null

        params.id = textToSpeechResultAction.id

        def model = controller.show()

        assert model.textToSpeechResultActionInstance == textToSpeechResultAction
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/textToSpeechResultAction/list'

        populateValidParams(params)
        def textToSpeechResultAction = new TextToSpeechResultAction(params)

        assert textToSpeechResultAction.save() != null

        params.id = textToSpeechResultAction.id

        def model = controller.edit()

        assert model.textToSpeechResultActionInstance == textToSpeechResultAction
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/textToSpeechResultAction/list'

        response.reset()

        populateValidParams(params)
        def textToSpeechResultAction = new TextToSpeechResultAction(params)

        assert textToSpeechResultAction.save() != null

        // test invalid parameters in update
        params.id = textToSpeechResultAction.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/textToSpeechResultAction/edit"
        assert model.textToSpeechResultActionInstance != null

        textToSpeechResultAction.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/textToSpeechResultAction/show/$textToSpeechResultAction.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        textToSpeechResultAction.clearErrors()

        populateValidParams(params)
        params.id = textToSpeechResultAction.id
        params.version = -1
        controller.update()

        assert view == "/textToSpeechResultAction/edit"
        assert model.textToSpeechResultActionInstance != null
        assert model.textToSpeechResultActionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/textToSpeechResultAction/list'

        response.reset()

        populateValidParams(params)
        def textToSpeechResultAction = new TextToSpeechResultAction(params)

        assert textToSpeechResultAction.save() != null
        assert TextToSpeechResultAction.count() == 1

        params.id = textToSpeechResultAction.id

        controller.delete()

        assert TextToSpeechResultAction.count() == 0
        assert TextToSpeechResultAction.get(textToSpeechResultAction.id) == null
        assert response.redirectedUrl == '/textToSpeechResultAction/list'
    }
}
