package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.Gorev;
import tr.com.abay.repository.GorevRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the GorevResource REST controller.
 *
 * @see GorevResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GorevResourceTest {

    private static final String DEFAULT_ADI = "A";
    private static final String UPDATED_ADI = "B";

    @Inject
    private GorevRepository gorevRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGorevMockMvc;

    private Gorev gorev;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GorevResource gorevResource = new GorevResource();
        ReflectionTestUtils.setField(gorevResource, "gorevRepository", gorevRepository);
        this.restGorevMockMvc = MockMvcBuilders.standaloneSetup(gorevResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        gorev = new Gorev();
        gorev.setAdi(DEFAULT_ADI);
    }

    @Test
    @Transactional
    public void createGorev() throws Exception {
        int databaseSizeBeforeCreate = gorevRepository.findAll().size();

        // Create the Gorev

        restGorevMockMvc.perform(post("/api/gorevs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gorev)))
                .andExpect(status().isCreated());

        // Validate the Gorev in the database
        List<Gorev> gorevs = gorevRepository.findAll();
        assertThat(gorevs).hasSize(databaseSizeBeforeCreate + 1);
        Gorev testGorev = gorevs.get(gorevs.size() - 1);
        assertThat(testGorev.getAdi()).isEqualTo(DEFAULT_ADI);
    }

    @Test
    @Transactional
    public void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = gorevRepository.findAll().size();
        // set the field null
        gorev.setAdi(null);

        // Create the Gorev, which fails.

        restGorevMockMvc.perform(post("/api/gorevs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gorev)))
                .andExpect(status().isBadRequest());

        List<Gorev> gorevs = gorevRepository.findAll();
        assertThat(gorevs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGorevs() throws Exception {
        // Initialize the database
        gorevRepository.saveAndFlush(gorev);

        // Get all the gorevs
        restGorevMockMvc.perform(get("/api/gorevs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gorev.getId().intValue())))
                .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI.toString())));
    }

    @Test
    @Transactional
    public void getGorev() throws Exception {
        // Initialize the database
        gorevRepository.saveAndFlush(gorev);

        // Get the gorev
        restGorevMockMvc.perform(get("/api/gorevs/{id}", gorev.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gorev.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGorev() throws Exception {
        // Get the gorev
        restGorevMockMvc.perform(get("/api/gorevs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGorev() throws Exception {
        // Initialize the database
        gorevRepository.saveAndFlush(gorev);

		int databaseSizeBeforeUpdate = gorevRepository.findAll().size();

        // Update the gorev
        gorev.setAdi(UPDATED_ADI);

        restGorevMockMvc.perform(put("/api/gorevs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gorev)))
                .andExpect(status().isOk());

        // Validate the Gorev in the database
        List<Gorev> gorevs = gorevRepository.findAll();
        assertThat(gorevs).hasSize(databaseSizeBeforeUpdate);
        Gorev testGorev = gorevs.get(gorevs.size() - 1);
        assertThat(testGorev.getAdi()).isEqualTo(UPDATED_ADI);
    }

    @Test
    @Transactional
    public void deleteGorev() throws Exception {
        // Initialize the database
        gorevRepository.saveAndFlush(gorev);

		int databaseSizeBeforeDelete = gorevRepository.findAll().size();

        // Get the gorev
        restGorevMockMvc.perform(delete("/api/gorevs/{id}", gorev.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Gorev> gorevs = gorevRepository.findAll();
        assertThat(gorevs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
