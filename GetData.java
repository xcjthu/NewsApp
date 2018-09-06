import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetData extends Thread{
    private HashMap<String, String> map = new HashMap<>();
    private Spider spider = new Spider();
    private CreatJson creatJson = new CreatJson();
    public static String[] kindNews = {"国内新闻", "国际新闻", "经济新闻", "体育新闻", "台湾新闻", "教育新闻", "游戏新闻"};
    static public Map<String, Integer> messageType = new HashMap<>();

    static public String jsonstring;

    public GetData(){
        map.put("国内新闻", "http://www.people.com.cn/rss/politics.xml");
        map.put("国际新闻", "http://www.people.com.cn/rss/world.xml");
        map.put("经济新闻", "http://www.people.com.cn/rss/finance.xml");
        map.put("体育新闻", "http://www.people.com.cn/rss/sports.xml");
        map.put("台湾新闻", "http://www.people.com.cn/rss/haixia.xml");
        map.put("教育新闻", "http://www.people.com.cn/rss/edu.xml");
        // map.put("强国论坛", "http://www.people.com.cn/rss/bbs.xml");
        map.put("游戏新闻", "http://www.people.com.cn/rss/game.xml");
        for (int i = 0; i < kindNews.length; ++ i){
            messageType.put(kindNews[i], i);
        }
        // map.put("中文新闻", "http://www.people.com.cn/rss/opml.xml");
    }

    public void run(){
        while (true){
            for (int i = 0; i < kindNews.length; i++) {
                List tmp = oneKind(kindNews[i]);
                SqlDataBase.getInstance().insert(tmp, kindNews[i]);
            }
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Item> oneKind(String kind){
        String url = map.get(kind);
        String xml = spider.crawl(url);
        if (xml != null){
            List<Item> list = spider.parseXml(xml);
            return list;
            // return creatJson.toJsonArray(list).toString();
        }
        return null;
    }

    public String forNewData(String kind){
        if (kind == "all"){
            JSONArray ans = new JSONArray();
            for (int i = 0; i < kindNews.length; i++) {
                List<Item> tmp = oneKind(kindNews[i]);
                ans.put(creatJson.toJsonArray(tmp));
            }
            return ans.toString();
        }
        else{
            return creatJson.toJsonArray(oneKind(kind)).toString();
        }

    }
}

