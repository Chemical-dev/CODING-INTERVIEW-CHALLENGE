package seerbit.seerbit_coding_interview_challenge.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seerbit.seerbit_coding_interview_challenge.requests.TransactionRequest;
import seerbit.seerbit_coding_interview_challenge.responses.TransactionStatistics;
import seerbit.seerbit_coding_interview_challenge.services.TransactionService;

@NoArgsConstructor
@Setter
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService; // Inject the TransactionService

    @PostMapping("/transactions")
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionRequest request) {
        return transactionService.processTransaction(request);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionStatistics> getTransactionStatistics() {
        TransactionStatistics statistics = transactionService.getTransactionStatistics();
        return ResponseEntity.ok(statistics);
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<Void> deleteAllTransactions() {
        return transactionService.deleteAllTransactions();
    }
}
