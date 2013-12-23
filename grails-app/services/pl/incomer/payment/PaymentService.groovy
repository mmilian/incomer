package pl.incomer.payment

import pl.touk.excel.export.XlsxExporter
import org.grails.plugins.excelimport.*
import static com.xlson.groovycsv.CsvParser.parseCsv
import static org.joda.time.format.DateTimeFormat.forPattern
import org.joda.time.format.*


class PaymentService {

    def converterService
    def excelImportService = new ExcelImportService() 

    def saveDotPayPayments(file) {
    	def payments = parseDotPay(file)
        savePayments(payments)    
    }


    def savePayPalPayments(file) {
        def payments = parsePayPal(file)
        savePayments(payments)    
    }

    def saveIpkoPayments(fileName) {
        def importer = new IpkoImporter(fileName,excelImportService)
        def payments = importer.getIncome()
        println "payments.size $payments.size"
        payments.each{
            println it
        }
        payments = parseIpko(payments)
        savePayments(payments)   
    }


    private def savePayments(payments) {
        def payment
        def map
        payments.each{ 
                payment = new Payment(it)                               
                payment.transactionId = it['id']
                payment.date = it['date']
                payment.amount = new BigDecimal(it['amount'])
                payment.email  = it['email']
                payment.firstName  = it['firstName']
                payment.name  = it['name']          
                payment.target = it['target']
                try {               
                    if (!payment.save()) {
                         payment.errors.each {
                         log.error "Could not save a payment because of:" + it
                          println "Could not save a payment because of:" + it
                        }
                    }
                } catch(e) {
                    println "Problem when saving payments" + e
                } 
        }
    }

    def exportPaymentsToXlsx(String exportedFile) {
        def withProperties = ['name','date','amount']
        List<Payment> payments = Payment.list()
        def file = new XlsxExporter(exportedFile).add(payments, withProperties).save()                    
    }


    private def parseDotPay(file) {             
        def data = parseCsv(file.newReader())
        def result = []
        for(line in data) {
            try {                     
            if (line.Stan == 'wykonana') result << [id : line.Numer, 
            date : forPattern("yyy-MM-dd HH:mm:ss").parseDateTime(line.Data).toDate()   , 
            amount : new BigDecimal(line.Kwota), 
            email : line.Email, 
            firstName : line.Imię, 
            name : line.Nazwisko,
            target : line.Opis,
            paymentType : Payment.PaymentType.DOTPAY]
            } catch (Exception e) {
                log.error "Could not parse entry in dotPayCsv file "  + e
            } 
        }
        return result
    }

    private def parsePayPal(file) {             
        def data = parseCsv(file.newReader())
        def result = []
        //println "Data $data"
        def i = 0
        for(line in data) {
            try {                     
            println "line" + (i++) 
            //println "line" + (i++) + " " + line
            if (line." Status" == 'Zakończone') result << [id : line." Numer identyfikacyjny transakcji".trim(), 
            date : forPattern("dd-MM-yyyy").parseDateTime(line.Data.trim()).toDate(), 
            amount : new BigDecimal(line." Brutto".trim().replace(",",".")), 
            email : line." Z adresu e-mail".trim(), 
            firstName : line." Imię i nazwisko (nazwa)".trim().tokenize()[0], 
            name : line." Imię i nazwisko (nazwa)".trim().tokenize()[1],
            target : line." Nazwa przedmiotu".trim(),
            paymentType : Payment.PaymentType.PAYPAL]
            } catch (Exception e) {
                log.error "Could not parse entry in PayPayCsv file " + e
                println "Could not parse entry in PayPayCsv file " + e                
            } 
        }
        return result
    }

    private def parseIpko(payments) {
        def result = []
        payments.each{
            try {
            result << [id : (it.date.toString()+(it.accountNumber-'Nr rach. przeciwst.:')).replaceAll(' ','').replaceAll('-',''), 
            date : forPattern("yyyy-MM-dd").parseDateTime(it.date).toDate(),//it.date.toDate(), 
            amount : new BigDecimal(it.amount), 
            email :  extractEmailFromTokens(it.target.tokenize())?:"no@email.com", 
            firstName : (it.data-"Dane adr. rach. przeciwst.: ").tokenize()[0],
            name : (it.data-"Dane adr. rach. przeciwst.: ").tokenize()[1],
            target : (it.target-"Tytuł:").trim(),
            paymentType : Payment.PaymentType.IPKO]
            } catch (Exception e) {
                log.error "Could not parse entry in iPko file " + e
                println "Could not parse entry in iPko file " + e                
            } 
        }
        println "result $result.size"
        return result
    }

    private def extractEmailFromTokens(tokens) {
        for (token in tokens) {
            if ((token.contains("@")) && new MailValidator(email:token).validate()) return token
        } 
        return null
    }
}