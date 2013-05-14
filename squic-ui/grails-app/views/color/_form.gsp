<%@ page import="org.soframel.squic.quiz.media.Color" %>



<div class="fieldcontain ${hasErrors(bean: colorInstance, field: 'colorCode', 'error')} ">
	<label for="colorCode">
		<g:message code="color.colorCode.label" default="Color Code" />
		
	</label>
	<g:textField name="colorCode" value="${colorInstance?.colorCode}"/>
</div>

