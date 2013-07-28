<%@ page import="org.soframel.squic.quiz.action.ReadResultAction" %>

<g:javascript library="jquery" plugin="jquery"/>
<g:javascript>
    $(document).ready(function() {

        $(".actionRadio_${goal}").each(function(){
           if($(this).attr("checked"))
            togglePanels_${goal}($(this).attr("value"));
        });

        $(".actionRadio_${goal}").click(function(){
            togglePanels_${goal}($(this).attr("value"));
        });

         $("#actionItems_${goal}").on("click", ".insertItem", function(event){
            //find position where to insert item
            var index=$(this).index(".insertItem")+1;
            //add editable line
            var htmlLine="<tr>"+
"<td><select name='action_${goal}.actionKind"+index+"' id='action_${goal}.actionKind"+index+"' required>"+
"<option value='QUESTION'>QUESTION</option>"+
"<option value='RESPONSE'>RESPONSE</option>"+
"<option value='GOODRESPONSE'>GOODRESPONSE</option>"+
"<option value='TEXT' selected>TEXT</option>"+
"</select>"+
"<td><input type='text' name='action_${goal}.text"+
                    index+
                    "' value=''/></td>"+
"<td><a class='deleteItem'>Delete</a></td>"+
"<td><a class='insertItem'>Insert after</a></td></tr>";
            $(this).closest("tr").after(htmlLine);
        });

        $("#actionItems_${goal}").on("click", ".deleteItem", function(event){
           $(this).closest("tr").remove();
        });

         $("#actionItems_${goal}").on("change", "select", function(event){
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
function togglePanels_${goal}(selectedPanel){
    if(selectedPanel=="read"){
        $("#readActionPanel_${goal}").show();
        $("#ttsActionPanel_${goal}").hide();
        $("#speechActionPanel_${goal}").hide();
    }
    if(selectedPanel=="tts"){
        $("#ttsActionPanel_${goal}").show();
        $("#readActionPanel_${goal}").hide();
        $("#speechActionPanel_${goal}").hide();
    }
    if(selectedPanel=="speech"){
        $("#speechActionPanel_${goal}").show();
        $("#readActionPanel_${goal}").hide();
        $("#ttsActionPanel_${goal}").hide();
    }
}

</g:javascript>

Type of action:
<g:radio id="actionSpeech_${goal}" class="actionRadio_${goal}" name="actionRadio_${goal}" value="speech" checked="${action==null || action instanceof org.soframel.squic.quiz.action.SpeechResultAction}"/> Speech file
<g:radio id="actionTTS_${goal}" class="actionRadio_${goal}" name="actionRadio_${goal}" value="tts" checked="${action!=null && action instanceof org.soframel.squic.quiz.action.TextToSpeechResultAction}"/> Text To Speech
<g:radio id="actionRead_${goal}" class="actionRadio_${goal}" name="actionRadio_${goal}" value="read" checked="${action!=null && action instanceof org.soframel.squic.quiz.action.ReadResultAction}"/> Text To Speech (complex)


<div id="readActionPanel_${goal}">

    <div class="fieldcontain ${hasErrors(bean: action, field: 'showResponseDialog', 'error')} ">
        <label for="showResponseDialog">
            <g:message code="textToSpeechResultAction.showResponseDialog.label" default="Show Response Dialog?" />
        </label>

        <g:if test="${action instanceof org.soframel.squic.quiz.action.ReadResultAction}">
            <g:checkBox name="action_${goal}.showResponseDialog" value="${action?.showResponseDialog}" />
        </g:if>
        <g:else>
            <g:checkBox name="action_${goal}.showResponseDialog" value="${false}" />
        </g:else>
    </div>

<div class="fieldcontain ${hasErrors(bean: action, field: 'items', 'error')} required">
	<label for="items">
		<g:message code="readResultAction.items.label" default="items" />
		<span class="required-indicator">*</span>
	</label>
    <table id="actionItems_${goal}">
        <thead>
            <tr>
                <th>Action Kind</th>
                <th>Text</th>
                <th></th>
                <th><a class='insertitem'>Insert after</a></th>
            </tr>
        </thead>
        <g:if test="${action!=null && action instanceof ReadResultAction && action?.items}">
            <g:set var="i" value="${0}"/>
            <g:each in="${action?.items}" var="item">
               <tr>
                <g:set var="disabled" value="disabled"/>
                <g:if test="${item.actionKind?.name()=='TEXT'}">
                    <g:set var="disabled" value="false"/>
                </g:if>

                   <td><g:select name="action_${goal}.actionKind${i}" from="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind?.values()}"
                                     keys="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind.values()*.name()}" required="" value="${item.actionKind?.name()}"/></td>
                   <td><g:textField name="action_${goal}.text${i}" value="${item.text}" disabled="${disabled}"/></td>
                   <td><a class='deleteItem'>Delete</a></td>
                   <td><a class='insertItem'>Insert after</a></td>
               </tr>
                <g:set var="i" value="${i+1}"/>
            </g:each>
        </g:if>
        <%-- <g:if test="${action!=null && action instanceof ReadResultAction && (action?.items==null ||action?.items.size()==0) }">--%>
        <g:else>
            <tr>
                <td><g:select name="action_${goal}.actionKind0" from="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind?.values()}"
                              keys="${org.soframel.squic.quiz.action.ReadActionItem$ActionKind.values()*.name()}" required="" value='TEXT'/></td>
                <td><g:textField name="action_${goal}.text0"/></td>
                <td><a class='deleteItem'>Delete</a></td>
                <td><a class='insertItem'>Insert after</a></td>
            </tr>
        </g:else>
    </table>
</div>

</div>

<div id="ttsActionPanel_${goal}">
    <div class="fieldcontain ${hasErrors(bean: action, field: 'text', 'error')} ">
        <label for="text">
            <g:message code="textToSpeechResultAction.text.label" default="Text" />

        </label>

        <g:if test="${action instanceof org.soframel.squic.quiz.action.TextToSpeechResultAction}">
            <g:textField name="action_${goal}.text" value="${action?.text}"/>
        </g:if>
        <g:else>
            <g:textField name="action_${goal}.text" value=""/>
        </g:else>
    </div>

</div>

<div id="speechActionPanel_${goal}">

    <div class="fieldcontain ${hasErrors(bean: action, field: 'speechFile', 'error')} required">
        <label for="speechFile">
            <g:message code="speechResultAction.speechFile.label" default="Speech File" />
            <span class="required-indicator">*</span>
        </label>

        <g:if test="${action instanceof org.soframel.squic.quiz.action.SpeechResultAction}">
            <g:textField name="action_${goal}.speechFile.file" value="${action?.speechFile?.file}"/>
        </g:if>
        <g:else>
            <g:textField name="action_${goal}.speechFile.file" value=""/>
        </g:else>
    </div>

</div>