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

