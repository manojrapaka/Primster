package tr.com.abay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Grup.
 */
@Entity
@Table(name = "grup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Grup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Size(min = 1, max = 20)
//    @Pattern(regexp = "^[a-zA-Z0-9]*$")        
    @Column(name = "adi", length = 20, nullable = false)
    private String adi;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)        
    @Column(name = "katsayi", nullable = false)
    private Double katsayi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public Double getKatsayi() {
        return katsayi;
    }

    public void setKatsayi(Double katsayi) {
        this.katsayi = katsayi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Grup grup = (Grup) o;

        if ( ! Objects.equals(id, grup.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Grup{" +
                "id=" + id +
                ", adi='" + adi + "'" +
                ", katsayi='" + katsayi + "'" +
                '}';
    }
}
