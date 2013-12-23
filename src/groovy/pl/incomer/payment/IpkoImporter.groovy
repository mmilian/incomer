package pl.incomer.payment

import org.grails.plugins.excelimport.*


class IpkoImporter extends AbstractExcelImporter {

static Map CONFIG_BOOK_COLUMN_MAP = [ sheet:'Lista transakcji', startRow: 1, columnMap: [ 'A':'date', 'D':'amount', 'G':'accountNumber','H':'data','I':'target' ] ]

def excelImportService = null

//TODO add possibilities to inject services here
public IpkoImporter(fileName,service) { 
	super(fileName) 
	excelImportService = service
}

List<Map> getIncome() { List incomeList = excelImportService.columns(workbook, CONFIG_BOOK_COLUMN_MAP) }

}