<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Squic</title>

		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}

			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 18em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>
	</head>
	<body>

<g:javascript>
$(document).ready(function() {
    $("#editMCQResponse").hide();
    $("#deleteMCQResponse").hide();

    $("#MCQResponsesSelect").change(function(){
                var id=$("#MCQResponsesSelect").val();
                if(id!=null && id!=""){
                    $("#editMCQResponse").show();
                    $("#deleteMCQResponse").show();
                }
                else{
                    $("#editMCQResponse").hide();
                    $("#deleteMCQResponse").hide();
                }
            });

    $("#confirmationDialog").dialog({
        autoOpen: false,
        height: 250,
        width: 400,
        modal: true,
        buttons: {
            Delete: function() {
                submitMCQDeleteForm();
                $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });


    $("#deleteMCQResponse").click(function(){
        $("#confirmationMessage").html("Are you sure you want to delete this Response?")
        $( "#confirmationDialog" ).dialog("open")
    });

});

function submitMCQDeleteForm(){
    $("#deleteMCQResponse").attr("value","delete");
    $("#MCQForm").submit();
}
</g:javascript>

<div id="confirmationDialog" title="Confirmation Dialog">
        <p id="confirmationMessage"></p>
</div>

		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="status" role="complementary">
			<h1>User</h1>
			<ul>
                   <g:if test="${session.user}">
				        <li>User: <g:fieldValue bean="${session.user}" field="username"/>
                            &nbsp; <g:link controller="user" action="edit" id="${session.user.id}">edit</g:link></li>
                       <li><g:link controller="user" action="logout">logout</g:link> </li>
                   </g:if>
                <g:else>
                    <li><g:link controller="user" action="login">login</g:link> </li>
                    <li><g:link controller="user" action="create">new user</g:link> </li>
                </g:else>
			</ul>
            <g:if test="${session.user}">
                <g:form controller="Menu" action="update">
                    <h1>Quizzes</h1>
                    <ul>
                        <li>
                            Quiz: <g:select name="quizzes" from="${session.user.quizzes}" onchange="this.form.submit()"
                            optionKey="id" optionValue="name" value="${session.quiz?.id}"
                            noSelection="['':'-none-']"/>
                        </li>
                        <li><g:link action="create" controller="Quiz">Create Quiz</g:link> </li>
                    </ul>
                 </g:form>
                    <g:if test="${session.quiz}">
                        <h2>Quiz Elements</h2>
                        <ul>
                            <li>
                                <g:form name="MCQForm" id="MCQForm" controller="MCQResponse" action="deleteFromMenu">
                                MCQ Responses:
                                <g:select id="MCQResponsesSelect" name="responses" from="${session.quiz.responses}"
                                    optionKey="id" noSelection="['':'-none-']"/>
                                 <p class="menuButtonsContainer">
                                     <g:link action="list" controller="MCQResponse" class="ui-icon ui-icon-extlink menuButton" title="list responses"/>
                                    <g:actionSubmit value="create" action="createFromMenu" class="ui-icon ui-icon-plusthick menuButton" title="create response"/>
                                    <g:actionSubmit id="editMCQResponse" value="edit" action="editFromMenu" class="ui-icon ui-icon-pencil menuButton" title="edit response"/>
                                     <input type="button" id="deleteMCQResponse" name="_action_deleteFromMenu" class="ui-icon ui-icon-trash menuButton" style="display: block;" title="delete response"/>
                                </p>

                                </g:form>
                            </li>

                        </ul>
                    </g:if>

            </g:if>
		</div>
		<div id="page-body" role="main">
			<h1>Welcome to Squic</h1>
			<p>This website lets you configure quizzes using the squic components, to be later exported as Android applications.</p>

            <g:if env="development">
                <div id="controller-list" role="navigation">
                    <h2>Available Controllers:</h2>
                    <ul>
                        <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                            <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
                        </g:each>
                    </ul>
                </div>
            </g:if>
		</div>
	</body>
</html>
