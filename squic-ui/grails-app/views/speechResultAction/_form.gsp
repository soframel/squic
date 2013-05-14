<%@ page import="org.soframel.squic.quiz.action.SpeechResultAction" %>


<div class="fieldcontain ${hasErrors(bean: speechResultActionInstance, field: 'speechFile', 'error')} required">
	<label for="speechFile">
		<g:message code="speechResultAction.speechFile.label" default="Speech File" />
        <g:textField name="text" value="${speechResultActionInstance?.speechFile?.file}"/>
        <span class="required-indicator">*</span>
	</label>
	
</div>

