package pl.incomer.payment


class Payment {


	String transactionId
	Date date
	BigDecimal amount
	String email
	String firstName
	String name
	String target
	PaymentType paymentType
	String initials
	String hashedMail
	String getType() {paymentType.toString()}
	
	enum PaymentType {
		DOTPAY,PAYPAL,IPKO
	} 


	static  headers = ['Date','Name', 'Amount', "Email", "Target", "Type"]
    static  withProperties = ['date','fullName','amount', 'email', "target", "type"]
    static  withHashedProperties = ['date','initials','amount', 'hashedMail', "standarizedTarget", "type"]

	static transients = ['initials','hashedMail','fullName','type','standarizedTarget']

    static constraints = {
		date nullable: false	
		amount min: BigDecimal.ZERO
		email email: true
		firstName nullable : true
		name nullable : true
		transactionId unique : true
		transactionId nullable : false
		target nullable: true
		paymentType nullable : false
    }

	static mapping = {
  		id generator: 'assigned', name : 'transactionId'
	}

	String getStandarizedTarget() {
		return target?.contains("zmieleni") ? "Wsparcie zmieleni" : "Wsparcie stowarzyszenie JOW" 
	}
	
	String getId() {
		transactionId
	}

	void setId(id) {
		transactionId = id
	}


	String getInitials() {
		try {
		return firstName[0] + name[0]
		} catch(Exception e) {
			log.error	 e
			return "" 
		}
	}

	String getFullName() {
		try {
		"${firstName} ${name}" 
		} catch(Exception e) {
			log.error	 e
			return "" 
		}
	}


	String getHashedMail() {
		try {
			def hashedMail = new StringBuffer()
			hashedMail << email[0] 
			def atPosition = email.indexOf('@')
			(atPosition-1).times { hashedMail << '#' }
			hashedMail << '@'		
			def secondPart = email.substring(atPosition+1)
			def dotPosition = secondPart.indexOf('.')
			(dotPosition-1).times { hashedMail << '#'}
			hashedMail << secondPart.substring(dotPosition-1)
			return hashedMail.toString()
		} catch (Exception e) {
			return '#wrongEmail#'
		}  
	}

}
