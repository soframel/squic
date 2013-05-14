
<%@ page import="org.soframel.squic.quiz.action.TextToSpeechResultAction" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'textToSpeechResultAction.label', default: 'TextToSpeechResultAction')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-textToSpeechResultAction" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-textToSpeechResultAction" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="text" title="${message(code: 'textToSpeechResultAction.text.label', default: 'Text')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${textToSpeechResultActionInstanceList}" status="i" var="textToSpeechResultActionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${textToSpeechResultActionInstance.id}">${fieldValue(bean: textToSpeechResultActionInstance, field: "text")}</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${textToSpeechResultActionInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
