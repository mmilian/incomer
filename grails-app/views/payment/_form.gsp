<%@ page import="pl.incomer.payment.Payment" %>



<div class="fieldcontain ${hasErrors(bean: paymentInstance, field: 'id', 'error')} ">
	<label for="id">
		<g:message code="payment.id.label" default="Id" />
		
	</label>
	<g:field type="number" name="id" value="${paymentInstance.id}" />
</div>

<div class="fieldcontain ${hasErrors(bean: paymentInstance, field: 'date', 'error')} required">
	<label for="date">
		<g:message code="payment.date.label" default="Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="date" precision="day"  value="${paymentInstance?.date}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: paymentInstance, field: 'amount', 'error')} required">
	<label for="amount">
		<g:message code="payment.amount.label" default="Amount" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="amount" value="${fieldValue(bean: paymentInstance, field: 'amount')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: paymentInstance, field: 'email', 'error')} ">
	<label for="email">
		<g:message code="payment.email.label" default="Email" />
		
	</label>
	<g:field type="email" name="email" value="${paymentInstance?.email}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: paymentInstance, field: 'firstName', 'error')} ">
	<label for="firstName">
		<g:message code="payment.firstName.label" default="First Name" />
		
	</label>
	<g:textField name="firstName" value="${paymentInstance?.firstName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: paymentInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="payment.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${paymentInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: paymentInstance, field: 'transactionId', 'error')} ">
	<label for="transactionId">
		<g:message code="payment.transactionId.label" default="Transaction Id" />
		
	</label>
	<g:textField name="transactionId" value="${paymentInstance?.transactionId}"/>
</div>

