package pl.incomer.payment

import grails.plugin.spock.IntegrationSpec
import org.joda.time.*
import static org.joda.time.format.DateTimeFormat.forPattern
import org.joda.time.format.*
import pl.incomer.payment.Payment
import spock.lang.Ignore


class PaymentServiceIntSpec extends IntegrationSpec {
	
	def paymentService
	def converterService

	
	void "take dotPay csv, save it, and then export to xlsx"() {
		given: "A dotPayCsv file in windows1250 encoding"
			def sourceFile = new File("./test/resources/doyPay.csv")
			def convertedToUTF8 = converterService.convertFileFromXToUtf8(sourceFile,"Cp1250")
		when:
			paymentService.saveDotPayPayments(convertedToUTF8)
			paymentService.exportPaymentsToXlsx("exportedDotPayPayments.xlsx")
		then: 
			true == new File("exportedDotPayPayments.xlsx").exists()
		cleanup:
			def file = new File("exportedDotPayPayments.xlsx")
			if (file.exists()) {
				file.delete()  
			}
			Payment.list().each{it.delete()}
	}

	void "take PayPay csv, save it, and then export to xlsx"() {
		given: "A PayPalCsv file in windows1250 encoding"
			def sourceFile = new File("./test/resources/payPal.csv")
			def convertedToUTF8 = converterService.convertFileFromXToUtf8(sourceFile,"Cp1250")
		when:
			paymentService.savePayPalPayments(convertedToUTF8)
			paymentService.exportPaymentsToXlsx("exportedPayPalPayments.xlsx")
		then: 
			true == new File("exportedPayPalPayments.xlsx").exists()		
		cleanup:
			def file = new File("exportedPayPalPayments.xlsx")
			if (file.exists()) {
				file.delete()  
			}
			Payment.list().each{it.delete()}
	}

	def cleanup() {		
	}
 }