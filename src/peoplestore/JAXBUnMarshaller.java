package peoplestore;

import peoplestore.generated.*;
import peoplestore.generated.HealthDataType;
import peoplestore.generated.PeopleType;
import peoplestore.generated.PersonType;

import javax.xml.bind.*;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;

/**
 * <h1>UnMarshalling</h1>
 * Creating a tree of ojects that represents the content and the organization
 * of an xml document.
 * 
 * This program allow us to see and print, as objects, the content
 * of the people.xml file, using the generated classes JAXB XJC.
 * 
 * @author karlitos
 *
 *
 *
 */
public class JAXBUnMarshaller {
	
	/**
	 * This method allows to UnMarshall a determinated xmlDocument file 
	 * passed as param and create a Schema Object, based
	 * on a .xsd schema corresponding to the xml document passed as parameter
	 * 
	 * In this case the schema passed is people.xsd that represents
	 * the schema for the people.xml file.
	 * 
	 * @param xmlDocument 	the document to be UnMarshalled
	 */
	
	public void unMarshall(File xmlDocument) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance("peoplestore.generated");

			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(
					"people.xsd"));
			unMarshaller.setSchema(schema);
			CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
			unMarshaller.setEventHandler(validationEventHandler);

			@SuppressWarnings("unchecked")
			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller
					.unmarshal(xmlDocument);

			PeopleType people = peopleElement.getValue();

			List<PersonType> personList = people.getPerson();
			for (int i = 0; i < personList.size(); i++) {
				
				/**
				 * Inside the for loop are printed
				 * all the values of the person, calling 
				 * the accessor method get.
				 * 
				 * Example:
				 * 			person.getFirstname()
				 * 			get the firstname of the person at index i
				 */
				PersonType person = (PersonType) personList.get(i);
				System.out.println("Id: "+ person.getId());
				System.out.println("Firstname: "+ person.getFirstname());
				System.out.println("Lastname: "+ person.getLastname());
				System.out.println("Birthday: "+ person.getBirthdate());
				HealthDataType hp = person.getHealthprofile();
				System.out.println(" HealthProfile: ");
				System.out.println(" Lastupdate: "+ hp.getLastupdate());
				System.out.println(" Weight: "+ hp.getWeight());
				System.out.println(" Height: " + hp.getHeight());
				System.out.println(" BMI: " + hp.getBmi());
				System.out.println("=======================");

			}
		} catch (JAXBException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] argv) {
		/**
		 * people.xml is the file used to be unmarshalled
		 * This file contains data and we could see 
		 * the result of the unmarshall.
		 */
		File xmlDocument = new File("people.xml");
		JAXBUnMarshaller jaxbUnmarshaller = new JAXBUnMarshaller();

		jaxbUnmarshaller.unMarshall(xmlDocument);

	}

	class CustomValidationEventHandler implements ValidationEventHandler {
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == ValidationEvent.WARNING) {
				return true;
			}
			if ((event.getSeverity() == ValidationEvent.ERROR)
					|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

				System.out.println("Validation Error:" + event.getMessage());

				ValidationEventLocator locator = event.getLocator();
				System.out.println("at line number:" + locator.getLineNumber());
				System.out.println("Unmarshalling Terminated");
				return false;
			}
			return true;
		}

	}
}
