package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Ulke;
import tr.com.abay.repository.UlkeRepository;
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
 * REST controller for managing Ulke.
 */
@RestController
@RequestMapping("/api")
public class UlkeResource {

    private final Logger log = LoggerFactory.getLogger(UlkeResource.class);

    @Inject
    private UlkeRepository ulkeRepository;

    /**
     * POST  /ulkes -> Create a new ulke.
     */
    @RequestMapping(value = "/ulkes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ulke> createUlke(@Valid @RequestBody Ulke ulke) throws URISyntaxException {
        log.debug("REST request to save Ulke : {}", ulke);
        if (ulke.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ulke cannot already have an ID").body(null);
        }
        Ulke result = ulkeRepository.save(ulke);
        return ResponseEntity.created(new URI("/api/ulkes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("ulke", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ulkes -> Updates an existing ulke.
     */
    @RequestMapping(value = "/ulkes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ulke> updateUlke(@Valid @RequestBody Ulke ulke) throws URISyntaxException {
        log.debug("REST request to update Ulke : {}", ulke);
        if (ulke.getId() == null) {
            return createUlke(ulke);
        }
        Ulke result = ulkeRepository.save(ulke);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("ulke", ulke.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ulkes -> get all the ulkes.
     */
    @RequestMapping(value = "/ulkes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ulke>> getAllUlkes(Pageable pageable)
        throws URISyntaxException {
        Page<Ulke> page = ulkeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ulkes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ulkes/:id -> get the "id" ulke.
     */
    @RequestMapping(value = "/ulkes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ulke> getUlke(@PathVariable Long id) {
        log.debug("REST request to get Ulke : {}", id);
        return Optional.ofNullable(ulkeRepository.findOne(id))
            .map(ulke -> new ResponseEntity<>(
                ulke,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ulkes/:id -> delete the "id" ulke.
     */
    @RequestMapping(value = "/ulkes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUlke(@PathVariable Long id) {
        log.debug("REST request to delete Ulke : {}", id);
        ulkeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ulke", id.toString())).build();
    }
}
