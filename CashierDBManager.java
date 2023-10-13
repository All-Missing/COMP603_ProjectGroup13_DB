package COMP603_ProjectGroup13;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public final class CashierDBManager {
    
    private static final String USER_NAME = "allmissing";
    private static final String PASSWORD = "pdc";
    private static final String URL = "jdbc:derby://localhost:1527/CashierDB;";
//    private static final String URL = "jdbc:derby:CashierDB_Ebd; create=true";
    
    Connection conn;
    
    public CashierDBManager()
    {
        establishCashierDBConnection();
    }
    
    public static void main(String[] args)
    {
        CashierDBManager cashierDBManager = new CashierDBManager();
        System.out.println(cashierDBManager.getCashierDBConnection());
    }
    
    public Connection getCashierDBConnection() {
        return this.conn;
    }
    
    public void establishCashierDBConnection()
    {
        if (this.conn == null)
        {   try
            {  conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                System.out.println(URL+"connected...");
                System.out.println("CashierDatabase connected successfully\n");
            }
            catch (SQLException e)
            {   System.out.println(e.getMessage());
            }            
        }        
    }
    
    public void closeCashierDBConnection()
    {   if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }     
    }    
    
     public ResultSet queryCahierDB(String sql) {

        Connection connection = this.conn;
        java.sql.Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultSet;
    }
     
     public void updateCashierDB(String sql)
     {
        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
     }
}
