package seerbit.seerbit_coding_interview_challenge.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import seerbit.seerbit_coding_interview_challenge.responses.TransactionStatistics;
import seerbit.seerbit_coding_interview_challenge.services.TransactionService;

class TransactionStatisticTest {

    @Test
    public void getTransactionStatistics_ReturnsOkResponseWithStatistics() {
        // Create a mock TransactionStatistics object
        TransactionStatistics expectedStatistics = new TransactionStatistics();
        // Set expected values for the statistics object
        expectedStatistics.setSum(new BigDecimal("100.00"));
        expectedStatistics.setAverage(new BigDecimal("50.00"));
        expectedStatistics.setMax(new BigDecimal("75.00"));
        expectedStatistics.setMin(new BigDecimal("25.00"));
        expectedStatistics.setCount(2);
        // Create a mock TransactionService
        TransactionService transactionService = mock(TransactionService.class);
        when(transactionService.getTransactionStatistics()).thenReturn(expectedStatistics);

        // Create an instance of TransactionController and inject the mock TransactionService
        TransactionController controller = new TransactionController(transactionService);

        // Call the method under test
        ResponseEntity<TransactionStatistics> response = controller.getTransactionStatistics();

        // Verify that the mock TransactionService's getTransactionStatistics method was called
        verify(transactionService, times(1)).getTransactionStatistics();

        // Assert the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStatistics, response.getBody());
    }

    @Test
    public void deleteAllTransactions_ReturnsNoContentResponse() {
        // Create a mock transaction service
        TransactionService transactionService = mock(TransactionService.class);

        // Create an instance of the controller with the mock service
        TransactionController transactionController = new TransactionController(transactionService);

        // Set up the mock service to return the desired response
        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();
        when(transactionService.deleteAllTransactions()).thenReturn(expectedResponse);

        // Call the deleteAllTransactions method on the controller
        ResponseEntity<Void> actualResponse = transactionController.deleteAllTransactions();

        // Assert that the actual response matches the expected response
        assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());
    }

}
