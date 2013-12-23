package pl.incomer.payment

import grails.test.mixin.*
import spock.lang.Specification
//import static org.joda.time.format.DateTimeFormat.forPattern

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Payment)
class PaymentSpec extends Specification {


	def setup() {
		mockForConstraintsTests(Payment, [new Payment(transactionId: '123456789')])
	}

	def cleanup() {
	}

	void "test payment all constraints"() {
		when:
    		def obj = new Payment("$field": val)
    	then: 	
			validateConstraints(obj, field, error)
	    where:
	    	error        | field           | val
    		'nullable'   | 'date'          | null 
    		'min'      	 | 'amount'        | '-1'
    		'valid'      | 'name'          | null    		
    		'email'      | 'email'         | 'email@email'
    		'valid'      | 'firstName'     | null
    		'unique'     | 'transactionId' | '123456789'    		  		
	}

	void "test payment with same id"() {
		when:
			def obj = new Payment(id: '123456789')
		then:
			assert !obj.validate()
	}

	void "test proper payment"() {
		setup:
			def date = new Date() //forPattern("yyy-MM-dd HH:mm:ss").parseDateTime("2013-03-03 20:04:42").toDate()
		when:
		    def obj = new Payment(transactionId:'123123', date : date, amount : new BigDecimal(100.00), email: 'test@test.com', firstName : 'firstName', name : 'name')
			println Payment.count()
			obj.save(flush:true, failOnError: true)
			println Payment.count()
		then:		 	 		 	
		 	 assert obj.validate()
		 	  1 == Payment.count()
	}


	void "test payment with no transactionId should fail"() {
		setup:
			def date = new Date() //forPattern("yyy-MM-dd HH:mm:ss").parseDateTime("2013-03-03 20:04:42").toDate()
		when:
		    def obj = new Payment(date : date, amount : new BigDecimal(100.00), email: 'test@test.com', firstName : 'firstName', name : 'name')
			obj.save(flush:true)
		then:		 	 		 	
		 	 assert !obj.validate()
		 	  0 == Payment.count()
	}


	void "test get initials for payment entry"() {
		when:
			def obj = new Payment(transactionId:'123123', date : new Date(), amount : new BigDecimal(100.00), email: 'test@test.com', firstName : 'firstName', name : 'name')			
		then:
			'fn' == obj.initials
	}

	void "test get initials for payment entry without names"() {
		when:
			def obj = new Payment(transactionId:'123123', date : new Date(), amount : new BigDecimal(100.00), email: 'test@test.com', firstName : null, name : null)			
		then:
			'' == obj.initials
	}


	void "test get hashed email"() {
		when:
			def obj = new Payment(transactionId:'123123', date : new Date(), amount : new BigDecimal(100.00), email: 'test@test.com', firstName : 'firstName', name : 'name')			
		then:
			't###@###t.com' == obj.hashedMail
	}

	void "test get hashed email when exception"() {
		when:
			def obj = new Payment(transactionId:'123123', date : new Date(), amount : new BigDecimal(100.00), email: 'testtestcom', firstName : 'firstName', name : 'name')			
		then:
			'#wrongEmail#' == obj.hashedMail
	}

	void "test get full name"() {
		when:
			def obj = new Payment(transactionId:'123123', date : new Date(), amount : new BigDecimal(100.00), email: 'testtestcom', firstName : 'firstName', name : 'name')			
		then:
			'firstName name' == obj.fullName
	}


  void validateConstraints(obj, field, error) {
       def validated = obj.validate()
       if (error && error != 'valid') {
           assert !validated
           assert obj.errors[field]
           assert error == obj.errors[field]
       } else {
           assert !obj.errors[field]
       }
   }


}