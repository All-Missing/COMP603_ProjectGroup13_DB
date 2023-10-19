package COMP603_ProjectGroup13_DB;

import COMP603_ProjectGroup13.ProductList;
import COMP603_ProjectGroup13.Staff_Record;
import COMP603_ProjectGroup13.Product;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class CashierDBStorage {
    
    private final CashierDBManager dbManager;
    private final Connection conn;
    private Statement statement;    
    private final ProductList productList;
    private final Staff_Record staffList;
    
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
            this.statement.addBatch("CREATE TABLE " + tableName + " (item_id VARCHAR(10) PRIMARY KEY, "
                    + "item VARCHAR(50), item_price DOUBLE, category VARCHAR(20))");
            
            //Retrieve product records value and insert data into sql table
            HashMap<String, Product> product_records = productList.getProduct_records();                        
            for (Map.Entry<String, Product> entry: product_records.entrySet()) {                
                String item_id = entry.getKey();
                Product product = entry.getValue();
                this.statement.addBatch("INSERT INTO " + tableName + " VALUES ('"
                        + item_id + "', '"
                        + product.getItem() + "', "
                        + product.getItemPrice() + ", '"
                        + product.getCategory() + "')");                                                         
            }
            this.statement.executeBatch();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }          
    }        
    
    //Create STAFF SQL table
    public void createStaffDB() {
            
        try {
            //connect and initialize db
            this.statement = conn.createStatement();  
            this.checkExistedTable("STAFF");//Check if table exisits. if not, manually create it            
            this.statement.addBatch("CREATE TABLE STAFF (staff_id VARCHAR(10) PRIMARY KEY, staff_name VARCHAR(20))");
            this.statement.executeBatch();
            
//            Retrieve product records value and insert data into sql table
            HashMap<String, String> staff_records = staffList.getStaff_list();
            for (Map.Entry<String, String> entry: staff_records.entrySet()) {                
                String staff_id = entry.getKey();
                String staff_name = entry.getValue();               
                this.statement.addBatch("INSERT INTO STAFF VALUES ('"+ staff_id+"', '"+ staff_name + "')");                         
            }                        
            this.statement.executeBatch();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }          
    }
         
    //Create Bill_Order table
    public void createOrderDB() {
        
        try {
            this.statement = conn.createStatement();
            this.checkExistedTable("BILL_ORDER");
            this.statement.addBatch("CREATE TABLE BILL_ORDER (shift_id INT, staff_id VARCHAR(10),"
                    + " order_id INT, bill DOUBLE)");                       
                        
            BufferedReader br;            
            try {   //Retrieved data.                                
                br = new BufferedReader(new FileReader("./file_records/BillOrder_Records.txt"));
                String line ="";
                String[] lineParts;
                Integer shift_id = 0;
                Integer order_id = 0;
                Double bill = 0.0;
                String staff_id = null;
                
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("---ShiftID: ")) {
                        lineParts = line.split(" ");
                        shift_id = Integer.parseInt(lineParts[1]);
                        staff_id = lineParts[3];
                    }   else if (line.startsWith("OrderID: ")) {
                        lineParts = line.split(" ");
                        order_id = Integer.parseInt(lineParts[1]);
                        bill = Double.parseDouble(lineParts[4]);
                        
                        //Insert data into Bill_Order table
                        this.statement.addBatch("INSERT INTO BILL_ORDER VALUES ("+ shift_id +",'"+staff_id + "', " + order_id + ", " + bill + ")");
                        this.statement.executeBatch();
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }                        
            this.statement.executeBatch();                        
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }        
    }
    
    public void createBalanceDB() {
        
        try {
            this.statement = conn.createStatement();
            this.checkExistedTable("BALANCE");
            this.statement.addBatch("CREATE TABLE BALANCE (shift_id INT PRIMARY KEY, staff_id VARCHAR(10), "
                    + "staff_name VARCHAR(20), total_balance DOUBLE)");            
            RetrieveCashierDB retrieveDB = new RetrieveCashierDB();
            
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader("./file_records/BillOrder_Records.txt"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("---ShiftID: ")) {
                      String[] lineParts = line.split(" ");
                      Integer shift_id = Integer.parseInt(lineParts[1]);
                      String staff_id = lineParts[3];
                      String staff_name = lineParts[5];
                      Double totalBalance = retrieveDB.getBillOrderPerfShift(shift_id);
                      
                      //Insert data into Balance table
                      this.statement.addBatch("INSERT INTO BALANCE VALUES (" + shift_id + ", '"
                              + staff_id + "', '"+ staff_name+"', "+totalBalance+")");
                    }                                  
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }                                  
            this.statement.executeBatch();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    //this method to check if a table is already exisited
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