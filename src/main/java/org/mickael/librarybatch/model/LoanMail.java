package org.mickael.librarybatch.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanMail {

    private Customer customer;
    private Book book;
    private LocalDate expectedReturn;

}
