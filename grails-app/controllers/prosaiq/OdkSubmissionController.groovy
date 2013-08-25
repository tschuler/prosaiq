package prosaiq

import prosaiq.OdkSubmission;

import org.springframework.dao.DataIntegrityViolationException

class OdkSubmissionController {
	
	def hl7GeneratorService

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	static allowedMethods = [delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [odkSubmissionInstanceList: OdkSubmission.list(params), odkSubmissionInstanceTotal: OdkSubmission.count()]
    }

    /*def create() {
        [odkSubmissionInstance: new OdkSubmission(params)]
    }

    def save() {
        def odkSubmissionInstance = new OdkSubmission(params)
        if (!odkSubmissionInstance.save(flush: true)) {
            render(view: "create", model: [odkSubmissionInstance: odkSubmissionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), odkSubmissionInstance.id])
        redirect(action: "show", id: odkSubmissionInstance.id)
    }*/

    def show(Long id) {
        def odkSubmissionInstance = OdkSubmission.get(id)
        if (!odkSubmissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
            redirect(action: "list")
            return
        }

        [odkSubmissionInstance: odkSubmissionInstance]
    }

    /*def edit(Long id) {
        def odkSubmissionInstance = OdkSubmission.get(id)
        if (!odkSubmissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
            redirect(action: "list")
            return
        }

        [odkSubmissionInstance: odkSubmissionInstance]
    }

    def update(Long id, Long version) {
        def odkSubmissionInstance = OdkSubmission.get(id)
        if (!odkSubmissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (odkSubmissionInstance.version > version) {
                odkSubmissionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'odkSubmission.label', default: 'OdkSubmission')] as Object[],
                          "Another user has updated this OdkSubmission while you were editing")
                render(view: "edit", model: [odkSubmissionInstance: odkSubmissionInstance])
                return
            }
        }

        odkSubmissionInstance.properties = params

        if (!odkSubmissionInstance.save(flush: true)) {
            render(view: "edit", model: [odkSubmissionInstance: odkSubmissionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), odkSubmissionInstance.id])
        redirect(action: "show", id: odkSubmissionInstance.id)
    }*/

    def delete(Long id) {
        def odkSubmissionInstance = OdkSubmission.get(id)
        if (!odkSubmissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
            redirect(action: "list")
            return
        }

        try {
            odkSubmissionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
            redirect(action: "show", id: id)
        }
    }
	
	def outputHl7 (Long id) {
		def odkSubmissionInstance = OdkSubmission.get(id)
		if (!odkSubmissionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
			redirect(action: "list")
			return
		}
		try {
			def hl7Message = ""
            switch (odkSubmissionInstance.formIdentifier.toString()) {
				case "psa2hl7": 
					hl7Message = hl7GeneratorService.ImportPSAIntoTestPatient(odkSubmissionInstance)
					break
				case "state_your_height-04": 
					hl7Message = hl7GeneratorService.ImportHeightEtcIntoTestPatient(odkSubmissionInstance)
					break
				case "OTR_RO-02": 
					hl7Message = hl7GeneratorService.ImportOTR_ROIntoTestPatient(odkSubmissionInstance)
					break
				default: 
					hl7Message = "No HL7 Generator set for this instance" //should never be displayed
					break
			} 
			
            render hl7Message.replace("\r", "<br />") //replacing needs to be removed before saving as file to be valid HL7
        }
        catch (DataIntegrityViolationException e) {
            //TODO needs different message code +/- excpetion type
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'odkSubmission.label', default: 'OdkSubmission'), id])
            redirect(action: "show", id: id)
        }
	}
}
