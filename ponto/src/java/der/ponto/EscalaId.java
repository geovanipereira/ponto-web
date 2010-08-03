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

/**
 *
 * @author Marcius
 */
@Embeddable
public class EscalaId implements Serializable {
    private static final long serialVersionUID = 1L;
    @ManyToOne
    @Basic(optional = false)
    @JoinColumn(name="MAT_FUNC")
    @PropertyDescriptor(index=0, autoSort = true, autoFilter = true)
    private Funcionario funcionario;

    @Basic(optional = false)
    @Column(name = "DAT_ESCALA", nullable = false, length=10)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(index=1, displayName="Data", autoSort = true)
    private Date dataEscala;

    public EscalaId() {
    } 

     public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
    public Date getDataEscala() {
        return dataEscala;
    }

    public void setDataEscala(Date dataEscala) {
        this.dataEscala = dataEscala;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EscalaId other = (EscalaId) obj;
        if (this.funcionario != other.funcionario && (this.funcionario == null || !this.funcionario.equals(other.funcionario))) {
            return false;
        }
        if (this.dataEscala != other.dataEscala && (this.dataEscala == null || !this.dataEscala.equals(other.dataEscala))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.funcionario != null ? this.funcionario.hashCode() : 0);
        hash = 47 * hash + (this.dataEscala != null ? this.dataEscala.hashCode() : 0);
        return hash;
    }    

    @Override
    public String toString() {
        return funcionario + " : " + dataEscala;
    }

}
