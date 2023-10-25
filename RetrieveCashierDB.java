package COMP603_ProjectGroup13_DB;

import COMP603_ProjectGroup13.Product;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RetrieveCashierDB {
    
    private final CashierDBManager dbManager;
    private final Connection conn;
    private Statement statement;
    private DecimalFormat df = new DecimalFormat("#.00");
    
    public RetrieveCashierDB() {
        dbManager = new CashierDBManager();
        conn = dbManager.getCashierDBConnection();
    }
        
    //This method will return a sum of bills, based on each specific shift_id
    public double getBillOrderPerfShift(int shift_id) {
    
        Double totalBill = 0.0;    
        //Construct sql query which to sum up bill column, based on a shift_id
        String sql = "SELECT SUM(bill) AS total_bill FROM BILL_ORDER WHERE shift_id = " + shift_id;    
        try {            
            ResultSet rs = dbManager.queryCahierDB(sql);
            if (rs.next())
                totalBill = Double.parseDouble(df.format(rs.getDouble("total_bill")));                        
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return totalBill;
    }
    
    //Retrieve product list
    public List<Product> RetrieveProductList() {
        List<Product> productList = new ArrayList<>();
        ResultSet rs = dbManager.queryCahierDB("SELECT * FROM PRODUCT");
        
        try {
            while (rs.next()) {
                String item_id = rs.getString("item_id");
                String item = rs.getString("item");
                Double item_price = rs.getDouble("item_price");
                String category = rs.getString("category");
                //Test these products can print out                
//                System.out.println("productID: "+item_id+" item: "+item+" itemPrice: "+item_price
//                                            +"cagetory: "+category); //Testing
                Product product = new Product(item_id, item, item_price, category);
                productList.add(product);
            }
//            System.out.println("Test retrieveProductList succeed!");
        } catch (SQLException ex) {
            Logger.getLogger(RetrieveCashierDB.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return productList;
    }
    
    public ResultSet retrieveProduct(int shiftID) {
        ResultSet rs = null;
        
        return rs;
    }
    
    //Give product id from a list
    public Product retrieveProductWithID(String itemID) {
        Product product = new Product();
        ResultSet rs = dbManager.queryCahierDB("SELECT * FROM PRODUCT WHERE item_id = '" + itemID + "'");
        
        if (rs == null)
            return null;
        
        try {
            while (rs.next()) {
               product.setItem_id(rs.getString("item_id"));
               product.setItem(rs.getString("item"));
               product.setItemPrice(rs.getDouble("item_price"));
               product.setCategory(rs.getString("category"));               
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return product;
    }
    
    public static void main(String[] args) {
        RetrieveCashierDB rcDB = new RetrieveCashierDB();
        Double sum_bill = rcDB.getBillOrderPerfShift(1);
        System.out.println(sum_bill);
        RetrieveCashierDB retrieveDB = new RetrieveCashierDB();
        System.out.println(retrieveDB.retrieveProductWithID("PI001"));
        List<Product> pLists = rcDB.RetrieveProductList();        
    }
    
}