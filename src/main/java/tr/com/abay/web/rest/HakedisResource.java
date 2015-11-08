package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Hakedis;
import tr.com.abay.repository.HakedisRepository;
import tr.com.abay.web.rest.util.HeaderUtil;
import tr.com.abay.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Hakedis.
 */
@RestController
@RequestMapping("/api")
public class HakedisResource {

    private final Logger log = LoggerFactory.getLogger(HakedisResource.class);

    @Inject
    private HakedisRepository hakedisRepository;

    /**
     * POST  /hakediss -> Create a new hakedis.
     */
    @RequestMapping(value = "/hakediss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hakedis> createHakedis(@Valid @RequestBody Hakedis hakedis) throws URISyntaxException {
        log.debug("REST request to save Hakedis : {}", hakedis);
        if (hakedis.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hakedis cannot already have an ID").body(null);
        }
        Hakedis result = hakedisRepository.save(hakedis);
        return ResponseEntity.created(new URI("/api/hakediss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("hakedis", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /hakediss -> Updates an existing hakedis.
     */
    @RequestMapping(value = "/hakediss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hakedis> updateHakedis(@Valid @RequestBody Hakedis hakedis) throws URISyntaxException {
        log.debug("REST request to update Hakedis : {}", hakedis);
        if (hakedis.getId() == null) {
            return createHakedis(hakedis);
        }
        Hakedis result = hakedisRepository.save(hakedis);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("hakedis", hakedis.getId().toString()))
                .body(result);
    }

    /**
     * GET  /hakediss -> get all the hakediss.
     */
    @RequestMapping(value = "/hakediss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Hakedis>> getAllHakediss(Pageable pageable)
        throws URISyntaxException {
        Page<Hakedis> page = hakedisRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hakediss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hakediss/:id -> get the "id" hakedis.
     */
    @RequestMapping(value = "/hakediss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hakedis> getHakedis(@PathVariable Long id) {
        log.debug("REST request to get Hakedis : {}", id);
        return Optional.ofNullable(hakedisRepository.findOne(id))
            .map(hakedis -> new ResponseEntity<>(
                hakedis,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hakediss/:id -> delete the "id" hakedis.
     */
    @RequestMapping(value = "/hakediss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHakedis(@PathVariable Long id) {
        log.debug("REST request to delete Hakedis : {}", id);
        hakedisRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hakedis", id.toString())).build();
    }
}
