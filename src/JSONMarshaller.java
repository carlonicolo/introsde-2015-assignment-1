import peoplestore.generated.*;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.*;


import model.HealthProfile;
import model.Person;
import dao.PeopleStore;

/**
 * This program allows to create
 * person objects and marshal them to JSON,
 * creating also a file .json
 *  
 * @author karlitos
 *
 */
public class JSONMarshaller {  	
	public static PeopleStore people = new PeopleStore();
	
	/**
	 * This method is used to initialize the DB with an 
	 * arbitrary chosen number of person.
	 * In this case have been initialized three persons.
	 * Then these person are added to the content data of the static Object people.
	 */
	public static void initializeDB() {
		/**
		 * Create HealthProfile object that will be used
		 * to complete the construction of the Person object
		 */
		HealthProfile hp1 = new HealthProfile(70.0, 1.85);
		Person pallino = new Person(new Long(1), "Yuri", "Zagov", "1977-06-21", hp1);
		HealthProfile hp2 = new HealthProfile(58.0, 1.92);
		Person pallo = new Person(new Long(2), "Fulano", "De Tal", "1984-06-21", hp2);
		HealthProfile hp3 = new HealthProfile(68.0, 1.72);
		Person john = new Person(new Long(3), "Jane", "Doe", "1985-03-20", hp3);
		
		//Add person to people Data
		people.getData().add(pallino);
		people.getData().add(pallo);
		people.getData().add(john);
	}	

	public static void main(String[] args) throws Exception {
		
		//Calling the static method initializeDB
		initializeDB();
		
		// Jackson Object Mapper 
		ObjectMapper mapper = new ObjectMapper();
		
		// Adding the Jackson Module to process JAXB annotations
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        
		
		mapper.registerModule(module);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        
        
        String result = mapper.writeValueAsString(people);
        
        //Print on the screen the people
        System.out.println(result);
        
        /**
         * Save in the file people.json the content of people.
         * In this case the three person Object 
         * created and stored in people
         */
        mapper.writeValue(new File("people.json"), people);
    }
}