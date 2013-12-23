package pl.incomer.payment

import org.springframework.dao.DataIntegrityViolationException
import pl.touk.excel.export.WebXlsxExporter
import org.springframework.http.HttpStatus
import grails.converters.JSON
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile

class PaymentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", upload: "POST", exportPayments: "GET", exportHashedPayments: "GET"]

    def converterService
    def paymentService

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [paymentInstanceList: Payment.list(params), paymentInstanceTotal: Payment.count()]
    }

    def create() {
        [paymentInstance: new Payment(params)]
    }

    def save() {
        def paymentInstance = new Payment(params)
        if (!paymentInstance.save(flush: true)) {
            render(view: "create", model: [paymentInstance: paymentInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'payment.label', default: 'Payment'), paymentInstance.id])
        redirect(action: "show", id: paymentInstance.id)
    }

    def show(Long id) {
        def paymentInstance = Payment.get(params.id)
        if (!paymentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'payment.label', default: 'Payment'), id])
            redirect(action: "list")
            return
        }

        [paymentInstance: paymentInstance]
    }

    def edit(Long id) {
        def paymentInstance = Payment.get(params.id)
        if (!paymentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'payment.label', default: 'Payment'), id])
            redirect(action: "list")
            return
        }

        [paymentInstance: paymentInstance]
    }

    def update(Long id, Long version) {
        def paymentInstance = Payment.get(params.id)
        if (!paymentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'payment.label', default: 'Payment'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (paymentInstance.version > version) {
                paymentInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'payment.label', default: 'Payment')] as Object[],
                          "Another user has updated this Payment while you were editing")
                render(view: "edit", model: [paymentInstance: paymentInstance])
                return
            }
        }

        paymentInstance.properties = params

        if (!paymentInstance.save(flush: true)) {
            render(view: "edit", model: [paymentInstance: paymentInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'payment.label', default: 'Payment'), paymentInstance.id])
        redirect(action: "show", id: paymentInstance.id)
    }

    def delete(Long id) {
        def paymentInstance = Payment.get(params.id)
        if (!paymentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'payment.label', default: 'Payment'), id])
            redirect(action: "list")
            return
        }

        try {
            paymentInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'payment.label', default: 'Payment'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'payment.label', default: 'Payment'), id])
            redirect(action: "show", id: id)
        }
    }

    def uploadDotPay() {
        def f = request.getFile('dotPayCsv')
        if (f.empty) {
            flash.message = 'file cannot be empty'
            render(view: 'list')
            return
        }
        def temp = File.createTempFile('temp', '.csv')
        f.transferTo(temp)
        def convertedToUTF8 = converterService.convertFileFromXToUtf8(temp,"Cp1250")
        paymentService.saveDotPayPayments(convertedToUTF8)
        redirect(action: "list")
    }

    def uploadPayPal() {
        def f = request.getFile('uploadPayPal')
        if (f.empty) {
            flash.message = 'file cannot be empty'
            render(view: 'list')
            return
        }
        def temp = File.createTempFile('temp', '.csv')
        f.transferTo(temp)
        def convertedToUTF8 = converterService.convertFileFromXToUtf8(temp,"Cp1250")
        paymentService.savePayPalPayments(convertedToUTF8)
        redirect(action: "list")
    }

    def uploadIpko() {
        def f = request.getFile('uploadIpko')
        if (f.empty) {
            flash.message = 'file cannot be empty'
            render(view: 'list')
            return
        }
        def temp = File.createTempFile('temp', '.xls')
        f.transferTo(temp)
        paymentService.saveIpkoPayments(temp)
        redirect(action: "list")
    }


    def exportPayments() {
        def headers = Payment.headers
        def withProperties = Payment.withProperties
        def payments = Payment.list()
        new WebXlsxExporter().with {
           setResponseHeaders(response)
           fillHeader(headers)
           add(payments, withProperties)
           save(response.outputStream)
        }   
    }

    def exportHashedPayments() {
        def headers = Payment.headers
        def withProperties = Payment.withHashedProperties
        def payments = Payment.list()
        new WebXlsxExporter().with {
           setResponseHeaders(response)
           fillHeader(headers)
           add(payments, withProperties)
           save(response.outputStream)
        }   
    }
}
