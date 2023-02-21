package expenseReport;

import java.math.BigDecimal;
import java.util.Date;



public class Transaction {
    private Date date;
    private BigDecimal amount;
    private String category;

    public Transaction(Date date, BigDecimal amount, String category) {
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}
