package br.com.tidicas.android;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * 
 * @author Evaldo Junior
 *
 */
public class RestFulClient {
	
	private static String convertStreamToString(InputStream is) {
    
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	
	    String line = null;
	    try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	      try {
	        is.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	    return sb.toString();
	}
  
	public static List<CadastroTo> returnServico() {
		List<CadastroTo> result = null;
		
        HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		String parametro1 = "C";
		String parametro2 = "D";
		String url = "http://192.168.1.30:8080/test/testejavarest/" + parametro1 + "/" + parametro2;
				
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/xml");
	    httpGet.setHeader("Content-Type", "application/xml");
		String text = null;
		
		try {
			//localContext.setAttribute("parametro1", "A");
			//localContext.setAttribute("parametro2", "B");
			//HttpResponse response = httpClient.execute(httpGet, localContext);				
			HttpResponse response = httpClient.execute(httpGet);				
			HttpEntity entity = response.getEntity();
			text = formatXMLString(getASCIIContentFromEntity(entity));
	        				
			if (text!=null) {
				
				result = getResult(text);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return result;
    }
 
	private static List<CadastroTo> getResult(String results) {
		Set<CadastroTo> cadastroTos = new HashSet<CadastroTo>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		    DocumentBuilder db=dbf.newDocumentBuilder();			    
		    Document doc = db.parse(new InputSource(new StringReader(results)));
		      
			NodeList nl = doc.getDocumentElement().getChildNodes();
			CadastroTo cadastroTo =null;
			  
			
			for( int i=0; i<nl.getLength(); i++ ) {  
				Node node = nl.item(i);
				if (cadastroTo == null){
					cadastroTo = new CadastroTo();
				}
				if (node.getNodeType() == Node.ELEMENT_NODE) {						
			        
					Element element = (Element) node;  
			        
			        if (element.getNodeName().equalsIgnoreCase("cadastroId")){
			        	cadastroTo.setCadastroId(Long.valueOf(element.getTextContent()));
			        }
			        if (element.getNodeName().equalsIgnoreCase("quantidade")){
			        	cadastroTo.setQuantidade(Integer.valueOf(element.getTextContent()));
			        }	
			        
				}   
				if (cadastroTo.getCadastroId()!=null && cadastroTo.getQuantidade()!=null){
					cadastroTos.add( cadastroTo );
				}
				
		    }				
							
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch(Exception e1){
			e1.printStackTrace();
		}
		List<CadastroTo> list= new ArrayList<CadastroTo>();
		list.addAll(cadastroTos);
		
		return list;
	}
	    
	private static String formatXMLString(String xmlString) {		    
	    final String LINE_BREAK = "\n";
	    xmlString = xmlString.replaceAll(LINE_BREAK, "");
	    StringBuffer outXml = new StringBuffer();
	    
	    Pattern pattern = Pattern.compile("(<[^/][^>]+>)?([^<]*)(</[^>]+>)?(<[^/][^>]+/>)?");
	    Matcher matcher = pattern.matcher(xmlString);
	    int tabCount = 0;
	    while (matcher.find()) {
	        String str1 = (null == matcher.group(1) || "null".equals(matcher.group())) ? "" : matcher.group(1);
	        String str2 = (null == matcher.group(2) || "null".equals(matcher.group())) ? "" : matcher.group(2);
	        String str3 = (null == matcher.group(3) || "null".equals(matcher.group())) ? "" : matcher.group(3);
	        String str4 = (null == matcher.group(4) || "null".equals(matcher.group())) ? "" : matcher.group(4);

	        if (matcher.group() != null && !matcher.group().trim().equals("")) {
	            outTabs(tabCount, outXml);
	            if (!str1.equals("") && str3.equals("")) {
	                ++tabCount;
	            }
	            if (str1.equals("") && !str3.equals("")) {
	                --tabCount;
	                outXml.deleteCharAt(outXml.length() - 1);
	            }

	            outXml.append(str1);
	            outXml.append(str2);
	            outXml.append(str3);
	            if (!str4.equals("")) {
	                outXml.append(LINE_BREAK);
	                outTabs(tabCount, outXml);
	                outXml.append(str4);
	            }
	            outXml.append(LINE_BREAK);
	        }
	    }
	    return outXml.toString();
	}

	private static void outTabs(int count, StringBuffer stringBuffer) {
	    for (int i = 0; i < count; i++) {
	        stringBuffer.append("\t");
	    }
	}

	private static String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();	
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);
			
			if (n>0){ 
				out.append(new String(b, 0, n));
			}	
		}
		
		return out.toString();
	}
}