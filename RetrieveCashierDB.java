package COMP603_ProjectGroup13_DB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RetrieveCashierDB {
    private final CashierDBManager dbManager;
    private final Connection conn;
    private Statement statement;
    
    public RetrieveCashierDB() {
        dbManager = new CashierDBManager();
        conn = dbManager.getCashierDBConnection();                
    }
    
 

    
    public static void main(String[] args) {
        RetrieveCashierDB rcDB = new RetrieveCashierDB();
        
    }
    
}
