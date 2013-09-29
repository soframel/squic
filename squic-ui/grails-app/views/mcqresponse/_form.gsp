<%@ page import="org.soframel.squic.quiz.response.*" %>


<g:javascript>
$(document).ready(function() {

    $('.colorPicker').ColorPicker({
	onSubmit: function(hsb, hex, rgb, el) {
		$(el).val(hex);
		$(el).ColorPickerHide();
	},
	onBeforeShow: function () {
		$(this).ColorPickerSetColor(this.value);
	}
})
.bind('keyup', function(){
	$(this).ColorPickerSetColor(this.value);
});



            //setting up initial value
    <%--if($("#textResponse").attr("checked")){
        toggleResponseType("text");
    }
    else if($("#colorResponse").attr("checked")){
        toggleResponseType("color");
    }
    else if($("#imageResponse").attr("checked")){
        toggleResponseType("image");
    }--%>

    //initial value - no JS
    <g:if test="${responseInstance==null || responseInstance instanceof org.soframel.squic.quiz.response.TextResponse}">
        toggleResponseType("text");
    </g:if>
    <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ColorResponse}">
        toggleResponseType("color");
    </g:if>
    <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ImageResponse}">
        toggleResponseType("image");
    </g:if>
});

        function toggleResponseType(type){
            if(type=="text"){
                $("#textResponsePanel").show();
                $("#colorResponsePanel").hide();
                $("#imageResponsePanel").hide();
                $(".textRequired").attr("required", "required");
                $(".colorRequired").removeAttr("required");
                $(".imageRequired").removeAttr("required");
            }
            else if(type=="color")
            {
                $("#colorResponsePanel").show();
                $("#textResponsePanel").hide();
                $("#imageResponsePanel").hide();
                $(".colorRequired").attr("required", "required");
                $(".textRequired").removeAttr("required");
                $(".imageRequired").removeAttr("required");
            }
            else if(type=="image")
            {
                $("#imageResponsePanel").show();
                $("#textResponsePanel").hide();
                $("#colorResponsePanel").hide();
                $(".imageRequired").attr("required", "required");
                $(".textRequired").removeAttr("required");
                $(".colorRequired").removeAttr("required");
            }
        }
</g:javascript>

<div class="subPanel" id="textResponsePanel">

    <div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'text', 'error')} ">
        <label for="text">
            <g:message code="textResponse.text.label" default="Text" />
            <span class="required-indicator">*</span>
        </label>
        <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.TextResponse}">
            <g:textField name="text" value="${responseInstance?.text}" class=".textRequired"/>
        </g:if>
        <g:else>
            <g:textField name="text" value="" class=".textRequired"/>
        </g:else>
    </div>
</div>

<div class="subPanel" id="colorResponsePanel">

    <div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'color', 'error')} required">
        <label for="color">
            <g:message code="colorResponse.color.label" default="Color" />
            <span class="required-indicator">*</span>
        </label>
        <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ColorResponse}">
            <g:textField name="colorCode" value="${responseInstance?.color?.colorCode}" class=".colorRequired colorPicker"/>
            <span style="background-color: #${responseInstance?.color?.colorCode}">&nbsp&nbsp&nbsp;&nbsp;&nbsp;</span>
        </g:if>
        <g:else>
            <g:textField name="colorCode" value="fe9810" class=".colorRequired colorPicker"/>
        </g:else>
    </div>
</div>

<div class="subPanel" id="imageResponsePanel">
    <div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'imageFile', 'error')} ">
        <label for="imageFile">
            <g:message code="imageResponse.imageFile.label" default="Image File" />
            <span class="required-indicator">*</span>
        </label>
        <g:if test="${responseInstance!=null && responseInstance instanceof org.soframel.squic.quiz.response.ImageResponse}">
            ${fieldValue(bean: responseInstance, field: 'imageFile')}
            &nbsp; &nbsp; change: <input type="file" name="imageFile" />
        </g:if>
        <g:else>
            <input type="file" name="imageFile" class=".imageRequired"/>
        </g:else>
    </div>
</div>