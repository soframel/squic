
<%@ page import="org.soframel.squic.quiz.question.initializable.calculation.CalculationQuestions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'calculationQuestions.label', default: 'CalculationQuestions')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-calculationQuestions" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-calculationQuestions" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list calculationQuestions">
			
				<g:if test="${calculationQuestionsInstance?.maxValue}">
				<li class="fieldcontain">
					<span id="maxValue-label" class="property-label"><g:message code="calculationQuestions.maxValue.label" default="Max Value" /></span>
					
						<span class="property-value" aria-labelledby="maxValue-label"><g:fieldValue bean="${calculationQuestionsInstance}" field="maxValue"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${calculationQuestionsInstance?.minValue}">
				<li class="fieldcontain">
					<span id="minValue-label" class="property-label"><g:message code="calculationQuestions.minValue.label" default="Min Value" /></span>
					
						<span class="property-value" aria-labelledby="minValue-label"><g:fieldValue bean="${calculationQuestionsInstance}" field="minValue"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${calculationQuestionsInstance?.nbRandom}">
				<li class="fieldcontain">
					<span id="nbRandom-label" class="property-label"><g:message code="calculationQuestions.nbRandom.label" default="Nb Random" /></span>
					
						<span class="property-value" aria-labelledby="nbRandom-label"><g:fieldValue bean="${calculationQuestionsInstance}" field="nbRandom"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${calculationQuestionsInstance?.nbOperands}">
				<li class="fieldcontain">
					<span id="nbOperands-label" class="property-label"><g:message code="calculationQuestions.nbOperands.label" default="Nb Operands" /></span>
					
						<span class="property-value" aria-labelledby="nbOperands-label"><g:fieldValue bean="${calculationQuestionsInstance}" field="nbOperands"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${calculationQuestionsInstance?.operator}">
				<li class="fieldcontain">
					<span id="operator-label" class="property-label"><g:message code="calculationQuestions.operator.label" default="Operator" /></span>
					
						<span class="property-value" aria-labelledby="operator-label"><g:fieldValue bean="${calculationQuestionsInstance}" field="operator"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${calculationQuestionsInstance?.id}" />
					<g:link class="edit" action="edit" id="${calculationQuestionsInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
