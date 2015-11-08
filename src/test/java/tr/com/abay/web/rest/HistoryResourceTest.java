package tr.com.abay.web.rest;

import tr.com.abay.Application;
import tr.com.abay.domain.History;
import tr.com.abay.repository.HistoryRepository;

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
 * Test class for the HistoryResource REST controller.
 *
 * @see HistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HistoryResourceTest {

    private static final String DEFAULT_KOLON_ADI = "AAAAA";
    private static final String UPDATED_KOLON_ADI = "BBBBB";
    private static final String DEFAULT_DEGER = "AAAAA";
    private static final String UPDATED_DEGER = "BBBBB";
    private static final String DEFAULT_TABLO_ADI = "AAAAA";
    private static final String UPDATED_TABLO_ADI = "BBBBB";

    private static final LocalDate DEFAULT_TARIH = new LocalDate(0L);
    private static final LocalDate UPDATED_TARIH = new LocalDate();

    private static final Long DEFAULT_REC_ID = 1L;
    private static final Long UPDATED_REC_ID = 2L;

    @Inject
    private HistoryRepository historyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHistoryMockMvc;

    private History history;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HistoryResource historyResource = new HistoryResource();
        ReflectionTestUtils.setField(historyResource, "historyRepository", historyRepository);
        this.restHistoryMockMvc = MockMvcBuilders.standaloneSetup(historyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        history = new History();
        history.setKolonAdi(DEFAULT_KOLON_ADI);
        history.setDeger(DEFAULT_DEGER);
        history.setTabloAdi(DEFAULT_TABLO_ADI);
        history.setTarih(DEFAULT_TARIH);
        history.setRecId(DEFAULT_REC_ID);
    }

    @Test
    @Transactional
    public void createHistory() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History

        restHistoryMockMvc.perform(post("/api/historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(history)))
                .andExpect(status().isCreated());

        // Validate the History in the database
        List<History> historys = historyRepository.findAll();
        assertThat(historys).hasSize(databaseSizeBeforeCreate + 1);
        History testHistory = historys.get(historys.size() - 1);
        assertThat(testHistory.getKolonAdi()).isEqualTo(DEFAULT_KOLON_ADI);
        assertThat(testHistory.getDeger()).isEqualTo(DEFAULT_DEGER);
        assertThat(testHistory.getTabloAdi()).isEqualTo(DEFAULT_TABLO_ADI);
        assertThat(testHistory.getTarih()).isEqualTo(DEFAULT_TARIH);
        assertThat(testHistory.getRecId()).isEqualTo(DEFAULT_REC_ID);
    }

    @Test
    @Transactional
    public void getAllHistorys() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historys
        restHistoryMockMvc.perform(get("/api/historys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
                .andExpect(jsonPath("$.[*].kolonAdi").value(hasItem(DEFAULT_KOLON_ADI.toString())))
                .andExpect(jsonPath("$.[*].deger").value(hasItem(DEFAULT_DEGER.toString())))
                .andExpect(jsonPath("$.[*].tabloAdi").value(hasItem(DEFAULT_TABLO_ADI.toString())))
                .andExpect(jsonPath("$.[*].tarih").value(hasItem(DEFAULT_TARIH.toString())))
                .andExpect(jsonPath("$.[*].recId").value(hasItem(DEFAULT_REC_ID.intValue())));
    }

    @Test
    @Transactional
    public void getHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get the history
        restHistoryMockMvc.perform(get("/api/historys/{id}", history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(history.getId().intValue()))
            .andExpect(jsonPath("$.kolonAdi").value(DEFAULT_KOLON_ADI.toString()))
            .andExpect(jsonPath("$.deger").value(DEFAULT_DEGER.toString()))
            .andExpect(jsonPath("$.tabloAdi").value(DEFAULT_TABLO_ADI.toString()))
            .andExpect(jsonPath("$.tarih").value(DEFAULT_TARIH.toString()))
            .andExpect(jsonPath("$.recId").value(DEFAULT_REC_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHistory() throws Exception {
        // Get the history
        restHistoryMockMvc.perform(get("/api/historys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

		int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history
        history.setKolonAdi(UPDATED_KOLON_ADI);
        history.setDeger(UPDATED_DEGER);
        history.setTabloAdi(UPDATED_TABLO_ADI);
        history.setTarih(UPDATED_TARIH);
        history.setRecId(UPDATED_REC_ID);

        restHistoryMockMvc.perform(put("/api/historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(history)))
                .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historys = historyRepository.findAll();
        assertThat(historys).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historys.get(historys.size() - 1);
        assertThat(testHistory.getKolonAdi()).isEqualTo(UPDATED_KOLON_ADI);
        assertThat(testHistory.getDeger()).isEqualTo(UPDATED_DEGER);
        assertThat(testHistory.getTabloAdi()).isEqualTo(UPDATED_TABLO_ADI);
        assertThat(testHistory.getTarih()).isEqualTo(UPDATED_TARIH);
        assertThat(testHistory.getRecId()).isEqualTo(UPDATED_REC_ID);
    }

    @Test
    @Transactional
    public void deleteHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

		int databaseSizeBeforeDelete = historyRepository.findAll().size();

        // Get the history
        restHistoryMockMvc.perform(delete("/api/historys/{id}", history.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<History> historys = historyRepository.findAll();
        assertThat(historys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
