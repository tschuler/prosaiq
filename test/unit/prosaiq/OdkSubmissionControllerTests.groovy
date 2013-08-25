package prosaiq



import org.junit.*

import prosaiq.OdkSubmission;
import prosaiq.OdkSubmissionController;
import grails.test.mixin.*

@TestFor(OdkSubmissionController)
@Mock(OdkSubmission)
class OdkSubmissionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/odkSubmission/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.odkSubmissionInstanceList.size() == 0
        assert model.odkSubmissionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.odkSubmissionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.odkSubmissionInstance != null
        assert view == '/odkSubmission/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/odkSubmission/show/1'
        assert controller.flash.message != null
        assert OdkSubmission.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/odkSubmission/list'

        populateValidParams(params)
        def odkSubmission = new OdkSubmission(params)

        assert odkSubmission.save() != null

        params.id = odkSubmission.id

        def model = controller.show()

        assert model.odkSubmissionInstance == odkSubmission
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/odkSubmission/list'

        populateValidParams(params)
        def odkSubmission = new OdkSubmission(params)

        assert odkSubmission.save() != null

        params.id = odkSubmission.id

        def model = controller.edit()

        assert model.odkSubmissionInstance == odkSubmission
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/odkSubmission/list'

        response.reset()

        populateValidParams(params)
        def odkSubmission = new OdkSubmission(params)

        assert odkSubmission.save() != null

        // test invalid parameters in update
        params.id = odkSubmission.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/odkSubmission/edit"
        assert model.odkSubmissionInstance != null

        odkSubmission.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/odkSubmission/show/$odkSubmission.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        odkSubmission.clearErrors()

        populateValidParams(params)
        params.id = odkSubmission.id
        params.version = -1
        controller.update()

        assert view == "/odkSubmission/edit"
        assert model.odkSubmissionInstance != null
        assert model.odkSubmissionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/odkSubmission/list'

        response.reset()

        populateValidParams(params)
        def odkSubmission = new OdkSubmission(params)

        assert odkSubmission.save() != null
        assert OdkSubmission.count() == 1

        params.id = odkSubmission.id

        controller.delete()

        assert OdkSubmission.count() == 0
        assert OdkSubmission.get(odkSubmission.id) == null
        assert response.redirectedUrl == '/odkSubmission/list'
    }
}
