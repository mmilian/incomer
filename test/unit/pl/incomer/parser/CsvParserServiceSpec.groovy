package pl.incomer.parser;

import spock.lang.*
import org.joda.time.*
import static org.joda.time.format.DateTimeFormat.forPattern
import org.joda.time.format.*
import spock.lang.Ignore


class CsvParserServiceSpec extends Specification {
	
	def static content = '''"LP","Numer","Data","Kwota","Wypłata","Prowizja","Kana³","Stan","Email","Opis","Control","Nazwisko","Imię","Transakcja powiazana"
"1","40383-M274","2013-03-03 17:30:35","10.00","9.70","0.30","mTransfer","wykonana","mateusz.armatys@gmail.com","billboardKrakow","","Armatys","Mateusz",""
"2","40383-M275","2013-03-03 20:03:47","10.00","9.70","0.30","mTransfer","wykonana","chmielowieck@gmail.com","billboardKrakow","","Wrońska","Ann",""
"3","40383-M276","2013-03-03 17:30:38","10.00","9.70","0.30","mTransfer","odrzucona","mateusz.armatys@gmail.com","billboardKrakow","","Armatys","Mateusz",""
'''
	
//	def csvParserService = new CsvParserService()	
	
	@Ignore
	void "Parse dotPay csv file to list of maps"() {
		given: "A file to parse"
		def temp = File.createTempFile('temp', '.txt')
		temp.write(content)		
		when: "The file is parsed"
		def result = csvParserService.parseDotPay(temp)
		then: "The list has two maps"
		result[0]['id'] == "40383-M274"
		result[0]['date'] == forPattern("yyy-MM-dd HH:mm:ss").parseDateTime("2013-03-03 17:30:35").toDate()
		result[0]['amount'] == new BigDecimal("10.00")
		result[0]['email'] == "mateusz.armatys@gmail.com"
		result[0]['firstName'] == "Mateusz"
		result[0]['name'] == "Armatys"
		result[1]['id'] == "40383-M275"
		result[1]['date'] == forPattern("yyy-MM-dd HH:mm:ss").parseDateTime("2013-03-03 20:03:47").toDate()
		result[1]['amount'] == new BigDecimal("10.00")
		result[1]['email'] == "chmielowieck@gmail.com"
		result[1]['firstName'] == "Ann"
		result[1]['name'] == "Wrońska"
		result[2] == null
	}		
}
