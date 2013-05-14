package org.soframel.squic.quiz.action



import org.junit.*
import grails.test.mixin.*

@TestFor(ReadResultActionController)
@Mock(ReadResultAction)
class ReadResultActionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/readResultAction/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.readResultActionInstanceList.size() == 0
        assert model.readResultActionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.readResultActionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.readResultActionInstance != null
        assert view == '/readResultAction/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/readResultAction/show/1'
        assert controller.flash.message != null
        assert ReadResultAction.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/readResultAction/list'

        populateValidParams(params)
        def readResultAction = new ReadResultAction(params)

        assert readResultAction.save() != null

        params.id = readResultAction.id

        def model = controller.show()

        assert model.readResultActionInstance == readResultAction
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/readResultAction/list'

        populateValidParams(params)
        def readResultAction = new ReadResultAction(params)

        assert readResultAction.save() != null

        params.id = readResultAction.id

        def model = controller.edit()

        assert model.readResultActionInstance == readResultAction
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/readResultAction/list'

        response.reset()

        populateValidParams(params)
        def readResultAction = new ReadResultAction(params)

        assert readResultAction.save() != null

        // test invalid parameters in update
        params.id = readResultAction.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/readResultAction/edit"
        assert model.readResultActionInstance != null

        readResultAction.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/readResultAction/show/$readResultAction.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        readResultAction.clearErrors()

        populateValidParams(params)
        params.id = readResultAction.id
        params.version = -1
        controller.update()

        assert view == "/readResultAction/edit"
        assert model.readResultActionInstance != null
        assert model.readResultActionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/readResultAction/list'

        response.reset()

        populateValidParams(params)
        def readResultAction = new ReadResultAction(params)

        assert readResultAction.save() != null
        assert ReadResultAction.count() == 1

        params.id = readResultAction.id

        controller.delete()

        assert ReadResultAction.count() == 0
        assert ReadResultAction.get(readResultAction.id) == null
        assert response.redirectedUrl == '/readResultAction/list'
    }
}
