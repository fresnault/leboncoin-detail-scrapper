package fr.fresnault.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.fresnault.domain.PropertyPhoto;
import fr.fresnault.repository.PropertyPhotoRepository;
import fr.fresnault.web.rest.errors.BadRequestAlertException;
import fr.fresnault.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PropertyPhoto.
 */
@RestController
@RequestMapping("/api")
public class PropertyPhotoResource {

    private final Logger log = LoggerFactory.getLogger(PropertyPhotoResource.class);

    private static final String ENTITY_NAME = "leboncoinDetailScrapperPropertyPhoto";

    private final PropertyPhotoRepository propertyPhotoRepository;

    public PropertyPhotoResource(PropertyPhotoRepository propertyPhotoRepository) {
        this.propertyPhotoRepository = propertyPhotoRepository;
    }

    /**
     * POST  /property-photos : Create a new propertyPhoto.
     *
     * @param propertyPhoto the propertyPhoto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new propertyPhoto, or with status 400 (Bad Request) if the propertyPhoto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/property-photos")
    @Timed
    public ResponseEntity<PropertyPhoto> createPropertyPhoto(@RequestBody PropertyPhoto propertyPhoto) throws URISyntaxException {
        log.debug("REST request to save PropertyPhoto : {}", propertyPhoto);
        if (propertyPhoto.getId() != null) {
            throw new BadRequestAlertException("A new propertyPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropertyPhoto result = propertyPhotoRepository.save(propertyPhoto);
        return ResponseEntity.created(new URI("/api/property-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /property-photos : Updates an existing propertyPhoto.
     *
     * @param propertyPhoto the propertyPhoto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated propertyPhoto,
     * or with status 400 (Bad Request) if the propertyPhoto is not valid,
     * or with status 500 (Internal Server Error) if the propertyPhoto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/property-photos")
    @Timed
    public ResponseEntity<PropertyPhoto> updatePropertyPhoto(@RequestBody PropertyPhoto propertyPhoto) throws URISyntaxException {
        log.debug("REST request to update PropertyPhoto : {}", propertyPhoto);
        if (propertyPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PropertyPhoto result = propertyPhotoRepository.save(propertyPhoto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, propertyPhoto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /property-photos : get all the propertyPhotos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of propertyPhotos in body
     */
    @GetMapping("/property-photos")
    @Timed
    public List<PropertyPhoto> getAllPropertyPhotos() {
        log.debug("REST request to get all PropertyPhotos");
        return propertyPhotoRepository.findAll();
    }

    /**
     * GET  /property-photos/:id : get the "id" propertyPhoto.
     *
     * @param id the id of the propertyPhoto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the propertyPhoto, or with status 404 (Not Found)
     */
    @GetMapping("/property-photos/{id}")
    @Timed
    public ResponseEntity<PropertyPhoto> getPropertyPhoto(@PathVariable String id) {
        log.debug("REST request to get PropertyPhoto : {}", id);
        Optional<PropertyPhoto> propertyPhoto = propertyPhotoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(propertyPhoto);
    }

    /**
     * DELETE  /property-photos/:id : delete the "id" propertyPhoto.
     *
     * @param id the id of the propertyPhoto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/property-photos/{id}")
    @Timed
    public ResponseEntity<Void> deletePropertyPhoto(@PathVariable String id) {
        log.debug("REST request to delete PropertyPhoto : {}", id);

        propertyPhotoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
