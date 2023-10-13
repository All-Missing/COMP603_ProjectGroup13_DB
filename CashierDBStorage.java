package COMP603_ProjectGroup13;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CashierDBStorage {
    
    private final CashierDBManager dbManager;
    private final Connection conn;
    private Statement statement;    
    private ProductList productList;
    private Staff_Record staffList;
    
    public CashierDBStorage() {
        dbManager = new CashierDBManager();
        conn = dbManager.getCashierDBConnection();        
        productList = new ProductList();
        staffList = new Staff_Record();
    }
    
    //Create PRODUCT SQL table
    public void createProductDB() {
        
        try {
            //connect and initialize db
            statement = conn.createStatement();
        
            String tableName = "PRODUCT";             
            checkExistedTable(tableName); //Check if table exisits. if not, manually create it
            
            String sqlTable = "CREATE TABLE " + tableName + " (ITEM_ID VARCHAR(10), "
                    + "ITEM VARCHAR(50), ITEM_PRICE DOUBLE, CATEGORY VARCHAR(20))";                    
            statement.executeUpdate(sqlTable);
            System.out.println("Table " + tableName +" created");
                       
            //Retrieve product records value and insert data into sql table
            HashMap<String, Product> product_records = productList.getProduct_records();                        
            for (Map.Entry<String, Product> entry: product_records.entrySet()) {
                
                String item_id = entry.getKey();
                Product product = entry.getValue();
                
                String insertQuery = "INSERT INTO " + tableName + " VALUES ('"
                        + item_id + "', '"
                        + product.getItem() + "', "
                        + product.getItemPrice() + ", '"
                        + product.getCategory() + "')";
                         
                statement.executeUpdate(insertQuery);
            }                        
        } catch (SQLException ex) {
            Logger.getLogger(CashierDBStorage.class.getName()).log(Level.SEVERE, null, ex);
        }          
    }        
    
    //Create STAFF SQL table
    public void createStaffDB() {
            
        try {
            //connect and initialize db
            statement = conn.createStatement();
        
            String tableName = "STAFF";             
            checkExistedTable(tableName); //Check if table exisits. if not, manually create it
            
            String sqlTable = "CREATE TABLE " + tableName + " (STAFF_ID VARCHAR(10), STAFF_NAME VARCHAR(20))";
                               
            statement.executeUpdate(sqlTable);
            System.out.println("Table " + tableName +" created");
                       
            //Retrieve product records value and insert data into sql table
            HashMap<String, String> staff_records = staffList.getStaff_list();
            for (Map.Entry<String, String> entry: staff_records.entrySet()) {
                
                String staff_id = entry.getKey();
                String staff_name = entry.getValue();
                
                 String insertQuery = "INSERT INTO " + tableName + " VALUES ('"
                        + staff_id + "', '"                       
                        + staff_name + "')";                         
                statement.executeUpdate(insertQuery);
            }                        
        } catch (SQLException ex) {
            Logger.getLogger(CashierDBStorage.class.getName()).log(Level.SEVERE, null, ex);
        }          
    } 
    
    private void checkExistedTable(String aTableName) {
        
        try {
            DatabaseMetaData dbmd = this.conn.getMetaData();
            String[] types = {"TABLE"};
            statement = this.conn.createStatement();
            ResultSet rs = dbmd.getTables(null, null, null, types);
            
            while (rs.next()) {
                String table_name = rs.getString("TABLE_NAME");
                System.out.println(table_name + " table is existing");
                if (table_name.equalsIgnoreCase(aTableName)) {
                    statement.executeUpdate("Drop table " + aTableName);
                    System.out.println("Table " + aTableName + " is deleted.\n");
                    break;
                }                                
            }
            rs.close();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void closeConnection() {
        this.dbManager.closeCashierDBConnection();
    }
    
}
