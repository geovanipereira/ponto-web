package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;
import util.convert.Convert;

/**
 *
 * @author Marcius
 */
@Embeddable
public class BatidaId implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull    
    @Column(name = "DATA", nullable = false, length = 10)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(index = 0, displayName = "Data")
    private Date data;
    @Basic(optional = false)
    @NotNull    
    @Column(name = "HORA", nullable = false, length = 5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(displayName = "Hora")
    private Date hora;
    @Basic(optional = false)
    @NotNull    
    @Column(name = "NUM", nullable = false, length = 10)
    @PropertyDescriptor(displayName = "Número")
    private Integer numero;

    public BatidaId() {
    }

    public BatidaId(Date data, Integer numero) {
        this.data = data;
        this.hora = data;
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (data != null ? data.hashCode() : 0);
        hash += (hora != null ? hora.hashCode() : 0);
        hash += (int) numero;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BatidaId)) {
            return false;
        }
        BatidaId other = (BatidaId) object;
        if ((this.data == null && other.data != null) || (this.data != null && !this.data.equals(other.data))) {
            return false;
        }
        if ((this.hora == null && other.hora != null) || (this.hora != null && !this.hora.equals(other.hora))) {
            return false;
        }
        if (!this.numero.equals(other.numero)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Convert.dateToString(data) + " " + Convert.timeToString(hora) + " : " + numero;
    }
}
