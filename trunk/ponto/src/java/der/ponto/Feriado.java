package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import util.convert.Convert;

@Entity
@Table(name = "FERIADOS", schema = "ponto")
public class Feriado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DAT_FERIADO", nullable = false)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(displayName = "Data",autoSort=true)
    private Date dataFeriado;
    @Basic(optional = false)
    @NotNull
    @Length(max=40)
    @Column(name = "DESCRICAO", nullable = false, length = 40)
    @PropertyDescriptor(displayName = "Descrição")
    private String descricao;

    public Feriado() {
    }

    public Date getDataFeriado() {
        return dataFeriado;
    }

    public void setDataFeriado(Date dataFeriado) {
        this.dataFeriado = dataFeriado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataFeriado != null ? dataFeriado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feriado)) {
            return false;
        }
        Feriado other = (Feriado) object;
        if ((this.dataFeriado == null && other.dataFeriado != null) || (this.dataFeriado != null && !this.dataFeriado.equals(other.dataFeriado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Convert.dateToString(dataFeriado) + "-" + descricao;
    }
}
