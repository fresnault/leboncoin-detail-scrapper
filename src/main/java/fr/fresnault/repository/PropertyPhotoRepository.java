package fr.fresnault.repository;

import fr.fresnault.domain.PropertyPhoto;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the PropertyPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyPhotoRepository extends MongoRepository<PropertyPhoto, String> {

}
