package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.Havuz;
import tr.com.abay.repository.HavuzRepository;

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
 * Test class for the HavuzResource REST controller.
 *
 * @see HavuzResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HavuzResourceTest {


    private static final LocalDate DEFAULT_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_TARIH = new LocalDate();

    private static final LocalDate DEFAULT_BAS_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_BAS_TARIH = new LocalDate();

    private static final LocalDate DEFAULT_BIT_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_BIT_TARIH = new LocalDate();

    private static final Double DEFAULT_TUTAR = 1D;
    private static final Double UPDATED_TUTAR = 2D;

    @Inject
    private HavuzRepository havuzRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHavuzMockMvc;

    private Havuz havuz;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HavuzResource havuzResource = new HavuzResource();
        ReflectionTestUtils.setField(havuzResource, "havuzRepository", havuzRepository);
        this.restHavuzMockMvc = MockMvcBuilders.standaloneSetup(havuzResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        havuz = new Havuz();
        havuz.setTarih(DEFAULT_TARIH);
        havuz.setBasTarih(DEFAULT_BAS_TARIH);
        havuz.setBitTarih(DEFAULT_BIT_TARIH);
        havuz.setTutar(DEFAULT_TUTAR);
    }

    @Test
    @Transactional
    public void createHavuz() throws Exception {
        int databaseSizeBeforeCreate = havuzRepository.findAll().size();

        // Create the Havuz

        restHavuzMockMvc.perform(post("/api/havuzs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(havuz)))
                .andExpect(status().isCreated());

        // Validate the Havuz in the database
        List<Havuz> havuzs = havuzRepository.findAll();
        assertThat(havuzs).hasSize(databaseSizeBeforeCreate + 1);
        Havuz testHavuz = havuzs.get(havuzs.size() - 1);
        assertThat(testHavuz.getTarih()).isEqualTo(DEFAULT_TARIH);
        assertThat(testHavuz.getBasTarih()).isEqualTo(DEFAULT_BAS_TARIH);
        assertThat(testHavuz.getBitTarih()).isEqualTo(DEFAULT_BIT_TARIH);
        assertThat(testHavuz.getTutar()).isEqualTo(DEFAULT_TUTAR);
    }

    @Test
    @Transactional
    public void checkTarihIsRequired() throws Exception {
        int databaseSizeBeforeTest = havuzRepository.findAll().size();
        // set the field null
        havuz.setTarih(null);

        // Create the Havuz, which fails.

        restHavuzMockMvc.perform(post("/api/havuzs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(havuz)))
                .andExpect(status().isBadRequest());

        List<Havuz> havuzs = havuzRepository.findAll();
        assertThat(havuzs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBasTarihIsRequired() throws Exception {
        int databaseSizeBeforeTest = havuzRepository.findAll().size();
        // set the field null
        havuz.setBasTarih(null);

        // Create the Havuz, which fails.

        restHavuzMockMvc.perform(post("/api/havuzs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(havuz)))
                .andExpect(status().isBadRequest());

        List<Havuz> havuzs = havuzRepository.findAll();
        assertThat(havuzs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBitTarihIsRequired() throws Exception {
        int databaseSizeBeforeTest = havuzRepository.findAll().size();
        // set the field null
        havuz.setBitTarih(null);

        // Create the Havuz, which fails.

        restHavuzMockMvc.perform(post("/api/havuzs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(havuz)))
                .andExpect(status().isBadRequest());

        List<Havuz> havuzs = havuzRepository.findAll();
        assertThat(havuzs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHavuzs() throws Exception {
        // Initialize the database
        havuzRepository.saveAndFlush(havuz);

        // Get all the havuzs
        restHavuzMockMvc.perform(get("/api/havuzs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(havuz.getId().intValue())))
                .andExpect(jsonPath("$.[*].tarih").value(hasItem(DEFAULT_TARIH.toString())))
                .andExpect(jsonPath("$.[*].basTarih").value(hasItem(DEFAULT_BAS_TARIH.toString())))
                .andExpect(jsonPath("$.[*].bitTarih").value(hasItem(DEFAULT_BIT_TARIH.toString())))
                .andExpect(jsonPath("$.[*].tutar").value(hasItem(DEFAULT_TUTAR.doubleValue())));
    }

    @Test
    @Transactional
    public void getHavuz() throws Exception {
        // Initialize the database
        havuzRepository.saveAndFlush(havuz);

        // Get the havuz
        restHavuzMockMvc.perform(get("/api/havuzs/{id}", havuz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(havuz.getId().intValue()))
            .andExpect(jsonPath("$.tarih").value(DEFAULT_TARIH.toString()))
            .andExpect(jsonPath("$.basTarih").value(DEFAULT_BAS_TARIH.toString()))
            .andExpect(jsonPath("$.bitTarih").value(DEFAULT_BIT_TARIH.toString()))
            .andExpect(jsonPath("$.tutar").value(DEFAULT_TUTAR.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHavuz() throws Exception {
        // Get the havuz
        restHavuzMockMvc.perform(get("/api/havuzs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHavuz() throws Exception {
        // Initialize the database
        havuzRepository.saveAndFlush(havuz);

		int databaseSizeBeforeUpdate = havuzRepository.findAll().size();

        // Update the havuz
        havuz.setTarih(UPDATED_TARIH);
        havuz.setBasTarih(UPDATED_BAS_TARIH);
        havuz.setBitTarih(UPDATED_BIT_TARIH);
        havuz.setTutar(UPDATED_TUTAR);

        restHavuzMockMvc.perform(put("/api/havuzs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(havuz)))
                .andExpect(status().isOk());

        // Validate the Havuz in the database
        List<Havuz> havuzs = havuzRepository.findAll();
        assertThat(havuzs).hasSize(databaseSizeBeforeUpdate);
        Havuz testHavuz = havuzs.get(havuzs.size() - 1);
        assertThat(testHavuz.getTarih()).isEqualTo(UPDATED_TARIH);
        assertThat(testHavuz.getBasTarih()).isEqualTo(UPDATED_BAS_TARIH);
        assertThat(testHavuz.getBitTarih()).isEqualTo(UPDATED_BIT_TARIH);
        assertThat(testHavuz.getTutar()).isEqualTo(UPDATED_TUTAR);
    }

    @Test
    @Transactional
    public void deleteHavuz() throws Exception {
        // Initialize the database
        havuzRepository.saveAndFlush(havuz);

		int databaseSizeBeforeDelete = havuzRepository.findAll().size();

        // Get the havuz
        restHavuzMockMvc.perform(delete("/api/havuzs/{id}", havuz.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Havuz> havuzs = havuzRepository.findAll();
        assertThat(havuzs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
