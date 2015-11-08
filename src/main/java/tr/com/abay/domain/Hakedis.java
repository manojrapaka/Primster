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
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Hakedis.
 */
@Entity
@Table(name = "hakedis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hakedis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "tarih")
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
    
    @Column(name = "prim")
    private Double prim;

    @ManyToOne
    private Calisan calisan;

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

    public Double getPrim() {
        return prim;
    }

    public void setPrim(Double prim) {
        this.prim = prim;
    }

    public Calisan getCalisan() {
        return calisan;
    }

    public void setCalisan(Calisan calisan) {
        this.calisan = calisan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Hakedis hakedis = (Hakedis) o;

        if ( ! Objects.equals(id, hakedis.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hakedis{" +
                "id=" + id +
                ", tarih='" + tarih + "'" +
                ", basTarih='" + basTarih + "'" +
                ", bitTarih='" + bitTarih + "'" +
                ", prim='" + prim + "'" +
                '}';
    }
}
