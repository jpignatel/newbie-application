package com.mycompany.newbie.web.rest;

import com.mycompany.newbie.repository.PortRepository;
import com.mycompany.newbie.service.PortService;
import com.mycompany.newbie.service.dto.PortDTO;
import com.mycompany.newbie.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.newbie.domain.Port}.
 */
@RestController
@RequestMapping("/api")
public class PortResource {

    private final Logger log = LoggerFactory.getLogger(PortResource.class);

    private final PortService portService;

    private final PortRepository portRepository;

    public PortResource(PortService portService, PortRepository portRepository) {
        this.portService = portService;
        this.portRepository = portRepository;
    }

    /**
     * {@code GET  /ports} : get all the ports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ports in body.
     */
    @GetMapping("/ports")
    public List<PortDTO> getAllPorts() {
        log.debug("REST request to get all Ports");
        return portService.findAll();
    }

    /**
     * {@code GET  /ports/:id} : get the "id" port.
     *
     * @param id the id of the portDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ports/{id}")
    public ResponseEntity<PortDTO> getPort(@PathVariable Long id) {
        log.debug("REST request to get Port : {}", id);
        Optional<PortDTO> portDTO = portService.findOne(id);
        return ResponseUtil.wrapOrNotFound(portDTO);
    }
}
