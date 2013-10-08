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

<%@ page import="prosaiq.OdkSubmission" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'odkSubmission.label', default: 'OdkSubmission')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-odkSubmission" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<!-- POST-SCAFFOLD <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li> -->
			</ul>
		</div>
		<div id="list-odkSubmission" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="submitted" title="${message(code: 'odkSubmission.submitted.label', default: 'Submitted')}" />
					
						<g:sortableColumn property="formIdentifier" title="${message(code: 'odkSubmission.formIdentifier.label', default: 'ODK Form Identifier')}" />
					
						<g:sortableColumn property="odkData" title="${message(code: 'odkSubmission.odkData.label', default: 'Odk Data(JSON)')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${odkSubmissionInstanceList}" status="i" var="odkSubmissionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						
						<!-- <td><g:link action="show" id="${odkSubmissionInstance.id}">${fieldValue(bean: odkSubmissionInstance, field: "submitted")}</g:link></td> -->
						<!-- POST-SCAFFOLD (replace previous commented out line with following and moved show-link to the odkData field (see below)-->
						<td><g:formatDate date="${odkSubmissionInstance.submitted}" format="dd/MM/yy - HH:mm:ss" /></td>
					
						<td>
                          <g:if test="${odkSubmissionInstance.formIdentifier}">${fieldValue(bean: odkSubmissionInstance, field: "formIdentifier")} [<g:link action="outputHl7" id="${odkSubmissionInstance.id}">generate HL7</g:link>]</g:if>
                          <g:else>${fieldValue(bean: odkSubmissionInstance, field: "formIdentifier")}</g:else>
                        </td>
					
						<!-- <td>${fieldValue(bean: odkSubmissionInstance, field: "odkData")}</td> -->
						<!-- POST-SCAFFOLD (replace previous commented out line with following block -->
                        <td>
                          <g:if test="${odkSubmissionInstance.odkData.size() > 70}">${odkSubmissionInstance.odkData.substring(0,69)+'... '}[<g:link action="show" id="${odkSubmissionInstance.id}">details</g:link>]</g:if>
                          <g:else>${odkSubmissionInstance.odkData}</g:else>
                        </td>
						
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${odkSubmissionInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
