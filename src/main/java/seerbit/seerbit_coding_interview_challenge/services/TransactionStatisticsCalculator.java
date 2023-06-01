package seerbit.seerbit_coding_interview_challenge.services;

import lombok.Getter;
import lombok.Setter;
import seerbit.seerbit_coding_interview_challenge.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Setter
@Getter
public class TransactionStatisticsCalculator {

    private BigDecimal sum;
    private BigDecimal max;
    private BigDecimal min;
    private long count;

    public TransactionStatisticsCalculator() {
        this.sum = BigDecimal.ZERO;
        this.max = BigDecimal.ZERO;
        this.min = BigDecimal.ZERO;
        this.count = 0;
    }

    public synchronized void addTransaction(Transaction transaction) {
        LocalDateTime currentTimestamp = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime thirtySecondsAgo = currentTimestamp.minusSeconds(30);
        LocalDateTime transactionTimestamp = transaction.getTimestamp();
        BigDecimal amount = transaction.getAmount();

        if (transactionTimestamp == null || amount == null || transactionTimestamp.isBefore(thirtySecondsAgo) || transactionTimestamp.isAfter(currentTimestamp)) {
            // Ignore invalid or outdated transactions
            return;
        }

        sum = sum.add(amount);
        max = max.max(amount);
        min = (min.compareTo(BigDecimal.ZERO) == 0) ? amount : min.min(amount);
        count++;
    }
}

