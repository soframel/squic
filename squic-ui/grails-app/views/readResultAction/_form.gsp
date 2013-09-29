<%@ page import="org.soframel.squic.quiz.action.ReadResultAction" %>

<g:javascript>
    $(document).ready(function() {

        $("#actionItems").on("click", ".insertItem", function(event){
            //find position where to insert item
            var index=$(this).index(".insertItem");
            //add editable line

            var htmlLine="<tr>"+
                    "<td><select name='actionKind"+index+"' id='ActionKing"+index+"' required>"+
                        "<option value='QUESTION'>QUESTION</option>"+
                        "<option value='RESPONSE'>RESPONSE</option>"+
                        "<option value='GOODRESPONSE'>GOODRESPONSE</option>"+
                        "<option value='TEXT' selected>TEXT</option>"+
                        "</select>"+
                    "<td><input type='text' name='text"+
                    index+
                    "' value=''/></td>"+
                    "<td><a class='deleteItem'>Delete</a></td>"+
                    "<td><a class='insertItem'>Insert after</a></td></tr>";
            $(this).closest("tr").after(htmlLine);
        });

        $("#actionItems").on("click", ".deleteItem", function(event){
           $(this).closest("tr").remove();
        });

        $("#actionItems").on("change", "select", function(event){
            var select=$(this);
            var textField=$(this).parent().next().children();
            if(select.val()=='TEXT'){
                textField.removeAttr('disabled');
            }
            else{
                textField.attr('disabled', 'disabled');
                textField.val("");
            }
        });
});
</g:javascript>


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
            <g:set var="i" value="${0}"/>
            <g:each in="${readResultActionInstance.items}" var="item">
               <tr>
                   <g:set var="disabled" value="disabled"/>
                   <g:if test="${item.actionKind?.name()=='TEXT'}">
                       <g:set var="disabled" value="false"/>
                   </g:if>

                   <td><g:select name="actionKind${i}" from="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind?.values()}"
                                 keys="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind.values()*.name()}" required="" value="${item.actionKind?.name()}"
                                /></td>
                   <td><g:textField name="text${i}" value="${item.text}" disabled="${disabled}"/></td>
                   <td><a class='deleteItem'>Delete</a></td>
                   <td><a class='insertItem'>Insert after</a></td>
               </tr>
                <g:set var="i" value="${i+1}"/>
            </g:each>
        </g:if>
        <g:else>
            <tr>
                <td><g:select name="actionKind0" from="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind?.values()}" keys="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind.values()*.name()}" required=""
                value='TEXT'/></td>
                <td><g:textField name="text0"/></td>
                <td><a class='deleteItem'>Delete</a></td>
                <td><a class='insertItem'>Insert after</a></td>
            </tr>
        </g:else>
    </table>
</div>

