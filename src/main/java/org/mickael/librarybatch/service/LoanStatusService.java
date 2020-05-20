package org.mickael.librarybatch.service;

import org.mickael.librarybatch.proxy.FeignProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanStatusService {
    private static final Logger logger = LoggerFactory.getLogger(LoanStatusService.class);
    private FeignProxy feignProxy;

    @Autowired
    public LoanStatusService(FeignProxy feignProxy) {
        this.feignProxy = feignProxy;
    }


    //why void ?
    public void updateLoanStatus(String accessToken){
        Integer nbOfUpdateLoan = feignProxy.updateLoanStatus(accessToken);
        logger.info("Number of loan status updated : " + nbOfUpdateLoan);
    }
}
