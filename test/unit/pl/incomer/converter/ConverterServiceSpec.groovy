package pl.incomer.converter;

import spock.lang.*

class ConverterServiceSpec extends Specification {

	def converterService = new ConverterService()

	void "Convert from given encoding to UTF8"() {
		given: "A File in windows1250 encoding"
			def sourceFile = new File("./test/resources/windows1250.csv")
		when: "convert from Cp1250 to UTF8"
			def outputFile = converterService.convertFileFromXToUtf8(sourceFile,"Cp1250")
		then: "should be return string in UTF8"
			outputFile instanceof File
			outputFile.text == "ąćęłńóśżź"
	}
}