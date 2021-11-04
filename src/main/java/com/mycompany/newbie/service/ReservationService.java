package com.mycompany.newbie.service;

import com.mycompany.newbie.service.dto.ReservationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.newbie.domain.Reservation}.
 */
public interface ReservationService {
    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    ReservationDTO save(ReservationDTO reservationDTO);

    /**
     * Partially updates a reservation.
     *
     * @param reservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReservationDTO> partialUpdate(ReservationDTO reservationDTO);

    /**
     * Get all the reservations.
     *
     * @return the list of entities.
     */
    List<ReservationDTO> findAll();

    /**
     * Get the "id" reservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservationDTO> findOne(Long id);

    /**
     * Delete the "id" reservation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
