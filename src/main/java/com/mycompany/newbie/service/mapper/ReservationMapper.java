package com.mycompany.newbie.service.mapper;

import com.mycompany.newbie.domain.Reservation;
import com.mycompany.newbie.service.dto.ReservationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {}
