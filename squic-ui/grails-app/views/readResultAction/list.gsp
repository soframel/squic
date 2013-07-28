
<%@ page import="org.soframel.squic.quiz.action.ReadResultAction" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'readResultAction.label', default: 'ReadResultAction')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-readResultAction" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-readResultAction" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					    <th>id</th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${readResultActionInstanceList}" status="i" var="readResultActionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					     <td><g:link action="show" id="${readResultActionInstance.id}">${fieldValue(bean: readResultActionInstance, field: "id")}</g:link></td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${readResultActionInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
