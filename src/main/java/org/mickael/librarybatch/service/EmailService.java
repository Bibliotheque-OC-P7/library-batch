package org.mickael.librarybatch.service;

import org.mickael.librarybatch.model.Book;
import org.mickael.librarybatch.model.Customer;
import org.mickael.librarybatch.model.Loan;
import org.mickael.librarybatch.model.LoanMail;
import org.mickael.librarybatch.proxy.FeignProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@EnableScheduling
@EnableAsync
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSender javaMailSender;
    private SimpleMailMessage preConfiguredMessage;
    private FeignProxy feignProxy;



    @Autowired
    public EmailService(JavaMailSender javaMailSender, SimpleMailMessage preConfiguredMessage, FeignProxy feignProxy) {
        this.javaMailSender = javaMailSender;
        this.preConfiguredMessage = preConfiguredMessage;
        this.feignProxy = feignProxy;
    }

    /**
     * This method retrieve all delays loans by calling the MS library-api and send a mail to all customers
     */
    public void sendRecoveryMail (String accessToken){
        List<LoanMail> loanMails = new ArrayList<>();

        //get all outdated loans
        List<Loan> loans = feignProxy.getLoanDelayLoan(accessToken);

        for (Loan loan : loans){
            Book book = new Book();
            book.setTitle(feignProxy.retrieveBook(loan.getBookId(),accessToken).getTitle());
            Customer customer= new Customer();
            customer.setEmail(feignProxy.retrieveCustomer(loan.getCustomerId(), accessToken).getEmail());
            customer.setFirstName(feignProxy.retrieveCustomer(loan.getCustomerId(), accessToken).getFirstName());
            customer.setLastName(feignProxy.retrieveCustomer(loan.getCustomerId(), accessToken).getLastName());
            LoanMail loanMail = new LoanMail();
            loanMail.setBook(book);
            loanMail.setCustomer(customer);
            if (loan.isExtend()){
                loanMail.setExpectedReturn(loan.getExtendLoanDate());
            } else {
                loanMail.setExpectedReturn(loan.getEndingLoanDate());
            }
            loanMails.add(loanMail);
        }

        for (LoanMail loanMail : loanMails){
            sendPreConfiguredMail(loanMail.getCustomer().getEmail(), loanMail.getCustomer().getFirstName(),
                                    loanMail.getCustomer().getLastName(), loanMail.getBook().getTitle(),
                                    formatDateToMail(loanMail.getExpectedReturn()));
            logger.info("date : " + formatDateToMail(loanMail.getExpectedReturn()));
        }

    }



    /**
     * This method will send a pre-configured message
     * @param argTo the email of the recipient
     * @param argFirst the firstName of the recipient
     * @param argLast the lastName of the recipient
     * @param argTitle the title of the book
     * @param date the date of the expected return
     *
     * */
    private void sendPreConfiguredMail(String argTo, String argFirst, String argLast, String argTitle, String date){
        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
        String text = String.format(Objects.requireNonNull(mailMessage.getText()),argFirst, argLast, argTitle, date);
        mailMessage.setTo(argTo);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }


    private void sendSimpleMessage(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    /**
     * This method format the expected return date
     *
     * @param date a date
     * @return a formatted date
     */
    private String formatDateToMail(LocalDate date){
        String pattern = "dd MMM yyyy";
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }


}
