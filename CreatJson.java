import org.json.JSONArray;

import java.util.List;

public class CreatJson {
    public JSONArray toJsonArray(List<Item> itemList){
        JSONArray ans = new JSONArray();
        for (int i = 0; i < itemList.size(); ++ i){
            ans.put(itemList.get(i).toJsonObject());
        }
        // System.out.println(ans.toString());
        return ans;
    }
}
