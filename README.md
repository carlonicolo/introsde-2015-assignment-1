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
5. Write a java application that does the **marshalling** and **un-marshalling** using classes generated with **JAXB XJC**.
6. Make your java application to convert also **JSON**.

In the [**II - paragraph [Task of the code]**](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#ii---task-of-the-code) is explained and showed the code corresponding each task.

---

In this project is used **ant** as tool for automating software build processes and **ivy** as dependency manager. In the [**III - paragraph - How to run **](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#iii---how-to-run) is explained how the build.xml file of ant works and then how run the implemented.

---

In the [**IV - paragraph [Extra features]**](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#iv---extra-features) are explained the extra features not requested by the assignment but implemented by myself.



# Index

1. [**I - paragraph [The code]**](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#i---the-code)
2. [**II - paragraph [Task of the code]**](https://github.com/carlonicolo/introsde-2015-assignment-1/blob/master/README.md#ii---task-of-the-code)
    1. First Part
           1.The class XPathHealthProfile.java
           2.Task1
           3.Task2
           4.Task3
    2. Second Part






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


### **Task 3 - A function which accepts a weight and an operator (=, > , <) as parameters and prints people that fulfill that condition**


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

As requested in the assignemnt specification, also because indispensible for generating classes with JAXB XJC, the first step to perform in order to continue and work on the marshalling and unmarshalling with generated classes, is to create the people.xsd schema for people.xml:

```xml

<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema">

	<!-- <xsd:element name="people">
		<xsd:complexType>
			<xsd:sequence>
				<xsd:element name="person" type="personType" maxOccurs="unbounded"/>
			</xsd:sequence>
		</xsd:complexType>
	</xsd:element> -->
<xsd:element name="people" type="peopleType"/>

<xsd:complexType name="peopleType">
			<xsd:sequence>
				<xsd:element name="person" type="personType" maxOccurs="unbounded"/>
			</xsd:sequence>
</xsd:complexType>

	<xsd:complexType name="personType">
		<xsd:sequence>
			<xsd:element name="firstname" type="xsd:string"/>
			<xsd:element name="lastname" type="xsd:string"/>
			<xsd:element name="birthdate" type="xsd:dateTime"/>
			<xsd:element name="healthprofile" type="healthDataType"/>
		</xsd:sequence>
		<xsd:attribute name="id" type="xsd:integer"/>
	</xsd:complexType>

	<xsd:complexType name="healthDataType">
		<xsd:sequence>
			<xsd:element name="lastupdate" type="xsd:dateTime"/>
			<xsd:element name="weight" type="xsd:decimal"/>
			<xsd:element name="height" type="xsd:decimal"/>
			<xsd:element name="bmi" type="xsd:decimal"/>
		</xsd:sequence>
	</xsd:complexType>

</xsd:schema>


```
This schema is very important because is from this that will be generated the classes with relatives methods and variables.
Well taking in account this declaration in the build.xml

```xml

...........

<property name="xjc.package" value="peoplestore.generated" />
...........

```

and after executing this target


```xml

<!-- This target generate the classes using xjc based on the given .xsd schema -->
	<target name="generate" depends="init">
		<taskdef name="xjc" classname="com.sun.tools.xjc.XJCTask" classpathref="lib.path.id">
		</taskdef>
		<xjc schema="people.xsd" destdir="${src.dir}" package="${xjc.package}" />
	</target>

```


the folder tree appear in this way:

![Tree folder project with generated classes](http://www.carlonicolo.com/IntroSDE/Assignment1/peopleStoreGenerated.png)

The classes generated that will be necessary to perfomr the tasks 4 and 5 are:
* **HealthDataType.java**;
* **ObjectFactory.java**;
* **PeopleType**;
* **PersonType**.


---


### **Task 4: Write a java application that does the marshalling using classes generated with JAXB XJC**
The main class that perform the marshalling is **JAXBMarshaller.java**.

```java
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
 *  2. createPerson(peoplestore.generated.ObjectFactory factory, String i, int yearBirth,int monthBirth, int dayBirth, int hourBirth, int minBirth, String firstname,String lastname, int yearUpdate, int monthUpdate, int dayUpdate, int hourUpdate, int minUpdate,int weight, double height, int bmi) that allows to create a person with params		
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
	public PersonType createPerson(peoplestore.generated.ObjectFactory factory, String i, int yearBirth,int monthBirth, int dayBirth, int hourBirth, int minBirth, String firstname,String lastname, int yearUpdate, int monthUpdate, int dayUpdate, int hourUpdate, int minUpdate, int weight, double height) throws DatatypeConfigurationException{
		
		
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

    ```


The code of this class is almost all commented then i will list some design decisions that i did.
* I decide to save the objects created in another and new file xml named **peopleMarshaller.xml** ;
* I create a static method to create new Person to make things easier 
```java
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
	public PersonType createPerson(peoplestore.generated.ObjectFactory factory, String i, int yearBirth,int monthBirth, int dayBirth, int hourBirth, int minBirth, String firstname,String lastname, int yearUpdate, int monthUpdate, int dayUpdate, int hourUpdate, int minUpdate, int weight, double height)
    ```
* A method to compute the bmi
```java
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
```	
	
	
---


### **Task 5: Write a java application that does the un-marshalling using classes generated with JAXB XJC**
In this class i take information from the file people.xml and store the information in the objects using the generated classes with JAXB XJC. The choice to use as file xml the file people.xml is due to the fact to be sure that in this way i'm sure that that file is not empty.
```java
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
```
Is important to underline the creation of the Schema from the people.xsd 
```java
SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
Schema schema = schemaFactory.newSchema(new File("people.xsd"));
unMarshaller.setSchema(schema);


```




---




### **Task 6: Make your java application to convert also JSON, create 3 persons using java and marshal them to JSON**
This file is named **JSONMarshaller.java** and is located in the **src/** folder. Instead of the previous two classes this file does not work with generated classes but with the classes in the **model/** and **dao/** folder 

![model and dao folder](http://www.carlonicolo.com/IntroSDE/Assignment1/peopleJSON.png)


The program generates 3 persons as requested and creates a file people.json where are stored all the information of the person created:

```java
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


```

---




# **III - How to run**
Before starting to see how to run the evaluation script and examinate each task how is structured i think that would be very useful to show and explain the most interesting and important parts of the build.xml file.

## ANT - the build.xml file
The build.xml file is a configuration file in which we define the behavior of our program and the sequence of the esecution and many other things... Due the fact that the build.xml are more than 200 rows and that i commented it in the crucial points, i think that would be useful and more interesting to point the attention on some parts. Then skip the parts donwloading ivy and show three important parts:

* General **properties definition**: fundamental to perform all tasks and have the right reference to point

```xml
<!-- PART 2: General properties definitions -->
	<property name="build.dir" value="build" />
	<property name="src.dir" value="src" />
	<property name="lib.dir" value="lib" />
	<property name="xjc.package" value="peoplestore.generated" />
	<property name="xjc.folder" value="peoplestore/generated" />
```
* The target **generate** that generates the classes with XJC

```xml
<!-- This target generate the classes using xjc based on the given .xsd schema -->
	<target name="generate" depends="init">
		<taskdef name="xjc" classname="com.sun.tools.xjc.XJCTask" classpathref="lib.path.id">
		</taskdef>
		<xjc schema="people.xsd" destdir="${src.dir}" package="${xjc.package}" />
	</target>
```

* The target **compile** that as suggest the name is used to compile the classes 
```xml
<!-- This target is used to compile the classes -->
	<target name="compile" depends="init, generate">
		<echo message="Compile target has been called" />
		<javac srcdir="${src.dir}" destdir="${build.dir}" classpathref="lib.path.id" includeAntRuntime="false">
		</javac>
		<copy todir="${build.dir}" overwrite="true">
			<fileset dir="${src.dir}">
				<include name="*.xml" />
			</fileset>
		</copy>
	</target>
```
* In the next subparagraph there is explained the target that we need to perform all requested funcionalities for the assignment, the target  **execute.evaluation**
 

## The target **execute.evaluation** 

The requested Evaluation script for this project consists in create a target in the build.xml file named execute.evaluation which:
* Make a function that prints all people in the list with detail;
* A function that accepts id=5 as parameter and prints the HealthProfile of the person with that id;
* A function which accepts a weight and an operator (=, > , <) as parameters and prints people that fulfill that condition > 90;
* marshaling to XML - create 3 persons using java and marshal them to XML. Print the content and save to .xml file
* un-marshaling from XML;
* marshaling to JSON - create 3 persons using java and marshal them to JSON. Print the content and save to .json file

Good then as first thing i want show here the code corresponding to execute.evaluation and then explain how it works:

```xml

<target name="execute.evaluation" depends="compile">
			<echo message="*********************************************" />
			<echo message="* execute.evaluation target has been called *" />
			<echo message="*********************************************" />
			<echo message="" />
			
			<echo message="==========================================================="/> 
			<echo message="*                          #Task1                         *"/>
			<echo message="*            Runs instruction 2 based on Lab 3            *"/>
			<echo message="* Function that prints all people in the list with detail *" />
			<echo message="===========================================================" />
			<java classname="XpathHealthProfile" classpath="build">
				<arg value="printAllPeople"/>
			</java>
			<echo message="========================= END Task1 ===========================" />
			
			<echo message="" />
			<echo message="=========================================================================================="/> 
			<echo message="*                                    #Task2                                              *"/>
			<echo message="*                      Runs instruction 3 based on Lab 3 with id = 5                     *"/>
			<echo message="*  A function which accepts an id as parameters HealthProfile of the person with that id *" />
			<echo message="==========================================================================================" />
			<java classname="XpathHealthProfile" classpath="build">
				<arg value="getPersonById"/>
				<arg value="5"/>
			</java>
			<echo message="================================== END Task2 ==================================="/>
			
			<echo message="" />
		    <echo message="================================================================================================================="/> 
			<echo message="                                            #Task3                                                              *"/>
			<echo message="                         Runs instruction 3 based on Lab 3 with weight > 90                                     *"/>
			<echo message="* A function which accepts a weight and an operator as parameters and prints people that fulfill that condition *" />
			<echo message="=================================================================================================================" />
			<java classname="XpathHealthProfile" classpath="build">
				<arg value="getPersonByWeight"/>
				<arg value="90"/>
				<arg value=">"/>
			</java>
			<echo message="========================================== END Task3 ============================================================" />
			
			<echo message="" />
		    <echo message="================================================================================================================="/> 
			<echo message="                                            #Task4                                                              *"/>
			<echo message="                               Runs instruction 2 based on Lab 4                                                *"/>
	        <echo message="              (marshaling to XMLcreate 3 persons using java and marshal them to XML)                            *"/>               
			<echo message="   Write a java application that does the marshalling using classes generated with JAXB XJC                     *"/>
			<echo message="=================================================================================================================" />
			<java classname="peoplestore.JAXBMarshaller" classpath="${build.dir}">
			<classpath>
				<fileset dir="${lib.dir}">
					<include name="**/*.jar" />
					<include name="*.jar" />
				</fileset>
			</classpath>
		        </java>
			<echo message="========================================== END Task4 ============================================================" />
			
			
			<echo message="" />
		    <echo message="================================================================================================================="/> 
			<echo message="                                            #Task5                                                              *"/>
			<echo message="                               Runs instruction 2 based on Lab 4                                                *"/>
	        <echo message="                      Runs instruction 2 based on Lab 4 (un-marshaling from XML)                                *"/>               
			<echo message="   Write a java application that does the un-marshalling using classes generated with JAXB XJC                  *"/>
			<echo message="=================================================================================================================" />
			<java classname="peoplestore.JAXBUnMarshaller" classpath="${build.dir}">
			<classpath>
				<fileset dir="${lib.dir}">
					<include name="**/*.jar" />
					<include name="*.jar" />
				</fileset>
			</classpath>
		        </java>
			<echo message="========================================== END Task5 ============================================================" />
			
			
			<echo message="" />
		    <echo message="================================================================================================================="/> 
			<echo message="                                            #Task6                                                              *"/>
			<echo message="                               Runs instruction 3 based on Lab 4                                                *"/>
			<echo message="            Marshaling to JSON - create 3 persons using java and marshal them to JSON                           *"/>
			<echo message="=================================================================================================================" />
			<java classname="JSONMarshaller" classpath="${build.dir}">
			<classpath>
				<fileset dir="${lib.dir}">
					<include name="**/*.jar" />
					<include name="*.jar" />
				</fileset>
			</classpath>
		        </java>
			<echo message="========================================== END Task6 ============================================================" />
		</target>


```

In easy to see, because is commented, each task where is executed then i only describe the most interesting thing the **tag arg** inside the **tag java**:

```xml

<echo message="================================================================================================================="/> 
			<echo message="                                            #Task3                                                              *"/>
			<echo message="                         Runs instruction 3 based on Lab 3 with weight > 90                                     *"/>
			<echo message="* A function which accepts a weight and an operator as parameters and prints people that fulfill that condition *" />
			<echo message="=================================================================================================================" />
			<java classname="XpathHealthProfile" classpath="build">
				<arg value="getPersonByWeight"/>
				<arg value="90"/>
				<arg value=">"/>
			</java>

```

What i'm doing here is simply passing to the java class **XpathHealthProfile** three **args**
* getPersonByWeight;
* 90;
* \> ;

Well it means that we are executing the programma like this:
```shell
$ java XpathHealthProfile getPersonByWeight 90 > 
```
in the main of the class XpathHealthProfile there is a series of if-else that check the first arg to see if there is a match with the methods implemented in the class. In this case i already showed that this method exists then the program works fine.


## **How run the assignment**

To execute the evaluation script, firt we go in the main root of the project(where is the build.xml) and we need to execute this command:

```shell
ant execute.evaluation
```

This target dependes on **compile** then is possible execute it without explicit call the **target compile** because **execute.evaluation** perform the compilation too. 

### **The output**

[Here](http://www.carlonicolo.com/IntroSDE/Assignment1/introsde-2015-assignment-1[CarloNicol%C3%B2].txt) there is the link to a txt file containing the output of the execute.evaluation target.
I added an extra feature that explain in the next paragraph, this extra feature allow me to create a file **introsde-2015-assignment-1[CarloNicolò].txt** with the output of **target execute.evaluation** 


# Extra Features

I developed some extra features in this assignment:

### In the XpathHealthProfile.java
* The method getHeightByName(String firstname, String lastname)
```java

else if(method.equals("getHeightByName")){
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
		........

```
above is where the methods is called and i stored the result from it in the variable nodeHeight. Below there is the code
of the method where i take the params to perform the query:

```java
/**
	 * Given the first name and last name of a person, it returns
	 * a Node object containing the node height of the person.
	 * 
	 * @param firstname 
	 * @param lastname
	 * @return node 	the Node object height
	 */
	public Node **getHeightByName(String firstname, String lastname)** throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[firstname='" + firstname + "' and lastname='" + lastname + "']/healthprofile/height");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node;
	}
```

The corresponding build.xml:
```xml
<!-- Execute only before compiling or executing execute.evaluation -->
	<!-- This target execute the method getWeightByName and return the weight of the person 
		with firstname and lastname passed as arg value -->
	<target name="execute.GetHeightByName">
		<input message="Please enter the firstname:" addproperty="GetHeightByName_firstname" />
		<input message="Please enter the lastname:" addproperty="GetHeightByName_lastname" />
		<java classname="XpathHealthProfile" classpath="build">
			<arg value="getHeightByName"/>
			<arg value="${GetHeightByName_firstname}" />
			<arg value="${GetHeightByName_lastname}" />
		</java>
	</target>
```


To execute this method:
```shell
$ ant execute.GetHeightByName
```
---

* The method **getWeightByName(String firstname, String lastname)**. It works as the method above
```shell
$ ant execute.GetWeightByName
```
---

* The functionality getPersonByIdInteractive(String id).
Thaks to this method is possible to take the input from the user then execute the program with a little bit of interactive.
```java
else if (method.equals("getPersonByIdInteractive")) {
	String s;
	Scanner in = new Scanner(System.in);
	System.out.println("Please enter an Id in the range [0001-0020]: ");
    s = in.nextLine();
	in.close();
				
	Long personId = Long.parseLong(s);
	
	.................
```
Above i decided to show only the significant part of the code.

In the build.xml:
```xml
<!-- Execute only before compiling or executing execute.evaluation -->
	<!-- This target execute the method getPersonByIdInteractive and return the complete profile 
	of the person corresponding to the param id -->
		<target name="execute.ScannerInputId">
			        <java classname="XpathHealthProfile" classpath="build">
			            <arg value="getPersonByIdInteractive"/>
			        </java>
			    </target>
```

To execute this from ant this is the command:

```shell

$ ant execute.ScannerInputId

```
---
 
* **target clean**

```xml

                                     <!-- TARGET TO CLEAN THE PROJECT -->
	<target name="clean">
		<echo message="Clean has been called" />
		
		<delete dir="${build.dir}" />
		<echo message="${build.dir} has been deleted" />
		
		<delete dir="${lib.dir}" />
		<echo message="${lib.dir} has been deleted" />
		
		<delete dir="${ivy.jar.dir}" />
	    <echo message="${ivy.dir} has been deleted" />
		
		<delete dir="${src.dir}/${xjc.folder}" />
		<echo message="${src.dir}/${xjc.folder} has been deleted" />
		
		<delete file="peopleMarshaller.xml" />
		<echo message="peopleMarshaller.xml has been deleted" />
		
		<delete file="people.json" />
		<echo message="people.json has been deleted" />
		
		<delete file="introsde-2015-assignment-1[CarloNicolò].txt" />
	    <echo message="introsde-2015-assignment-1[CarloNicolò].txt has been deleted" />
		
		<delete file="introsde-2015-assignment-1-InteractiveVers[CarloNicolò].txt" />
	    <echo message="introsde-2015-assignment-1-InteractiveVers[CarloNicolò].txt has been deleted" />
		
	</target>

```
Command:

```shell

$ ant clean

```

---

* **target execute.InteractiveEvaluation** that uses the input directly from ant and has also a graphical interface if executed in eclipse

```shell

$ ant execute.InteractiveEvaluation

```
This is what you get if you execute ant inside eclipse:

![](http://www.carlonicolo.com/IntroSDE/Assignment1/ExecuteInteractive.png)

