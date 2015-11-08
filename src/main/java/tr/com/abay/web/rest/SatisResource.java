package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Satis;
import tr.com.abay.repository.SatisRepository;
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
 * REST controller for managing Satis.
 */
@RestController
@RequestMapping("/api")
public class SatisResource {

    private final Logger log = LoggerFactory.getLogger(SatisResource.class);

    @Inject
    private SatisRepository satisRepository;

    /**
     * POST  /satiss -> Create a new satis.
     */
    @RequestMapping(value = "/satiss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Satis> createSatis(@Valid @RequestBody Satis satis) throws URISyntaxException {
        log.debug("REST request to save Satis : {}", satis);
        if (satis.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new satis cannot already have an ID").body(null);
        }
        Satis result = satisRepository.save(satis);
        return ResponseEntity.created(new URI("/api/satiss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("satis", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /satiss -> Updates an existing satis.
     */
    @RequestMapping(value = "/satiss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Satis> updateSatis(@Valid @RequestBody Satis satis) throws URISyntaxException {
        log.debug("REST request to update Satis : {}", satis);
        if (satis.getId() == null) {
            return createSatis(satis);
        }
        Satis result = satisRepository.save(satis);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("satis", satis.getId().toString()))
                .body(result);
    }

    /**
     * GET  /satiss -> get all the satiss.
     */
    @RequestMapping(value = "/satiss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Satis>> getAllSatiss(Pageable pageable)
        throws URISyntaxException {
        Page<Satis> page = satisRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/satiss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /satiss/:id -> get the "id" satis.
     */
    @RequestMapping(value = "/satiss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Satis> getSatis(@PathVariable Long id) {
        log.debug("REST request to get Satis : {}", id);
        return Optional.ofNullable(satisRepository.findOne(id))
            .map(satis -> new ResponseEntity<>(
                satis,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /satiss/:id -> delete the "id" satis.
     */
    @RequestMapping(value = "/satiss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSatis(@PathVariable Long id) {
        log.debug("REST request to delete Satis : {}", id);
        satisRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("satis", id.toString())).build();
    }
}
