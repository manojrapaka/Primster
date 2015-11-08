package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.Ulke;
import tr.com.abay.repository.UlkeRepository;

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
 * Test class for the UlkeResource REST controller.
 *
 * @see UlkeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UlkeResourceTest {

    private static final String DEFAULT_ADI = "A";
    private static final String UPDATED_ADI = "B";

    @Inject
    private UlkeRepository ulkeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUlkeMockMvc;

    private Ulke ulke;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UlkeResource ulkeResource = new UlkeResource();
        ReflectionTestUtils.setField(ulkeResource, "ulkeRepository", ulkeRepository);
        this.restUlkeMockMvc = MockMvcBuilders.standaloneSetup(ulkeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ulke = new Ulke();
        ulke.setAdi(DEFAULT_ADI);
    }

    @Test
    @Transactional
    public void createUlke() throws Exception {
        int databaseSizeBeforeCreate = ulkeRepository.findAll().size();

        // Create the Ulke

        restUlkeMockMvc.perform(post("/api/ulkes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ulke)))
                .andExpect(status().isCreated());

        // Validate the Ulke in the database
        List<Ulke> ulkes = ulkeRepository.findAll();
        assertThat(ulkes).hasSize(databaseSizeBeforeCreate + 1);
        Ulke testUlke = ulkes.get(ulkes.size() - 1);
        assertThat(testUlke.getAdi()).isEqualTo(DEFAULT_ADI);
    }

    @Test
    @Transactional
    public void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = ulkeRepository.findAll().size();
        // set the field null
        ulke.setAdi(null);

        // Create the Ulke, which fails.

        restUlkeMockMvc.perform(post("/api/ulkes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ulke)))
                .andExpect(status().isBadRequest());

        List<Ulke> ulkes = ulkeRepository.findAll();
        assertThat(ulkes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUlkes() throws Exception {
        // Initialize the database
        ulkeRepository.saveAndFlush(ulke);

        // Get all the ulkes
        restUlkeMockMvc.perform(get("/api/ulkes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ulke.getId().intValue())))
                .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI.toString())));
    }

    @Test
    @Transactional
    public void getUlke() throws Exception {
        // Initialize the database
        ulkeRepository.saveAndFlush(ulke);

        // Get the ulke
        restUlkeMockMvc.perform(get("/api/ulkes/{id}", ulke.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ulke.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUlke() throws Exception {
        // Get the ulke
        restUlkeMockMvc.perform(get("/api/ulkes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUlke() throws Exception {
        // Initialize the database
        ulkeRepository.saveAndFlush(ulke);

		int databaseSizeBeforeUpdate = ulkeRepository.findAll().size();

        // Update the ulke
        ulke.setAdi(UPDATED_ADI);

        restUlkeMockMvc.perform(put("/api/ulkes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ulke)))
                .andExpect(status().isOk());

        // Validate the Ulke in the database
        List<Ulke> ulkes = ulkeRepository.findAll();
        assertThat(ulkes).hasSize(databaseSizeBeforeUpdate);
        Ulke testUlke = ulkes.get(ulkes.size() - 1);
        assertThat(testUlke.getAdi()).isEqualTo(UPDATED_ADI);
    }

    @Test
    @Transactional
    public void deleteUlke() throws Exception {
        // Initialize the database
        ulkeRepository.saveAndFlush(ulke);

		int databaseSizeBeforeDelete = ulkeRepository.findAll().size();

        // Get the ulke
        restUlkeMockMvc.perform(delete("/api/ulkes/{id}", ulke.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ulke> ulkes = ulkeRepository.findAll();
        assertThat(ulkes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
