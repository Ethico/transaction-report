package expenseReport;

import java.util.*;

public class TransactionClient {

    public static void main(String[] args) {
        TransactionReportService service = new TransactionReportService();
        List<Transaction> transactions = service.populateTransactions("transactions.txt",",");
        System.out.println("Monthly income : "+service.getMonthlyIncome(transactions));
        System.out.println("Monthly expenses : "+service.getMonthlyExpenses(transactions));
        System.out.println("Total Monthly savings : "+service.getMonthlySavings(transactions));
        System.out.println("Top Expense category : "+service.topExpenseCategory(transactions));
    }
}
