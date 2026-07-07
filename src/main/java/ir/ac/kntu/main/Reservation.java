package ir.ac.kntu.main;

import ir.ac.kntu.modules.LibraryItem;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.util.IdGenerator;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private String id;
    private LibraryItem item;
    private NormalUser reservor;
    private ReservationStatus status;
    private LocalDate activationDate;

    public Reservation(LibraryItem item, NormalUser reservor) {
        this.id = IdGenerator.generateReservationId();
        this.item = item;
        this.reservor = reservor;
        this.status = ReservationStatus.WAITING;
        this.activationDate = null;
        item.addToAQueue(this);
    }

    public void borrowReserved(){
        if (this.status != ReservationStatus.ACTIVE){
            throw new IllegalStateException("This reservation is not yet active.");
        }
        this.status = ReservationStatus.COMPLETED;
        this.item.removeFromQueue(this);
    }

    public void cancelReservation(){
        this.status = ReservationStatus.CANCELED;
        this.item.removeFromQueue(this);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Reservation that = (Reservation) obj;
        return Objects.equals(id, that.id) && Objects.equals(item, that.item) && Objects.equals(reservor, that.reservor);
    }

    public boolean equals2(Object obj) {
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Reservation that = (Reservation) obj;
        return Objects.equals(item, that.item) && Objects.equals(reservor, that.reservor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, reservor);
    }

    @Override
    public String toString() {
        return "Reservation {" +
                "ID= " + id +
                ", item= " + item +
                ", reservor= " + reservor +
                ", status= " + status +
                '}';
    }

    public String getId() {
        return id;
    }

    public LibraryItem getItem() {
        return item;
    }

    public NormalUser getReservor() {
        return reservor;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }
}
