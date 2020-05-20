package org.mickael.librarybatch.proxy;


import org.mickael.librarybatch.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "library-gateway", url = "localhost:8763")
public interface FeignProxy {

    @PostMapping("/library-auth-server/login")
    ResponseEntity<?> login(@RequestBody AccountLoginBean accountLoginBean);

    @GetMapping("/library-ms-loan/api/loans/delay")
    List<Loan> getLoanDelayLoan(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/library-ms-loan/api/loans/update-status")
    Integer updateLoanStatus(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/library-ms-customer/api/customers/{id}")
    Customer retrieveCustomer(@PathVariable("id") Integer id, @RequestHeader("Authorization") String accessToken);

    @GetMapping("/library-ms-book/api/books/{id}")
    Book retrieveBook(@PathVariable("id") Integer id, @RequestHeader("Authorization") String accessToken);

    @GetMapping("/library-ms-book/api/copies/available/book/{bookId}")
    boolean checkIfCopyAvailableForBook(@PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

    @GetMapping("/library-ms-book/api/copies/available-number/book/{bookId}")
    Integer numberOfCopyAvailableForBook(@PathVariable("bookId")  Integer bookId, @RequestHeader("Authorization") String accessToken);


    @GetMapping("/library-ms-reservation/api/reservations")
    List<Reservation> getReservations(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/library-ms-reservation/api/reservations/customer/{customerId}/book/{bookId}/")
    boolean checkIfReservationExist(@PathVariable("customerId") Integer customerId, @PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

    @DeleteMapping("/library-ms-reservation/api/reservations/customer/{customerId}/book/{bookId}/")
    void deleteReservationAfterLoan(@PathVariable("customerId") Integer customerId, @PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

    @DeleteMapping("/library-ms-reservation/api/reservations/{id}")
    void deleteReservationAfterTwoDays(@PathVariable("id") Integer id, @RequestHeader("Authorization") String accessToken);


    @PutMapping("/library-ms-reservation/api/reservations/book/{bookId}")
    void updateReservation(@PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

    @PutMapping("/library-ms-reservation/api/reservations/book/{bookId}/refresh")
    void updateDateReservation(@PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

}
