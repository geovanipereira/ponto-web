package der.ponto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;
import util.convert.Convert;

/**
 *
 * @author Marcius
 */
@Entity
@Table(name = "MES_PROCESSADO", schema = "ponto")
public class MesProcessado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DAT_MES", nullable = false, length=10)
    @Temporal(TemporalType.DATE)
    private Date dataMes;

    public MesProcessado() {
    }

    public Date getDataMes() {
        return dataMes;
    }

    public void setDataMes(Date dataMes) {
        this.dataMes = dataMes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MesProcessado other = (MesProcessado) obj;
        if (this.dataMes != other.dataMes && (this.dataMes == null || !this.dataMes.equals(other.dataMes))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.dataMes != null ? this.dataMes.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return Convert.dateToString(dataMes);
    }

}
