package com.harlov.jsf.xmlParsingProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.FileUtils;
//Sax allows only to read a xml file
import org.xml.sax.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

@ManagedBean(name = "xMLFileHandler", eager = true)
@SessionScoped
public class XMLFileHandler {
	private List<File> files;
	private String path;
	private ArrayList<Person> persons;
	
	public XMLFileHandler() {
		this.path = "C:\\andy\\Development\\Projects\\JSFManagedBeanXMLFilesSearch&Parcing\\testXMLFileSearch";
		this.persons = new ArrayList<Person>();
	}
	
	public String searchXMLFiles() throws Exception{
		this.persons.clear();
		
		File directory = new File(this.path);
		String[] extensions = new String[] {"xml"};
		
		System.out.println("Looking for .xml files in directory " + directory.getCanonicalPath());
		this.files = (List<File>) FileUtils.listFiles(directory, extensions, true);
		
		if(this.files != null) {
			for (File file : files) {
				System.out.println("file: " + file.getCanonicalPath());
				parseXMLFile(file.getCanonicalPath());
			}
		}
		
		for(Person person: this.persons) {
			System.out.println("id = " + person.getId());
			System.out.println("firstName = " + person.getFirstName());
			System.out.println("lastName = " + person.getLastName());
		}
		
		return "success";
	}
	
	public void parseXMLFile(String filePath) {
		Document xmlDocument = getDocument(filePath);
		
		if(xmlDocument != null) {
			String elementName = "person";
			String attributeName = "id";
			NodeList listOfPersons = xmlDocument.getElementsByTagName("person");
			getElementAndAttribute(listOfPersons, elementName, attributeName);
		}
	}

	private void getElementAndAttribute(NodeList listOfPersons, String elementName, String attributeName) {
		String id;
		String firstName;
		String lastName;
		
		try {
			for(int i = 0; i < listOfPersons.getLength(); i++) {
				id = "";
				firstName = "";
				lastName = "";
				
				Node personNode = listOfPersons.item(i);
				Element personElement = (Element) personNode; //tag <person> 
				if(personElement.hasAttribute(attributeName)) {
					id = personElement.getAttribute(attributeName);
					//System.out.println("Person #" + i + " has id = " + id);
				}

				NodeList firstNameList = personElement.getElementsByTagName("firstName");
				Node firstNameNode = firstNameList.item(0);
				firstName = firstNameNode.getTextContent();
				//System.out.println("Person #" + i + " has firstName = " + firstName);
				
				NodeList lastNameList = personElement.getElementsByTagName("lastName");
				Node lastNameNode = lastNameList.item(0);
				lastName = lastNameNode.getTextContent();
				//System.out.println("Person #" + i + " has lastName = " + lastName);
				
				Person person = new Person(id, firstName, lastName);
				this.persons.add(person);
			}
		} catch(Exception exeption) {
			exeption.printStackTrace();
		}
	}

	private Document getDocument(String filePath) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(filePath));
		} catch (Exception exeption) {
			System.out.println(exeption.getMessage());
		}
		return null;
	}

	public ArrayList<Person> getPersons() {
		return persons;
	}
	
	
}
