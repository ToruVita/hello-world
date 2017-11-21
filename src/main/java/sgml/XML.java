package sgml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML {
	private Document document;
	public void delSpace(Node node) {
		Node root = node.getParentNode();
		Node prev = node.getPreviousSibling();
		if ("#text".equals(prev.getNodeName())) {
			String str = prev.getNodeValue();
			String regex = "^[\\s|\\t]+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				root.removeChild(prev);
			}
		}
	}
	public Element createDocumentElement(String root, String encode) {
		Element rootElement = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			document = documentBuilder.newDocument();
			rootElement = document.createElement(root);
			document.appendChild(rootElement);
		} catch (ParserConfigurationException e) {
		}
		return rootElement;
	}
	public Document load(String fileName) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			if(new File(fileName).exists()){
				FileInputStream fileInputStream = new FileInputStream(fileName);
				document = documentBuilder.parse(fileInputStream);
				fileInputStream.close();
			}else{
				URL url = new URL(fileName);
				document = documentBuilder.parse(url.openStream());
			}
		} catch (ParserConfigurationException e) {
		} catch (FileNotFoundException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
		return document;
	}
	public void write(String fileName) {
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			transformerFactory.setAttribute("indent-number", "4");
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					new FileOutputStream(fileName), "utf-8");
			transformer.transform(new DOMSource(document), new StreamResult(
					outputStreamWriter));
			try {
				outputStreamWriter.close();
			} catch (IOException e) {
			}
		} catch (TransformerConfigurationException e) {
		} catch (FileNotFoundException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (TransformerException e) {
		}
	}
	public void output(String encode) {
		StringWriter stringWriter = null;
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			transformerFactory.setAttribute("indent-number", "4");
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, encode);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			stringWriter = new StringWriter();
			StreamResult streamResult = new StreamResult(stringWriter);
			DOMSource domSource = new DOMSource(document);
			transformer.transform(domSource, streamResult);
			// stringWriter.flush();
			System.out.println(stringWriter.toString());
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		}
		try {
			stringWriter.close();
		} catch (IOException e) {
		}
	}
	public Document getDocument() {
		return document;
	}
	public void setNodeAttr(Node node, String attrName, String value){
		NamedNodeMap attrs = node.getAttributes();
		Node n = attrs.getNamedItem(attrName);
		n.setNodeValue(value);
	}
	public String getNodeAttr(Node node, String attrName){
		if(node.hasAttributes()){
			NamedNodeMap attrs = node.getAttributes();
			if(attrs.getNamedItem(attrName)!=null){
				return attrs.getNamedItem(attrName).getNodeValue();
			}
		}
		return null;
	}
	public Node createNode(String tag, String val){
		Node node = document.createElement(tag);
		if(val != null && val.length()>0){
			Node text = document.createTextNode(val);
			node.appendChild(text);
		}
		return node;
	}
	public void rewrite(String name, String value){
		if(name.startsWith("#")){
			Node node = document.getElementById(name);
		}else{
			NodeList nodes = document.getElementsByTagName(name);
			for(int i = 0; i < nodes.getLength(); i++){
				Node n = nodes.item(i);
				n.setTextContent(value);
			}
		}
		
	}
	// 単一ノード取得
	public String getTextViaXmlPath(String path){
//		String path = "/projectDescription/linkedResources/link/location";
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			return xpath.evaluate(path, document) ;
		} catch (XPathExpressionException e) {
			return path ;
		}
	}
}