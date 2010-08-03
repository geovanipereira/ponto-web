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
public class HorarioEspecialPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @Basic(optional = false)
    @NotNull
    @JoinColumn(name="MAT_FUNC")
    @PropertyDescriptor(index=0, autoSort = true, autoFilter = true)
    private Funcionario funcionario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DAT_ESPECIAL", nullable = false, length=10)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(index=1, displayName="Data Especial", autoSort = true)
    private Date dataEspecial;

    public HorarioEspecialPK() {
    }    

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getDataEspecial() {
        return dataEspecial;
    }

    public void setDataEspecial(Date dataEspecial) {
        this.dataEspecial = dataEspecial;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HorarioEspecialPK other = (HorarioEspecialPK) obj;
        if (this.funcionario != other.funcionario && (this.funcionario == null || !this.funcionario.equals(other.funcionario))) {
            return false;
        }
        if (this.dataEspecial != other.dataEspecial && (this.dataEspecial == null || !this.dataEspecial.equals(other.dataEspecial))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.funcionario != null ? this.funcionario.hashCode() : 0);
        hash = 41 * hash + (this.dataEspecial != null ? this.dataEspecial.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return funcionario + " : " + dataEspecial;
    }

}
