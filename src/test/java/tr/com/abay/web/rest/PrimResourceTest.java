package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.Prim;
import tr.com.abay.repository.PrimRepository;

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
 * Test class for the PrimResource REST controller.
 *
 * @see PrimResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrimResourceTest {

    private static final String DEFAULT_ADI = "AAAAA";
    private static final String UPDATED_ADI = "BBBBB";

    private static final Double DEFAULT_YUZDE = 0D;
    private static final Double UPDATED_YUZDE = 1D;

    @Inject
    private PrimRepository primRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPrimMockMvc;

    private Prim prim;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrimResource primResource = new PrimResource();
        ReflectionTestUtils.setField(primResource, "primRepository", primRepository);
        this.restPrimMockMvc = MockMvcBuilders.standaloneSetup(primResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        prim = new Prim();
        prim.setAdi(DEFAULT_ADI);
        prim.setYuzde(DEFAULT_YUZDE);
    }

    @Test
    @Transactional
    public void createPrim() throws Exception {
        int databaseSizeBeforeCreate = primRepository.findAll().size();

        // Create the Prim

        restPrimMockMvc.perform(post("/api/prims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prim)))
                .andExpect(status().isCreated());

        // Validate the Prim in the database
        List<Prim> prims = primRepository.findAll();
        assertThat(prims).hasSize(databaseSizeBeforeCreate + 1);
        Prim testPrim = prims.get(prims.size() - 1);
        assertThat(testPrim.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testPrim.getYuzde()).isEqualTo(DEFAULT_YUZDE);
    }

    @Test
    @Transactional
    public void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = primRepository.findAll().size();
        // set the field null
        prim.setAdi(null);

        // Create the Prim, which fails.

        restPrimMockMvc.perform(post("/api/prims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prim)))
                .andExpect(status().isBadRequest());

        List<Prim> prims = primRepository.findAll();
        assertThat(prims).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYuzdeIsRequired() throws Exception {
        int databaseSizeBeforeTest = primRepository.findAll().size();
        // set the field null
        prim.setYuzde(null);

        // Create the Prim, which fails.

        restPrimMockMvc.perform(post("/api/prims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prim)))
                .andExpect(status().isBadRequest());

        List<Prim> prims = primRepository.findAll();
        assertThat(prims).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrims() throws Exception {
        // Initialize the database
        primRepository.saveAndFlush(prim);

        // Get all the prims
        restPrimMockMvc.perform(get("/api/prims"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prim.getId().intValue())))
                .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI.toString())))
                .andExpect(jsonPath("$.[*].yuzde").value(hasItem(DEFAULT_YUZDE.doubleValue())));
    }

    @Test
    @Transactional
    public void getPrim() throws Exception {
        // Initialize the database
        primRepository.saveAndFlush(prim);

        // Get the prim
        restPrimMockMvc.perform(get("/api/prims/{id}", prim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prim.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI.toString()))
            .andExpect(jsonPath("$.yuzde").value(DEFAULT_YUZDE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrim() throws Exception {
        // Get the prim
        restPrimMockMvc.perform(get("/api/prims/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrim() throws Exception {
        // Initialize the database
        primRepository.saveAndFlush(prim);

		int databaseSizeBeforeUpdate = primRepository.findAll().size();

        // Update the prim
        prim.setAdi(UPDATED_ADI);
        prim.setYuzde(UPDATED_YUZDE);

        restPrimMockMvc.perform(put("/api/prims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prim)))
                .andExpect(status().isOk());

        // Validate the Prim in the database
        List<Prim> prims = primRepository.findAll();
        assertThat(prims).hasSize(databaseSizeBeforeUpdate);
        Prim testPrim = prims.get(prims.size() - 1);
        assertThat(testPrim.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testPrim.getYuzde()).isEqualTo(UPDATED_YUZDE);
    }

    @Test
    @Transactional
    public void deletePrim() throws Exception {
        // Initialize the database
        primRepository.saveAndFlush(prim);

		int databaseSizeBeforeDelete = primRepository.findAll().size();

        // Get the prim
        restPrimMockMvc.perform(delete("/api/prims/{id}", prim.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Prim> prims = primRepository.findAll();
        assertThat(prims).hasSize(databaseSizeBeforeDelete - 1);
    }
}
