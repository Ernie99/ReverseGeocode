package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    //http://nominatim.openstreetmap.org/reverse?format=xml&lat=52.5487429714954&lon=-1.81602098644987&zoom=18&addressdetails=1
    public static double testLat = 52.5487429714954;
    public static double testLon = -1.181602098644987;

    public static double dallasLat = 32.7758;
    public static double dallasLon = -96.7967; // negative
    //http://nominatim.openstreetmap.org/reverse?format=xml&lat=32.7758&lon=-96.7967&zoom=18&addressdetails=1

    static final String KEY_CITY = "city"; // parent node
    static final String KEY_CNTY = "county";
    static final String KEY_STAT = "state";
    static final String KEY_ZIPC = "postcode";

    static final String KEY_ITEM = "reversegeocode";
    static int trys = 0;

    public static void main(String[] args) {

        for (int i = 0; 1 < 10; i++){
            doOnce(dallasLat,dallasLon);
        }

    }

    public static void doOnce(double lat, double lon){
        String s = getXML(getURL(lat,lon));
//        System.out.println(s);
        // write your code here
        Document doc = getDomElement(s); // getting DOM element

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            String city = getValue(e, KEY_CITY); // name child value
            String county = getValue(e, KEY_CNTY); // cost child value
            String state = getValue(e, KEY_STAT); // description child value
            String zip = getValue(e, KEY_ZIPC); // description child value


            System.out.println("********** "+"try: " + trys + " **********");
            System.out.println("city: " + city);
            System.out.println("county: " + county);
            System.out.println("state: " + state);
            System.out.println("zip: " + zip);
            System.out.println("**************************************");
            trys++;
        }

    }

    static String getURL(double lat, double lon){
        String s = "http://nominatim.openstreetmap.org/reverse?format=xml&lat="+lat+"&lon="+lon+"&zoom=18&addressdetails=1";
        return s;
    }

    static String getXML(String url){
        URL mUrl = null;
        try{
            mUrl = new URL(url);

        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        HttpURLConnection conn = null;

        try {

            conn = (HttpURLConnection) mUrl.openConnection();
//            conn.setRequestMethod("POST");
            Map<String, List<String>> requestsMap = conn.getRequestProperties();
            Set<String> requestKeys = requestsMap.keySet();
            for (String k : requestKeys){
//                System.out.println("RequestKey: " + k + "RequestValue: " + requestsMap.get(k));
            }


            Map<String, List<String>> hdrs = conn.getHeaderFields();
            int responseCode = conn.getResponseCode();
            System.out.println("code: ------ " + responseCode);
            Set<String> hdrKeys = hdrs.keySet();
            System.out.println("----------------------- from server");
            for (String k : hdrKeys){
//                System.out.println("Key: " + k + " Value: " + hdrs.get(k));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();


            conn.disconnect();
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void printXML(String s){
        return;
    }

    public static String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
    }

    public static final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public static Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } catch (SAXException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    static void addCsvLine(String fname, ArrayList<String> values){

    }


}
