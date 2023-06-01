package seerbit.seerbit_coding_interview_challenge.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import seerbit.seerbit_coding_interview_challenge.model.Transaction;
import seerbit.seerbit_coding_interview_challenge.requests.TransactionRequest;
import seerbit.seerbit_coding_interview_challenge.responses.TransactionStatistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class TransactionService {
    private TransactionStatisticsCalculator transactionStatisticsCalculator = new TransactionStatisticsCalculator();
    public synchronized ResponseEntity<Void> processTransaction(TransactionRequest request) {
        if (request == null || request.getAmount() == null || request.getTimestamp() == null) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal amount;
        LocalDateTime timestamp;

        try {
            amount = new BigDecimal(request.getAmount());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            timestamp = LocalDateTime.parse(request.getTimestamp(), formatter);

            LocalDateTime currentTimestamp = LocalDateTime.now(ZoneOffset.UTC);
            if (timestamp.isAfter(currentTimestamp)) {
                return ResponseEntity.unprocessableEntity().build();
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            return ResponseEntity.unprocessableEntity().build();
        }

        LocalDateTime currentTimestamp = LocalDateTime.now(ZoneOffset.UTC);
        System.out.println(currentTimestamp);
        if (timestamp.isBefore(currentTimestamp.minusSeconds(30))) {
            System.out.println(timestamp.isBefore(currentTimestamp.minusSeconds(60)));
            return ResponseEntity.noContent().build();
        }

        Transaction transaction = new Transaction(amount, timestamp);
        transactionStatisticsCalculator.addTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    public synchronized TransactionStatistics getTransactionStatistics() {
            BigDecimal sum = transactionStatisticsCalculator.getSum();
            BigDecimal max = transactionStatisticsCalculator.getMax();
            BigDecimal min = transactionStatisticsCalculator.getMin();
            long count = transactionStatisticsCalculator.getCount();

            BigDecimal average = (count == 0) ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
            return new TransactionStatistics(sum, average, max, min, count);
    }
    public synchronized ResponseEntity<Void> deleteAllTransactions() {
        try {
            transactionStatisticsCalculator = new TransactionStatisticsCalculator();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}










