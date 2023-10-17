package COMP603_ProjectGroup13_DB;


public class CashierDBRun {
    
    
    public static void main(String[] args) {
        CashierDBStorage cashierStorage = new CashierDBStorage();
        cashierStorage.createProductDB();
        cashierStorage.createStaffDB();        
        cashierStorage.createOrderDB();
    }
    
}
