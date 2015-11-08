package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.Calisan;
import tr.com.abay.repository.CalisanRepository;

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
 * Test class for the CalisanResource REST controller.
 *
 * @see CalisanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CalisanResourceTest {

    private static final String DEFAULT_ADI = "A";
    private static final String UPDATED_ADI = "B";

    @Inject
    private CalisanRepository calisanRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCalisanMockMvc;

    private Calisan calisan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CalisanResource calisanResource = new CalisanResource();
        ReflectionTestUtils.setField(calisanResource, "calisanRepository", calisanRepository);
        this.restCalisanMockMvc = MockMvcBuilders.standaloneSetup(calisanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        calisan = new Calisan();
        calisan.setAdi(DEFAULT_ADI);
    }

    @Test
    @Transactional
    public void createCalisan() throws Exception {
        int databaseSizeBeforeCreate = calisanRepository.findAll().size();

        // Create the Calisan

        restCalisanMockMvc.perform(post("/api/calisans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calisan)))
                .andExpect(status().isCreated());

        // Validate the Calisan in the database
        List<Calisan> calisans = calisanRepository.findAll();
        assertThat(calisans).hasSize(databaseSizeBeforeCreate + 1);
        Calisan testCalisan = calisans.get(calisans.size() - 1);
        assertThat(testCalisan.getAdi()).isEqualTo(DEFAULT_ADI);
    }

    @Test
    @Transactional
    public void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = calisanRepository.findAll().size();
        // set the field null
        calisan.setAdi(null);

        // Create the Calisan, which fails.

        restCalisanMockMvc.perform(post("/api/calisans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calisan)))
                .andExpect(status().isBadRequest());

        List<Calisan> calisans = calisanRepository.findAll();
        assertThat(calisans).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalisans() throws Exception {
        // Initialize the database
        calisanRepository.saveAndFlush(calisan);

        // Get all the calisans
        restCalisanMockMvc.perform(get("/api/calisans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(calisan.getId().intValue())))
                .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI.toString())));
    }

    @Test
    @Transactional
    public void getCalisan() throws Exception {
        // Initialize the database
        calisanRepository.saveAndFlush(calisan);

        // Get the calisan
        restCalisanMockMvc.perform(get("/api/calisans/{id}", calisan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(calisan.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCalisan() throws Exception {
        // Get the calisan
        restCalisanMockMvc.perform(get("/api/calisans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalisan() throws Exception {
        // Initialize the database
        calisanRepository.saveAndFlush(calisan);

		int databaseSizeBeforeUpdate = calisanRepository.findAll().size();

        // Update the calisan
        calisan.setAdi(UPDATED_ADI);

        restCalisanMockMvc.perform(put("/api/calisans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calisan)))
                .andExpect(status().isOk());

        // Validate the Calisan in the database
        List<Calisan> calisans = calisanRepository.findAll();
        assertThat(calisans).hasSize(databaseSizeBeforeUpdate);
        Calisan testCalisan = calisans.get(calisans.size() - 1);
        assertThat(testCalisan.getAdi()).isEqualTo(UPDATED_ADI);
    }

    @Test
    @Transactional
    public void deleteCalisan() throws Exception {
        // Initialize the database
        calisanRepository.saveAndFlush(calisan);

		int databaseSizeBeforeDelete = calisanRepository.findAll().size();

        // Get the calisan
        restCalisanMockMvc.perform(delete("/api/calisans/{id}", calisan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Calisan> calisans = calisanRepository.findAll();
        assertThat(calisans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
