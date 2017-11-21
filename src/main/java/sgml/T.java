package sgml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class T {

	public static void main(String[] args) {
		File file = new File(System.getProperty("user.dir"), ".project");
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());
		try {
			InputStream in = new FileInputStream(file);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			// 単一ノード取得
			String location = "/projectDescription/name";
			location = "//nature";
			System.out.println( xpath.evaluate(location, doc) );
			location = "//nature[2]";
			System.out.println( xpath.evaluate(location, doc) );
			// 複数ノード取得
			location = "//nature";
			NodeList entries = (NodeList) xpath.evaluate(
			                        location, doc, XPathConstants.NODESET );
		    System.out.println( entries.getLength() );
			for( int i = 0; i < entries.getLength(); i++ ) {
			    System.out.println( entries.item(i).getTextContent() );
			}
			in.close();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
