package fr.fresnault.web.rest;

import fr.fresnault.LeboncoinDetailScrapperApp;

import fr.fresnault.domain.Property;
import fr.fresnault.repository.PropertyRepository;
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
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static fr.fresnault.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.fresnault.domain.enumeration.Source;
import fr.fresnault.domain.enumeration.Transaction;
import fr.fresnault.domain.enumeration.Type;
/**
 * Test class for the PropertyResource REST controller.
 *
 * @see PropertyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeboncoinDetailScrapperApp.class)
public class PropertyResourceIntTest {

    private static final Source DEFAULT_REF_SOURCE = Source.LEBONCOIN;
    private static final Source UPDATED_REF_SOURCE = Source.LEBONCOIN;

    private static final String DEFAULT_REF_ID = "AAAAAAAAAA";
    private static final String UPDATED_REF_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Transaction DEFAULT_TRANSACTION = Transaction.SELL;
    private static final Transaction UPDATED_TRANSACTION = Transaction.RENT;

    private static final Type DEFAULT_TYPE = Type.HOUSE;
    private static final Type UPDATED_TYPE = Type.FLAT;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_ROOM_COUNT = 1;
    private static final Integer UPDATED_ROOM_COUNT = 2;

    private static final Integer DEFAULT_BEDROOM_COUNT = 1;
    private static final Integer UPDATED_BEDROOM_COUNT = 2;

    private static final BigDecimal DEFAULT_LIVING_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIVING_AREA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SURFACE_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_SURFACE_AREA = new BigDecimal(2);

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restPropertyMockMvc;

    private Property property;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PropertyResource propertyResource = new PropertyResource(propertyRepository);
        this.restPropertyMockMvc = MockMvcBuilders.standaloneSetup(propertyResource)
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
    public static Property createEntity() {
        Property property = new Property()
            .refSource(DEFAULT_REF_SOURCE)
            .refId(DEFAULT_REF_ID)
            .name(DEFAULT_NAME)
            .transaction(DEFAULT_TRANSACTION)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .price(DEFAULT_PRICE)
            .roomCount(DEFAULT_ROOM_COUNT)
            .bedroomCount(DEFAULT_BEDROOM_COUNT)
            .livingArea(DEFAULT_LIVING_AREA)
            .surfaceArea(DEFAULT_SURFACE_AREA)
            .url(DEFAULT_URL);
        return property;
    }

    @Before
    public void initTest() {
        propertyRepository.deleteAll();
        property = createEntity();
    }

    @Test
    public void createProperty() throws Exception {
        int databaseSizeBeforeCreate = propertyRepository.findAll().size();

        // Create the Property
        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isCreated());

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeCreate + 1);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getRefSource()).isEqualTo(DEFAULT_REF_SOURCE);
        assertThat(testProperty.getRefId()).isEqualTo(DEFAULT_REF_ID);
        assertThat(testProperty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProperty.getTransaction()).isEqualTo(DEFAULT_TRANSACTION);
        assertThat(testProperty.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProperty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProperty.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProperty.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProperty.getRoomCount()).isEqualTo(DEFAULT_ROOM_COUNT);
        assertThat(testProperty.getBedroomCount()).isEqualTo(DEFAULT_BEDROOM_COUNT);
        assertThat(testProperty.getLivingArea()).isEqualTo(DEFAULT_LIVING_AREA);
        assertThat(testProperty.getSurfaceArea()).isEqualTo(DEFAULT_SURFACE_AREA);
        assertThat(testProperty.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    public void createPropertyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propertyRepository.findAll().size();

        // Create the Property with an existing ID
        property.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkRefSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setRefSource(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkRefIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setRefId(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setName(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTransactionIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setTransaction(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setType(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setCreatedDate(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setUrl(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllProperties() throws Exception {
        // Initialize the database
        propertyRepository.save(property);

        // Get all the propertyList
        restPropertyMockMvc.perform(get("/api/properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(property.getId())))
            .andExpect(jsonPath("$.[*].refSource").value(hasItem(DEFAULT_REF_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].refId").value(hasItem(DEFAULT_REF_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].roomCount").value(hasItem(DEFAULT_ROOM_COUNT)))
            .andExpect(jsonPath("$.[*].bedroomCount").value(hasItem(DEFAULT_BEDROOM_COUNT)))
            .andExpect(jsonPath("$.[*].livingArea").value(hasItem(DEFAULT_LIVING_AREA.intValue())))
            .andExpect(jsonPath("$.[*].surfaceArea").value(hasItem(DEFAULT_SURFACE_AREA.intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    public void getProperty() throws Exception {
        // Initialize the database
        propertyRepository.save(property);

        // Get the property
        restPropertyMockMvc.perform(get("/api/properties/{id}", property.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(property.getId()))
            .andExpect(jsonPath("$.refSource").value(DEFAULT_REF_SOURCE.toString()))
            .andExpect(jsonPath("$.refId").value(DEFAULT_REF_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.transaction").value(DEFAULT_TRANSACTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.roomCount").value(DEFAULT_ROOM_COUNT))
            .andExpect(jsonPath("$.bedroomCount").value(DEFAULT_BEDROOM_COUNT))
            .andExpect(jsonPath("$.livingArea").value(DEFAULT_LIVING_AREA.intValue()))
            .andExpect(jsonPath("$.surfaceArea").value(DEFAULT_SURFACE_AREA.intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    public void getNonExistingProperty() throws Exception {
        // Get the property
        restPropertyMockMvc.perform(get("/api/properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateProperty() throws Exception {
        // Initialize the database
        propertyRepository.save(property);

        int databaseSizeBeforeUpdate = propertyRepository.findAll().size();

        // Update the property
        Property updatedProperty = propertyRepository.findById(property.getId()).get();
        updatedProperty
            .refSource(UPDATED_REF_SOURCE)
            .refId(UPDATED_REF_ID)
            .name(UPDATED_NAME)
            .transaction(UPDATED_TRANSACTION)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .price(UPDATED_PRICE)
            .roomCount(UPDATED_ROOM_COUNT)
            .bedroomCount(UPDATED_BEDROOM_COUNT)
            .livingArea(UPDATED_LIVING_AREA)
            .surfaceArea(UPDATED_SURFACE_AREA)
            .url(UPDATED_URL);

        restPropertyMockMvc.perform(put("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProperty)))
            .andExpect(status().isOk());

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getRefSource()).isEqualTo(UPDATED_REF_SOURCE);
        assertThat(testProperty.getRefId()).isEqualTo(UPDATED_REF_ID);
        assertThat(testProperty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProperty.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testProperty.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProperty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProperty.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProperty.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProperty.getRoomCount()).isEqualTo(UPDATED_ROOM_COUNT);
        assertThat(testProperty.getBedroomCount()).isEqualTo(UPDATED_BEDROOM_COUNT);
        assertThat(testProperty.getLivingArea()).isEqualTo(UPDATED_LIVING_AREA);
        assertThat(testProperty.getSurfaceArea()).isEqualTo(UPDATED_SURFACE_AREA);
        assertThat(testProperty.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    public void updateNonExistingProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().size();

        // Create the Property

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyMockMvc.perform(put("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProperty() throws Exception {
        // Initialize the database
        propertyRepository.save(property);

        int databaseSizeBeforeDelete = propertyRepository.findAll().size();

        // Get the property
        restPropertyMockMvc.perform(delete("/api/properties/{id}", property.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Property.class);
        Property property1 = new Property();
        property1.setId("id1");
        Property property2 = new Property();
        property2.setId(property1.getId());
        assertThat(property1).isEqualTo(property2);
        property2.setId("id2");
        assertThat(property1).isNotEqualTo(property2);
        property1.setId(null);
        assertThat(property1).isNotEqualTo(property2);
    }
}
