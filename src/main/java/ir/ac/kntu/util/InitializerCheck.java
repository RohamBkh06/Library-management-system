package ir.ac.kntu.util;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.modules.Reservation;
import ir.ac.kntu.modules.ReservationStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class InitializerCheck implements Runnable {

    private InitializerCheck(){}

    private void checkActivation(Reservation reservation){
        if (reservation.getItem().getActiveReservation().equals(reservation)){
            reservation.setStatus(ReservationStatus.ACTIVE);
            reservation.setActivationDate(LocalDate.now());
        }
    }

    private void checkExpiration(Reservation reservation){
        if (reservation.getStatus() == ReservationStatus.ACTIVE ){
            if (ChronoUnit.DAYS.between(reservation.getActivationDate(), LocalDate.now()) > SystemProperties.getReserveExpireDays()){
                reservation.setStatus(ReservationStatus.EXPIRED);
                reservation.getItem().removeFromQueue(reservation);
            }
        }
    }

    @Override
    public void run(){
        for (NormalUser value : LibraryManger.getInstance().getUserById().values()) {
            for (Reservation reservation : value.getReservationById().values()) {
                checkActivation(reservation);
                checkExpiration(reservation);
            }
        }
    }
}
