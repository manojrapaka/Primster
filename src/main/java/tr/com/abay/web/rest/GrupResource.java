package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Grup;
import tr.com.abay.repository.GrupRepository;
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
 * REST controller for managing Grup.
 */
@RestController
@RequestMapping("/api")
public class GrupResource {

    private final Logger log = LoggerFactory.getLogger(GrupResource.class);

    @Inject
    private GrupRepository grupRepository;

    /**
     * POST  /grups -> Create a new grup.
     */
    @RequestMapping(value = "/grups",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Grup> createGrup(@Valid @RequestBody Grup grup) throws URISyntaxException {
        log.debug("REST request to save Grup : {}", grup);
        if (grup.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new grup cannot already have an ID").body(null);
        }
        Grup result = grupRepository.save(grup);
        return ResponseEntity.created(new URI("/api/grups/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("grup", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /grups -> Updates an existing grup.
     */
    @RequestMapping(value = "/grups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Grup> updateGrup(@Valid @RequestBody Grup grup) throws URISyntaxException {
        log.debug("REST request to update Grup : {}", grup);
        if (grup.getId() == null) {
            return createGrup(grup);
        }
        Grup result = grupRepository.save(grup);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("grup", grup.getId().toString()))
                .body(result);
    }

    /**
     * GET  /grups -> get all the grups.
     */
    @RequestMapping(value = "/grups",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Grup>> getAllGrups(Pageable pageable)
        throws URISyntaxException {
        Page<Grup> page = grupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/grups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /grups/:id -> get the "id" grup.
     */
    @RequestMapping(value = "/grups/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Grup> getGrup(@PathVariable Long id) {
        log.debug("REST request to get Grup : {}", id);
        return Optional.ofNullable(grupRepository.findOne(id))
            .map(grup -> new ResponseEntity<>(
                grup,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /grups/:id -> delete the "id" grup.
     */
    @RequestMapping(value = "/grups/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGrup(@PathVariable Long id) {
        log.debug("REST request to delete Grup : {}", id);
        grupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("grup", id.toString())).build();
    }
}
