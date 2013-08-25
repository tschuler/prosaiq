
<%@ page import="prosaiq.OdkSubmission" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'odkSubmission.label', default: 'OdkSubmission')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-odkSubmission" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<!-- POST-SCAFFOLD <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li> -->
			</ul>
		</div>
		<div id="show-odkSubmission" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list odkSubmission">
			
				<g:if test="${odkSubmissionInstance?.submitted}">
				<li class="fieldcontain">
					<span id="submitted-label" class="property-label"><g:message code="odkSubmission.submitted.label" default="Submitted" /></span>
					
						<span class="property-value" aria-labelledby="submitted-label"><g:formatDate date="${odkSubmissionInstance?.submitted}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${odkSubmissionInstance?.formIdentifier}">
				<li class="fieldcontain">
					<span id="formIdentifier-label" class="property-label"><g:message code="odkSubmission.formIdentifer.label" default="Form Identifier" /></span>
					
						<span class="property-value" aria-labelledby="formIdentifier-label"><g:fieldValue bean="${odkSubmissionInstance}" field="formIdentifier"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${odkSubmissionInstance?.variableValue}">
                <li class="fieldcontain">
                    <span id="variableValue-label" class="property-label"><g:message code="odkSubmission.variableValue.label" default="Value" /></span>
                    
                        <span class="property-value" aria-labelledby="variableValue-label"><g:fieldValue bean="${odkSubmissionInstance}" field="variableValue"/></span>
                    
                </li>
                </g:if>
				
				<g:if test="${odkSubmissionInstance?.odkData}">
				<li class="fieldcontain">
					<span id="odkData-label" class="property-label"><g:message code="odkSubmission.odkData.label" default="Odk Data (JSON)" /></span>
					
						<span class="property-value" aria-labelledby="odkData-label"><g:fieldValue bean="${odkSubmissionInstance}" field="odkData"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${odkSubmissionInstance?.id}" />
					<!-- <g:link class="edit" action="edit" id="${odkSubmissionInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link> -->
					<!-- POST-SCAFFOLD: adapt preceeding to following line -->
					<g:link class="edit" action="outputHl7" id="${odkSubmissionInstance?.id}"><g:message code="odkSubmission.button.generatehl7.label" default="Generate HL7 from this submission" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'odkSubmission.button.delete.label', default: 'Delete this submission')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /> 
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
