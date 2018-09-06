import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Server extends Thread{
    // protected DatagramSocket socket = null;
    Socket socket;
    CreatJson creatJson = new CreatJson();
    int num = 0;
    String jsonData;


    public Server(Socket socket, int num){
        super();

        this.socket = socket;
        this.num = num;
        jsonData = GetData.jsonstring;
        // jsonData = "[[{\"title\": \"侠客岛：中国为什么要投资非洲？\", \"url\": \"https://new.qq.com/omn/20180903/20180903A1Z1BM.html\"}], [{\"title\": \"侠客岛：中国为什么要投资非洲？\", \"url\": \"https://new.qq.com/omn/20180903/20180903A1Z1BM.html\"}], [{\"title\": \"侠客岛：中国为什么要投资非洲？\", \"url\": \"https://new.qq.com/omn/20180903/20180903A1Z1BM.html\"}], [{\"title\": \"侠客岛：中国为什么要投资非洲？\", \"url\": \"https://new.qq.com/omn/20180903/20180903A1Z1BM.html\"}], [{\"title\": \"侠客岛：中国为什么要投资非洲？\", \"url\": \"https://new.qq.com/omn/20180903/20180903A1Z1BM.html\"}], [{\"title\": \"侠客岛：中国为什么要投资非洲？\", \"url\": \"https://new.qq.com/omn/20180903/20180903A1Z1BM.html\"}], [{\"title\": \"侠客岛：中国为什么要投资非洲？\", \"url\": \"https://new.qq.com/omn/20180903/20180903A1Z1BM.html\"}]]";
    }
    public void run(){

        try {
            String line;
            // BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            InputStream is = socket.getInputStream();

            byte[] buf = new byte[1024];
            int len = 0;
            String request = "";
            while ((len = is.read(buf)) != -1){

                request = new String(buf, 0, len);
                System.out.println("request: " + request);
                JSONObject message = new JSONObject(request);
                String type = (String) message.get("type");
                int id = message.getInt("id");

                switch (type){
                    case "all":
                        JSONArray ans = new JSONArray();
                        for (int i = 0; i < GetData.kindNews.length; ++ i){
                            String kind = GetData.kindNews[i];
                            JSONArray tmp = creatJson.toJsonArray(SqlDataBase.getInstance().getDateFromDataBase(kind, id));
                            ans.put(tmp);
                        }
                        os.print(0 + ans.toString() + "$");
                        System.out.println("send message " + 0 + ans.toString() + "$");
                        os.flush();
                        break;
                    default:
                        os.print("1" + GetData.messageType.get(type) + creatJson.toJsonArray(SqlDataBase.getInstance().getDateFromDataBase(type, id)).toString() + "$");
                        os.flush();
                        break;
                }
                /*
                if (request.equals("moreData")){
                    os.print(jsonData);
                    os.flush();
                    System.out.println("sendDataToClient");
                }
                */
            }
            is.close();
            os.close();
            socket.close();
            /*
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) {
                socket.receive(packet);
                String message = new String(buf);
                if (message.equals("moreData")){
                    System.out.println("moreData");
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    byte[] tmp = jsonData.getBytes();
                    socket.send(new DatagramPacket(tmp, tmp.length, address, port));
                }
            }
            */
        } catch (IOException e) {
            System.out.println("Fail");
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
