<%@ page import="org.soframel.squic.quiz.action.TextToSpeechResultAction" %>



<div class="fieldcontain ${hasErrors(bean: textToSpeechResultActionInstance, field: 'text', 'error')} ">
	<label for="text">
		<g:message code="textToSpeechResultAction.text.label" default="Text" />
		
	</label>
	<g:textField name="text" value="${textToSpeechResultActionInstance?.text}"/>
</div>

