package COMP603_ProjectGroup13;


public class CashierDBRun {
    
    
    public static void main(String[] args) {
        CashierDBStorage cashierStorage = new CashierDBStorage();
        cashierStorage.createProductDB();
        cashierStorage.createStaffDB();
    }
    
}
