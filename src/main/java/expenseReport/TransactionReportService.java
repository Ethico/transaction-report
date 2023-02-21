package expenseReport;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionReportService {

    public static final String DD_MM_YYYY = "dd/mm/yyyy";

    public List<Transaction> populateTransactions(String fileName,String seperator) {
        List<Transaction> transactionsList = new ArrayList<>();
        URL input = ClassLoader.getSystemResource(fileName);
        if(input == null) {
            throw new TransactionReportException("fileName is incorrect or file not found");
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             new File(input.getFile()).toPath());) {

            List<String> lines = reader.lines().collect(Collectors.toList());


            for (String line : lines) {
                String[] transactionLine = line.split(seperator);
                Transaction transaction = new Transaction(new SimpleDateFormat(DD_MM_YYYY).parse(transactionLine[0]),
                        new BigDecimal(transactionLine[1]),
                        transactionLine[2]);
                transactionsList.add(transaction);
            }


        } catch (IOException e) {
            throw new TransactionReportException(e.getCause());
        } catch (ParseException e) {
            throw new TransactionReportException(e.getCause());
        }

        return transactionsList;
    }

    public BigDecimal getMonthlyIncome(List<Transaction> transactionsList) {
        BigDecimal monthlyIncome = transactionsList.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .map(t -> t.getAmount())
                .reduce((a1, a2) -> a1.add(a2))
                .orElse(new BigDecimal(0));

        return monthlyIncome;
    }

    public BigDecimal getMonthlyExpenses(List<Transaction> transactionsList) {
        BigDecimal monthlyExpenses = transactionsList.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(t -> t.getAmount())
                .reduce((a1, a2) -> a1.add(a2))
                .orElse(new BigDecimal(0));
        return monthlyExpenses;
    }

    public BigDecimal getMonthlySavings(List<Transaction> transactionsList) {
        return getMonthlyExpenses(transactionsList).add(getMonthlyIncome(transactionsList));
    }

    public String topExpenseCategory(List<Transaction> transactionsList) {
        Map<String, BigDecimal> topExpense = new HashMap<>();

        Map<String, BigDecimal> transactionsByExpenses = transactionsList.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.groupingBy(Transaction::getCategory,
                                               Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
       return transactionsByExpenses.entrySet() .stream()
                                                .max((v1, v2) -> v1.getValue().abs().compareTo(v1.getValue().abs()))
                                                .get().getKey();
    }
}
