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
        // Check if the request or its properties are null
        if (request == null || request.getAmount() == null || request.getTimestamp() == null) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal amount;
        LocalDateTime timestamp;

        try {
            // Parse the amount and timestamp from the request
            amount = new BigDecimal(request.getAmount());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            timestamp = LocalDateTime.parse(request.getTimestamp(), formatter);

            LocalDateTime currentTimestamp = LocalDateTime.now(ZoneOffset.UTC);
            // Check if the timestamp is in the future
            if (timestamp.isAfter(currentTimestamp)) {
                return ResponseEntity.unprocessableEntity().build();
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            return ResponseEntity.unprocessableEntity().build();
        }

        LocalDateTime currentTimestamp = LocalDateTime.now(ZoneOffset.UTC);
        System.out.println(currentTimestamp);

        // This condition checks if the transaction timestamp is older than 30 seconds
        if (timestamp.isBefore(currentTimestamp.minusSeconds(30))) {
            System.out.println(timestamp.isBefore(currentTimestamp.minusSeconds(60)));
            return ResponseEntity.noContent().build();
        }

        // Create a new transaction and add it to the transaction statistics calculator
        Transaction transaction = new Transaction(amount, timestamp);
        transactionStatisticsCalculator.addTransaction(transaction);

        // Return a success response with the created status
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    public synchronized TransactionStatistics getTransactionStatistics() {
        // Get the required statistics from the transaction statistics calculator
        BigDecimal sum = transactionStatisticsCalculator.getSum();
        BigDecimal max = transactionStatisticsCalculator.getMax();
        BigDecimal min = transactionStatisticsCalculator.getMin();
        long count = transactionStatisticsCalculator.getCount();

        // Calculate the average if there are transactions
        BigDecimal average = (count == 0) ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);

        // Create a new TransactionStatistics object with the calculated statistics
        return new TransactionStatistics(sum, average, max, min, count);
    }

    public synchronized ResponseEntity<Void> deleteAllTransactions() {
        try {
            // Create a new instance of the TransactionStatisticsCalculator to reset the transaction statistics
            transactionStatisticsCalculator = new TransactionStatisticsCalculator();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Return an error response if an exception occurs during the reset operation
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}










