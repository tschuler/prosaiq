<!--
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
-->

<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main" />
<title>Welcome to Grails</title>
<style type="text/css" media="screen">
#status {
	background-color: #eee;
	border: .2em solid #fff;
	margin: 2em 2em 1em;
	padding: 1em;
	width: 12em;
	float: left;
	-moz-box-shadow: 0px 0px 1.25em #ccc;
	-webkit-box-shadow: 0px 0px 1.25em #ccc;
	box-shadow: 0px 0px 1.25em #ccc;
	-moz-border-radius: 0.6em;
	-webkit-border-radius: 0.6em;
	border-radius: 0.6em;
}

.ie6 #status {
	display: inline;
	/* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
}

#status ul {
	font-size: 0.9em;
	list-style-type: none;
	margin-bottom: 0.6em;
	padding: 0;
}

#status li {
	line-height: 1.3;
}

#status h1 {
	text-transform: uppercase;
	font-size: 1.1em;
	margin: 0 0 0.3em;
}

#page-body {
	margin: 2em 1em 1.25em 18em;
}

h2 {
	margin-top: 1em;
	margin-bottom: 0.3em;
	font-size: 1em;
}

p {
	line-height: 1.5;
	margin: 0.25em 0;
}

#controller-list ul {
	list-style-position: inside;
}

#controller-list ol ul {
	list-style-position: inside;
	margin-left: 2em;
}

#controller-list ol {
	list-style-position: inside;
}

#controller-list li {
	line-height: 1.3;
	list-style-position: inside;
	margin: 0.25em 0;
}

@media screen and (max-width: 480px) {
	#status {
		display: none;
	}
	#page-body {
		margin: 0 1em 1em;
	}
	#page-body h1 {
		margin-top: 0;
	}
}
</style>
</head>
<body>
	<a href="#page-body" class="skip"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>
	<div id="status" role="complementary">
		<h1>Application Status</h1>
		<ul>
			<li>App version: <g:meta name="app.version" /></li>
			<li>Grails version: <g:meta name="app.grails.version" /></li>
			<li>Groovy version: ${GroovySystem.getVersion()}</li>
			<li>JVM version: ${System.getProperty('java.version')}</li>
			<li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
			<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
			<li>Domains: ${grailsApplication.domainClasses.size()}</li>
			<li>Services: ${grailsApplication.serviceClasses.size()}</li>
			<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
		</ul>
		<h1>Installed Plugins</h1>
		<ul>
			<g:each var="plugin"
				in="${applicationContext.getBean('pluginManager').allPlugins}">
				<li>
					${plugin.name} - ${plugin.version}
				</li>
			</g:each>
		</ul>
	</div>
	<div id="page-body" role="main">
		<h1>
			Welcome to the PROsaiq demonstrator
		</h1>
		<p>This proof-of-concept demonstrator shows how data collected via
			a browser-based form or a dedicated Android app (often used with a portable device such as a tablet or mobile phone) can be used to
			generate a customised HL7 message. Typically this generated message is imported into
			another clinical information system such as the the oncology information system MOSAIQ®.</p>
		<p>
			Specifically the PROsaiq demonstrator lets the user input an
			arbitrary <a
				href="http://en.wikipedia.org/wiki/Prostate-specific_antigen"
				target="_blank">PSA</a> value via a browser-based form or an Android app. Uploading this
			value triggers a cascade of steps (see following technologies and
			process sections). A few seconds later a new entry in the PRO<i>saiq</i>
			web application's submissions list will appear. By clicking on the
			'generate HL7' link a HL7 Version 2.3.1 lab message (Australian <a
				href="http://infostore.saiglobal.com/store/Details.aspx?DocN=AS0733758460AT"
				target="_blank">AS4700.2–2004 standard</a>) containing the chosen
			PSA value will be dynamically generated and displayed.
		</p>
		<div id="LinkToSubmissionList">
			<span> Direct link to the PRO<i>saiq</i> web application's <g:link
					controller="odkSubmission" action="list">
				submissions
				list</g:link>
			</span>
		</div>

		<div id="controller-list" role="navigation">
			<h2>Technologies</h2>
			<ul>
			    <li><a href="https://enketo.org/"
                    target="_blank">Enketo</a> (OpenRosa-compliant browser-based form rendering engine)</li>
				<li><a href="http://opendatakit.org/use/collect/"
					target="_blank">ODK Collect</a> (OpenRosa-compliant data collection app for
					Android devices)</li>
				<li><a href="https://formhub.org/" target="_blank">Formhub</a>
					(OpenRosa-compliant server software that can receive data from Enketo or ODK Collect)</li>
				<li><a href="http://grails.org/" target="_blank">Grails</a>
					(JAVA Web application framework used as basis for the software that
					generates the HL7 message)</li>
				<li><a href="http://hl7api.sourceforge.net/" target="_blank">HAPI</a>
					(JAVA software library that supports the HL7 message generation and validation)</li>
			</ul>
		</div>

		<div id="controller-list" role="navigation">
			<h2>Using the Demonstrator --NEEDS UPDATING--</h2>
			<p>Try out the demonstrator by following these steps:</p>
			<ol>
				<li>Install ODK Collect</li>
				<ul>
					<li>follow the installation instructions at <a
						href="http://opendatakit.org/use/collect/" target="_blank">http://opendatakit.org/use/collect/</a></li>
					<li>Version 1.2.2 was used at the time of writing these notes</li>
				</ul>
				<li>Configure the ODK Collect preferences</li>
				<ul>
					<li>access the preferences by pressing the menu button in the
						ODK Collect start screen and clicking on 'General Settings'</li>
					<li>set Server Platform to 'Other'</li>
					<li>set URL to 'https://formhub.org/odk2hl7demo'</li>
					<li>leave all other settings at default (User and Password can
						be blank)</li>
				</ul>
				<li>Download the 'Submit a PSA value' form</li>
				<ul>
					<li>click on 'Get Blank Form' button in ODK Collect's main
						menu</li>
					<li>select and download the 'Submit a PSA value' form</li>
				</ul>
				<li>Fill out the 'Submit a PSA value' form</li>
				<ul>
					<li>click on 'Fill Blank Form' button in ODK Collect's main
						menu</li>
					<li>select the 'Submit a PSA value' form</li>
					<li>fill it in by swiping through and following the prompts</li>
					<li>once at the end of the form leave the 'finalised' checked
						and click the 'Save Form and Exit'</li>
				</ul>
				<li>Send the the finalised form to the server</li>
				<ul>
					<li>click on 'Send Finalised Form' button</li>
					<li>select and upload the filled in form (a success message
						confirms that the upload worked)</li>
				</ul>
				<li>Look at the submitted data in the ODK4PRO<i>to</i> web application
					and generate a HL7 message</li>
				<ul>
					<li>a few seconds after the upload a new entry in the
						ODK4PRO<i>to</i> web application will appear (if already open the list at
						<g:link controller="odkSubmission" action="list">http://odk4proto.herokuapp.com/odkSubmission/list</g:link>
						needs to be refreshed)
					<li>by clicking on the 'details' link the complete submitted
						data string will be displayed</li>
					<li>by clicking on 'generate HL7' link the dynamically
						generated HL7 message will be displayed</li>
				</ul>
				<p>
					<u>Additional (optional) steps:</u>
				<p>
				<p>When submitting the data from ODK Collect it is first sent to
					Formhub before being relayed to the ODK4PRO<i>to</i> web application.</p>
				<li>Log into the Formhub software</li>
				<ul>
					<li>browse to <a href="https://formhub.org" target="_blank">https://formhub.org</a></li>
					<li>using the user name 'odk2hl7demo'</li>
					<li>the password can be requested by sending an email to
						thilo.schuler@gmail.com</li>
				</ul>
				<li>Explore the formhub.org server's features</li>
				<ul>
					<li>in particular data input via an offline-capable
						browser-based form (accessible via the 'Enter Web Form' button)</li>
				</ul>
			</ol>
		</div>
		<div id="controller-list" role="navigation">
            <h2>Process</h2>
            <p>A more detailed description of the underlying technical process e.g. coupling of the involved systems via web services will follow.</p>
        </div>
	</div>
</body>
</html>
