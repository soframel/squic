<%@ page import="org.soframel.squic.quiz.response.*" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'MCQResponse.label', default: 'MCQ Response')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
    <g:javascript>
        $(document).ready(function() {

            //when clicked
            $("#textResponse").click(function(){
                toggleResponseType("text");
            });
            $("#colorResponse").click(function(){
                toggleResponseType("color");
            });
            $("#imageResponse").click(function(){
                toggleResponseType("image");
            });
        });

    </g:javascript>
		<a href="#create-textResponse" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-textResponse" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${textResponseInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${textResponseInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
            <g:uploadForm action="save">

                <g:radio id="textResponse" name="responseType" value="text" checked="${responseInstance==null || responseInstance instanceof org.soframel.squic.quiz.response.TextResponse}"/> Text Response &nbsp;
                <g:radio id="colorResponse" name="responseType" value="color" checked="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ColorResponse}"/> Color Response &nbsp;
                <g:radio id="imageResponse" name="responseType" value="image" checked="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ImageResponse}"/> Image Response


                <fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:uploadForm>
		</div>
	</body>
</html>
