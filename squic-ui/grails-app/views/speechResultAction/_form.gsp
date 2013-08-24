<%@ page import="org.soframel.squic.quiz.action.SpeechResultAction" %>


<div class="fieldcontain ${hasErrors(bean: speechResultActionInstance, field: 'speechFile', 'error')} required">
	<label for="speechFile">
		<g:message code="speechResultAction.speechFile.label" default="Speech File" />

        <g:if test="${speechFile!=null && speechFile!=''}">
            ${fieldValue(bean: speechResultActionInstance, field: 'speechFile')}
            &nbsp; &nbsp; change: <input type="file" name="speechFileFile" />
        </g:if>
        <g:else>
            <input type="file" name="speechFileFile" />
        </g:else>

        <span class="required-indicator">*</span>
	</label>
	
</div>

