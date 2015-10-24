# **Introsde 2015 Assignment-1**

This is the first assignment for the "Introduction to service design and engineering" course. The assignment is divided in two parts:
1. The first part based on the knowledge and use of **XML**, **XPATH** and the techniques performed with the Java language to explore and retrieve information from an XML file.These contents are treated in the [LAB03](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-3).

2. The second part concerns the techniques to map XML and JSON to and from Objects using the **JAXB** **XJC** and the the Serialization to JSON, **marshalling** and **unmarshalling**. These contents are treated in the [LAB04](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-4).

In the [**I - paragraph [The code]**](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#i---the-code) there is a general view of the project and its folders and files to understand how the code is structured and organized.

---

The tasks expected to be performed in this assignment are six.The first three based on LAB03 and the other three on the LAB04:

1. make a function that prints all people in the list with detail.
2. Create a function that accepts **id** as parameter and prints the **HealtProfile** of the person with that id.
3. A function which accepts a **weight** and an **operator (=, > , <)** as parameters and prints people that fulfill that condition.
4. Create the **XML schema XSD** file for the example XML document of people.
5. Write a java application that does the **marshalling** and **un-marshalling** using classes generated with J**AXB XJC**.
6. Make your java application to convert also **JSON**.

In the [**II - paragraph [Task of the code]**](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#ii---task-of-the-code) is explained and showed the code corresponding each task.

---

In this project is used **ant** as tool for automating software build processes and **ivy** as dependency manager. In the [**III - paragraph - How to run **](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#iii---how-to-run) is explained how the build.xml file of ant works and then how run the implemented.

---

In the [**IV - paragraph [Extra features]**](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#iv---extra-features) are explained the extra features not requested by the assignment but implemented by myself.

# **I - The code**
The first thing requested and necessary to start with the project is to create an XML file representing people and containing the information about 20 person and the relative health profile of each person. The structure is showed below:
```xml
<?xml version="1.1" encoding="US-ASCII"?>
<people>
  <person id="0001">
    <firstname>Larissa</firstname>
    <lastname>Gaylord</lastname>
    <birthdate>1976-02-27T00:00:00+00:00</birthdate>
    <healthprofile>
      <lastupdate>2015-10-16T16:52:24-08:00</lastupdate>
      <weight>71.81</weight>
      <height>1.54</height>
      <bmi>30.28</bmi>
    </healthprofile>
  </person>
....
<person id="0020">
    <firstname>Agnes</firstname>
    <lastname>Schuster</lastname>
    <birthdate>1982-05-21T00:00:00+00:00</birthdate>
    <healthprofile>
      <lastupdate>2015-10-07T16:52:24-08:00</lastupdate>
      <weight>103.99</weight>
      <height>1.64</height>
      <bmi>38.66</bmi>
    </healthprofile>
  </person>
</people>
```
This file will be used to perform tasks based on the first part and also to create the corresponding xsd schema essential to execute the tasks requested in the second part(create classes, marshalling and unmarshalling).



---

![Tree project](http://www.carlonicolo.com/IntroSDE/Assignment1/treeAssignmetFolder.png)

In the above image, is showed the source tree of the project. In this way is possibile to have a better idea of how the code is organized and structured. All classes are included in the **src** folder and three subfolders.
* **XpatHealthProfile.java**: is the class used to explore the **people.xml** file and retrieve all requested information from it using **xpath** as requested by the specification of the assignment.This class allow to perform all tasks requested for the first part of the assignment.
* In the **/peoplestore** folder there are the java files used to execute the **marshaller** and **unmarshaller**.
* **JSONMarshaller.java** file is used to perform the marshalling to JSON and need the classes included in the **model** folder and **dao** folder



Whereas in the root folder of the project there are the configuration files with the people.xml and the corresponding schema.
* **build.xml**: is the configuration file of ant;
* **ivy.xml**: contains the dependecies for the ivy manager;
* **people.xml**: is the xml file containing the information about person;
* **people.xsd**: is the xsd schema of people.xml
* **README.md**: is the documentation file, what you are reading now.




# **II - Task of the code**
In this paragraph i explain each task implemented, showing the significant parts of the code, in order to better understand how it works and what are the choice made.

## **First Part**

### **The class XpathHealthProfile.java**
Before starting with the explanation of the first three tasks, i want start with the exploration of the class **XpathHealtProfile.java** to have a better idea of how is structured and how it works.

I declared a couple of variable corresponding to the elements(nodes and attributes included) of the people.xml file and in this way store information and create accessor methods[get();set()]

```java
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
	
	............
	
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
	
	.........
	
```

I have also provided this class to a **ToString()** method to use it on the object and retrieve all information needed:
```java
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

```
Now we could see what happen and how the people.xml file is loaded and parsed. In effect in order to manipulate and move inside the **people.xml** file we need at first two things:
1. Load the xml in memory;
2. create the xpath object.

```java
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

```

Another very important concept that is how works the **XPathExpression** and its method **xpath.compile()**. This is very important because we use path expressions to navigate the XML and perform query information inside them. In effect in all implemented methods for the first three tasks i used this query language. 
The last information before starting to examine the tasks is to explain how the main class works and what do. In this way, also if i use ant to compile and do execute the project**[III paragraph - how to run]**, the user will be able to execute the program also without ant.

The main class starts creating a new object of the class and instantiating a variable, node of type Node.
```java
public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		XpathHealthProfile test = new XpathHealthProfile();
		test.loadXML();

		Node node;

```

Now there is a series of if-else to check wich method the user want to execute:
```java

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
				
	......................			

```



---


### **Task 1: Make a function that prints all people in the list with detail**

```java

..........

else {
			String method = args[0];
			//Execution of the method PrintAllPeople
			if (method.equals("printAllPeople")) {
				try{
					test.printAllPeople();
				} catch(Exception e) {
					System.out.println(e);
				}
				
	................

```
At this point we call the args passed to the program is equals to printAllPeople that is the name of a method implemented and the show and print in the console all the people in the people.xml file. This peace of code does not show the method but only the how this method is invoked, then let see the code of the printAllPeople()

```java

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

```

In the code is possible to see how the XPathExpression works and in the for loop how i used the requested methods getWeight(id) and getHeight(id) to retrieve the weight and height of a person given an id:

```java

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
	
	

```



---


### **Task 2: A function that accepts id as parameter and prints the HealthProfile of the person with that id** 

This is the code corresponding the method that i called getPersonById(Long id):

```java

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


```

this method is called in the main and return a node that will be used to retrive all information:

```java

else if (method.equals("getPersonById")) {
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

```

The code is commented, by the way the interesting things are:
* the use of the class Attr to get the attribute value of the element person

```java
Long personId = Long.parseLong(args[1]);
node = test.getPersonById(personId);
Element nelement = (Element) node;
				
Attr attr = (Attr) nelement.getAttributeNode("id");
			    
//Store in the variable 'id_value' the value associated to the node id
String id_value = attr.getValue();
```

* the use of try-catch to intercept errors and suggest the right way to pass the arg;
* the use of accessor method set;
* and the use of the to ToString() method on the object **test** to print the output:

```java
System.out.println(test);

```
In the next section [III - paragraph [how to run]](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#iii---how-to-run) i will show the output of all tasks.



---


## **Task 3 - A function which accepts a weight and an operator (=, > , <) as parameters and prints people that fulfill that condition**


This is the method getPersonByWeight:

```java

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

```
the query is formed by the two values passed as parameters.

In the code below is showed when and how is called the method getPersonByWeight(Double weight, String operator). 


```java

else if (method.equals("getPersonByWeight")) {
				
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



```

There is nothing new respect to what showed before.



---


## Second Part 































