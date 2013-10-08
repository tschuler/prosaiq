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

package prosaiq

import java.util.Date;

class OdkSubmission {

	String formIdentifier
	
	Float variableValue
	
	String odkData
	
	Date submitted = new Date()
	
	static constraints = {
		formIdentifier (nullable: true) //null if non-ODK submission
		submitted ()//'format' doesn't seem to work
		variableValue (nullable: true)
		odkData (blank: false)	
	}
	
	static mapping = {
		odkData type: 'text' //to allow large strings
	 }
}
