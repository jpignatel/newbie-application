package com.mycompany.newbie.service.mapper;

import com.mycompany.newbie.domain.Port;
import com.mycompany.newbie.service.dto.PortDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Port} and its DTO {@link PortDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PortMapper extends EntityMapper<PortDTO, Port> {}
