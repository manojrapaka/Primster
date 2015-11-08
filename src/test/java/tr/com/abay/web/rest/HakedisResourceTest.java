package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.Hakedis;
import tr.com.abay.repository.HakedisRepository;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the HakedisResource REST controller.
 *
 * @see HakedisResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HakedisResourceTest {


    private static final LocalDate DEFAULT_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_TARIH = new LocalDate();

    private static final LocalDate DEFAULT_BAS_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_BAS_TARIH = new LocalDate();

    private static final LocalDate DEFAULT_BIT_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_BIT_TARIH = new LocalDate();

    private static final Double DEFAULT_PRIM = 1D;
    private static final Double UPDATED_PRIM = 2D;

    @Inject
    private HakedisRepository hakedisRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHakedisMockMvc;

    private Hakedis hakedis;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HakedisResource hakedisResource = new HakedisResource();
        ReflectionTestUtils.setField(hakedisResource, "hakedisRepository", hakedisRepository);
        this.restHakedisMockMvc = MockMvcBuilders.standaloneSetup(hakedisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hakedis = new Hakedis();
        hakedis.setTarih(DEFAULT_TARIH);
        hakedis.setBasTarih(DEFAULT_BAS_TARIH);
        hakedis.setBitTarih(DEFAULT_BIT_TARIH);
        hakedis.setPrim(DEFAULT_PRIM);
    }

    @Test
    @Transactional
    public void createHakedis() throws Exception {
        int databaseSizeBeforeCreate = hakedisRepository.findAll().size();

        // Create the Hakedis

        restHakedisMockMvc.perform(post("/api/hakediss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hakedis)))
                .andExpect(status().isCreated());

        // Validate the Hakedis in the database
        List<Hakedis> hakediss = hakedisRepository.findAll();
        assertThat(hakediss).hasSize(databaseSizeBeforeCreate + 1);
        Hakedis testHakedis = hakediss.get(hakediss.size() - 1);
        assertThat(testHakedis.getTarih()).isEqualTo(DEFAULT_TARIH);
        assertThat(testHakedis.getBasTarih()).isEqualTo(DEFAULT_BAS_TARIH);
        assertThat(testHakedis.getBitTarih()).isEqualTo(DEFAULT_BIT_TARIH);
        assertThat(testHakedis.getPrim()).isEqualTo(DEFAULT_PRIM);
    }

    @Test
    @Transactional
    public void checkBasTarihIsRequired() throws Exception {
        int databaseSizeBeforeTest = hakedisRepository.findAll().size();
        // set the field null
        hakedis.setBasTarih(null);

        // Create the Hakedis, which fails.

        restHakedisMockMvc.perform(post("/api/hakediss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hakedis)))
                .andExpect(status().isBadRequest());

        List<Hakedis> hakediss = hakedisRepository.findAll();
        assertThat(hakediss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBitTarihIsRequired() throws Exception {
        int databaseSizeBeforeTest = hakedisRepository.findAll().size();
        // set the field null
        hakedis.setBitTarih(null);

        // Create the Hakedis, which fails.

        restHakedisMockMvc.perform(post("/api/hakediss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hakedis)))
                .andExpect(status().isBadRequest());

        List<Hakedis> hakediss = hakedisRepository.findAll();
        assertThat(hakediss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHakediss() throws Exception {
        // Initialize the database
        hakedisRepository.saveAndFlush(hakedis);

        // Get all the hakediss
        restHakedisMockMvc.perform(get("/api/hakediss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hakedis.getId().intValue())))
                .andExpect(jsonPath("$.[*].tarih").value(hasItem(DEFAULT_TARIH.toString())))
                .andExpect(jsonPath("$.[*].basTarih").value(hasItem(DEFAULT_BAS_TARIH.toString())))
                .andExpect(jsonPath("$.[*].bitTarih").value(hasItem(DEFAULT_BIT_TARIH.toString())))
                .andExpect(jsonPath("$.[*].prim").value(hasItem(DEFAULT_PRIM.doubleValue())));
    }

    @Test
    @Transactional
    public void getHakedis() throws Exception {
        // Initialize the database
        hakedisRepository.saveAndFlush(hakedis);

        // Get the hakedis
        restHakedisMockMvc.perform(get("/api/hakediss/{id}", hakedis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hakedis.getId().intValue()))
            .andExpect(jsonPath("$.tarih").value(DEFAULT_TARIH.toString()))
            .andExpect(jsonPath("$.basTarih").value(DEFAULT_BAS_TARIH.toString()))
            .andExpect(jsonPath("$.bitTarih").value(DEFAULT_BIT_TARIH.toString()))
            .andExpect(jsonPath("$.prim").value(DEFAULT_PRIM.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHakedis() throws Exception {
        // Get the hakedis
        restHakedisMockMvc.perform(get("/api/hakediss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHakedis() throws Exception {
        // Initialize the database
        hakedisRepository.saveAndFlush(hakedis);

		int databaseSizeBeforeUpdate = hakedisRepository.findAll().size();

        // Update the hakedis
        hakedis.setTarih(UPDATED_TARIH);
        hakedis.setBasTarih(UPDATED_BAS_TARIH);
        hakedis.setBitTarih(UPDATED_BIT_TARIH);
        hakedis.setPrim(UPDATED_PRIM);

        restHakedisMockMvc.perform(put("/api/hakediss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hakedis)))
                .andExpect(status().isOk());

        // Validate the Hakedis in the database
        List<Hakedis> hakediss = hakedisRepository.findAll();
        assertThat(hakediss).hasSize(databaseSizeBeforeUpdate);
        Hakedis testHakedis = hakediss.get(hakediss.size() - 1);
        assertThat(testHakedis.getTarih()).isEqualTo(UPDATED_TARIH);
        assertThat(testHakedis.getBasTarih()).isEqualTo(UPDATED_BAS_TARIH);
        assertThat(testHakedis.getBitTarih()).isEqualTo(UPDATED_BIT_TARIH);
        assertThat(testHakedis.getPrim()).isEqualTo(UPDATED_PRIM);
    }

    @Test
    @Transactional
    public void deleteHakedis() throws Exception {
        // Initialize the database
        hakedisRepository.saveAndFlush(hakedis);

		int databaseSizeBeforeDelete = hakedisRepository.findAll().size();

        // Get the hakedis
        restHakedisMockMvc.perform(delete("/api/hakediss/{id}", hakedis.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hakedis> hakediss = hakedisRepository.findAll();
        assertThat(hakediss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
