package fr.fresnault.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.fresnault.domain.Property;
import fr.fresnault.repository.PropertyRepository;
import fr.fresnault.service.PropertyService;
import fr.fresnault.web.rest.errors.BadRequestAlertException;
import fr.fresnault.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Property.
 */
@RestController
@RequestMapping("/api")
public class PropertyResource {

	private final Logger log = LoggerFactory.getLogger(PropertyResource.class);

	private static final String ENTITY_NAME = "leboncoinDetailScrapperProperty";

	private final PropertyRepository propertyRepository;

	private final PropertyService propertyService;

	public PropertyResource(PropertyRepository propertyRepository, PropertyService propertyService) {
		this.propertyRepository = propertyRepository;
		this.propertyService = propertyService;
	}

	/**
	 * POST /properties : Create a new property.
	 *
	 * @param property
	 *            the property to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new property, or with status 400 (Bad Request) if the property
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/properties")
	@Timed
	public ResponseEntity<Property> createProperty(@Valid @RequestBody Property property) throws URISyntaxException {
		log.debug("REST request to save Property : {}", property);
		if (property.getId() != null) {
			throw new BadRequestAlertException("A new property cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Property result = propertyRepository.save(property);
		return ResponseEntity.created(new URI("/api/properties/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /properties : Updates an existing property.
	 *
	 * @param property
	 *            the property to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         property, or with status 400 (Bad Request) if the property is not
	 *         valid, or with status 500 (Internal Server Error) if the property
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/properties")
	@Timed
	public ResponseEntity<Property> updateProperty(@Valid @RequestBody Property property) throws URISyntaxException {
		log.debug("REST request to update Property : {}", property);
		if (property.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}

		Property result = null;

		try {
			result = propertyService.scrapProperty(property);
			propertyRepository.save(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, property.getId().toString()))
				.body(result);
	}

	/**
	 * GET /properties : get all the properties.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         properties in body
	 */
	@GetMapping("/properties")
	@Timed
	public List<Property> getAllProperties() {
		log.debug("REST request to get all Properties");
		return propertyRepository.findAll();
	}

	/**
	 * GET /properties/:id : get the "id" property.
	 *
	 * @param id
	 *            the id of the property to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         property, or with status 404 (Not Found)
	 */
	@GetMapping("/properties/{id}")
	@Timed
	public ResponseEntity<Property> getProperty(@PathVariable String id) {
		log.debug("REST request to get Property : {}", id);
		Optional<Property> property = propertyRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(property);
	}

	/**
	 * DELETE /properties/:id : delete the "id" property.
	 *
	 * @param id
	 *            the id of the property to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/properties/{id}")
	@Timed
	public ResponseEntity<Void> deleteProperty(@PathVariable String id) {
		log.debug("REST request to delete Property : {}", id);

		propertyRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
	}
}
