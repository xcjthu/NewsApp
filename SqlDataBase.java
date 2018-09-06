import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlDataBase {
    int num = 0;
    private Connection connection = null;
    private Statement statement;
    static SqlDataBase sqlDataBase = null;

    private boolean isused = false;

    public static SqlDataBase getInstance(){
        if (sqlDataBase == null){
            sqlDataBase = new SqlDataBase();
        }
        return sqlDataBase;
    }

    private SqlDataBase(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            connection.setAutoCommit(false);
            System.out.println("Open database successfully");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*CREATE TABLE news(title text not null, url text not null, author text, pubdate date not null, description text);*/
    /*CREATE UNIQUE INDEX onlytitle on news (title, pubdate);*/
    private void insert(Item item, String kind){

        // String sql = "insert into news (title, url, author, pubdate, description) values (%s, %s, %s, %s, %s)" % (item.getTitle(), item.getUrl(), item.getAuthor(), item.getPubDate(), );
        // String sql = "insert into news (title, url, author, pubdate, description, kind, id) values (" + item.getTitle() + ", " + item.getUrl() + ", " + item.getAuthor() + ", " + item.getPubDate() + ", " + item.getDescription() + ");";
        String sql = item.getInsertSql(kind, num);
        try {
            //System.out.println(sql);
            statement.execute(sql);

            num += 1;
        } catch (SQLException e) {
            System.out.println("insert error");
            e.printStackTrace();
        }
    }

    private void lock(){
        while (isused){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(List<Item> itemList, String kind){
        lock();
        isused = true;

        try {
            statement = connection.createStatement();
            for (int i = 0; i < itemList.size(); i++) {
                insert(itemList.get(i), kind);
            }
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        isused = false;
    }

    public List<Item> getDateFromDataBase(String kind, int id){

        lock();
        isused = true;
        try {
            List<Item> ans = new ArrayList<>();
            statement = connection.createStatement();
            String sql = "select * from news where kind = \"" + kind + "\" and id > " + id + " order by id desc";
            ResultSet resultSet = statement.executeQuery(sql);
            int num = 0;
            while (resultSet.next()){
                num += 1;
                Item tmp = new Item(resultSet.getString("title"), resultSet.getString("url"), resultSet.getInt("id"));
                // System.out.println(tmp.getTitle());
                ans.add(tmp);
                if (num > 20)
                    break;
            }
            statement.close();
            return ans;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            isused = false;
        }
        return null;
    }
}
