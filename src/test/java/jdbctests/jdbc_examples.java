package jdbctests;

import org.testng.annotations.Test;

import java.sql.*;

public class jdbc_examples {

    //connection string
    String dbUrl = "jdbc:oracle:thin:@18.207.248.211:1521:xe";
    String dbUsername = "hr";
    String dbPassword = "hr";

    @Test
    public void test1() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from employees");

        while(resultSet.next()){
            String regionName = resultSet.getString("email");
            System.out.println(regionName);
        }

        //using same object to run another query
        //================
        resultSet = statement.executeQuery("Select * from countries");
        while(resultSet.next()){
            System.out.println(resultSet.getString(2));
        }

        //close connection
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void test2() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from departments");

        //how to find how many rows I have for the query
        //go to last row
        resultSet.last();

        // get the row count
        int rowCount = resultSet.getRow();

        System.out.println("rowCount = " + rowCount);

        System.out.println(resultSet.getString("department_name"));

        //we need to move before the first row to get full list since we are at the last row right now
        resultSet.beforeFirst();
        while (resultSet.next()){
            System.out.println(resultSet.getString("department_name"));
        }
        
        //close connection
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void metadata() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from departments");

        //get the database related data inside the dbMetadata object
        DatabaseMetaData dbMetadata = connection.getMetaData();

        System.out.println("User: " + dbMetadata.getUserName());
        System.out.println("Database product name: "+ dbMetadata.getDatabaseProductName());
        System.out.println("Database product version: "+ dbMetadata.getDatabaseProductVersion());
        System.out.println("Driver: "+ dbMetadata.getDriverName());
        System.out.println("Driver version: "+ dbMetadata.getDriverVersion());

        //get the resultset object metadata
        ResultSetMetaData rsMetadata = resultSet.getMetaData();

        //how many column we have ?
        System.out.println("Column count:"+rsMetadata.getColumnCount());

        //column names
        //System.out.println(rsMetadata.getColumnName(8));

        //print all the column names dynamically
        int columnCount = rsMetadata.getColumnCount();
        for (int i = 1; i <=columnCount; i++) {
            System.out.println(rsMetadata.getColumnName(i));
        }

        //close connection
        resultSet.close();
        statement.close();
        connection.close();
    }




}
