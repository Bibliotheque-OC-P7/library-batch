package org.mickael.librarybatch.service;

import org.mickael.librarybatch.model.Reservation;
import org.mickael.librarybatch.proxy.FeignProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private FeignProxy feignProxy;

    @Autowired
    public ReservationService(FeignProxy feignProxy) {
        this.feignProxy = feignProxy;
    }

    public void updateListReservation(String accessToken){
        List<Reservation> reservationList = feignProxy.getReservations(accessToken);
        for (Reservation reservation : reservationList){
            if (reservation.getEndOfPriority().isAfter(LocalDate.now())){
                feignProxy.deleteReservationAfterTwoDays(reservation.getId(), accessToken);
            }
        }
    }
}
