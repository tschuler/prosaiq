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

import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.model.v231.message.ORU_R01;
import ca.uhn.hl7v2.model.primitive.CommonTS;
import java.text.SimpleDateFormat;

import prosaiq.OdkSubmission;
import groovy.json.JsonSlurper;

class Hl7Exception extends RuntimeException {
	String message
	Exception exception
}

class Hl7GeneratorService {

    def ImportPSAIntoTestPatient (OdkSubmission odkSubmissionInstance) {
		
		//try { 
			String msgTempl = "MSH|^~\\&|OMNI-Lab|P208|MOSAIQ||201107201645+1100||ORU^R01^ORU_R01|SEALSS_20110720.4476|P|2.3.1^AUS&Australia&ISO3166-1|3143273||||AUS||EN^English^ISO639-1\r" +
			// MSH-10: Message Control ID (ST)
			"PID|1||8191821^^^^UR~G^^^AUSDVA^DVA|8191821^^^^016^P208|ISOFT^WOLL^^^MRS||19731007|F|||114 CROWN ST^^WOLLONGONG^NSW^2500||^PRN^PH^^^^442284758\r" +
			"OBR|1||11W136835O^2206.AUSNATA^2206^L|I237^PSA^NATA2206|||201107201600|||||||201107201638|SER&Serum&HL70070|094544DY^NASSER^ELIAS HABIB^^^DR^^^AUSHICPR^L^^^MCR||||||201107201645||IMM|F||^^^20110720^^R|^^^^^^^^NATA2206^L^^^MCR||||1^^201107201645+1100\r" +
			// OBR-3: Filler Order Number; OBR-4: Universal Service ID; OBR-7: Observation Date/Time; OBR-14: Specimen received Date/Time; OBR-22: Results status change; OBR-27:Quantity/Timing?; OBR-32: ?Principal Results Interpreter
			"OBX|1|NM|I0242^PSAI^NATA2206^^^||4.0|ng/mL|||||F|||201107201600+1100|SEALSS^2206^AUSNATA\r" +
			// OBX-14: Date/Time of Observation
			"NTE|2||\\.br\\Please note, the tumour marker method and calibration have changed\\.br\\from 13th July 2010. Due to the specificity of the tumour marker\\.br\\method, previousl tumour marker results cannot be compared to the new\\.br\\method results. Results from 13th July 2010 should only be compared\\.br\\with further results generated with this new method.\\.br\\\r"
			
			// Parse message in HL7 version 2.3.1
			HapiContext context = new DefaultHapiContext();
			CanonicalModelClassFactory mcf = new CanonicalModelClassFactory("2.3.1");
			context.setModelClassFactory(mcf);
			PipeParser parser = context.getPipeParser();
			ca.uhn.hl7v2.model.v231.message.ORU_R01 hapiMsg = (ca.uhn.hl7v2.model.v231.message.ORU_R01) parser.parse(msgTempl);
			
			// Replace PSA value and various timestamps in template via terser
			Terser terser = new Terser(hapiMsg);
			terser.set("/.OBX-5", odkSubmissionInstance.variableValue.toString());
			
			String msh10ControlID = "PROSAIQ_" + odkSubmissionInstance.id.toString();
			terser.set("/.MSH-10", msh10ControlID);
			
			// Set filler order ID (here:clear same as message control ID)
			terser.set("/.OBR-3-1", msh10ControlID);
			
			terser.set("/.MSH-13", "-1");  // don't use sequence numbers
			
			Calendar calendar = Calendar.getInstance(); // current date/time (base time)
			TimeZone timeZone = TimeZone.getTimeZone("Australia/Sydney");
			calendar.setTimeZone(timeZone);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmZ");
			simpleDateFormat.setTimeZone(timeZone);  // via SimpleDateFormat timeZone adapts to daylight savings (not directly?!?)
			String hl7TsNow = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			// without hours and minutes or timezone
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");
			String TsToday = simpleDateFormat2.format(calendar.getTime()).toString();
			
			calendar.add(Calendar.HOUR, -1); // 1h before base time
			String hl7TsNowMinus1h = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			calendar.add(Calendar.HOUR, -1); // 2h before base time
			String hl7TsNowMinus2h = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			terser.set("/.MSH-7", hl7TsNow);
			terser.set("/.OBR-7", hl7TsNowMinus2h);
			terser.set("/.OBR-14", hl7TsNowMinus1h);
			terser.set("/.OBR-22", hl7TsNow); 
			terser.set("/.OBR-27-4", TsToday); //^^^20110720^^R
			terser.set("/.OBR-32-3", hl7TsNow);	//1^^201107201645+1100
			terser.set("/.OBX-14", hl7TsNowMinus2h);
				
			return hapiMsg.encode()
		
		/*} catch (Exception e) {
			throw new Hl7Exception(message: "Error during Generation of HL7 message", exception: e)
		}*/
		
    }
	
