package com.mycompany.newbie.service;

import com.mycompany.newbie.domain.Port;
import com.mycompany.newbie.repository.PortRepository;
import com.mycompany.newbie.service.dto.PortDTO;
import com.mycompany.newbie.service.mapper.PortMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Port}.
 */
@Service
@Transactional
public class PortService {

    private final Logger log = LoggerFactory.getLogger(PortService.class);

    private final PortRepository portRepository;

    private final PortMapper portMapper;

    public PortService(PortRepository portRepository, PortMapper portMapper) {
        this.portRepository = portRepository;
        this.portMapper = portMapper;
    }

    /**
     * Save a port.
     *
     * @param portDTO the entity to save.
     * @return the persisted entity.
     */
    public PortDTO save(PortDTO portDTO) {
        log.debug("Request to save Port : {}", portDTO);
        Port port = portMapper.toEntity(portDTO);
        port = portRepository.save(port);
        return portMapper.toDto(port);
    }

    /**
     * Partially update a port.
     *
     * @param portDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PortDTO> partialUpdate(PortDTO portDTO) {
        log.debug("Request to partially update Port : {}", portDTO);

        return portRepository
            .findById(portDTO.getId())
            .map(existingPort -> {
                portMapper.partialUpdate(existingPort, portDTO);

                return existingPort;
            })
            .map(portRepository::save)
            .map(portMapper::toDto);
    }

    /**
     * Get all the ports.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PortDTO> findAll() {
        log.debug("Request to get all Ports");
        return portRepository.findAll().stream().map(portMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one port by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PortDTO> findOne(Long id) {
        log.debug("Request to get Port : {}", id);
        return portRepository.findById(id).map(portMapper::toDto);
    }

    /**
     * Delete the port by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Port : {}", id);
        portRepository.deleteById(id);
    }
}
