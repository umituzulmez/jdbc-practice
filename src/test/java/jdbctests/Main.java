package jdbctests;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {

        //connection string
        String dbUrl = "jdbc:oracle:thin:@18.207.248.211:1521:xe";
        String dbUsername = "hr";
        String dbPassword = "hr";

        //create connection to database
        Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);

        //create statement object
        Statement statement = connection.createStatement();

        //run query and get the result in result object
        ResultSet resultSet = statement.executeQuery("select * from employees");

        //move pointer to next row
        //resultSet.next();
        //System.out.println(resultSet.getString("region_name"));

       while(resultSet.next()){
           //System.out.println(resultSet.getInt("region_id") + "-" + resultSet.getString("region_name"));
           System.out.println(resultSet.getInt(1) + "-" + resultSet.getString(2));
       }

        //close connection
        resultSet.close();
        statement.close();
        connection.close();
    }
}
