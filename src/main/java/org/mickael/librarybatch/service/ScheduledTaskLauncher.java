package org.mickael.librarybatch.service;

import org.mickael.librarybatch.model.AccountLoginBean;
import org.mickael.librarybatch.proxy.FeignProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@EnableAsync
public class ScheduledTaskLauncher {

    private EmailService emailService;
    private LoanStatusService loanStatusService;
    private FeignProxy feignProxy;


    @Autowired
    public ScheduledTaskLauncher(EmailService emailService, LoanStatusService loanStatusService,
                                 FeignProxy feignProxy) {
        this.emailService = emailService;
        this.loanStatusService = loanStatusService;
        this.feignProxy = feignProxy;
    }

    /**
     * Scheduled task run in loop for the demo
     * (cron="0 0 1 * * ?") prod context : run task every days at 01:00 am
     *
     * First, get a security token with batch account,
     * then run the mail task.
     */
    @Scheduled(cron="*/30 * * * * ?")
    public void runScheduledTask(){
        String accessToken = feignProxy.login(new AccountLoginBean()).getHeaders().getFirst("Authorization");
        loanStatusService.updateLoanStatus(accessToken);
        emailService.sendRecoveryMail(accessToken);
    }

    /*
      Scheduled task run in loop for the demo
      (cron="0 0 1 * * ?") prod context : run task every days at 01:00 am

      First, get a security token with batch account,
      then run the mail task.
     */
/*    @Scheduled(cron="0 0 1 * * ?")
    public void runScheduledTask(){
        //String accessToken = feignLoanProxy.login(new AccountLoginBean()).getHeaders().getFirst("Authorization");
        loanStatusService.updateLoanStatus();
        emailService.sendRecoveryMail();
    }*/


}
