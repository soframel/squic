<%@ page import="org.soframel.squic.quiz.action.ReadResultAction" %>

<div class="fieldcontain ${hasErrors(bean: textToSpeechResultActionInstance, field: 'text', 'error')} ">
    <label for="text">
        <g:message code="readResultAction.text.label" default="Text" />

    </label>
    <g:textField name="text" value="${readResultAction?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: readResultActionInstance, field: 'items', 'error')} required">
	<label for="items">
		<g:message code="readResultAction.items.label" default="Items" />
		<span class="required-indicator">*</span>
	</label>
	
</div>

