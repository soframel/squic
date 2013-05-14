package org.soframel.squic.quiz.media



import org.junit.*
import grails.test.mixin.*

@TestFor(ColorController)
@Mock(Color)
class ColorControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/color/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.colorInstanceList.size() == 0
        assert model.colorInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.colorInstance != null
    }

    void testSave() {
        controller.save()

        assert model.colorInstance != null
        assert view == '/color/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/color/show/1'
        assert controller.flash.message != null
        assert Color.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/color/list'

        populateValidParams(params)
        def color = new Color(params)

        assert color.save() != null

        params.id = color.id

        def model = controller.show()

        assert model.colorInstance == color
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/color/list'

        populateValidParams(params)
        def color = new Color(params)

        assert color.save() != null

        params.id = color.id

        def model = controller.edit()

        assert model.colorInstance == color
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/color/list'

        response.reset()

        populateValidParams(params)
        def color = new Color(params)

        assert color.save() != null

        // test invalid parameters in update
        params.id = color.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/color/edit"
        assert model.colorInstance != null

        color.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/color/show/$color.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        color.clearErrors()

        populateValidParams(params)
        params.id = color.id
        params.version = -1
        controller.update()

        assert view == "/color/edit"
        assert model.colorInstance != null
        assert model.colorInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/color/list'

        response.reset()

        populateValidParams(params)
        def color = new Color(params)

        assert color.save() != null
        assert Color.count() == 1

        params.id = color.id

        controller.delete()

        assert Color.count() == 0
        assert Color.get(color.id) == null
        assert response.redirectedUrl == '/color/list'
    }
}
