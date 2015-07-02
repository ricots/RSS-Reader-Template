package com.ihostingbd.rssreadertemplate;

import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Osman Goni Nahid on 02-Jul-15.
 */
public class RSSReaderClient {

    DefaultHttpClient httpClient = new DefaultHttpClient();
    public Document getRSSFromServer(String url){
        Document document = null;
        document = getDomFromXMLString(getFeedFromServer(url));
        return document;
    }

    private String getFeedFromServer(String url){
        String xml = null;
        if(android.os.Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try{
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return xml;
    }
    private Document getDomFromXMLString (String xml){
        Document document = null;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            document=documentBuilder.parse(inputSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return document;
    }
    public String getValue(Element item, String key){
        NodeList nodeList = item.getElementsByTagName(key);
        return this.getElementValue(nodeList.item(0));
    }
    public final String getElementValue(Node node){
        Node childNode ;
        if(node != null){
            if(node.hasChildNodes()){
                for(childNode=node.getFirstChild();childNode!=null;childNode=childNode.getNextSibling()){
                    if(childNode.getNodeType()==Node.TEXT_NODE){
                        return childNode.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}
