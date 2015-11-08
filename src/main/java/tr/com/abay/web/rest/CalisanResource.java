package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Calisan;
import tr.com.abay.repository.CalisanRepository;
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
 * REST controller for managing Calisan.
 */
@RestController
@RequestMapping("/api")
public class CalisanResource {

    private final Logger log = LoggerFactory.getLogger(CalisanResource.class);

    @Inject
    private CalisanRepository calisanRepository;

    /**
     * POST  /calisans -> Create a new calisan.
     */
    @RequestMapping(value = "/calisans",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Calisan> createCalisan(@Valid @RequestBody Calisan calisan) throws URISyntaxException {
        log.debug("REST request to save Calisan : {}", calisan);
        if (calisan.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new calisan cannot already have an ID").body(null);
        }
        Calisan result = calisanRepository.save(calisan);
        return ResponseEntity.created(new URI("/api/calisans/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("calisan", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /calisans -> Updates an existing calisan.
     */
    @RequestMapping(value = "/calisans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Calisan> updateCalisan(@Valid @RequestBody Calisan calisan) throws URISyntaxException {
        log.debug("REST request to update Calisan : {}", calisan);
        if (calisan.getId() == null) {
            return createCalisan(calisan);
        }
        Calisan result = calisanRepository.save(calisan);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("calisan", calisan.getId().toString()))
                .body(result);
    }

    /**
     * GET  /calisans -> get all the calisans.
     */
    @RequestMapping(value = "/calisans",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Calisan>> getAllCalisans(Pageable pageable)
        throws URISyntaxException {
        Page<Calisan> page = calisanRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/calisans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /calisans/:id -> get the "id" calisan.
     */
    @RequestMapping(value = "/calisans/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Calisan> getCalisan(@PathVariable Long id) {
        log.debug("REST request to get Calisan : {}", id);
        return Optional.ofNullable(calisanRepository.findOne(id))
            .map(calisan -> new ResponseEntity<>(
                calisan,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /calisans/:id -> delete the "id" calisan.
     */
    @RequestMapping(value = "/calisans/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCalisan(@PathVariable Long id) {
        log.debug("REST request to delete Calisan : {}", id);
        calisanRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("calisan", id.toString())).build();
    }
}
