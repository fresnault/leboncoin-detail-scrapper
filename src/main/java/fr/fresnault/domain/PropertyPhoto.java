package fr.fresnault.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PropertyPhoto.
 */
@Document(collection = "property_photo")
public class PropertyPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("photo_seq")
    private Integer photoSeq;

    @Field("photo_thumb_url")
    private String photoThumbUrl;

    @Field("photo_url")
    private String photoUrl;

    @DBRef
    @Field("property")
    @JsonIgnoreProperties("photos")
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPhotoSeq() {
        return photoSeq;
    }

    public PropertyPhoto photoSeq(Integer photoSeq) {
        this.photoSeq = photoSeq;
        return this;
    }

    public void setPhotoSeq(Integer photoSeq) {
        this.photoSeq = photoSeq;
    }

    public String getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public PropertyPhoto photoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
        return this;
    }

    public void setPhotoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public PropertyPhoto photoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Property getProperty() {
        return property;
    }

    public PropertyPhoto property(Property property) {
        this.property = property;
        return this;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PropertyPhoto propertyPhoto = (PropertyPhoto) o;
        if (propertyPhoto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), propertyPhoto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PropertyPhoto{" +
            "id=" + getId() +
            ", photoSeq=" + getPhotoSeq() +
            ", photoThumbUrl='" + getPhotoThumbUrl() + "'" +
            ", photoUrl='" + getPhotoUrl() + "'" +
            "}";
    }
}
