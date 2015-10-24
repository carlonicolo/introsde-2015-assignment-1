package peoplestore;

import peoplestore.generated.*;
import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

//import dao.PeopleStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * <h1>Marshaller</h1>
 * The JAXBMarshaller program create an xml file using the generated classes (with JAXB XJC).
 * In particular there are two methods: 
 * 	1. generateXMLDocument(File xmlDocument) that put in an xml file all the information formatted
 * 	   structured and stored in an xml file
 * 
 *  2. createPerson(peoplestore.generated.ObjectFactory factory, String i, int yearBirth,int monthBirth, int dayBirth, int hourBirth, int minBirth, 
			String firstname,String lastname, int yearUpdate, int monthUpdate, int dayUpdate, int hourUpdate, int minUpdate,
			int weight, double height, int bmi) that allows to create a person with params		
 * 
 * The execution creates an xml file named peopleMarshaller.xml
 * In fact the main point of this class is to execute the <b>Marshall</b>:
 * transforming the memory representation of an object to a data format suitable for storage or transmission, xml file.
 * 
 * @author Carlo Nicolò
 *
 */
public class JAXBMarshaller {
	
	/**
	 * This method is used to generate an xml document then 
	 * storing and saving the content of the objects in the 
	 * xmlDocument param passed in the method.
	 * Are used the classes generated with JAXB XJC
	 * 
	 * @param xmlDocument 	is where the content of the objects will be stored
	 * @throws DatatypeConfigurationException
	 */
	public void generateXMLDocument(File xmlDocument) throws DatatypeConfigurationException {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance("peoplestore.generated");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			
			
			peoplestore.generated.ObjectFactory factory = new peoplestore.generated.ObjectFactory();
			PeopleType people = factory.createPeopleType();
			
			/**
			 * Creating three person using 
			 * the method createPerson()
			 */
			PersonType person1 = factory.createPersonType();
			person1 = createPerson(factory, "1", 1979, 14, 10, 10, 15, "Jack", "McCaller", 2015, 4, 18, 9, 00, 100, 1.75);
			PersonType person2 = factory.createPersonType();
			person2 = createPerson(factory, "2", 1980, 23, 9, 11, 15, "Smith", "Jonson", 2015, 8, 10, 8, 00, 90, 1.85);
			PersonType person3 = factory.createPersonType();
			person3 = createPerson(factory, "3", 1985, 18, 10, 13, 15, "Jack", "Brigs", 2015, 6, 22, 10, 00, 80, 1.65);
			
			/**
			 * Creating a List of PersonType and then
			 * add the three person created before
			 */
			List<PersonType> personList = people.getPerson();
			personList.add(person1);
			personList.add(person2);
			personList.add(person3);
			
			/**
			 * The last phase:
			 * 1. Create a list of JAXBElement where store the people created.
			 * 2. Marshall the element in the xmlDocument param passed in the method.
			 * 3. Marshalling into the system default output.
			 */
			JAXBElement<PeopleType> peopleElement = factory.createPeople(people);
			marshaller.marshal(peopleElement, new FileOutputStream(xmlDocument));
			marshaller.marshal(peopleElement, System.out);

		} catch (IOException e) {
			System.out.println(e.toString());

		} catch (JAXBException e) {
			System.out.println(e.toString());

		}

	}
	
	/**
	 * This method allows to create a person with a series of requested params.
	 * In this way we could easily create as much as person we want using 
	 * this method.
	 * 
	 * @param factory 		Object factory from generated classes
	 * @param i 			this is the Id of the person
	 * @param yearBirth 	year of the Gregorian Calendar for birthdate
	 * @param monthBirth 	month of the Gregorian Calendar for birthdate
	 * @param dayBirth 		day of the Gregorian Calendar for birthdate
	 * @param hourBirth 	hour of the Gregorian Calendar for birthdate
	 * @param minBirth 		minutes of the Gregorian Calendar for birthdate
	 * @param firstname 	firstname of the person
	 * @param lastname 		lastname of the person 
	 * @param yearUpdate 	year of the Gregorian Calendar for lastupdate
	 * @param monthUpdate 	month of the Gregorian Calendar for lastupdate
	 * @param dayUpdate 	day of the Gregorian Calendar for lastupdate
	 * @param hourUpdate 	hour of the Gregorian Calendar for lastupdate
	 * @param minUpdate 	minutes of the Gregorian Calendar for lastupdate
	 * @param weight 		weight of the person
	 * @param height 		height of the person 
	 * @return person   	PersonType object 
	 * @throws DatatypeConfigurationException 
	 */
	public PersonType createPerson(peoplestore.generated.ObjectFactory factory, String i, int yearBirth,int monthBirth, int dayBirth, int hourBirth, int minBirth, 
			String firstname,String lastname, int yearUpdate, int monthUpdate, int dayUpdate, int hourUpdate, int minUpdate,
			int weight, double height) throws DatatypeConfigurationException{
		
		
		//Create a PersonType object
		PersonType person = factory.createPersonType();
		
		BigInteger id_pinco = new BigInteger(i);
		person.setId(id_pinco);
		
		//Set firstname and lastname
		person.setFirstname(firstname);
		person.setLastname(lastname);
		
		/**
		 * Creating the GregorianCalendar with containing the date of birtday.
		 * Then create an XMLGregorianCalendar, named xml_pincodate, using the DatatypeFactory
		 * and finally set the birthdate with this value using the accessor method
		 * setBirthdate(xml_pincodate)
		 */
		GregorianCalendar pinco_date = new GregorianCalendar(yearBirth, monthBirth, dayBirth, hourBirth, minBirth);
		XMLGregorianCalendar xml_pincodate = DatatypeFactory.newInstance().newXMLGregorianCalendar(pinco_date);
        person.setBirthdate(xml_pincodate);
        
		//Create healthProfile
		HealthDataType person_health_pinco = factory.createHealthDataType();
		GregorianCalendar pinco_lastupdate = new GregorianCalendar(yearUpdate, monthUpdate, dayUpdate, hourUpdate, minUpdate);
		XMLGregorianCalendar xml_pincolastupdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(pinco_lastupdate);
		person_health_pinco.setLastupdate(xml_pincolastupdate);
		
		//Convert the weight to the right type to be accepted by the accessor method setWeight()
		BigDecimal pinco_weight = new BigDecimal(weight);
		
		//Convert the height to the right type to be accepted by the accessor method setHeight()
		Double d_height = new Double(height);
		BigDecimal pinco_height = BigDecimal.valueOf(d_height);

		
		
		//Create the local variable to store the value of weight.
		double bmi_weight = weight;
		
		/**
		 * Calling the mehtod computeBmi(double bim_weight, double bim_height )
		 * to compute the bmi of a person.
		 */
		Double bmi_method = computeBmi(bmi_weight, d_height);
		BigDecimal b = new BigDecimal(bmi_method);
		BigDecimal pinco_bmi = b.setScale(2, RoundingMode.DOWN);
		
		
		
		
		/**
		 * Using the accessor method set on the variables 
		 * to easily set its value and have a "complete" 
		 * person object to return
		 */
		person_health_pinco.setWeight(pinco_weight);
		person_health_pinco.setHeight(pinco_height);
		person_health_pinco.setBmi(pinco_bmi);
		person.setHealthprofile(person_health_pinco);
		
		
		return person;
	}
	
	/**
	 * This method allows to compute the bmi of a person and format the result.
	 * 
	 * @param bim_weight 		The weight of a person
	 * @param bim_height		The height of a person
	 * @return computed_bim 	The bim of a person with weight and height passed as params
	 */
	public static double computeBmi(double bim_weight, double bim_height){
		double computed_bim = bim_weight/(Math.pow(bim_height, 2));
		new DecimalFormat("#.##").format(computed_bim);
		return computed_bim;
	}
	

	public static void main(String[] argv) throws DatatypeConfigurationException {
		String xmlDocument = "peopleMarshaller.xml";
		JAXBMarshaller jaxbMarshaller = new JAXBMarshaller();
		jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
	}
}
