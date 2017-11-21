
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sgml.XML;

public class WebXML extends XML {
	/**
	 * プライベートメンバー変数
	 * web.xmlファイル
	 */
	private File webXml;
	/**
	 * プライベートメンバー変数
	 * rootエレメント
	 */
	private Element root;
	/**
	 * コンストラクター
	 * rootエレメントに属性を設定します
	 */
	public WebXML(){
		super();
		root = createDocumentElement("web-app", "utf-8");
		root.setAttribute("xmlns", "http://xmlns.jcp.org/xml/ns/javaee");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd");
		root.setAttribute("version", "3.1");
		root.setAttribute("metadata-complete", "true");
	}
	/**
	 * 公開フォルダーにWEB-INFフォルダーを作成し，web.xmlを想定します．
	 * @param serveFolder 公開フォルダー
	 */
	public void setServeFolder(File serveFolder){
		File parentFolder = new File(serveFolder, "WEB-INF");
		parentFolder.mkdir();
		webXml = new File(parentFolder, "web.xml");
	}
	/**
	 * web.xmlを実際に書き込みます．
	 */
	public void write(){
		write(webXml.getAbsolutePath());
	}
	/**
	 * ロールリスト
	 */
	ArrayList <String> roles = new ArrayList <String>();
	/**
	 * 新しくロールを設定します
	 * @param rolStr
	 */
	public void appendRole(String rolStr){
		roles.add(rolStr);
	}
	public void setRoles(ArrayList <String> roles){
		this.roles = roles;
	}
	HashMap <String, String> contextMap = new HashMap <String, String>();
	public void addContext(String key, String value){
		contextMap.put(key, value);
	}
	void make(){
		Arrays.asList("context-param","description", "display-name", "icon", "distributable", "context-param", "filter", "filter-mapping", "listener", "servlet", "servlet-mapping", "session-config", "mime-mapping", "welcome-file-list", "error-page", "jsp-config", "security-constraint", "login-config", "security-role", "env-entry", "ejb-ref", "ejb-local-ref", "service-ref", "resource-ref", "resource-env-ref", "message-destination-ref", "message-destination", "locale-encoding-mapping-list").forEach(e->{
			if(e.equals("error-page")){
				Arrays.asList("401","403","404","500","505").forEach(e1->{
					Node temp = root.appendChild(createNode(e,""));
					temp.appendChild(createNode("error-code", e1));
//					temp.appendChild(createNode("exception-type", ""));
					temp.appendChild(createNode("location", "/"+e1+".html"));
					new HTMLFile(webXml.getParentFile().getParentFile().getAbsolutePath()+"/"+e1+".html");
				});
			}else if(e.equals("context-param")){
				if(contextMap.size()>0){
					Node temp = root.appendChild(createNode(e,""));
					contextMap.forEach((s1,s2)->{
						temp.appendChild(createNode("param-name", s1));
						temp.appendChild(createNode("param-value", s2));
					});
				}
			}else if(e.equals("security-constraint")){
				roles.forEach(e1->{
					Node temp = root.appendChild(createNode(e,""));
//					temp.appendChild(createNode("display-name", ""));
					Node wk = temp.appendChild(createNode("web-resource-collection", ""));
					wk.appendChild(createNode("web-resource-name", e1));
					wk.appendChild(createNode("url-pattern", "/"+e1+"/*"));
					Node wk2 = temp.appendChild(createNode("auth-constraint", ""));
					wk2.appendChild(createNode("role-name", e1));
				});
			}else if(e.equals("login-config")){
				if(!roles.isEmpty()){
					Node temp = root.appendChild(createNode(e,""));
					temp.appendChild(createNode("auth-method", "FORM"));
//					temp.appendChild(createNode("realm-name", ""));
					Node wk = temp.appendChild(createNode("form-login-config", ""));
					wk.appendChild(createNode("form-login-page", "/login.html"));
					wk.appendChild(createNode("form-error-pagee", "/error.html"));
					new LoginFile(webXml.getParentFile().getParentFile().getAbsolutePath()+"/login.html");
					new LogoutFile(webXml.getParentFile().getParentFile().getAbsolutePath()+"/logout.jsp");
					new HTMLFile(webXml.getParentFile().getParentFile().getAbsolutePath()+"/error.html");
				}
			}else if(e.equals("security-role")){
				roles.forEach(e1->{
					Node temp = root.appendChild(createNode(e,""));
//					temp.appendChild(createNode("description", ""));
					temp.appendChild(createNode("realm-name", e1));
				});
			}
		});
	}
}
class LogoutFile{
	LogoutFile(String path){
		try {
			File file = new File(path);
			FileWriter wr = new FileWriter(file);
			wr.write("<%\n");
			wr.write("\tsession.invalidate();\n");
			wr.write("\tresponse.sendRedirect(\"/index.html\");\n");
			wr.write("%>\n");
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class LoginFile extends HTMLFile{
	LoginFile(String path){
		super(path);
	}
	void make(){
		Element form = (Element) root.appendChild(createNode("FORM",""));
		form.setAttribute("method", "post");
		form.setAttribute("action", "j_security_check");
		Element idField = (Element) form.appendChild(createNode("input",""));
		idField.setAttribute("name", "j_username");
		idField.setAttribute("placeholder", "username");
		Element passField = (Element) form.appendChild(createNode("input",""));
		passField.setAttribute("name", "j_password");
		passField.setAttribute("placeholder", "password");
		Element submitButton = (Element) form.appendChild(createNode("input",""));
		submitButton.setAttribute("name", "submit");
		submitButton.setAttribute("type", "submit");
		submitButton.setAttribute("value", "Login");
		
	}
}
class HTMLFile extends XML{
	File file;
	Element root;
	HTMLFile(String path){
		super();
		file = new File(path);
		root = createDocumentElement("HTML", "utf-8");
		make();
		write(path);
	}
	void make(){
		Node temp = root.appendChild(createNode("H1",file.getName()));
	}
}