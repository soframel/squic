
<%@ page import="org.soframel.squic.quiz.action.SpeechResultAction" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'speechResultAction.label', default: 'SpeechResultAction')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-speechResultAction" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-speechResultAction" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="speechFile" title="${message(code: 'speechResultAction.speechFile.label', default: 'Speech File')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${speechResultActionInstanceList}" status="i" var="speechResultActionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${speechResultActionInstance.id}">${fieldValue(bean: speechResultActionInstance, field: "speechFile.file")}</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${speechResultActionInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
