import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




/**
 * In this program is used the XPath library 
 * to explore, examinate and parse an xml file.
 * 
 * In particular xpath is used to parse the xml document
 * in order to catch nodes and attributes and its relatives values
 * 
 * Have been performed various methods that allow to search a
 * a determinate value in the xml file and retrive all associated data.
 * 
 * Example:
 * 			with the method getWeight(Long id) is possible
 * 			to know the weight of the person associated
 * 			to the id param passed.
 * 
 * @author Carlo Nicolò
 * 
 */
public class XpathHealthProfile {

	Document doc;
	XPath xpath;
	
	/**
	 * Variables used to store the value
	 * of the nodes.
	 * The name used for the variable are self-explaining
	 * 
	 * Example:
	 * 			xpath_firstname is used to store
	 * 			the value of firstname node. 
	 */
	private String xpath_firstname;
	private String xpath_lastname;
	private String xpath_birthday;
	private String xpath_lastupdate;
	private String xpath_weight;
	private String xpath_height;
	private String xpath_bmi;
	
	

	/**
	 * Load the xml in memory
	 */
	public void loadXML() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		doc = builder.parse("people.xml");
		getXPathObj();
	}
	
	/**
	 * creating xpath object
	 */
	public XPath getXPathObj() {

		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		return xpath;
	}

	/**
	 * Given the first name and last name of a person, it returns
	 * a Node object containing the node weight of the person.
	 * 
	 * @param firstname 
	 * @param lastname
	 * @return node 	the Node object weight
	 */
	public Node getWeightByName(String firstname, String lastname) throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[firstname='" + firstname + "' and lastname='" + lastname + "']/healthprofile/weight");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node;
	}
	
	/**
	 * Given the first name and last name of a person, it returns
	 * a Node object containing the node height of the person.
	 * 
	 * @param firstname 
	 * @param lastname
	 * @return node 	the Node object height
	 */
	public Node getHeightByName(String firstname, String lastname) throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[firstname='" + firstname + "' and lastname='" + lastname + "']/healthprofile/height");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node;
	}
	
	
	/**
	 * Given an id with this method we are able to find a person
	 * and store the value of its weight using the accessor method
	 * setXpath_weight().
	 * Return a String that contains the weight value associated to
	 * the @param id 
	 * 
	 * @param 	id used to search a person 
	 * @return 	xpath_weight the weight of the people with the specified id
	 */
	public String getWeight(Long id) throws XPathExpressionException {
		String id_s = String.format("%04d", id);
		XPathExpression expr = xpath.compile("/people/person[@id='" + id_s + "']");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		Element nelement = (Element) node;
		setXpath_weight(nelement.getElementsByTagName("weight").item(0).getTextContent());
		return getXpath_weight();
	}
	
	/**
	 * Given an id with this method we are able to find a person
	 * and store the value of its height using the accessor method
	 * setXpath_height().
	 * 
	 * Return a String that contains the weight value associated to
	 * the @param id. 
	 * 
	 * @param 	id used to search a person 
	 * @return 	xpath_height the height of the people with the specified id
	 */
	public String getHeight(Long id) throws XPathExpressionException {
		String id_s = String.format("%04d", id);
		XPathExpression expr = xpath.compile("/people/person[@id='" + id_s + "']");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		Element nelement = (Element) node;
		setXpath_height(nelement.getElementsByTagName("height").item(0).getTextContent());
		return getXpath_height();
	}
	
	
	/**
	 * Print all the people stored in the file people.xml.
	 * For each person are printed all the details. 
	 */
	public void printAllPeople() throws XPathExpressionException {
		
		XPathExpression expr = xpath.compile("/people/person");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		
		
		/**
		 * Create a variable long type that will be incremented in the for loop
		 * at same time with the index of nodes.
		 * In this way will be possible to pass the variable to the method 
		 * and have the right index and the right value expected 
		 * from the methods getWeight(Long id) and getHeight(Long id) 
		 */
		long a = 0001;
		
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println("========================================");
			System.out.println("ID: "+ nodes.item(i).getAttributes().item(0).getTextContent() );
			NodeList person= nodes.item(i).getChildNodes();   //get all child nodes of the parent node person
			Element nelement = (Element) person;
			System.out.println("Firstname: " + nelement.getElementsByTagName("firstname").item(0).getTextContent());
			System.out.println("Lastname: " + nelement.getElementsByTagName("lastname").item(0).getTextContent());
			System.out.println("Birthdate: " + nelement.getElementsByTagName("birthdate").item(0).getTextContent());
			System.out.println("Healthprofile");
			System.out.println(" Lastupdate: " +nelement.getElementsByTagName("lastupdate").item(0).getTextContent());
			
			//Using the method getWeight(Long id) to get the weight, as requested in the assignment
			System.out.println(" Weight: " + getWeight(a));
			//Using the method getHeight(Long id) to get the height, as requested in the assignment
			System.out.println(" Height: " + getHeight(a));
			
			System.out.println(" Bmi: " + nelement.getElementsByTagName("bmi").item(0).getTextContent());
			a++;
			System.out.println("");
		} 
	}
	
	

	/**
	 * Given an id of a person, the method return the node corresponding
	 * to the person whit that id.
	 * 
	 * @param id 		id of the person stored in the people.xml
	 * @return node 	contains the person
	 */
	public Node getPersonById(Long id) throws XPathExpressionException {
		String id_s = String.format("%04d", id);
		XPathExpression expr = xpath.compile("/people/person[@id='" + id_s + "']");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node;
	}
	
	

	/**
	 * Given a weight and an operator,
	 * return all people that satisfy the condition
	 * 
	 * @param weight 	Double values of the weight
	 * @param operator 	=,<,> express the condition
	 * @return nodes 	NodeList containing the people
	 */
	public NodeList getPersonByWeight(Double weight, String operator) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//healthprofile[weight " + operator + "'" + weight + "']/..");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;		
		return nodes;
	}
	

	
	@Override
	public String toString() {
		return "Firstname: " + xpath_firstname 
				+ "\nLastname: " + xpath_lastname
				+ "\nBirthday: " + xpath_birthday
				+ "\nHealtProfile:"
				+ "\n Lastupdate: " + xpath_lastupdate
				+ "\n Weight: " + xpath_weight 
				+ "\n Height: " + xpath_height 
				+ "\n BMI: " + xpath_bmi + "\n";
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		XpathHealthProfile test = new XpathHealthProfile();
		test.loadXML();

		Node node;
		
		/**
		 * Here starts a series of if else that checks the first argument passed 
		 * in the execution of the program, in order to see if there is 
		 * a match between that argument and the string corresponding to a method to call.
		 * In the case of matching, the corresponding method will be executed.
		 * 
		 * Example: java XpathHealthProfile printAllPeople
		 * in this way you will be executed the method printAllPeople 
		 */
		int argCount = args.length;
		if (argCount == 0) {
			System.out.println("I cannot execute any action.");
		} else {
			String method = args[0];
			//Execution of the method PrintAllPeople
			if (method.equals("printAllPeople")) {
				try{
					test.printAllPeople();
				} catch(Exception e) {
					System.out.println(e);
				}
				
				/**
				 * Execution of the method getWeight(Long id)
				 * In this case, with this method we need also to manage
				 * the param that will be given to the method getWeight(Long id).
				 * Then we catch this arg and convert it to the Long type, in order to be 
				 * used as param for the getWeight(Long id) method.
				 */
			} else if(method.equals("getWeight")){
				Long personId = Long.parseLong(args[1]);
				System.out.println(test.getWeight(personId));
								
				//Execution of the method getHeight
			} else if(method.equals("getHeight")){
				Long personId = Long.parseLong(args[1]);
				System.out.println(test.getHeight(personId));

				//Execution of the method getWeightByName
			} else if(method.equals("getWeightByName")){
				String firstname = args[1];
				String lastname = args[2];
				Node nodeWeight = test.getWeightByName(firstname, lastname);
				
				try{
					System.out.println("The weight of " + firstname + " " + lastname + " is: " + nodeWeight.getTextContent());
				}catch(NullPointerException e){
					System.out.println(e);
					System.out.println("I'm sorry but the person with credentianl ["+ firstname + lastname + "] " + "doesn't exist.");
				}
				
				//Execution of the method getHeightByName
			} else if(method.equals("getHeightByName")){
				String firstname = args[1];
				String lastname = args[2];
				Node nodeHeight = test.getHeightByName(firstname, lastname);
				
				/**
				 * If exists a person with the passed firstname and lastname than print all information
				 * otherwise catch the NullPointerException to inform 
				 * that doesn't exist a person with the credential inserted 
				 */
				try{
					System.out.println("The height of " + firstname + " " + lastname + " is: " + nodeHeight.getTextContent());
				}catch(NullPointerException e){
					System.out.println(e);
					System.out.println("I'm sorry but the person with credentianl ["+ firstname + lastname + "] " + "doesn't exist.");
				}
				
				//Execution of the method getPersonById
			} else if (method.equals("getPersonById")) {
				Long personId = Long.parseLong(args[1]);
				node = test.getPersonById(personId);
				Element nelement = (Element) node;
				
				Attr attr = (Attr) nelement.getAttributeNode("id");
			    
				//Store in the variable 'id_value' the value associated to the node id
			    String id_value = attr.getValue();
			    
			    /**
			     * Create a Long variable to store the value of the value and cast it to Long
			     * The variable 'value' will be used to call the methods getWeight(id) and getHeight(id)
			     * and set the variable xpath_weight and xpath_height using the 
			     * methods setXpath_weight and setXpath_height
			     */
			    Long id = Long.parseLong(id_value);
	
				try{
					test.setXpath_firstname(nelement.getElementsByTagName("firstname").item(0).getTextContent());
					test.setXpath_lastname(nelement.getElementsByTagName("lastname").item(0).getTextContent());
					test.setXpath_birthday(nelement.getElementsByTagName("birthdate").item(0).getTextContent());
					test.setXpath_lastupdate(nelement.getElementsByTagName("lastupdate").item(0).getTextContent());
					test.setXpath_height(nelement.getElementsByTagName("height").item(0).getTextContent());
					
					/**
					 * Using the getWeight(Long id) method as requested 
					 * in the assignment to get the weight of passed id 
					 */
					test.setXpath_weight(test.getWeight(id));
					
					/**
					 * Using the getHeight(Long id) method as requested 
					 * in the assignment to get the weight of passed id
					 */
					test.setXpath_height(test.getHeight(id));
					
					test.setXpath_bmi(nelement.getElementsByTagName("bmi").item(0).getTextContent());
					
					System.out.println("Id: " + id_value);
					// Using the method ToString() to get all the information 
					System.out.println(test);
					
					/**
					 * Using the catch method to catch eventually errors
					 * In particular when and if the user would like execute this program
					 * setting the value of id out of the allowed range of values.
					 * id values range [1-20]
					 */
				}catch(Exception e){
					System.out.println(e);
					System.out.println("I'm sorry but remember"
							+ " that the id is a number between 1 and 20 "
							+ " and you inserted ["+ personId + "] "
							+ " that is not correct.");
				}
				
			}else if (method.equals("getPersonByIdInteractive")) {
				String s;
				Scanner in = new Scanner(System.in);
				System.out.println("Please enter an Id in the range [0001-0020]: ");
				s = in.nextLine();
				in.close();
				
				Long personId = Long.parseLong(s);
				try{
				node = test.getPersonById(personId);
				Element nelement = (Element) node;
				
				Attr attr = (Attr) nelement.getAttributeNode("id");
			    
				//Store in the variable 'id_value' the value associated to the node id
			    String id_value = attr.getValue();
			    
			    /**
			     * Create a Long variable to store the value of the value and cast it to Long
			     * The variable 'value' will be used to call the methods getWeight(id) and getHeight(id)
			     * and set the variable xpath_weight and xpath_height using the 
			     * methods setXpath_weight and setXpath_height
			     */
			    Long id = Long.parseLong(id_value);
	
				//try{
					test.setXpath_firstname(nelement.getElementsByTagName("firstname").item(0).getTextContent());
					test.setXpath_lastname(nelement.getElementsByTagName("lastname").item(0).getTextContent());
					test.setXpath_birthday(nelement.getElementsByTagName("birthdate").item(0).getTextContent());
					test.setXpath_lastupdate(nelement.getElementsByTagName("lastupdate").item(0).getTextContent());
					test.setXpath_height(nelement.getElementsByTagName("height").item(0).getTextContent());
					
					/**
					 * Using the getWeight(Long id) method as requested 
					 * in the assignment to get the weight of passed id 
					 */
					test.setXpath_weight(test.getWeight(id));
					
					/**
					 * Using the getHeight(Long id) method as requested 
					 * in the assignment to get the weight of passed id
					 */
					test.setXpath_height(test.getHeight(id));
					
					test.setXpath_bmi(nelement.getElementsByTagName("bmi").item(0).getTextContent());
					
					System.out.println("Id: " + id_value);
					// Using the method ToString() to get all the information 
					System.out.println(test);
					
					/**
					 * Using the catch method to catch eventually errors
					 * In particular when and if the user would like execute this program
					 * setting the value of id out of the allowed range of values.
					 * id values range [1-20]
					 */
				}catch(Exception e){
					System.out.println(e);
					System.out.println("I'm sorry but remember"
							+ " that the id is a number between 1 and 20 "
							+ " and you inserted ["+ personId + "] "
							+ " that is not correct.");
				}
				
			}else if (method.equals("getPersonByWeight")) {
				
				try{
				Double weight = Double.parseDouble(args[1]);
				String operator = args[2];
				String s = "><=";
				if(s.contains(operator)){
					NodeList nodes = test.getPersonByWeight(weight,operator);
					for (int i = 0; i < nodes.getLength(); i++) {
						System.out.println("========================================");
						System.out.println("ID: "+ nodes.item(i).getAttributes().item(0).getTextContent() );
						NodeList person= nodes.item(i).getChildNodes();   //get all child nodes of the parent node person
						Element nelement = (Element) person;
						test.setXpath_firstname(nelement.getElementsByTagName("firstname").item(0).getTextContent());
						test.setXpath_lastname(nelement.getElementsByTagName("lastname").item(0).getTextContent());
						test.setXpath_birthday(nelement.getElementsByTagName("birthdate").item(0).getTextContent());
						test.setXpath_lastupdate(nelement.getElementsByTagName("lastupdate").item(0).getTextContent());
						
						/**
						 * In one line is stored the right value to set
						 * the xpath_weight using the method setXpath_weight.
						 * 
						 * First step: get the content of the attribute
						 * Second step: cast the obtained value to Long type 
						 * Third step: pass the value to the getWeight method
						 * Fourth step: finally use the xpath_weight using the accessor method setXpath_weight() 
						 */
						test.setXpath_weight(test.getWeight(Long.parseLong(nodes.item(i).getAttributes().item(0).getTextContent())));
						test.setXpath_height(test.getHeight(Long.parseLong(nodes.item(i).getAttributes().item(0).getTextContent())));
						
						test.setXpath_bmi(nelement.getElementsByTagName("bmi").item(0).getTextContent());
						
						// Get all information using the method ToString()
						System.out.println(test);
					} 
				}else{
					System.out.println("I'm sorry but only comparison characters [< > =] are allowed for the method getPersonByWeight()");
					}
				
				}catch(Exception e){
					System.out.println(e);
					System.out.println("Only numbers are allowed.");
				}
				
			} else {
				System.out.println("The system did not find the method '"+method+"'");
			}
		}

		
		
	}
	
	public String getXpath_firstname() {
		return xpath_firstname;
	}

	public void setXpath_firstname(String xpath_firstname) {
		this.xpath_firstname = xpath_firstname;
	}
	
	public String getXpath_lastname() {
		return xpath_lastname;
	}

	public void setXpath_lastname(String xpath_lastname) {
		this.xpath_lastname = xpath_lastname;
	}

	public String getXpath_birthday() {
		return xpath_birthday;
	}

	public void setXpath_birthday(String xpath_birthday) {
		this.xpath_birthday = xpath_birthday;
	}
	
	public String getXpath_lastupdate() {
		return xpath_lastupdate;
	}

	public void setXpath_lastupdate(String xpath_lastupdate) {
		this.xpath_lastupdate = xpath_lastupdate;
	}

	public String getXpath_weight() {
		return xpath_weight;
	}

	public void setXpath_weight(String xpath_weight) {
		this.xpath_weight = xpath_weight;
	}

	public String getXpath_height() {
		return xpath_height;
	}

	public void setXpath_height(String xpath_height) {
		this.xpath_height = xpath_height;
	}

	public String getXpath_bmi() {
		return xpath_bmi;
	}

	public void setXpath_bmi(String xpath_bmi) {
		this.xpath_bmi = xpath_bmi;
	}
	
}