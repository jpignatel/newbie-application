package com.mycompany.newbie.service.mapper;

import com.mycompany.newbie.service.api.dto.Reservation;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import vx.swxmlapi.xdata.CustomDbSearchOUT.Reservations;

/**
 * Mapper for the entity {@link Reservations.Reservation} and its DTO {@link Reservation}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReservationSeawebMapper extends EntityMapper<Reservation, Reservations.Reservation> {
	
	ReservationSeawebMapper INSTANCE = Mappers.getMapper(ReservationSeawebMapper.class);

	@Mapping(target = "id", source = "resID")
	@Mapping(target = "from", source = "firstSail.from.dateTime")
	@Mapping(target = "to", source = "firstSail.to.dateTime")
	@Mapping(target = "ship", source = "firstSail.ship")
	@Mapping(target = "portFrom", source = "firstSail.portFrom")
	@Mapping(target = "portTo", source = "firstSail.portTo")
	Reservation toDto(Reservations.Reservation reservation);
}
