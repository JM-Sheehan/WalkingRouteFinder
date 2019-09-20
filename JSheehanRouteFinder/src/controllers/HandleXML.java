package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import application.Main;

public class HandleXML {
	private static XStream xstream = new XStream(new StaxDriver());
	
	public HandleXML() {
		
	}
	
	/*
	 * Method for reading the "routes.xml" file and populating the landmarksMap hashMap.
	 */
	public static void parseMap() {
		try {
			/*
			 * reads the "routes.xml" file and converts it to a document format.
			 */
			File inputFile = new File ("routes.xml");
			DocumentBuilderFactory dbFactory =  DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
//			Searches for elements in the xml file with the landmark tag.
			NodeList nList = doc.getElementsByTagName("landmark");
			
//			Iterate over list of landmark elements and construct landmark objects with the name attribute of each landmark element.
			for(int i =0; i<nList.getLength(); i++) {
				Node nNode = nList.item(i);
				NodeList children = nNode.getChildNodes();

				String name =  children.item(1).getTextContent();
			
				Landmark<?> landmark = new Landmark<String>(name);
//				Puts landmark object in the landmarkMap hashMap.
				Main.landmarksMap.put(landmark.id, landmark);
			}

//			Iterate over the landmarks and find all links(links use the tag adjacent). 
			for(int i=0; i<nList.getLength();i++ ) {
				Element node = (Element) nList.item(i);
				NodeList adjacents = node.getElementsByTagName("adjacent");
				
//				Each node contains the attributes of a link.
				for(int d =0; d<adjacents.getLength();d++) {
					Node linkNode= adjacents.item(d);
					NodeList linkAtt = linkNode.getChildNodes();
//					End point of links id as a String.
					String destinationS = linkAtt.item(1).getTextContent();
//					Distance between start point and end point..
					String distanceS = linkAtt.item(3).getTextContent();
//					Streets linking start and end point.
					String streets = linkAtt.item(5).getTextContent();


					int sourceId;
					int destId;
					int distance;
//					Source id is the current iteration of loop + 1.
					
					sourceId =  i+1;
					destId =  Integer.valueOf(destinationS);
					distance = Integer.valueOf(distanceS);
					/*
					 * Gets the landmark stored at the current source ID and creates a link between it
					 * and the destination node of the current link with the given distance and streets between.
					 */
					Main.landmarksMap.get(sourceId).connectDirected(Main.landmarksMap.get(destId), distance, streets);
				}
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Iterates over the map of landmarks and rewrites the "routes.xml" file to save any
	 * updates made by the user.
	 */
	public static void saveMap() {
		/*
		 * Creates a string to store the updated xml and iterates over the landmarksMap
		 * to construct this string.
		 */
		 String test = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"\n"+ "<graph>";
		 for(int i=1;i<=Main.landmarksMap.size();i++) {
			 test+="\n"+"	<landmark id=\"" + i + "\">";
			 test+="\n"+"		<data>"+ Main.landmarksMap.get(i).data + "</data>";
			 test+= "\n"+"		<adjacents>";
			 for(int j=0;j<Main.landmarksMap.get(i).neighbours.size();j++) {
				 String adj = "\n"+"			<adjacent>";
				 adj+="\n"+ "				<destination>" + Main.landmarksMap.get(i).neighbours.get(j).landmarkTwo.id + "</destination>";
				 adj+="\n"+ "				<distance>" + Main.landmarksMap.get(i).neighbours.get(j).distance + "</distance>";
				 adj+="\n"+ "				<Streets>" + Main.landmarksMap.get(i).neighbours.get(j).streets + "</Streets>";
				 adj+="\n"+ "			</adjacent>";
				 test+=adj;
			 }
			 test+= "\n"+"		</adjacents>";
			 test+="\n"+ "	</landmark>";
		 }
		 test+="\n"+"</graph>";
		 Document doc;
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = null;
		 try {
			 builder = factory.newDocumentBuilder();
			 
			 doc = builder.parse(new InputSource(new StringReader(test)));//Reads the constructed string and creates a document with this content.
			 System.out.println("Done");
			 
		 }catch(Exception e) {
			 doc = null;
			 e.printStackTrace();
		 }
		 try {
			 DOMSource source = new DOMSource(doc);
			FileWriter writer = new FileWriter(new File("routes.xml"));
			StreamResult result = new StreamResult(writer);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(source, result);//Saves source to result.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
}
