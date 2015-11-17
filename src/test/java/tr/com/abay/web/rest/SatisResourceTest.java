package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.Satis;
import tr.com.abay.repository.SatisRepository;

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
 * Test class for the SatisResource REST controller.
 *
 * @see SatisResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SatisResourceTest {

    private static final String DEFAULT_FATURA_NO = "AAAAA";
    private static final String UPDATED_FATURA_NO = "BBBBB";

    private static final LocalDate DEFAULT_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_TARIH = new LocalDate();

    private static final Double DEFAULT_TUTAR = 0D;
    private static final Double UPDATED_TUTAR = 1D;

    private static final LocalDate DEFAULT_FATURA_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_FATURA_TARIH = new LocalDate();

    @Inject
    private SatisRepository satisRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSatisMockMvc;

    private Satis satis;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SatisResource satisResource = new SatisResource();
        ReflectionTestUtils.setField(satisResource, "satisRepository", satisRepository);
        this.restSatisMockMvc = MockMvcBuilders.standaloneSetup(satisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        satis = new Satis();
        satis.setFaturaNo(DEFAULT_FATURA_NO);
        satis.setTarih(DEFAULT_TARIH);
        satis.setTutar(DEFAULT_TUTAR);
        satis.setFaturaTarih(DEFAULT_FATURA_TARIH);
    }

    @Test
    @Transactional
    public void createSatis() throws Exception {
        int databaseSizeBeforeCreate = satisRepository.findAll().size();

        // Create the Satis

        restSatisMockMvc.perform(post("/api/satiss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satis)))
                .andExpect(status().isCreated());

        // Validate the Satis in the database
        List<Satis> satiss = satisRepository.findAll();
        assertThat(satiss).hasSize(databaseSizeBeforeCreate + 1);
        Satis testSatis = satiss.get(satiss.size() - 1);
        assertThat(testSatis.getFaturaNo()).isEqualTo(DEFAULT_FATURA_NO);
        assertThat(testSatis.getTarih()).isEqualTo(DEFAULT_TARIH);
        assertThat(testSatis.getTutar()).isEqualTo(DEFAULT_TUTAR);
        assertThat(testSatis.getFaturaTarih()).isEqualTo(DEFAULT_FATURA_TARIH);
    }

    @Test
    @Transactional
    public void checkTutarIsRequired() throws Exception {
        int databaseSizeBeforeTest = satisRepository.findAll().size();
        // set the field null
        satis.setTutar(null);

        // Create the Satis, which fails.

        restSatisMockMvc.perform(post("/api/satiss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satis)))
                .andExpect(status().isBadRequest());

        List<Satis> satiss = satisRepository.findAll();
        assertThat(satiss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSatiss() throws Exception {
        // Initialize the database
        satisRepository.saveAndFlush(satis);

        // Get all the satiss
        restSatisMockMvc.perform(get("/api/satiss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(satis.getId().intValue())))
                .andExpect(jsonPath("$.[*].faturaNo").value(hasItem(DEFAULT_FATURA_NO.toString())))
                .andExpect(jsonPath("$.[*].tarih").value(hasItem(DEFAULT_TARIH.toString())))
                .andExpect(jsonPath("$.[*].tutar").value(hasItem(DEFAULT_TUTAR.doubleValue())))
                .andExpect(jsonPath("$.[*].faturaTarih").value(hasItem(DEFAULT_FATURA_TARIH.toString())));
    }

    @Test
    @Transactional
    public void getSatis() throws Exception {
        // Initialize the database
        satisRepository.saveAndFlush(satis);

        // Get the satis
        restSatisMockMvc.perform(get("/api/satiss/{id}", satis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(satis.getId().intValue()))
            .andExpect(jsonPath("$.faturaNo").value(DEFAULT_FATURA_NO.toString()))
            .andExpect(jsonPath("$.tarih").value(DEFAULT_TARIH.toString()))
            .andExpect(jsonPath("$.tutar").value(DEFAULT_TUTAR.doubleValue()))
            .andExpect(jsonPath("$.faturaTarih").value(DEFAULT_FATURA_TARIH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSatis() throws Exception {
        // Get the satis
        restSatisMockMvc.perform(get("/api/satiss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSatis() throws Exception {
        // Initialize the database
        satisRepository.saveAndFlush(satis);

		int databaseSizeBeforeUpdate = satisRepository.findAll().size();

        // Update the satis
        satis.setFaturaNo(UPDATED_FATURA_NO);
        satis.setTarih(UPDATED_TARIH);
        satis.setTutar(UPDATED_TUTAR);
        satis.setFaturaTarih(UPDATED_FATURA_TARIH);

        restSatisMockMvc.perform(put("/api/satiss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satis)))
                .andExpect(status().isOk());

        // Validate the Satis in the database
        List<Satis> satiss = satisRepository.findAll();
        assertThat(satiss).hasSize(databaseSizeBeforeUpdate);
        Satis testSatis = satiss.get(satiss.size() - 1);
        assertThat(testSatis.getFaturaNo()).isEqualTo(UPDATED_FATURA_NO);
        assertThat(testSatis.getTarih()).isEqualTo(UPDATED_TARIH);
        assertThat(testSatis.getTutar()).isEqualTo(UPDATED_TUTAR);
        assertThat(testSatis.getFaturaTarih()).isEqualTo(UPDATED_FATURA_TARIH);
    }

    @Test
    @Transactional
    public void deleteSatis() throws Exception {
        // Initialize the database
        satisRepository.saveAndFlush(satis);

		int databaseSizeBeforeDelete = satisRepository.findAll().size();

        // Get the satis
        restSatisMockMvc.perform(delete("/api/satiss/{id}", satis.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Satis> satiss = satisRepository.findAll();
        assertThat(satiss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
