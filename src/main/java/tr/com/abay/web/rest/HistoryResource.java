package tr.com.abay.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.abay.domain.History;
import tr.com.abay.repository.HistoryRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing History.
 */
@RestController
@RequestMapping("/api")
public class HistoryResource {

    private final Logger log = LoggerFactory.getLogger(HistoryResource.class);

    @Inject
    private HistoryRepository historyRepository;

    /**
     * POST  /historys -> Create a new history.
     */
    @RequestMapping(value = "/historys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<History> createHistory(@RequestBody History history) throws URISyntaxException {
        log.debug("REST request to save History : {}", history);
        if (history.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new history cannot already have an ID").body(null);
        }
        History result = historyRepository.save(history);
        return ResponseEntity.created(new URI("/api/historys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("history", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /historys -> Updates an existing history.
     */
    @RequestMapping(value = "/historys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<History> updateHistory(@RequestBody History history) throws URISyntaxException {
        log.debug("REST request to update History : {}", history);
        if (history.getId() == null) {
            return createHistory(history);
        }
        History result = historyRepository.save(history);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("history", history.getId().toString()))
                .body(result);
    }

    /**
     * GET  /historys -> get all the historys.
     */
    @RequestMapping(value = "/historys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<History>> getAllHistorys(Pageable pageable)
        throws URISyntaxException {
        Page<History> page = historyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/historys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /historys/:id -> get the "id" history.
     */
    @RequestMapping(value = "/historys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<History> getHistory(@PathVariable Long id) {
        log.debug("REST request to get History : {}", id);
        return Optional.ofNullable(historyRepository.findOne(id))
            .map(history -> new ResponseEntity<>(
                history,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /historys/:id -> delete the "id" history.
     */
    @RequestMapping(value = "/historys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        log.debug("REST request to delete History : {}", id);
        historyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("history", id.toString())).build();
    }
}
