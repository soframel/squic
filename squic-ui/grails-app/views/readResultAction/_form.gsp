<%@ page import="org.soframel.squic.quiz.action.ReadResultAction" %>


<div class="fieldcontain ${hasErrors(bean: readResultActionInstance, field: 'items', 'error')} required">
	<label for="items">
		<g:message code="readResultAction.items.label" default="Items" />
		<span class="required-indicator">*</span>
	</label>
	
</div>

