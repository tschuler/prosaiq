package prosaiq

import groovy.xml.*;
import groovy.json.*;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContextSupport;
import ca.uhn.hl7v2.model.v25.datatype.CE;
import ca.uhn.hl7v2.model.v25.datatype.ST;
import ca.uhn.hl7v2.model.v25.datatype.TX;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.model.v231.message.ORU_R01;
import ca.uhn.hl7v2.model.primitive.CommonTS;
import java.text.SimpleDateFormat;

import prosaiq.OdkSubmission;


class RestController {
	
	def hl7GeneratorService

	//def index() { }

	def display() {

		request.withFormat {
			xml {
				// read XML
				log.debug "\n\nRequest XML: \n\n" + XmlUtil.serialize(request.XML) + "\n\n"
				render "COOL - Look at console!\n\n"
			}
			json {
				// read JSON
				//log.debug "\n\nRequest JSON: \n\n" + JsonOutput.prettyPrint(request.JSON.toString())  + "\n\n"
				log.debug "\n\nRequest JSON: \n\n" + request.JSON.toString()  + "\n\n"
				render "COOL - Look at console!\n\n"
			}
			all {
				render "TRY AGAIN - Make sure 'Content-Type' is set to 'text/xml' or 'application/json'!\n\n"
			}
		}
	}
	
	/*def submit() { 
	//Receives submissions from ODK client via ODK server in JSON format
		
		if (request.format == "json") {
			def odkSubmissionInstance
						
			if (params.psa_value) {
				
				if (params.psa_value.isFloat()) {
					
					//TODO this section needs exception handling
					odkSubmissionInstance = new OdkSubmission(psaValue: params.psa_value.toFloat(), odkData: request.JSON.toString())
					
				} else {
					response.status = 403
					render contentType: "text/xml", encoding: "utf-8", {
						errors {
							error("Conversion error: '" + params.psa_value + "' can't be converted into a float value")
						}
					}
					return  //conversion error - abort action
				}
			} else {
			odkSubmissionInstance = new OdkSubmission(odkData: request.JSON.toString())
			}
		/*if (params.psa_value) {
			odkSubmissionInstance = new OdkSubmission(psaValue: '99'.toFloat(), odkData: 'psa')
		} else {
			odkSubmissionInstance = new OdkSubmission(odkData: 'other')
		}*/
		
			/*if (odkSubmissionInstance.save(flush:true)) {
				
				//TODO this block needs i/o exception handling
				// Generate HL7 message and put in polling directory (currently disabled as happens in on demand via UI link in HL7Service)
				def hl7Message = hl7Service.generatePSAForTestPatient(odkSubmissionInstance)
				def file = new File('/mnt/hgfs/ExchangeWithUbuntuVM/ODK4PRO_' + odkSubmissionInstance.id.toString() + '.oru')
				file.write(hl7Message)
				
				response.status = 201
				//render "ok (201)\n"
				render contentType: "application/xml", {
					id(odkSubmissionInstance.id)
				}
			} else {
				response.status = 403
				//render "error (403)\n"
				render contentType: "text/xml", encoding: "utf-8", {
					errors {
						error {
							odkSubmissionInstance?.errors?.fieldErrors?.each { err ->
								field(err.field)
								message(g.message(error: err)) //resolves error code
							}
						}
					}
				}
			}
		}
	}*/
	
