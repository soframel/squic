<%@ page import="org.soframel.squic.quiz.automatic.CalculationQuestions" %>



<!--<div class="fieldcontain ${hasErrors(bean: calculationQuestionsInstance, field: 'id', 'error')} ">
	<label for="id">
		<g:message code="calculationQuestions.id.label" default="Id" />
		
	</label>
	<g:textField name="id" value="${calculationQuestionsInstance?.id}"/>
</div>-->

<div class="fieldcontain ${hasErrors(bean: calculationQuestionsInstance, field: 'maxValue', 'error')} required">
	<label for="maxValue">
		<g:message code="calculationQuestions.maxValue.label" default="Max Value" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="maxValue" type="number" value="${calculationQuestionsInstance.maxValue}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: calculationQuestionsInstance, field: 'minValue', 'error')} required">
	<label for="minValue">
		<g:message code="calculationQuestions.minValue.label" default="Min Value" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="minValue" type="number" value="${calculationQuestionsInstance.minValue}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: calculationQuestionsInstance, field: 'nbRandom', 'error')} required">
	<label for="nbRandom">
		<g:message code="calculationQuestions.nbRandom.label" default="Nb Random" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="nbRandom" type="number" value="${calculationQuestionsInstance.nbRandom}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: calculationQuestionsInstance, field: 'nbOperands', 'error')} required">
	<label for="nbOperands">
		<g:message code="calculationQuestions.nbOperands.label" default="Nb Operands" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="nbOperands" type="number" value="${calculationQuestionsInstance.nbOperands}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: calculationQuestionsInstance, field: 'operator', 'error')} required">
	<label for="operator">
		<g:message code="calculationQuestions.operator.label" default="Operator" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="operator" from="${org.soframel.squic.quiz.automatic.Operator?.values()}" keys="${org.soframel.squic.quiz.automatic.Operator.values()*.name()}" required="" value="${calculationQuestionsInstance?.operator?.name()}"/>
</div>

