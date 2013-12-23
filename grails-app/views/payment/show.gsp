
<%@ page import="pl.incomer.payment.Payment" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'payment.label', default: 'Payment')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-payment" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-payment" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list payment">
			
				<g:if test="${paymentInstance?.date}">
				<li class="fieldcontain">
					<span id="date-label" class="property-label"><g:message code="payment.date.label" default="Date" /></span>
					
						<span class="property-value" aria-labelledby="date-label"><g:formatDate date="${paymentInstance?.date}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${paymentInstance?.amount}">
				<li class="fieldcontain">
					<span id="amount-label" class="property-label"><g:message code="payment.amount.label" default="Amount" /></span>
					
						<span class="property-value" aria-labelledby="amount-label"><g:fieldValue bean="${paymentInstance}" field="amount"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${paymentInstance?.email}">
				<li class="fieldcontain">
					<span id="email-label" class="property-label"><g:message code="payment.email.label" default="Email" /></span>
					
						<span class="property-value" aria-labelledby="email-label"><g:fieldValue bean="${paymentInstance}" field="email"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${paymentInstance?.firstName}">
				<li class="fieldcontain">
					<span id="firstName-label" class="property-label"><g:message code="payment.firstName.label" default="First Name" /></span>
					
						<span class="property-value" aria-labelledby="firstName-label"><g:fieldValue bean="${paymentInstance}" field="firstName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${paymentInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="payment.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${paymentInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${paymentInstance?.transactionId}">
				<li class="fieldcontain">
					<span id="transactionId-label" class="property-label"><g:message code="payment.transactionId.label" default="Transaction Id" /></span>
					
						<span class="property-value" aria-labelledby="transactionId-label"><g:fieldValue bean="${paymentInstance}" field="transactionId"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${paymentInstance?.id}" />
					<g:link class="edit" action="edit" id="${paymentInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
