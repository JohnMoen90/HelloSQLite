import java.sql.*;

public class HelloDB {

    public static void main(String[] args) throws SQLException {

        String url = "jdbc:sqlite:hello.sqlite";

        Connection connection = DriverManager.getConnection(url);

        Statement statement = connection.createStatement();

        String createTableSql = "CREATE TABLE IF NOT EXISTS cats (name TEXT, age INTEGER)";
        statement.execute(createTableSql);

        String insertDataSql = "INSERT INTO cats VALUES ('Maru', 10)";
        statement.execute(insertDataSql);

        insertDataSql = "INSERT INTO cats VALUES ('Hello Kitty', 45)";
        statement.execute(insertDataSql);

        insertDataSql = "INSERT INTO cats VALUES ('Grumpy Cat', 5)";
        statement.execute(insertDataSql);

        insertDataSql = "INSERT INTO cats VALUES ('Lil Bub', 6)";
        statement.execute(insertDataSql);

        String getAllCatsSql ="SELECT * FROM cats";
        ResultSet allCats = statement.executeQuery(getAllCatsSql);

        while (allCats.next()) {
            String catName = allCats.getString("name");
            int catAge = allCats.getInt("age");
            System.out.printf("%s is %d years old\n", catName, catAge);
        }

        allCats.close();

//        String dropTableSql = "DROP TABLE cats";
//        statement.execute(dropTableSql);

        statement.close();
        connection.close();

    }

}
