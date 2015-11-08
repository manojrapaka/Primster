package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Gorev;
import tr.com.abay.repository.GorevRepository;
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
 * REST controller for managing Gorev.
 */
@RestController
@RequestMapping("/api")
public class GorevResource {

    private final Logger log = LoggerFactory.getLogger(GorevResource.class);

    @Inject
    private GorevRepository gorevRepository;

    /**
     * POST  /gorevs -> Create a new gorev.
     */
    @RequestMapping(value = "/gorevs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gorev> createGorev(@Valid @RequestBody Gorev gorev) throws URISyntaxException {
        log.debug("REST request to save Gorev : {}", gorev);
        if (gorev.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new gorev cannot already have an ID").body(null);
        }
        Gorev result = gorevRepository.save(gorev);
        return ResponseEntity.created(new URI("/api/gorevs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("gorev", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /gorevs -> Updates an existing gorev.
     */
    @RequestMapping(value = "/gorevs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gorev> updateGorev(@Valid @RequestBody Gorev gorev) throws URISyntaxException {
        log.debug("REST request to update Gorev : {}", gorev);
        if (gorev.getId() == null) {
            return createGorev(gorev);
        }
        Gorev result = gorevRepository.save(gorev);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("gorev", gorev.getId().toString()))
                .body(result);
    }

    /**
     * GET  /gorevs -> get all the gorevs.
     */
    @RequestMapping(value = "/gorevs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Gorev>> getAllGorevs(Pageable pageable)
        throws URISyntaxException {
        Page<Gorev> page = gorevRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gorevs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gorevs/:id -> get the "id" gorev.
     */
    @RequestMapping(value = "/gorevs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gorev> getGorev(@PathVariable Long id) {
        log.debug("REST request to get Gorev : {}", id);
        return Optional.ofNullable(gorevRepository.findOne(id))
            .map(gorev -> new ResponseEntity<>(
                gorev,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gorevs/:id -> delete the "id" gorev.
     */
    @RequestMapping(value = "/gorevs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGorev(@PathVariable Long id) {
        log.debug("REST request to delete Gorev : {}", id);
        gorevRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gorev", id.toString())).build();
    }
}
