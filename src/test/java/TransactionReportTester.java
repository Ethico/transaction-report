import expenseReport.Transaction;
import expenseReport.TransactionClient;
import expenseReport.TransactionReportException;
import expenseReport.TransactionReportService;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionReportTester {
    private static List<Transaction> transactions = new ArrayList<>();
    private TransactionReportService reportService;
    @BeforeEach
    public  void setupTransactions() throws ParseException {
        Transaction t1 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("15/02/2023"),new BigDecimal(100), "salary");
        Transaction t2 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("16/02/2023"),new BigDecimal(200), "salary");
        Transaction t3 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("17/02/2023"),new BigDecimal(-100), "foodpanda");
        Transaction t4 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("18/02/2023"),new BigDecimal(-200), "swiggy");
        Transaction t5 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("17/02/2023"),new BigDecimal(-500), "foodpanda");

        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);
        transactions.add(t5);
    }

    @AfterEach
    public  void clearTransactions() throws ParseException {
        transactions.clear();
    }


    @Test
    public void testMonthlyIncomeIfNoIncomeOnlyExpenses() throws ParseException {
        reportService = new TransactionReportService();
        Transaction t3 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("15/02/2023"),new BigDecimal(-100), "foodpanda");
        Transaction t4 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("16/02/2023"),new BigDecimal(-100), "foodpanda");
        transactions.clear();
        transactions.add(t3);
        transactions.add(t4);
        Assertions.assertTrue( reportService.getMonthlyIncome(transactions).compareTo(new BigDecimal(0)) == 0);
    }


    @Test
    public void testMonthlyIncomeIfNoExpensesOnlyIncome() throws ParseException {
        reportService = new TransactionReportService();
        Transaction t3 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("15/02/2023"),new BigDecimal(100), "salary");
        Transaction t4 = new Transaction(new SimpleDateFormat("dd/mm/yyyy").parse("16/02/2023"),new BigDecimal(100), "salary");
        transactions.clear();
        transactions.add(t3);
        transactions.add(t4);
        Assertions.assertTrue( reportService.getMonthlyExpenses(transactions).compareTo(new BigDecimal(0)) == 0);
    }

    @Test
    public void testMonthlyIncome(){
        reportService = new TransactionReportService();
        Assertions.assertTrue( reportService.getMonthlyIncome(transactions).compareTo(new BigDecimal(300)) == 0);
    }

    @Test
    public void testMonthlyExpenses(){
        reportService = new TransactionReportService();
        Assertions.assertTrue( reportService.getMonthlyExpenses(transactions).compareTo(new BigDecimal(-800)) == 0);
    }
    @Test
    public void testMonthlySavings(){
        reportService = new TransactionReportService();
        Assertions.assertTrue( reportService.getMonthlySavings(transactions).compareTo(new BigDecimal(-500)) == 0);
    }

    @Test
    public void testPopulateTransactionsWithIncorrectFilePath(){
        reportService = new TransactionReportService();
        Assertions.assertThrows(TransactionReportException.class , () -> {
            reportService.populateTransactions("incorrectfile.txt",",");
        });
    }

    @Test
    public void testTopExpenseCategory(){
        reportService = new TransactionReportService();
        Assertions.assertEquals(reportService.topExpenseCategory(transactions),"foodpanda");
    }

}
