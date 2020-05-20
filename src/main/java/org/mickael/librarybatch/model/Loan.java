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
public class Loan {

    private Integer id;
    private LocalDate beginLoanDate;
    private LocalDate endingLoanDate;
    private LocalDate extendLoanDate;
    private LocalDate returnLoanDate;
    private boolean extend;
    private String loanStatus;
    private Integer customerId;
    private Integer copyId;
    private Integer bookId;
}