	def ImportHeightEtcIntoTestPatient(OdkSubmission odkSubmissionInstance) {
		
		//try {
			String msgTempl = "MSH|^~\\&|OMNI-Lab|P208|MOSAIQ||201107201645+1100||ORU^R01^ORU_R01|SEALSS_20110720.4476|P|2.3.1^AUS&Australia&ISO3166-1|-1||||AUS||EN^English^ISO639-1\r" +
			// MSH-7: Date/Time of Message; MSH-10: Message Control ID (ST); MSH-13: Sequence Number (-1 if not used)
			"PID|1||8191821^^^^UR~G^^^AUSDVA^DVA|8191821^^^^016^P208|ISOFT^WOLL^^^MRS||19731007|F|||114 CROWN ST^^WOLLONGONG^NSW^2500||^PRN^PH^^^^442284758\r" +
			"OBR|1||11W136835O^2206.AUSNATA|^OTR|||201107201600|||0945449B^NASSER^ELIAS^^^DR^^^AUSHICPR^L^^^MCR||||||0945449B^NASSER^ELIAS^^^DR^^^AUSHICPR^L^^^MCR|||||||||F||||||\r"
			// OBR-3: Filler Order Number; OBR-4: Universal Service ID; OBR-7: Observation Date/Time; OBR-10: Collector Identifier; OBR-14: Specimen received Date/Time; OBR-16: Ordering Provider; OBR-22: Results status change; OBR-27:Quantity/Timing?; OBR-32: ?Principal Results Interpreter
			
			// Parse template message in HL7 version 2.3.1
			HapiContext context = new DefaultHapiContext();
			CanonicalModelClassFactory mcf = new CanonicalModelClassFactory("2.3.1");
			context.setModelClassFactory(mcf);
			PipeParser parser = context.getPipeParser();
			ca.uhn.hl7v2.model.v231.message.ORU_R01 hapiMsg = (ca.uhn.hl7v2.model.v231.message.ORU_R01) parser.parse(msgTempl);
			
			// Initialise terser and slurper for convenient read/write access and date/time utilities (incl HL7 format)
			Terser terser = new Terser(hapiMsg)
			def slurper = new JsonSlurper()
			def parsedJson = slurper.parseText(odkSubmissionInstance.odkData.toString())
			Calendar calendar = Calendar.getInstance(); // current date/time (base time)
			TimeZone timeZone = TimeZone.getTimeZone("Australia/Sydney");
			calendar.setTimeZone(timeZone);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmZ");
			simpleDateFormat.setTimeZone(timeZone);  // via SimpleDateFormat timeZone adapts to daylight savings (not directly?!?)
			String hl7TsNow = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			// Set details in MSH segment
			terser.set("/.MSH-7", hl7TsNow) // Date/Time of message
			def messageControlID = "PROSAIQ_" + odkSubmissionInstance.id.toString()
			terser.set("/.MSH-10", messageControlID)
			
			// Set details in OBR segment
			// Set filler order ID (arbitrary but unique - here 2 components were chosen)
			terser.set("/.OBR-3-1", messageControlID)
			//terser.set("/.OBR-3-2", odkSubmissionInstance.formIdentifier.toString())
			terser.set("/.OBR-7", hl7TsNow) // Observation Date/Time
	
			// Add OBX segments for each submitted survey element
			
			// odk --> mosaig ID mapping plus other information e.g. type used to generate +/- validate HL7 message (format: groovy list of maps)
			def mappingInfo =[
				[odk:"classification_data/profession", mosaiq:"OdkPrfssn", hl7Type:"ST"],
				[odk:"classification_data/gender", mosaiq:"OdkGndr", hl7Type:"ST"],
				[odk:"height_in_centimetres", mosaiq:"ODK4PRO-Height", hl7Type:"NM", unit:"cm", comment:"comment"]  //comment's value is the id to another submitted element
			]
			def int OBXSegmentCounter = 0
			//def submittedSurveyElements = ""
			for (element in mappingInfo) {
				def odkElementId = element["odk"]
				def mosaiqElementId = element["mosaiq"]
				def unitForNumericals = element["unit"]
				def commentId = element["comment"]
				if (parsedJson."$odkElementId") {
					OBXSegmentCounter += 1
					//submittedSurveyElements = submittedSurveyElements + ", " + odkElementId
					if (element["hl7Type"] == "NM") {
						if (parsedJson."$odkElementId".isFloat()) {
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-1", OBXSegmentCounter.toString())
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-2", "NM")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-3", "$mosaiqElementId")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-5", parsedJson."$odkElementId")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-6", "$unitForNumericals")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-11", "F") // Observation Result Status
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-14", hl7TsNow) // Date/Time of the Observation
							if (commentId) {
								def comment = parsedJson."$commentId"
								if (comment) {
									terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/NTE-1", "1")
									terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/NTE-3","$comment")
								}
							}
						} else {
							//TODO Error handling
							println ("Float conversion error in:"+ parsedJson."$odkElementId")
						}
					} else { // hl7Type is ST
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-1", OBXSegmentCounter.toString())
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-2", "ST")
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-3", "$mosaiqElementId")
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-5", parsedJson."$odkElementId")
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-11", "F") // Observation Result Status
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-14", hl7TsNow) // Date/Time of the Observation
					}
				}
			}
					
			return hapiMsg.encode()
		
		/*} catch (Exception e) {
			throw new Hl7Exception(message: "Error during Generation of HL7 message", exception: e)
		}*/
	}
	
	// Previous implementation
	/*def ImportHeightEtcIntoTestPatient (OdkSubmission odkSubmissionInstance) {
		
		try { 
			String msgTempl = "MSH|^~\\&|OMNI-Lab|P208|MOSAIQ||201107201645+1100||ORU^R01^ORU_R01|SEALSS_20110720.4476|P|2.3.1^AUS&Australia&ISO3166-1|3143273||||AUS||EN^English^ISO639-1\r" +
			// MSH-10: Message Control ID (ST)
			"PID|1||8191821.ILL^^^^UR~G^^^AUSDVA^DVA|8191821^^^^016^P208|ISOFT^WOLL^^^MRS||19731007|F|||114 CROWN ST^^WOLLONGONG^NSW^2500||^PRN^PH^^^^442284758\r" +
			"OBR|1||11W136835O^2206.AUSNATA^2206^L|I237^PSA^NATA2206|||201107201600|||||||201107201638|SER&Serum&HL70070|094544DY^NASSER^ELIAS HABIB^^^DR^^^AUSHICPR^L^^^MCR||||||201107201645||IMM|F||^^^20110720^^R|^^^^^^^^NATA2206^L^^^MCR||||1^^201107201645+1100\r" +
			// OBR-3: Filler Order Number; OBR-4: Universal Service ID; OBR-7: Observation Date/Time; OBR-14: Specimen received Date/Time; OBR-22: Results status change; OBR-27:Quantity/Timing?; OBR-32: ?Principal Results Interpreter
			"OBX|1|NM|I0242^PSAI^NATA2206^^^||4.0|ng/mL|||||F|||201107201600+1100|SEALSS^2206^AUSNATA\r" +
			// OBX-14: Date/Time of Observation
			"NTE|1||\\.br\\Comment:\\.br\\^\r"
			
			// Parse message in HL7 version 2.3.1
			HapiContext context = new DefaultHapiContext();
			CanonicalModelClassFactory mcf = new CanonicalModelClassFactory("2.3.1");
			context.setModelClassFactory(mcf);
			PipeParser parser = context.getPipeParser();
			ca.uhn.hl7v2.model.v231.message.ORU_R01 hapiMsg = (ca.uhn.hl7v2.model.v231.message.ORU_R01) parser.parse(msgTempl);
			
			//parse JSON string for convenient access e.g. via parsedJson.comment
			def slurper = new JsonSlurper()
			def parsedJson = slurper.parseText(odkSubmissionInstance.odkData.toString())
	
			// Replace PSA value and various timestamps in template via terser
			Terser terser = new Terser(hapiMsg);
			terser.set("/.OBX-5", odkSubmissionInstance.variableValue.toString());
			
			Calendar calendar = Calendar.getInstance(); // current date/time (base time)
			TimeZone timeZone = TimeZone.getTimeZone("Australia/Sydney");
			calendar.setTimeZone(timeZone);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmZ");
			simpleDateFormat.setTimeZone(timeZone);  // via SimpleDateFormat timeZone adapts to daylight savings (not directly?!?)
			
			String hl7TsNow = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			calendar.add(Calendar.HOUR, -1); // 1h before base time
			String hl7TsNowMinus1h = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			calendar.add(Calendar.HOUR, -1); // 2h before base time
			String hl7TsNowMinus2h = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			terser.set("/.MSH-7", hl7TsNow);
			terser.set("/.OBR-7", hl7TsNowMinus2h);
			terser.set("/.OBR-14", hl7TsNowMinus1h);
			terser.set("/.OBR-22", hl7TsNow);
			terser.set("/.OBX-14", hl7TsNowMinus2h);
			
			//println (hapiMsg.printStructure(true))
			terser.set("/.OBXNTE/NTE-3-2", parsedJson.comment)
					
			return hapiMsg.encode()
		
		} catch (Exception e) {
			throw new Hl7Exception(message: "Error during Generation of HL7 message", exception: e)
		}
	}*/
	
	def ImportOTR_ROIntoTestPatient(OdkSubmission odkSubmissionInstance) {
		
		//try {
			String msgTempl = "MSH|^~\\&|OMNI-Lab|P208|MOSAIQ||201107201645+1100||ORU^R01^ORU_R01|SEALSS_20110720.4476|P|2.3.1^AUS&Australia&ISO3166-1|-1||||AUS||EN^English^ISO639-1\r" +
			// MSH-7: Date/Time of Message; MSH-10: Message Control ID (ST); MSH-13: Sequence Number (-1 if not used)
			"PID|1||8191821^^^^UR~G^^^AUSDVA^DVA|8191821^^^^016^P208|ISOFT^WOLL^^^MRS||19731007|F|||114 CROWN ST^^WOLLONGONG^NSW^2500||^PRN^PH^^^^442284758\r" +
			"OBR|1||11W136835O^2206.AUSNATA|^OTR|||201107201600|||0945449B^NASSER^ELIAS^^^DR^^^AUSHICPR^L^^^MCR||||||0945449B^NASSER^ELIAS^^^DR^^^AUSHICPR^L^^^MCR|||||||||F||||||\r"
			// OBR-3: Filler Order Number; OBR-4: Universal Service ID; OBR-7: Observation Date/Time; OBR-10: Collector Identifier; OBR-14: Specimen received Date/Time; OBR-16: Ordering Provider; OBR-22: Results status change; OBR-27:Quantity/Timing?; OBR-32: ?Principal Results Interpreter
			
			// Parse template message in HL7 version 2.3.1
			HapiContext context = new DefaultHapiContext();
			CanonicalModelClassFactory mcf = new CanonicalModelClassFactory("2.3.1");
			context.setModelClassFactory(mcf);
			PipeParser parser = context.getPipeParser();
			ca.uhn.hl7v2.model.v231.message.ORU_R01 hapiMsg = (ca.uhn.hl7v2.model.v231.message.ORU_R01) parser.parse(msgTempl);
			
			// Initialise terser and slurper for convenient read/write access and date/time utilities (incl HL7 format)
			Terser terser = new Terser(hapiMsg)
			def slurper = new JsonSlurper()
			def parsedJson = slurper.parseText(odkSubmissionInstance.odkData.toString())
			Calendar calendar = Calendar.getInstance(); // current date/time (base time)
			TimeZone timeZone = TimeZone.getTimeZone("Australia/Sydney");
			calendar.setTimeZone(timeZone);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmZ");
			simpleDateFormat.setTimeZone(timeZone);  // via SimpleDateFormat timeZone adapts to daylight savings (not directly?!?)
			String hl7TsNow = new CommonTS(simpleDateFormat.format(calendar.getTime())).getValue()
			
			// Set details in MSH segment
			terser.set("/.MSH-7", hl7TsNow) // Date/Time of message
			def messageControlID = "PROSAIQ_" + odkSubmissionInstance.id.toString()
			terser.set("/.MSH-10", messageControlID)
			
			// Set details in OBR segment
			// Set filler order ID (arbitrary but unique - here 2 components were chosen)
			terser.set("/.OBR-3-1", messageControlID)
			//terser.set("/.OBR-3-2", odkSubmissionInstance.formIdentifier.toString())
			terser.set("/.OBR-7", hl7TsNow) // Observation Date/Time
	
			// Add OBX segments for each submitted survey element
			
			// odk --> mosaig ID mapping plus other information e.g. type used to generate +/- validate HL7 message (format: groovy list of maps) 
			def mappingInfo =[
				[odk:"group_otr_ro/group_thorax/lung_acute", mosaiq:"OTR-Lng-Act", hl7Type:"ST"],	
				[odk:"group_otr_ro/group_skin/skin_acute", mosaiq:"OTR-Skn-Act", hl7Type:"ST"],
				[odk:"group_otr_ro/group_weight/weight", mosaiq:"1010.1", hl7Type:"NM", unit:"kg"]		
			]
			def int OBXSegmentCounter = 0
			//def submittedSurveyElements = ""
			for (element in mappingInfo) {
				def odkElementId = element["odk"]
				def mosaiqElementId = element["mosaiq"]
				def unitForNumericals = element["unit"] 
				if (parsedJson."$odkElementId") {
					OBXSegmentCounter += 1
					//submittedSurveyElements = submittedSurveyElements + ", " + odkElementId
					if (element["hl7Type"] == "NM") {
						if (parsedJson."$odkElementId".isFloat()) {
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-1", OBXSegmentCounter.toString())
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-2", "NM")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-3", "$mosaiqElementId")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-5", parsedJson."$odkElementId")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-6", "$unitForNumericals")
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-11", "F") // Observation Result Status
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-14", hl7TsNow) // Date/Time of the Observation
							terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/NTE-3-2", parsedJson.comment)
						} else {
							//TODO Error handling
							println ("Float conversion error in:"+ parsedJson."$odkElementId")
						}
					} else { // hl7Type is ST
					 	terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-1", OBXSegmentCounter.toString())
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-2", "ST")
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-3", "$mosaiqElementId")
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-5", parsedJson."$odkElementId")
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-11", "F") // Observation Result Status
						terser.set("/.OBXNTE(" + (OBXSegmentCounter-1).toString() + ")/OBX-14", hl7TsNow) // Date/Time of the Observation
					}
				}
			}
			/*if (submittedSurveyElementsCounter > 0) {
				submittedSurveyElements = submittedSurveyElements.substring(2)
			}*/
			
			//terser.set("/.OBX-5", odkSubmissionInstance.variableValue.toString());
			
			//return "Number of submitted survey elements: $submittedSurveyElementsCounter ($submittedSurveyElements)"
			
			//println (hapiMsg.printStructure(true))
			//return hapiMsg.printStructure(true)
					
			return hapiMsg.encode()
		
		/*} catch (Exception e) {
			throw new Hl7Exception(message: "Error during Generation of HL7 message", exception: e)
		}*/
	}
}
