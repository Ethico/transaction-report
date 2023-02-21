package expenseReport;

public class TransactionReportException extends RuntimeException{
    public TransactionReportException(String message) {
        super(message);
    }

    public TransactionReportException(Throwable cause) {
        super(cause);
    }
}
