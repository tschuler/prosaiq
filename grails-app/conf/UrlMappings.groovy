class UrlMappings {

	static mappings = {
		
		"/rest-api/display"(controller:"rest", parseRequest: true) {
			action = [POST: "display"]
		}
		
		"/rest-api/submit"(controller:"rest", parseRequest: true) {
			action = [POST: "submit"]
		}
		
		"/rest-api/json2hl7"(controller:"rest", parseRequest: true) {
			action = [POST: "json2hl7"]
		}
		
		"/rest-api/testterser"(controller:"rest", parseRequest: true) {
			action = [POST: "testterser"]
		}
		
		"/rest-api/hl7-231vs25"(controller:"rest", parseRequest: true) {
			action = [POST: "hl7-231vs25"]
		}

		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
