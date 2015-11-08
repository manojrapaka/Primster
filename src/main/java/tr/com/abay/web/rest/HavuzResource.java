package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Havuz;
import tr.com.abay.repository.HavuzRepository;
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
 * REST controller for managing Havuz.
 */
@RestController
@RequestMapping("/api")
public class HavuzResource {

    private final Logger log = LoggerFactory.getLogger(HavuzResource.class);

    @Inject
    private HavuzRepository havuzRepository;

    /**
     * POST  /havuzs -> Create a new havuz.
     */
    @RequestMapping(value = "/havuzs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Havuz> createHavuz(@Valid @RequestBody Havuz havuz) throws URISyntaxException {
        log.debug("REST request to save Havuz : {}", havuz);
        if (havuz.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new havuz cannot already have an ID").body(null);
        }
        Havuz result = havuzRepository.save(havuz);
        return ResponseEntity.created(new URI("/api/havuzs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("havuz", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /havuzs -> Updates an existing havuz.
     */
    @RequestMapping(value = "/havuzs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Havuz> updateHavuz(@Valid @RequestBody Havuz havuz) throws URISyntaxException {
        log.debug("REST request to update Havuz : {}", havuz);
        if (havuz.getId() == null) {
            return createHavuz(havuz);
        }
        Havuz result = havuzRepository.save(havuz);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("havuz", havuz.getId().toString()))
                .body(result);
    }

    /**
     * GET  /havuzs -> get all the havuzs.
     */
    @RequestMapping(value = "/havuzs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Havuz>> getAllHavuzs(Pageable pageable)
        throws URISyntaxException {
        Page<Havuz> page = havuzRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/havuzs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /havuzs/:id -> get the "id" havuz.
     */
    @RequestMapping(value = "/havuzs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Havuz> getHavuz(@PathVariable Long id) {
        log.debug("REST request to get Havuz : {}", id);
        return Optional.ofNullable(havuzRepository.findOne(id))
            .map(havuz -> new ResponseEntity<>(
                havuz,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /havuzs/:id -> delete the "id" havuz.
     */
    @RequestMapping(value = "/havuzs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHavuz(@PathVariable Long id) {
        log.debug("REST request to delete Havuz : {}", id);
        havuzRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("havuz", id.toString())).build();
    }
}
