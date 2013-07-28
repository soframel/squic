<%@ page import="org.soframel.squic.user.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="edit-user" class="content scaffold-edit" role="main">
			<h1><g:message code="user.login.title"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${userInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${userInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="doLogin" >
				<fieldset class="form">

                    <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'username', 'error')} required">
                        <label for="username">
                            <g:message code="user.username.label" default="Username" />
                            <span class="required-indicator">*</span>
                        </label>
                        <g:textField name="username" required="" value="${userInstance?.username}"/>
                    </div>

                    <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} ">
                        <label for="password">
                            <g:message code="user.password.label" default="Password" />

                        </label>
                        <g:passwordField name="password" value="${userInstance?.password}" />
                    </div>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="login" class="login" value="${message(code: 'user.login.button', default: 'Login')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
