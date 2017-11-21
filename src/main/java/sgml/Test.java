package sgml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class Test {

	public static void main(String[] args) {
		 try {
			// ===== XML 文書の読み込み =====
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse( new File(System.getProperty("user.dir"), ".project") );
			// ===== javax.xml.xpath.XPath を使う方法 =====
			// 準備
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			// 単一ノード取得
			String location = "/projectDescription/linkedResources/link/location";
			System.out.println( xpath.evaluate(location, doc) );
			// 複数ノード取得
			location = "//t1/t2[2]/text()";
			NodeList entries = (NodeList) xpath.evaluate(
			                        location, doc, XPathConstants.NODESET );
			for( int i = 0; i < entries.getLength(); i++ ) {
			    System.out.println( entries.item(i).getNodeValue() );
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
