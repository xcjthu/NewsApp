import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        GetData getData = new GetData();
        // System.out.println();
        GetData.jsonstring = getData.forNewData("all");
        getData.start();
        try {
            int num = 0;
            ServerSocket serverSocket = new ServerSocket(8888);
            while(true){
                System.out.println("waiting for client...");
                Socket socket = serverSocket.accept();
                String ip = socket.getInetAddress().getHostAddress();
                System.out.println(ip);
                new Server(socket, num).start();
                num += 1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
