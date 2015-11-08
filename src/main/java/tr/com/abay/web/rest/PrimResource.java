package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.Prim;
import tr.com.abay.repository.PrimRepository;
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
 * REST controller for managing Prim.
 */
@RestController
@RequestMapping("/api")
public class PrimResource {

    private final Logger log = LoggerFactory.getLogger(PrimResource.class);

    @Inject
    private PrimRepository primRepository;

    /**
     * POST  /prims -> Create a new prim.
     */
    @RequestMapping(value = "/prims",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prim> createPrim(@Valid @RequestBody Prim prim) throws URISyntaxException {
        log.debug("REST request to save Prim : {}", prim);
        if (prim.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prim cannot already have an ID").body(null);
        }
        Prim result = primRepository.save(prim);
        return ResponseEntity.created(new URI("/api/prims/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("prim", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /prims -> Updates an existing prim.
     */
    @RequestMapping(value = "/prims",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prim> updatePrim(@Valid @RequestBody Prim prim) throws URISyntaxException {
        log.debug("REST request to update Prim : {}", prim);
        if (prim.getId() == null) {
            return createPrim(prim);
        }
        Prim result = primRepository.save(prim);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("prim", prim.getId().toString()))
                .body(result);
    }

    /**
     * GET  /prims -> get all the prims.
     */
    @RequestMapping(value = "/prims",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Prim>> getAllPrims(Pageable pageable)
        throws URISyntaxException {
        Page<Prim> page = primRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prims");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prims/:id -> get the "id" prim.
     */
    @RequestMapping(value = "/prims/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prim> getPrim(@PathVariable Long id) {
        log.debug("REST request to get Prim : {}", id);
        return Optional.ofNullable(primRepository.findOne(id))
            .map(prim -> new ResponseEntity<>(
                prim,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prims/:id -> delete the "id" prim.
     */
    @RequestMapping(value = "/prims/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrim(@PathVariable Long id) {
        log.debug("REST request to delete Prim : {}", id);
        primRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prim", id.toString())).build();
    }
}
