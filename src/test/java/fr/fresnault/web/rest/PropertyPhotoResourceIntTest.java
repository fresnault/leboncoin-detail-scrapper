package fr.fresnault.web.rest;

import fr.fresnault.LeboncoinDetailScrapperApp;

import fr.fresnault.domain.PropertyPhoto;
import fr.fresnault.repository.PropertyPhotoRepository;
import fr.fresnault.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;


import static fr.fresnault.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PropertyPhotoResource REST controller.
 *
 * @see PropertyPhotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeboncoinDetailScrapperApp.class)
public class PropertyPhotoResourceIntTest {

    private static final Integer DEFAULT_PHOTO_SEQ = 1;
    private static final Integer UPDATED_PHOTO_SEQ = 2;

    private static final String DEFAULT_PHOTO_THUMB_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_THUMB_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    @Autowired
    private PropertyPhotoRepository propertyPhotoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restPropertyPhotoMockMvc;

    private PropertyPhoto propertyPhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PropertyPhotoResource propertyPhotoResource = new PropertyPhotoResource(propertyPhotoRepository);
        this.restPropertyPhotoMockMvc = MockMvcBuilders.standaloneSetup(propertyPhotoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyPhoto createEntity() {
        PropertyPhoto propertyPhoto = new PropertyPhoto()
            .photoSeq(DEFAULT_PHOTO_SEQ)
            .photoThumbUrl(DEFAULT_PHOTO_THUMB_URL)
            .photoUrl(DEFAULT_PHOTO_URL);
        return propertyPhoto;
    }

    @Before
    public void initTest() {
        propertyPhotoRepository.deleteAll();
        propertyPhoto = createEntity();
    }

    @Test
    public void createPropertyPhoto() throws Exception {
        int databaseSizeBeforeCreate = propertyPhotoRepository.findAll().size();

        // Create the PropertyPhoto
        restPropertyPhotoMockMvc.perform(post("/api/property-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propertyPhoto)))
            .andExpect(status().isCreated());

        // Validate the PropertyPhoto in the database
        List<PropertyPhoto> propertyPhotoList = propertyPhotoRepository.findAll();
        assertThat(propertyPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        PropertyPhoto testPropertyPhoto = propertyPhotoList.get(propertyPhotoList.size() - 1);
        assertThat(testPropertyPhoto.getPhotoSeq()).isEqualTo(DEFAULT_PHOTO_SEQ);
        assertThat(testPropertyPhoto.getPhotoThumbUrl()).isEqualTo(DEFAULT_PHOTO_THUMB_URL);
        assertThat(testPropertyPhoto.getPhotoUrl()).isEqualTo(DEFAULT_PHOTO_URL);
    }

    @Test
    public void createPropertyPhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propertyPhotoRepository.findAll().size();

        // Create the PropertyPhoto with an existing ID
        propertyPhoto.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyPhotoMockMvc.perform(post("/api/property-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propertyPhoto)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyPhoto in the database
        List<PropertyPhoto> propertyPhotoList = propertyPhotoRepository.findAll();
        assertThat(propertyPhotoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllPropertyPhotos() throws Exception {
        // Initialize the database
        propertyPhotoRepository.save(propertyPhoto);

        // Get all the propertyPhotoList
        restPropertyPhotoMockMvc.perform(get("/api/property-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyPhoto.getId())))
            .andExpect(jsonPath("$.[*].photoSeq").value(hasItem(DEFAULT_PHOTO_SEQ)))
            .andExpect(jsonPath("$.[*].photoThumbUrl").value(hasItem(DEFAULT_PHOTO_THUMB_URL.toString())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL.toString())));
    }
    
    @Test
    public void getPropertyPhoto() throws Exception {
        // Initialize the database
        propertyPhotoRepository.save(propertyPhoto);

        // Get the propertyPhoto
        restPropertyPhotoMockMvc.perform(get("/api/property-photos/{id}", propertyPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(propertyPhoto.getId()))
            .andExpect(jsonPath("$.photoSeq").value(DEFAULT_PHOTO_SEQ))
            .andExpect(jsonPath("$.photoThumbUrl").value(DEFAULT_PHOTO_THUMB_URL.toString()))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL.toString()));
    }

    @Test
    public void getNonExistingPropertyPhoto() throws Exception {
        // Get the propertyPhoto
        restPropertyPhotoMockMvc.perform(get("/api/property-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePropertyPhoto() throws Exception {
        // Initialize the database
        propertyPhotoRepository.save(propertyPhoto);

        int databaseSizeBeforeUpdate = propertyPhotoRepository.findAll().size();

        // Update the propertyPhoto
        PropertyPhoto updatedPropertyPhoto = propertyPhotoRepository.findById(propertyPhoto.getId()).get();
        updatedPropertyPhoto
            .photoSeq(UPDATED_PHOTO_SEQ)
            .photoThumbUrl(UPDATED_PHOTO_THUMB_URL)
            .photoUrl(UPDATED_PHOTO_URL);

        restPropertyPhotoMockMvc.perform(put("/api/property-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPropertyPhoto)))
            .andExpect(status().isOk());

        // Validate the PropertyPhoto in the database
        List<PropertyPhoto> propertyPhotoList = propertyPhotoRepository.findAll();
        assertThat(propertyPhotoList).hasSize(databaseSizeBeforeUpdate);
        PropertyPhoto testPropertyPhoto = propertyPhotoList.get(propertyPhotoList.size() - 1);
        assertThat(testPropertyPhoto.getPhotoSeq()).isEqualTo(UPDATED_PHOTO_SEQ);
        assertThat(testPropertyPhoto.getPhotoThumbUrl()).isEqualTo(UPDATED_PHOTO_THUMB_URL);
        assertThat(testPropertyPhoto.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    public void updateNonExistingPropertyPhoto() throws Exception {
        int databaseSizeBeforeUpdate = propertyPhotoRepository.findAll().size();

        // Create the PropertyPhoto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyPhotoMockMvc.perform(put("/api/property-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propertyPhoto)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyPhoto in the database
        List<PropertyPhoto> propertyPhotoList = propertyPhotoRepository.findAll();
        assertThat(propertyPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePropertyPhoto() throws Exception {
        // Initialize the database
        propertyPhotoRepository.save(propertyPhoto);

        int databaseSizeBeforeDelete = propertyPhotoRepository.findAll().size();

        // Get the propertyPhoto
        restPropertyPhotoMockMvc.perform(delete("/api/property-photos/{id}", propertyPhoto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PropertyPhoto> propertyPhotoList = propertyPhotoRepository.findAll();
        assertThat(propertyPhotoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyPhoto.class);
        PropertyPhoto propertyPhoto1 = new PropertyPhoto();
        propertyPhoto1.setId("id1");
        PropertyPhoto propertyPhoto2 = new PropertyPhoto();
        propertyPhoto2.setId(propertyPhoto1.getId());
        assertThat(propertyPhoto1).isEqualTo(propertyPhoto2);
        propertyPhoto2.setId("id2");
        assertThat(propertyPhoto1).isNotEqualTo(propertyPhoto2);
        propertyPhoto1.setId(null);
        assertThat(propertyPhoto1).isNotEqualTo(propertyPhoto2);
    }
}
