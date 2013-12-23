package pl.incomer.converter;

public class ConverterService {

	File convertFileFromXToUtf8(file,encoding) {
				def source = file.getText(encoding)
				def content = new String(source.getBytes("UTF-8"), "UTF-8")
				def outputFile = File.createTempFile('temp', '.csv')
				outputFile.write(content)
				return outputFile		
	}
}