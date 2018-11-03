package fr.fresnault.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.fresnault.domain.enumeration.Source;

import fr.fresnault.domain.enumeration.Transaction;

import fr.fresnault.domain.enumeration.Type;

/**
 * A Property.
 */
@Document(collection = "property")
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("ref_source")
    private Source refSource;

    @NotNull
    @Field("ref_id")
    private String refId;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("transaction")
    private Transaction transaction;

    @NotNull
    @Field("type")
    private Type type;

    @Field("description")
    private String description;

    @NotNull
    @Field("created_date")
    private Instant createdDate;

    @Field("price")
    private BigDecimal price;

    @Field("room_count")
    private Integer roomCount;

    @Field("bedroom_count")
    private Integer bedroomCount;

    @Field("living_area")
    private BigDecimal livingArea;

    @Field("surface_area")
    private BigDecimal surfaceArea;

    @NotNull
    @Field("url")
    private String url;

    @DBRef
    @Field("city")
    private City city;

    @DBRef
    @Field("photo")
    private Set<PropertyPhoto> photos = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Source getRefSource() {
        return refSource;
    }

    public Property refSource(Source refSource) {
        this.refSource = refSource;
        return this;
    }

    public void setRefSource(Source refSource) {
        this.refSource = refSource;
    }

    public String getRefId() {
        return refId;
    }

    public Property refId(String refId) {
        this.refId = refId;
        return this;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getName() {
        return name;
    }

    public Property name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Property transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Type getType() {
        return type;
    }

    public Property type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public Property description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Property createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Property price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public Property roomCount(Integer roomCount) {
        this.roomCount = roomCount;
        return this;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getBedroomCount() {
        return bedroomCount;
    }

    public Property bedroomCount(Integer bedroomCount) {
        this.bedroomCount = bedroomCount;
        return this;
    }

    public void setBedroomCount(Integer bedroomCount) {
        this.bedroomCount = bedroomCount;
    }

    public BigDecimal getLivingArea() {
        return livingArea;
    }

    public Property livingArea(BigDecimal livingArea) {
        this.livingArea = livingArea;
        return this;
    }

    public void setLivingArea(BigDecimal livingArea) {
        this.livingArea = livingArea;
    }

    public BigDecimal getSurfaceArea() {
        return surfaceArea;
    }

    public Property surfaceArea(BigDecimal surfaceArea) {
        this.surfaceArea = surfaceArea;
        return this;
    }

    public void setSurfaceArea(BigDecimal surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public String getUrl() {
        return url;
    }

    public Property url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public City getCity() {
        return city;
    }

    public Property city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<PropertyPhoto> getPhotos() {
        return photos;
    }

    public Property photos(Set<PropertyPhoto> propertyPhotos) {
        this.photos = propertyPhotos;
        return this;
    }

    public Property addPhoto(PropertyPhoto propertyPhoto) {
        this.photos.add(propertyPhoto);
        propertyPhoto.setProperty(this);
        return this;
    }

    public Property removePhoto(PropertyPhoto propertyPhoto) {
        this.photos.remove(propertyPhoto);
        propertyPhoto.setProperty(null);
        return this;
    }

    public void setPhotos(Set<PropertyPhoto> propertyPhotos) {
        this.photos = propertyPhotos;
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
        Property property = (Property) o;
        if (property.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), property.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Property{" +
            "id=" + getId() +
            ", refSource='" + getRefSource() + "'" +
            ", refId='" + getRefId() + "'" +
            ", name='" + getName() + "'" +
            ", transaction='" + getTransaction() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", price=" + getPrice() +
            ", roomCount=" + getRoomCount() +
            ", bedroomCount=" + getBedroomCount() +
            ", livingArea=" + getLivingArea() +
            ", surfaceArea=" + getSurfaceArea() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
