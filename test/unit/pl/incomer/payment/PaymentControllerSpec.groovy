package pl.incomer.payment

import org.junit.*
import grails.test.mixin.*
import spock.lang.Specification
import spock.lang.Ignore
import pl.incomer.payment.Payment
import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import pl.incomer.converter.ConverterService

@TestFor(PaymentController)
@Mock(Payment)
class PaymentControllerSpec extends Specification  {

    PaymentService paymentService = Mock(PaymentService)
    ConverterService converterService = Mock(ConverterService)

    def static content = '''"LP","Numer","Data","Kwota","Wypłata","Prowizja","Kana³","Stan","Email","Opis","Control","Nazwisko","Imię","Transakcja powiazana"
"1","40383-M274","2013-03-03 17:30:35","10.00","9.70","0.30","mTransfer","wykonana","mateusz.armatys@gmail.com","billboardKrakow","","Armatys","Mateusz",""
"2","40383-M275","2013-03-03 20:03:47","10.00","9.70","0.30","mTransfer","wykonana","chmielowieck@gmail.com","billboardKrakow","","Wrońska","Ann",""
"3","40383-M276","2013-03-03 17:30:38","10.00","9.70","0.30","mTransfer","odrzucona","mateusz.armatys@gmail.com","billboardKrakow","","Armatys","Mateusz",""
'''

    void setup() {
        controller.paymentService = paymentService
        controller.converterService = converterService
    }

    void "upload file from disk"() {
        setup: 
            final file = new GrailsMockMultipartFile("csv", content.bytes)
            request.addFile(file)
            def temp = File.createTempFile('temp', '.csv')
            temp.write(content)                        
        when:
            controller.upload()
        then:
            1 * paymentService.saveDotPayPayments(_ as File)
            1 * converterService.convertFileFromXToUtf8(_,"Cp1250") >> temp 
            0 * _._ 
    }

    @Ignore
    void "export all payments to file payments.xlsx"() {
        when:
            controller.exportPayments()
        then:
            controller.response.getContentType() == "application/vnd.ms-excel"
    }

    @Ignore
    void "export all hashed payments to file payments.xlsx"() {
        when:
            controller.exportHashedPayments()
        then:
            controller.response.getContentType() == "application/vnd.ms-excel"
    }
}
