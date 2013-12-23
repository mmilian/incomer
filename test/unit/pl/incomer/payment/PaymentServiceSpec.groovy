package pl.incomer.payment
	
import grails.test.mixin.*
import spock.lang.Specification
import org.joda.time.*
import static org.joda.time.format.DateTimeFormat.forPattern
import org.joda.time.format.*
import pl.incomer.payment.Payment

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PaymentService)
@Mock(Payment)
class PaymentServiceSpec extends Specification {

	def result = []
	def resultPayPal = []
	def resultIpko = []
	
	def static contentDotPay = '''"LP","Numer","Data","Kwota","Wypłata","Prowizja","Kanał","Stan","Email","Opis","Control","Nazwisko","Imię","Transakcja powiazana"
"1","40383-M274","2013-03-03 17:30:35","10.00","9.70","0.30","mTransfer","wykonana","jan.kowalski@zmieleni.pl","billboardKrakow","","Kowalski","Jan",""
"2","40383-M275","2013-03-03 20:03:47","10.00","9.70","0.30","mTransfer","wykonana","ewa.nowak@zmieleni.pl","zmieleni","","Nowak","Ewa",""
"3","40383-M276","2013-03-03 17:30:38","10.00","9.70","0.30","mTransfer","odrzucona","andrzej.krawiec@zmieleni.pl","billboardKrakow","","Krawiec","Andrzej",""
'''

	def static contentPayPal = '''Data, Godzina, Strefa czasowa, Imię i nazwisko (nazwa), Typ, Status, Waluta, Brutto, Opłata, Netto, Z adresu e-mail, Na adres e-mail, Numer identyfikacyjny transakcji, Status kontrahenta, Status adresu, Nazwa przedmiotu, Identyfikator przedmiotu, Koszt wysyłki oraz koszty manipulacyjne, Kwota ubezpieczenia, Podatek od sprzedaży, Nazwa opcji 1, Wartość opcji 1, Nazwa opcji 2, Wartość opcji 2, Witryna aukcji, Nazwa kupującego, Adres URL przedmiotu, Data zamknięcia, Numer identyfikacyjny w systemie Escrow, Identyfikator faktury, Txn ID przedmiotu, Numer faktury, Numer niestandardowy, Identyfikator potwierdzenia, Saldo, Adres — wiersz 1, Adres — wiersz 2/dzielnica/osiedle, Miejscowość, Stan/prowincja/województwo/region/terytorium/prefektura/republika, Kod pocztowy, Kraj, Numer telefonu do kontaktu,  
"07-07-2013","12:11:41","CEST","Jan Kowalski","Darowizna otrzymana","Zakończone","PLN","20,00","-1,38","18,62","jankowalski@zmieleni.pl","paypal@jow.pl","52E51028S9941611S","Spoza Stanów Zjednoczonych — Niezweryfikowany","Spoza Stanów Zjednoczonych","Darowizna zmieleni.pl","zmieleni","0,00","","0,00","","","","","","","","","","","","","","","12 534,82","Zmielona 31","","Warszawa","Mazowieckie","51-622","Polska","",
"06-07-2013","22:11:05","CEST","Anna Kowalska","Darowizna otrzymana","Zakończone","PLN","30,00","-1,67","28,33","ania.kowalska@zmieleni.pl","paypal@jow.pl","0UX987929H6758357","Spoza Stanów Zjednoczonych — Zweryfikowany","Spoza Stanów Zjednoczonych","Darowizna zmieleni.pl","zmieleni","0,00","","0,00","","","","","","","","","","","","","","","12 516,20","Zmielona 2A","","Warszawa","Mazowieckie","51-622","Polska","",
'''	

	def paymentService = new PaymentService()

