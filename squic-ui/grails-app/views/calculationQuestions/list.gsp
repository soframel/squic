
<%@ page import="org.soframel.squic.quiz.automatic.CalculationQuestions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'calculationQuestions.label', default: 'CalculationQuestions')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-calculationQuestions" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-calculationQuestions" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="maxValue" title="${message(code: 'calculationQuestions.maxValue.label', default: 'Max Value')}" />
					
						<g:sortableColumn property="minValue" title="${message(code: 'calculationQuestions.minValue.label', default: 'Min Value')}" />
					
						<g:sortableColumn property="nbRandom" title="${message(code: 'calculationQuestions.nbRandom.label', default: 'Nb Random')}" />
					
						<g:sortableColumn property="nbOperands" title="${message(code: 'calculationQuestions.nbOperands.label', default: 'Nb Operands')}" />
					
						<g:sortableColumn property="operator" title="${message(code: 'calculationQuestions.operator.label', default: 'Operator')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${calculationQuestionsInstanceList}" status="i" var="calculationQuestionsInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${calculationQuestionsInstance.id}">${fieldValue(bean: calculationQuestionsInstance, field: "maxValue")}</g:link></td>
					
						<td>${fieldValue(bean: calculationQuestionsInstance, field: "minValue")}</td>
					
						<td>${fieldValue(bean: calculationQuestionsInstance, field: "nbRandom")}</td>
					
						<td>${fieldValue(bean: calculationQuestionsInstance, field: "nbOperands")}</td>
					
						<td>${fieldValue(bean: calculationQuestionsInstance, field: "operator")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${calculationQuestionsInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
