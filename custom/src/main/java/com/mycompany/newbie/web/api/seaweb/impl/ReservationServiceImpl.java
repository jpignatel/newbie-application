package com.mycompany.newbie.web.api.seaweb.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.newbie.repository.UserRepository;
import com.mycompany.newbie.security.SecurityUtils;
import com.mycompany.newbie.service.api.dto.Reservation;
import com.mycompany.newbie.service.dto.AdminUserDTO;
import com.mycompany.newbie.service.mapper.ReservationSeawebMapper;
import com.mycompany.newbie.web.api.ReservationsApiDelegate;

import vx.swxmlapi.facade.util.XDataFactory;
import vx.swxmlapi.xdata.Boolean;
import vx.swxmlapi.xdata.CustomDbSearchIN;
import vx.swxmlapi.xdata.CustomDbSearchOUT;
import vx.swxmlapi.xdata.ResStatus;


/**
 * Service Implementation for managing {@link Reservation}.
 */
@Service
public class ReservationServiceImpl implements ReservationsApiDelegate {

    private final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final UserRepository userRepository;

    private final ReservationSeawebMapper reservationMapper;

	public ReservationServiceImpl(ReservationSeawebMapper reservationMapper, UserRepository userRepository) {
        this.reservationMapper = reservationMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
	public ResponseEntity<Reservation> getReservationById(Long id) {
		
    	AdminUserDTO userDto =
    	        SecurityUtils
    	        .getCurrentUserLogin()
    	        .flatMap(userRepository::findOneByLogin)
    	        .map(AdminUserDTO::new)
    	        .get();

    	    	// list of reservation statuses to show
    	        List<ResStatus> resStatuses = new ArrayList<ResStatus>();
    	        resStatuses.add(ResStatus.OF);
    	        resStatuses.add(ResStatus.BK);

    	        // prepare request
    	        CustomDbSearchIN cdbsin = new CustomDbSearchIN();
    	        cdbsin.setMsgHeader(XDataFactory.createMsgHeader(userDto.getFirstName(), "FR"));
    	        cdbsin.setReservations(new CustomDbSearchIN.Reservations());
    	        cdbsin.getReservations().setResID(Integer.parseInt(Long.toString(id)));
    	        cdbsin.getReservations().setClient(new CustomDbSearchIN.Reservations.Client());
    	        cdbsin.getReservations().getClient().setClientID(Integer.parseInt(userDto.getLastName()));
    	        cdbsin.getReservations().getResStatuses().addAll(resStatuses);

    	        // don't include reservation with receipts, they
    	        // are modified via agency and shouldn't be displayed here any more
    	        cdbsin.getReservations().setIncludeWithoutReceiptsOnly(Boolean.Y);

    	        cdbsin.setOptions(new CustomDbSearchIN.Options());
    	        cdbsin.getOptions().getIncludeElements().add("PaymentSchedule");

    	        // process request
    	        List<CustomDbSearchOUT.Reservations.Reservation> _reservations = null;
    	        if ( cdbsin.getReservations().getClient().getClientID() != null && cdbsin.getReservations().getResID() != null) {
    	            // make sure, client id is specified (performance reqiurement)
    	            CustomDbSearchOUT cdbsout = (CustomDbSearchOUT ) vx.swxmlapi.facade.BaseFacade.processRequest( cdbsin);

    	            // Add the found reservations to the allowed reservations set
    	            if ( cdbsout.getReservations() != null ) {
    	                _reservations = cdbsout.getReservations().getReservation();
    	            }
    	        } else {
    	            log.error("client id or reservation id is missing in CustomDbSearch.Reservations, skipping call...");
    	            return new ResponseEntity<Reservation>(HttpStatus.BAD_REQUEST);
    	        }
    	        
    	        if (_reservations == null) {
    	            log.error("no result found in CustomDbSearch.Reservations, skipping call...");
    	            return new ResponseEntity<Reservation>(HttpStatus.BAD_REQUEST);
    	        }

    	        Stream<Reservation> reservations = _reservations.stream().map(reservationMapper::toDto);
    	        log.info(reservations.toString());
    	        		
    	        List<Reservation> list = reservations.collect(Collectors.toCollection(LinkedList::new));
    	        
    	        ResponseEntity<Reservation> response = new ResponseEntity<Reservation>(list.get(0), HttpStatus.OK);
    	        
    	        
    	        return response;
	}
	
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<Reservation>> getReservations() {

    	AdminUserDTO userDto =
        SecurityUtils
        .getCurrentUserLogin()
        .flatMap(userRepository::findOneByLogin)
        .map(AdminUserDTO::new)
        .get();

    	// list of reservation statuses to show
        List<ResStatus> resStatuses = new ArrayList<ResStatus>();
        resStatuses.add(ResStatus.OF);
        resStatuses.add(ResStatus.BK);

        // prepare request
        CustomDbSearchIN cdbsin = new CustomDbSearchIN();
        cdbsin.setMsgHeader(XDataFactory.createMsgHeader(userDto.getFirstName(), "FR"));
        cdbsin.setReservations(new CustomDbSearchIN.Reservations());
        cdbsin.getReservations().setClient(new CustomDbSearchIN.Reservations.Client());
        cdbsin.getReservations().getClient().setClientID(Integer.parseInt(userDto.getLastName()));
        cdbsin.getReservations().getResStatuses().addAll(resStatuses);

        // don't include reservation with receipts, they
        // are modified via agency and shouldn't be displayed here any more
        cdbsin.getReservations().setIncludeWithoutReceiptsOnly(Boolean.Y);

        cdbsin.setOptions(new CustomDbSearchIN.Options());
        cdbsin.getOptions().getIncludeElements().add("PaymentSchedule");

        // process request
        List<CustomDbSearchOUT.Reservations.Reservation> _reservations = null;
        if ( cdbsin.getReservations().getClient().getClientID() != null) {
            // make sure, client id is specified (performance reqiurement)
            CustomDbSearchOUT cdbsout = (CustomDbSearchOUT ) vx.swxmlapi.facade.BaseFacade.processRequest( cdbsin);

            // Add the found reservations to the allowed reservations set
            if ( cdbsout.getReservations() != null ) {
                _reservations = cdbsout.getReservations().getReservation();
            }
        } else {
            log.error("client id is missing in CustomDbSearch.Reservations, skipping call...");
            return new ResponseEntity<List<Reservation>>(HttpStatus.BAD_REQUEST);
        }
        
        if (_reservations == null) {
            _reservations = new ArrayList<CustomDbSearchOUT.Reservations.Reservation>();
        }

        Stream<Reservation> reservations = _reservations.stream().map(reservationMapper::toDto);
        log.info(reservations.toString());
        		
        List<Reservation> list = reservations.collect(Collectors.toCollection(LinkedList::new));
        
        ResponseEntity<List<Reservation>> response = new ResponseEntity<List<Reservation>>(list, HttpStatus.OK);
        
        
        return response;

    }
}
