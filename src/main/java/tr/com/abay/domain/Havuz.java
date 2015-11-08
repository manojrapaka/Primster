package tr.com.abay.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tr.com.abay.domain.util.CustomLocalDateSerializer;
import tr.com.abay.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Havuz.
 */
@Entity
@Table(name = "havuz")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Havuz implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "tarih", nullable = false)
    private LocalDate tarih;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "bas_tarih", nullable = false)
    private LocalDate basTarih;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "bit_tarih", nullable = false)
    private LocalDate bitTarih;
    
    @Column(name = "tutar")
    private Double tutar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public LocalDate getBasTarih() {
        return basTarih;
    }

    public void setBasTarih(LocalDate basTarih) {
        this.basTarih = basTarih;
    }

    public LocalDate getBitTarih() {
        return bitTarih;
    }

    public void setBitTarih(LocalDate bitTarih) {
        this.bitTarih = bitTarih;
    }

    public Double getTutar() {
        return tutar;
    }

    public void setTutar(Double tutar) {
        this.tutar = tutar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Havuz havuz = (Havuz) o;

        if ( ! Objects.equals(id, havuz.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Havuz{" +
                "id=" + id +
                ", tarih='" + tarih + "'" +
                ", basTarih='" + basTarih + "'" +
                ", bitTarih='" + bitTarih + "'" +
                ", tutar='" + tutar + "'" +
                '}';
    }
}
