package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 *
 * @author 39291
 */
@Entity
@Table(name = "HORARIOS_ESPECIAIS", schema = "ponto")
public class HorarioEspecial implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId    
    protected HorarioEspecialPK id;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "COD_GRADE", nullable = false, length=5)
    @PropertyDescriptor(index=3)
    private Short codigoGrade;

    public HorarioEspecial() {
    }    

    public HorarioEspecialPK getId() {
        return id;
    }

    public void setId(HorarioEspecialPK id) {
        this.id = id;
    }

    public Short getCodigoGrade() {
        return codigoGrade;
    }

    public void setCodigoGrade(Short codigoGrade) {
        this.codigoGrade = codigoGrade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorarioEspecial)) {
            return false;
        }
        HorarioEspecial other = (HorarioEspecial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " " + codigoGrade;
    }

}
