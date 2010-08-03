/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;

/**
 *
 * @author 39291
 */
@Embeddable
public class HistoricoPK implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @Basic(optional = false)
    @NotNull
    @JoinColumn(name="MAT_FUNC")
    @PropertyDescriptor(index=0)
    private Funcionario funcionario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DAT_MES", nullable = false, length=10)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(index=1, displayName="Data Mês")
    private Date dataMes;

    public HistoricoPK() {
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
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
        final HistoricoPK other = (HistoricoPK) obj;
        if (this.funcionario != other.funcionario && (this.funcionario == null || !this.funcionario.equals(other.funcionario))) {
            return false;
        }
        if (this.dataMes != other.dataMes && (this.dataMes == null || !this.dataMes.equals(other.dataMes))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.funcionario != null ? this.funcionario.hashCode() : 0);
        hash = 73 * hash + (this.dataMes != null ? this.dataMes.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return funcionario + " : " + dataMes;
    }

}
