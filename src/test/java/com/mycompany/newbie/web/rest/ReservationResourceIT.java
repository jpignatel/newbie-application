package com.mycompany.newbie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.newbie.IntegrationTest;
import com.mycompany.newbie.domain.Reservation;
import com.mycompany.newbie.domain.enumeration.ResStatus;
import com.mycompany.newbie.repository.ReservationRepository;
import com.mycompany.newbie.service.dto.ReservationDTO;
import com.mycompany.newbie.service.mapper.ReservationMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReservationResourceIT {

    private static final String DEFAULT_GUEST_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUEST_FULL_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_INITIAL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INITIAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ResStatus DEFAULT_RES_STATUS = ResStatus.SH;
    private static final ResStatus UPDATED_RES_STATUS = ResStatus.OF;

    private static final String DEFAULT_SHIP = "AAAAAAAAAA";
    private static final String UPDATED_SHIP = "BBBBBBBBBB";

    private static final String DEFAULT_PORT_FROM = "AAAAAAAAAA";
    private static final String UPDATED_PORT_FROM = "BBBBBBBBBB";

    private static final Instant DEFAULT_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PORT_TO = "AAAAAAAAAA";
    private static final String UPDATED_PORT_TO = "BBBBBBBBBB";

    private static final Instant DEFAULT_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reservations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .guestFullName(DEFAULT_GUEST_FULL_NAME)
            .initialDate(DEFAULT_INITIAL_DATE)
            .resStatus(DEFAULT_RES_STATUS)
            .ship(DEFAULT_SHIP)
            .portFrom(DEFAULT_PORT_FROM)
            .from(DEFAULT_FROM)
            .portTo(DEFAULT_PORT_TO)
            .to(DEFAULT_TO);
        return reservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createUpdatedEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .guestFullName(UPDATED_GUEST_FULL_NAME)
            .initialDate(UPDATED_INITIAL_DATE)
            .resStatus(UPDATED_RES_STATUS)
            .ship(UPDATED_SHIP)
            .portFrom(UPDATED_PORT_FROM)
            .from(UPDATED_FROM)
            .portTo(UPDATED_PORT_TO)
            .to(UPDATED_TO);
        return reservation;
    }

    @BeforeEach
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();
        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getGuestFullName()).isEqualTo(DEFAULT_GUEST_FULL_NAME);
        assertThat(testReservation.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testReservation.getResStatus()).isEqualTo(DEFAULT_RES_STATUS);
        assertThat(testReservation.getShip()).isEqualTo(DEFAULT_SHIP);
        assertThat(testReservation.getPortFrom()).isEqualTo(DEFAULT_PORT_FROM);
        assertThat(testReservation.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testReservation.getPortTo()).isEqualTo(DEFAULT_PORT_TO);
        assertThat(testReservation.getTo()).isEqualTo(DEFAULT_TO);
    }

    @Test
    @Transactional
    void createReservationWithExistingId() throws Exception {
        // Create the Reservation with an existing ID
        reservation.setId(1L);
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].guestFullName").value(hasItem(DEFAULT_GUEST_FULL_NAME)))
            .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].resStatus").value(hasItem(DEFAULT_RES_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ship").value(hasItem(DEFAULT_SHIP)))
            .andExpect(jsonPath("$.[*].portFrom").value(hasItem(DEFAULT_PORT_FROM)))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].portTo").value(hasItem(DEFAULT_PORT_TO)))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())));
    }

    @Test
    @Transactional
    void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc
            .perform(get(ENTITY_API_URL_ID, reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.guestFullName").value(DEFAULT_GUEST_FULL_NAME))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.resStatus").value(DEFAULT_RES_STATUS.toString()))
            .andExpect(jsonPath("$.ship").value(DEFAULT_SHIP))
            .andExpect(jsonPath("$.portFrom").value(DEFAULT_PORT_FROM))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM.toString()))
            .andExpect(jsonPath("$.portTo").value(DEFAULT_PORT_TO))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .guestFullName(UPDATED_GUEST_FULL_NAME)
            .initialDate(UPDATED_INITIAL_DATE)
            .resStatus(UPDATED_RES_STATUS)
            .ship(UPDATED_SHIP)
            .portFrom(UPDATED_PORT_FROM)
            .from(UPDATED_FROM)
            .portTo(UPDATED_PORT_TO)
            .to(UPDATED_TO);
        ReservationDTO reservationDTO = reservationMapper.toDto(updatedReservation);

        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getGuestFullName()).isEqualTo(UPDATED_GUEST_FULL_NAME);
        assertThat(testReservation.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testReservation.getResStatus()).isEqualTo(UPDATED_RES_STATUS);
        assertThat(testReservation.getShip()).isEqualTo(UPDATED_SHIP);
        assertThat(testReservation.getPortFrom()).isEqualTo(UPDATED_PORT_FROM);
        assertThat(testReservation.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testReservation.getPortTo()).isEqualTo(UPDATED_PORT_TO);
        assertThat(testReservation.getTo()).isEqualTo(UPDATED_TO);
    }

    @Test
    @Transactional
    void putNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .resStatus(UPDATED_RES_STATUS)
            .portFrom(UPDATED_PORT_FROM)
            .from(UPDATED_FROM)
            .portTo(UPDATED_PORT_TO)
            .to(UPDATED_TO);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getGuestFullName()).isEqualTo(DEFAULT_GUEST_FULL_NAME);
        assertThat(testReservation.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testReservation.getResStatus()).isEqualTo(UPDATED_RES_STATUS);
        assertThat(testReservation.getShip()).isEqualTo(DEFAULT_SHIP);
        assertThat(testReservation.getPortFrom()).isEqualTo(UPDATED_PORT_FROM);
        assertThat(testReservation.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testReservation.getPortTo()).isEqualTo(UPDATED_PORT_TO);
        assertThat(testReservation.getTo()).isEqualTo(UPDATED_TO);
    }

    @Test
    @Transactional
    void fullUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .guestFullName(UPDATED_GUEST_FULL_NAME)
            .initialDate(UPDATED_INITIAL_DATE)
            .resStatus(UPDATED_RES_STATUS)
            .ship(UPDATED_SHIP)
            .portFrom(UPDATED_PORT_FROM)
            .from(UPDATED_FROM)
            .portTo(UPDATED_PORT_TO)
            .to(UPDATED_TO);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getGuestFullName()).isEqualTo(UPDATED_GUEST_FULL_NAME);
        assertThat(testReservation.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testReservation.getResStatus()).isEqualTo(UPDATED_RES_STATUS);
        assertThat(testReservation.getShip()).isEqualTo(UPDATED_SHIP);
        assertThat(testReservation.getPortFrom()).isEqualTo(UPDATED_PORT_FROM);
        assertThat(testReservation.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testReservation.getPortTo()).isEqualTo(UPDATED_PORT_TO);
        assertThat(testReservation.getTo()).isEqualTo(UPDATED_TO);
    }

    @Test
    @Transactional
    void patchNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
