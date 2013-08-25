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
