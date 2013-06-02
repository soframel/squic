<%@ page import="org.soframel.squic.quiz.action.ReadResultAction" %>

<g:javascript library="jquery" plugin="jquery"/>

<g:javascript>
    $(document).ready(function() {

        $("#actionItems").on("click", ".insertItem", function(event){
            //find position where to insert item
            var index=$(this).index(".insertItem");
            //add editable line
            $(this).closest("tr").after("<tr>"+
                    '<td><g:select from="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind?.values()}" keys="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind.values()*.name()}" required="" name="actionKind'+index+'"/></td>'+
                    "<td><input type='text' name='text"+index+"' value=''/></td>"+
                    "<td><a class='deleteItem'>Delete</a></td>"+
                    "<td><a class='insertItem'>Insert after</a></td></tr>");
        });

        $("#actionItems").on("click", ".deleteItem", function(event){
           $(this).closest("tr").remove();
        });

});
</g:javascript>

<div class="fieldcontain ${hasErrors(bean: textToSpeechResultActionInstance, field: 'text', 'error')} ">
    <label for="text">
        <g:message code="readResultAction.text.label" default="Text" />

    </label>
    <g:textField name="text" value="${readResultAction?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: readResultActionInstance, field: 'items', 'error')} required">
	<label for="items">
		<g:message code="readResultAction.items.label" default="Items" />
		<span class="required-indicator">*</span>
	</label>
    <table id="actionItems">
        <thead>
            <tr>
                <th>Action Kind</th>
                <th>Text</th>
                <th></th>
                <th><a class='insertItem'>Insert after</a></th>
            </tr>
        </thead>
        <g:if test="${readResultActionInstance.items}">
            <g:each in="${readResultActionInstance.items}" var="item">
               <tr>
                   <td><td><g:select name="actionKind" from="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind?.values()}" keys="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind.values()*.name()}" required="" value="${readActionItemInstance?.actionKind?.name()}"/></td></td>
                   <td><g:textField name="text" value="${item.text}"/></td>
                   <td><a class='deleteItem'>Delete</a></td>
                   <td><a class='insertItem'>Insert after</a></td>
               </tr>
            </g:each>
        </g:if>
        <g:if test="not(${readResultActionInstance.items})">
            <tr>
                <td><g:select name="actionKind0" from="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind?.values()}" keys="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind.values()*.name()}" required=""/></td>
                <td><g:textField name="text0"/></td>
                <td><a class='deleteItem'>Delete</a></td>
                <td><a class='insertItem'>Insert after</a></td>
            </tr>
        </g:if>
    </table>
</div>

