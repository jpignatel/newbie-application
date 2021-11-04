package com.mycompany.newbie.service.dto;

import com.mycompany.newbie.domain.enumeration.ResStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.newbie.domain.Reservation} entity.
 */
public class ReservationDTO implements Serializable {

    private Long id;

    private String guestFullName;

    private Instant initialDate;

    private ResStatus resStatus;

    private String ship;

    private String portFrom;

    private Instant from;

    private String portTo;

    private Instant to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestFullName() {
        return guestFullName;
    }

    public void setGuestFullName(String guestFullName) {
        this.guestFullName = guestFullName;
    }

    public Instant getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Instant initialDate) {
        this.initialDate = initialDate;
    }

    public ResStatus getResStatus() {
        return resStatus;
    }

    public void setResStatus(ResStatus resStatus) {
        this.resStatus = resStatus;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public String getPortFrom() {
        return portFrom;
    }

    public void setPortFrom(String portFrom) {
        this.portFrom = portFrom;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public String getPortTo() {
        return portTo;
    }

    public void setPortTo(String portTo) {
        this.portTo = portTo;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationDTO)) {
            return false;
        }

        ReservationDTO reservationDTO = (ReservationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reservationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id=" + getId() +
            ", guestFullName='" + getGuestFullName() + "'" +
            ", initialDate='" + getInitialDate() + "'" +
            ", resStatus='" + getResStatus() + "'" +
            ", ship='" + getShip() + "'" +
            ", portFrom='" + getPortFrom() + "'" +
            ", from='" + getFrom() + "'" +
            ", portTo='" + getPortTo() + "'" +
            ", to='" + getTo() + "'" +
            "}";
    }
}
