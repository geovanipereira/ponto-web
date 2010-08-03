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

@Embeddable
public class FrequenciaPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @Basic(optional = false)
    @NotNull
    @JoinColumn(name="MAT_FUNC")
    @PropertyDescriptor(index=0)
    private Funcionario funcionario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DAT_FREQ", nullable = false, length=10)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(index=1)
    private Date data;

    public FrequenciaPK() {
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FrequenciaPK other = (FrequenciaPK) obj;
        if (this.funcionario != other.funcionario && (this.funcionario == null || !this.funcionario.equals(other.funcionario))) {
            return false;
        }
        if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.funcionario != null ? this.funcionario.hashCode() : 0);
        hash = 89 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return funcionario + " : " + data;
    }
}
