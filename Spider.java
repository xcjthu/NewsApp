import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spider {
    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private DocumentBuilder db;
    private String pattern = "<item>\\s+<title><(.*?)></title>\\s+<link>(.*?)</link>\\s+<pubDate>(.*?)</pubDate>\\s+<author>(.*?)</author>\\s+<description><(.*?)></description>\\s+</item>";
    private Pattern p;


    public Spider(){
        p = Pattern.compile(pattern);
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String crawl(String url){
        /** crawl news, if unsuccessful, return null, else return the string
         * */
        try {
            URL cs;
            cs = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(cs.openStream()));
            String ans = "";
            String readline = null;
            while ((readline = reader.readLine()) != null){
                ans += readline;
            }
            reader.close();
            return ans;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Item> parseXml(String xmlString){

        Matcher m = p.matcher(xmlString);
        List<Item> ans = new ArrayList<>();

        while (m.find()){
            Item tmp = new Item();
            tmp.addAttrs("title", m.group(1));
            tmp.addAttrs("link", m.group(2));
            ans.add(tmp);
        }
        return ans;
        /*
        try {

            Document doc = db.parse(new ByteArrayInputStream(xmlString.getBytes()));
            NodeList nodeList = doc.getElementsByTagName("item");
            List<Item> ans = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); ++ i){

                Node item = nodeList.item(i);
                NodeList attrs = item.getChildNodes();

                // System.out.println(item.getNodeValue());
                // NamedNodeMap attrs = item.getAttributes();
                Item tmp = new Item();

                for (int j = 0; j < attrs.getLength(); ++ j){
                    Node attr = attrs.item(j);
                    System.out.println(attr.getNodeName() + "  " + attr.getNodeValue() + " end");
                    tmp.addAttrs(attr.getNodeName(), attr.getNodeValue());
                }
                System.out.println();
                ans.add(tmp);
            }
            return ans;
        } catch (SAXException e) {
            System.out.println("parse xml error");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("parse xml error");
            e.printStackTrace();
        }
        return null;
        */

    }
}
