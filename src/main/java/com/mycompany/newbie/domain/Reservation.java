package com.mycompany.newbie.domain;

import com.mycompany.newbie.domain.enumeration.ResStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "guest_full_name")
    private String guestFullName;

    @Column(name = "initial_date")
    private Instant initialDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "res_status")
    private ResStatus resStatus;

    @Column(name = "ship")
    private String ship;

    @Column(name = "port_from")
    private String portFrom;

    @Column(name = "jhi_from")
    private Instant from;

    @Column(name = "port_to")
    private String portTo;

    @Column(name = "jhi_to")
    private Instant to;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestFullName() {
        return this.guestFullName;
    }

    public Reservation guestFullName(String guestFullName) {
        this.setGuestFullName(guestFullName);
        return this;
    }

    public void setGuestFullName(String guestFullName) {
        this.guestFullName = guestFullName;
    }

    public Instant getInitialDate() {
        return this.initialDate;
    }

    public Reservation initialDate(Instant initialDate) {
        this.setInitialDate(initialDate);
        return this;
    }

    public void setInitialDate(Instant initialDate) {
        this.initialDate = initialDate;
    }

    public ResStatus getResStatus() {
        return this.resStatus;
    }

    public Reservation resStatus(ResStatus resStatus) {
        this.setResStatus(resStatus);
        return this;
    }

    public void setResStatus(ResStatus resStatus) {
        this.resStatus = resStatus;
    }

    public String getShip() {
        return this.ship;
    }

    public Reservation ship(String ship) {
        this.setShip(ship);
        return this;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public String getPortFrom() {
        return this.portFrom;
    }

    public Reservation portFrom(String portFrom) {
        this.setPortFrom(portFrom);
        return this;
    }

    public void setPortFrom(String portFrom) {
        this.portFrom = portFrom;
    }

    public Instant getFrom() {
        return this.from;
    }

    public Reservation from(Instant from) {
        this.setFrom(from);
        return this;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public String getPortTo() {
        return this.portTo;
    }

    public Reservation portTo(String portTo) {
        this.setPortTo(portTo);
        return this;
    }

    public void setPortTo(String portTo) {
        this.portTo = portTo;
    }

    public Instant getTo() {
        return this.to;
    }

    public Reservation to(Instant to) {
        this.setTo(to);
        return this;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
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