		def setup() {
			result[0] = [:]
			result[1] = [:]
			result[0]['id'] = "40383-M274"
			result[0]['date'] = forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2013-03-03 17:30:35").toDate()
			result[0]['amount'] = new BigDecimal("10.00")
			result[0]['email'] = "jan.kowalski@zmieleni.pl"
			result[0]['firstName'] = "Jan"
			result[0]['name'] = "Kowalski"
			result[0]['target'] = "billboardKrakow"
			result[1]['id'] = "40383-M275"
			result[1]['date'] = forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2013-03-03 20:03:47").toDate()
			result[1]['amount'] = new BigDecimal("10.00")
			result[1]['email'] = "ewa.nowak@zmieleni.pl"
			result[1]['firstName'] = "Ewa"
			result[1]['name'] = "Nowak"
			result[1]['target'] = "zmieleni"
			resultPayPal[0] = [:]
			resultPayPal[0]['id'] = "52E51028S9941611S"
			resultPayPal[0]['date'] = forPattern("MM-dd-yyyy").parseDateTime("07-07-2013").toDate()
			resultPayPal[0]['amount'] = new BigDecimal("20.00")
			resultPayPal[0]['email'] = "jankowalski@zmieleni.pl"
			resultPayPal[0]['firstName'] = "Jan"
			resultPayPal[0]['name'] = "Kowalski"
			resultPayPal[0]['target'] = "Darowizna zmieleni.pl"
			resultIpko[0] = [:]
			resultIpko[0]['id'] = "2013061950102055581111178597800061"
			resultIpko[0]['date'] = forPattern("yyyy-MM-dd").parseDateTime("2013-06-19").toDate()
			resultIpko[0]['amount'] = new BigDecimal("10.00")
			resultIpko[0]['email'] = "jannowak@zmieleni.pl"
			resultIpko[0]['firstName'] = "Jan"
			resultIpko[0]['name'] = "Nowak"
			resultIpko[0]['target'] = "zmieleni.pl jannowak@zmieleni.pl"

		}

	def cleanup() {
		Payment.list().each{it.delete()}
	}


	void "save dot pay list of payments"() {
		when:
			def temp = File.createTempFile('temp', '.csv')
			temp.write(contentDotPay)	
		    paymentService.saveDotPayPayments(temp)
		then:
			2 == Payment.count()
			def payments = Payment.list()
			result[0]['id'] == payments[0].transactionId
			result[0]['date'] == payments[0].date
			result[0]['amount'] == payments[0].amount
			result[0]['email'] == payments[0].email
			result[0]['firstName'] == payments[0].firstName
			result[0]['name'] == payments[0].name
			result[0]['target'] == payments[0].target
			result[1]['id'] == payments[1].transactionId
			result[1]['date'] == payments[1].date
			result[1]['amount'] == payments[1].amount
			result[1]['email'] == payments[1].email
			result[1]['firstName'] == payments[1].firstName
			result[1]['name'] == payments[1].name
			result[1]['target'] == payments[1].target
	}

	void "save paypal list of payments"() {
		when:
			def temp = File.createTempFile('temp', '.csv')
			temp.write(contentPayPal)	
		    paymentService.savePayPalPayments(temp)
		then:
			2 == Payment.count()
			def payments = Payment.list()
			resultPayPal[0]['id'] == payments[0].transactionId
			resultPayPal[0]['date'] == payments[0].date
			resultPayPal[0]['amount'] == payments[0].amount
			resultPayPal[0]['email'] == payments[0].email
			resultPayPal[0]['firstName'] == payments[0].firstName
			resultPayPal[0]['name'] == payments[0].name
			resultPayPal[0]['target'] == payments[0].target
	}

	@Ignore
	void "save ipko list of payments"() {
		when:
			def source =  "./test/resources/ipko.xls"
			paymentService.saveIpkoPayments(source)
		then:
			1 == Payment.count()
			def payments = Payment.list()
			resultIpko[0]['id'] == payments[0].transactionId
			resultIpko[0]['date'] == payments[0].date
			resultIpko[0]['amount'] == payments[0].amount
			resultIpko[0]['email'] == payments[0].email
			resultIpko[0]['firstName'] == payments[0].firstName
			resultIpko[0]['name'] == payments[0].name
			resultIpko[0]['target'] == payments[0].target
	}

}