	def submit() {
		//Receives submissions from ODK client via ODK server in JSON format
			
			if (request.format == "json") {
				def odkSubmissionInstance
				def isFromFormhubAndSupportedForm = false	
				if (params._xform_id_string) { // i.e. likely from formhub 
					
					if(params._xform_id_string == "psa2hl7") {
						isFromFormhubAndSupportedForm = true
						if (params.psa_value.isFloat()) {
						//TODO this section needs exception handling
						odkSubmissionInstance = new OdkSubmission(formIdentifier: params._xform_id_string, variableValue: params.psa_value.toFloat(), odkData: request.JSON.toString())
						} else {
							response.status = 403
							render contentType: "text/xml", encoding: "utf-8", {
								errors {
									error("Conversion error: '" + params.psa_value + "' can't be converted into a float value")
								}
							}
						return  //conversion error - abort action
						}
					}
					
					if(params._xform_id_string == "state_your_height-04") {
						isFromFormhubAndSupportedForm = true
						def height_in_centimetres = params.'height_in_centimetres' 
						if (height_in_centimetres.isFloat()) {
							//TODO this section needs exception handling
							odkSubmissionInstance = new OdkSubmission(formIdentifier: params._xform_id_string, variableValue: height_in_centimetres.toFloat(), odkData: request.JSON.toString())
						} else {
							response.status = 403
							render contentType: "text/xml", encoding: "utf-8", {
								errors {
									error("Conversion error: '" + height_in_centimetres + "' can't be converted into a float value")
								}
							}
						return  //conversion error - abort action
						}
					}
					
					if(params._xform_id_string == "OTR_RO-02") {
						isFromFormhubAndSupportedForm = true
						if (params.'group_otr_ro/group_weight/weight') {
							def weight_in_kg = params.'group_otr_ro/group_weight/weight'
							if (weight_in_kg.isFloat()) {
								//TODO this section needs exception handling
								odkSubmissionInstance = new OdkSubmission(formIdentifier: params._xform_id_string, variableValue: weight_in_kg.toFloat(), odkData: request.JSON.toString())
							} else {
								response.status = 403
								render contentType: "text/xml", encoding: "utf-8", {
									errors {
										error("Conversion error: '" + weight_in_kg + "' can't be converted into a float value")
									}
								}
							return  //conversion error - abort action
							}
						} else {  //weight not set
							odkSubmissionInstance = new OdkSubmission(formIdentifier: params._xform_id_string, odkData: request.JSON.toString())
						}
					}
					
				} 
				if (! isFromFormhubAndSupportedForm) { // but submission still in JSON format...
					odkSubmissionInstance = new OdkSubmission(odkData: request.JSON.toString())
				}
			
				if (odkSubmissionInstance.save(flush:true)) {
					if (isFromFormhubAndSupportedForm) {
						//TODO this block needs i/o exception handling
						// Generate HL7 message and put in polling directory (currently disabled as happens in on demand via UI link in HL7Service)
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
							//default:
						}
						if (hl7Message != "") {
							def file = new File('/mnt/hgfs/GeneratedFiles_on_WOLCCC15/ODK4PRO2_' + odkSubmissionInstance.id.toString() + '.oru')
							file.write(hl7Message)
						}  
					}
					response.status = 201
					render contentType: "application/xml", {
						id(odkSubmissionInstance.id)
					}
				} else { // odkSubmissionInstance could not be persisted
					response.status = 403
					//render "error (403)\n"
					render contentType: "text/xml", encoding: "utf-8", {
						errors {
							error {
								odkSubmissionInstance?.errors?.fieldErrors?.each { err ->
									field(err.field)
									message(g.message(error: err)) //resolves error code
								}
							}
						}
					}
				}
			}
		}
	

	def json2hl7() {
		if (request.format == "json") {
			
			String msgTempl = "MSH|^~\\&|OMNI-Lab|P208|MOSAIQ||201205091532+1100||ORU^R01^ORU_R01|ROBBEN_20120509.4254|D|2.3.1^AUS&Australia&ISO3166-1|4469949||||AUS||EN^English^ISO639-1|\r" + // MSH-7: Date/Time of message
			"PID|1||1234567.ILL^^^^UR~1234 12345 6^^^AUSHIC^MC|1234567^^^^016^P208|WURST^HANS^^^MR||19700509|M|||21 LITTLE STREET^^WHEREEVER^NSW^1234||^PRN^PH^^^^42850131|\r" +
			"OBR|1||12W3456789E^2206.AUSNATA^2206^L|C089^FEST^NATA2206|||201205091335|||||||201205091439|SER&Serum&HL70070|123456MH^INTERN^SIMON POOR^^^DR^^^AUSHICPR^L^^^MCR||||||201205091532||CH|F||^^^20120405^^R|654321JH^SMITH^GP^^^DR^^^NATA2206^L^^^MCR~222222A^HERZ^DOPPEL^^^DR^^^AUSHICPR^L^^^MCR||||AUTO-AUTH&AUTHORISATION&AUTOMATIC^^201205091532+1100|\r" +
			// OBR-3: Filler Order Number; OBR-4: Universal Service ID; OBR-7: Observation Date/Time; OBR-14: Specimen received Date/Time; OBR-22: Results status change; OBR-27:Quantity/Timing?; OBR-32: ?Principal Results Interpreter
			"OBX|1|NM|19199-9^PSA TOTAL^LN^^^||1.7|ng/mL|0.3 - 2.5|N|||F|||201205091335+1100|ROBBEN^2206^AUSNATA|\r"
			// OBX-14: Date/Time of Observation
			
			//log.debug "\n\nMarshalled JSON paramters: \n" + "a_string - " + params.a_string.toString() + "\na_decimal - " + params.a_decimal.toString() + "\n\n"
			//render "COOL - Look in console!\n\n"

			// Parse message in HL7 version 2.3.1
			HapiContext context = new DefaultHapiContext();
			CanonicalModelClassFactory mcf = new CanonicalModelClassFactory("2.3.1");
			context.setModelClassFactory(mcf);
			PipeParser parser = context.getPipeParser();
			ca.uhn.hl7v2.model.v231.message.ORU_R01 hapiMsg = (ca.uhn.hl7v2.model.v231.message.ORU_R01) parser.parse(msgTempl);
			
			// Replace PSA value and various timestamps in template via terser
			Terser terser = new Terser(hapiMsg);
			terser.set("/.OBX-5", params.a_decimal.toString());
			
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
			
			
			//log.debug "Test CommonTS: " + hl7TsNow + " - " + hl7TsNowMinus1h + " - " + hl7TsNowMinus2h
			
			
			def file = new File('/home/thiloschuler/generated.hl7')
			file.write(hapiMsg.encode())
			
			//log.debug "\n\nExample HL7v2.5 msg: \nn" + parser.encode(msg) + "\n\n" + msg.printStructure() + "\n\n"
			log.debug "\n\nHL7 message: \n\n" + hapiMsg.encode().replace("\r", "\r\n") + "\n\n" //+ msg.printStructure() + "\n\n"
			render "\nCOOL - Look in console!\n\n"
				
		}
		else {
			render "PROBLEM with restful submission e.g. non-JSON format."
		}
	}
	
	def firsttest() {
		if (request.format == "json") {
			
			//log.debug "\n\nMarshalled JSON paramters: \n" + "a_string - " + params.a_string.toString() + "\na_decimal - " + params.a_decimal.toString() + "\n\n"
			//render "COOL - Look in console!\n\n"

			// First, a message object is constructed
			ORU_R01 msg = new ORU_R01();

			/*
			 * The initQuickstart method populates all of the mandatory fields in the
			 * MSH segment of the message, including the message type, the timestamp,
			 * and the control ID.
			 */
			msg.initQuickstart("ORU", "R01", "T");

			/*
			 * The OBR segment is contained within a group called ORDER_OBSERVATION,
			 * which is itself in a group called PATIENT_RESULT. These groups are
			 * reached using named accessors.
			 */
			ORU_R01_ORDER_OBSERVATION orderObservation = msg.getPATIENT_RESULT().getORDER_OBSERVATION();

			// Populate the OBR
			OBR obr = orderObservation.getOBR();
			obr.getSetIDOBR().setValue("1");
			obr.getFillerOrderNumber().getEntityIdentifier().setValue("1234");
			obr.getFillerOrderNumber().getNamespaceID().setValue("LAB");
			obr.getUniversalServiceIdentifier().getIdentifier().setValue("88304");

			/*
			 * The OBX segment is in a repeating group called OBSERVATION. You can
			 * use a named accessor which takes an index to access a specific
			 * repetition. You can ask for an index which is equal to the
			 * current number of repetitions,and a new repetition will be created.
			 */
			ORU_R01_OBSERVATION observation = orderObservation.getOBSERVATION(0);

			// Populate the first OBX
			OBX obx = observation.getOBX();
			obx.getSetIDOBX().setValue("1");
			obx.getObservationIdentifier().getIdentifier().setValue("88304");
			obx.getObservationSubID().setValue("1");

			// The first OBX has a value type of CE. So first, we populate OBX-2 with "CE"...
			obx.getValueType().setValue("CE");

			// ... then we create a CE instance to put in OBX-5.
			CE ce = new CE(msg);
			ce.getIdentifier().setValue("T57000");
			ce.getText().setValue("GALLBLADDER");
			ce.getNameOfCodingSystem().setValue("SNM");
			Varies value = obx.getObservationValue(0);
			value.setData(ce);
			
			HapiContext context = new DefaultHapiContext();
			Parser parser = context.getPipeParser();
			//log.debug "\n\nExample HL7v2.5 msg: \nn" + parser.encode(msg) + "\n\n" + msg.printStructure() + "\n\n"
			log.debug "\n\nExample HL7v2.5 msg: \n\n" + msg.encode().replace("\r", "\r\n") + "\n\n" + msg.printStructure() + "\n\n"
			render "COOL - Look in console!\n\n"
				
		}
		else {
			render "PROBLEM with restful submission e.g. non-JSON format."
		}
	}
	
	def testterser() {
		String msg = "MSH|^~\\&|OMNI-Lab|P208|MOSAIQ||201205091532+1100||ORU^R01^ORU_R01|SEALSS_20120509.4254|P|2.3.1^AUS&Australia&ISO3166-1|4469949||||AUS||EN^English^ISO639-1|\r" + 
					"PID|1||4525311.ILL^^^^UR~2454 66221 6^^^AUSHIC^MC|4525311^^^^016^P208|MADDEN^ANDREW^^^MR||19700509|M|||21 JOSEPH STREET^^WOONONA^NSW^2517||^PRN^PH^^^^12345678|\r" +
					"OBR|1||12W090213E^2206.AUSNATA^2206^L|C089^FEST^NATA2206|||201205091335|||||||201205091439|SER&Serum&HL70070|096649MH^BEST^JOHN^^^DR^^^AUSHICPR^L^^^MCR||||||201205091532||CH|F||^^^20120405^^R|048037JH^BROWN^KATHERINE MARGARET^^^DR^^^NATA2206^L^^^MCR~2674328A^CARROLL^ALISTAIR^^^DR^^^AUSHICPR^L^^^MCR~2206853W^CHIENG^YEW HUNG^^^DR^^^AUSHICPR^L^^^MCR||||AUTO-AUTH&AUTHORISATION&AUTOMATIC^^201205091532+1100|\r" +
					"OBX|1|NM|C1040^IRON^NATA2206^^^||36.4|umol/L|8.1 - 32.6|H|||F|||201205091335+1100|SEALSS^2206^AUSNATA|\r" +
					"OBX|2|NM|C1050^TRANSFERRIN^NATA2206^^^||2.5|g/L|1.8 - 3.3||||F|||201205091335+1100|SEALSS^2206^AUSNATA|\r"
	
		HapiContext context = new DefaultHapiContext();
		Parser p = context.getGenericParser();
		Message hapiMsg = p.parse(msg);
					
		
		
		Terser terser = new Terser(hapiMsg);
		
		log.debug "\n\nPRE: \n" + hapiMsg.encode().replace("\r", "\r\n") + "\n"
		
		
		terser.set("/.PID-5-2", "PAUL")
		//terser.set("/.PID-5", "MADDEN^PAULA^^^MRS") // doesn't work as '/.PID-5' refers to '/.PID-5-1'
		
		log.debug "\n\nPOST: \n" + hapiMsg.encode().replace("\r", "\r\n") + "\n"
		
		//render "OUTPUT: " + terser.get("/.MSH-3") + " -- " + terser.get("/.MSH-9") + "\n\n";
		render "COOL - Look in console!\n\n"
	}
	def hl7231vs25() {
		String msg = "MSH|^~\\&|OMNI-Lab|P208|MOSAIQ||201205091532+1100||ORU^R01^ORU_R01|SEALSS_20120509.4254|P|2.3.1^AUS&Australia&ISO3166-1|4469949||||AUS||EN^English^ISO639-1|\r" +
		"PID|1||4525311.ILL^^^^UR~2454 66221 6^^^AUSHIC^MC|4525311^^^^016^P208|MADDEN^ANDREW^^^MR||19700509|M|||21 JOSEPH STREET^^WOONONA^NSW^2517||^PRN^PH^^^^42850131|\r" +
		"OBR|1||12W090213E^2206.AUSNATA^2206^L|C089^FEST^NATA2206|||201205091335|||||||201205091439|SER&Serum&HL70070|096649MH^MARLTON^SIMON JOHN^^^DR^^^AUSHICPR^L^^^MCR||||||201205091532||CH|F||^^^20120405^^R|048037JH^BROWN^KATHERINE MARGARET^^^DR^^^NATA2206^L^^^MCR~2674328A^CARROLL^ALISTAIR^^^DR^^^AUSHICPR^L^^^MCR~2206853W^CHIENG^YEW HUNG^^^DR^^^AUSHICPR^L^^^MCR||||AUTO-AUTH&AUTHORISATION&AUTOMATIC^^201205091532+1100|\r" +
		"OBX|1|NM|C1040^IRON^NATA2206^^^||36.4|umol/L|8.1 - 32.6|H|||F|||201205091335+1100|SEALSS^2206^AUSNATA|\r" +
		"OBX|2|NM|C1050^TRANSFERRIN^NATA2206^^^||2.5|g/L|1.8 - 3.3||||F|||201205091335+1100|SEALSS^2206^AUSNATA|\r"
		
		HapiContext context25 = new DefaultHapiContext();
		
		// Create the MCF. We want all parsed messages to be for HL7 version 2.5,
		CanonicalModelClassFactory mcf25 = new CanonicalModelClassFactory("2.5");
		context25.setModelClassFactory(mcf25);
		
		// Pass the MCF to the parser in its constructor
		PipeParser parser25	= context25.getPipeParser();
		
		// The parser parses the v2.3 message to a "v25" structure
		ca.uhn.hl7v2.model.v25.message.ORU_R01 hapiMsg25 = (ca.uhn.hl7v2.model.v25.message.ORU_R01) parser25.parse(msg);
		
		HapiContext context231 = new DefaultHapiContext();
		
		// Create the MCF. We want all parsed messages to be for HL7 version 2.3.1,
		CanonicalModelClassFactory mcf231 = new CanonicalModelClassFactory("2.3.1");
		context231.setModelClassFactory(mcf231);
		
		// Pass the MCF to the parser in its constructor
		PipeParser parser231	= context231.getPipeParser();
		
		// The parser parses the v2.3 message to a "v25" structure
		ca.uhn.hl7v2.model.v231.message.ORU_R01 hapiMsg231 = (ca.uhn.hl7v2.model.v231.message.ORU_R01) parser231.parse(msg);
		
		render	"\n\n === Parsed as HL7v2.5 ===\n\n- CONTENT -\n" + hapiMsg25.encode().replace("\r", "\r\n") + "\n- STRUCTURE -\n" + hapiMsg25.printStructure() + "\n" +
		"\n\n === Parsed as HL7v2.3.1 ===\n\n- CONTENT -\n" + hapiMsg231.encode().replace("\r", "\r\n") + "\n- STRUCTURE -\n" + hapiMsg231.printStructure() + "\n"
		
	}
}
