<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'MCQResponse.label', default: 'MCQ Response')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-textResponse" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-textResponse" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="text" title="Id" />
					    <g:sortableColumn property="text" title="Type" />
                        <g:sortableColumn property="text" title="text/color/image" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${responseInstanceList}" status="i" var="responseInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td><g:link action="edit" id="${responseInstance.id}">${fieldValue(bean: responseInstance, field: "id")}</g:link></td>

                        <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.TextResponse}">
                            <td>text</td>
                            <td>${fieldValue(bean: responseInstance, field: "text")}</td>
                        </g:if>
                        <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ColorResponse}">
                            <td>color</td>
                            <td>${fieldValue(bean: responseInstance, field: "color.colorCode")}
                                <span style="background-color: #${responseInstance?.color?.colorCode}">&nbsp&nbsp&nbsp;&nbsp;&nbsp;</span>
                            </td>
                        </g:if>
                        <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ImageResponse}">
                            <td>image</td>
                            <td>${fieldValue(bean: responseInstance, field: "imageFile")}</td>
                        </g:if>

					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${responseInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
