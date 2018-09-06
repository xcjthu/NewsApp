import org.json.JSONException;
import org.json.JSONObject;


public class Item {

    private String title;
    private String url;
    private String author;
    private String pubDate;
    private String description;
    private int id;

    public Item(String title, String url, int id){
        this.title = title;
        this.url = url;
        this.id = id;

    }

    public Item(){

    }

    public void addAttrs(String attrName, String attrValue){
        switch (attrName){
            case "title":
                title = attrValue;
                break;
            case "link":
                url = attrValue;
                break;
            case "author":
                author = attrValue;
                break;
            case "pubDate":
                pubDate = attrValue;
                break;
            case "description":
                description = attrValue;
                break;
        }
    }

    public String getPubDate(){
        return pubDate;
    }

    public String getAuthor(){
        return author;
    }
    public String getDescription(){
        return description;
    }

    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return url;
    }

    public String getInsertSql(String kind, int id){
        return "insert into news (title, url, author, pubdate, description, kind, id) values (\"" + title + "\", \"" + url + "\", \"" + author + "\", \"" + pubDate + "\", \"" + description + "\", \"" + kind + "\", " + id + ");";
    }

    public JSONObject toJsonObject() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("url", url);
            jsonObject.put("id", id);
            return jsonObject;
        } catch (JSONException e) {
            System.out.println("Item to Json Object error");
            e.printStackTrace();
            return null;
        }
    }
}

