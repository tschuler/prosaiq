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



<div class="fieldcontain ${hasErrors(bean: odkSubmissionInstance, field: 'submitted', 'error')} required">
	<label for="submitted">
		<g:message code="odkSubmission.submitted.label" default="Submitted" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="submitted" format="dd-MM-yyyy" precision="day"  value="${odkSubmissionInstance?.submitted}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: odkSubmissionInstance, field: 'psaValue', 'error')} ">
	<label for="psaValue">
		<g:message code="odkSubmission.psaValue.label" default="Psa Value" />
		
	</label>
	<g:field name="psaValue" value="${fieldValue(bean: odkSubmissionInstance, field: 'psaValue')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: odkSubmissionInstance, field: 'odkData', 'error')} required">
	<label for="odkData">
		<g:message code="odkSubmission.odkData.label" default="Odk Data" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="odkData" required="" value="${odkSubmissionInstance?.odkData}"/>
</div>

