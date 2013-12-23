package pl.incomer.payment

import grails.validation.Validateable

@grails.validation.Validateable
class MailValidator {
    String email

    static constraints = {
        email(blank: false, email: true)
    }
}