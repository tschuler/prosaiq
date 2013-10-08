/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Thilo Schuler <thilo.schuler@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
