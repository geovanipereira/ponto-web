package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

/**
 * Entidade Lotacao
 *
 * @author Marcius
 * @Version 1.0
 * @since 09/12/2008
 * @see
 */
@Entity
@Table(name = "LOTACOES", schema = "ponto")
public class Lotacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotEmpty
    @Basic(optional = false)
    @Column(name = "COD_LOTACAO", nullable = false, length = 9)
    @PropertyDescriptor(displayName="Código")
    private String codigo;

    @NotEmpty
    @Length(min=3,max=60)
    @Column(name = "DESCRICAO", nullable = false, length = 60)
    @PropertyDescriptor(displayName="Descrição",autoFilter=true,autoSort=true)
    private String descricao;

    @NotEmpty
    @Column(name = "SIGLA", nullable = false, length = 10)
    @PropertyDescriptor(autoFilter=true,autoSort=true)
    private String sigla;

    @ManyToOne
    @JoinColumn(name = "ORIENTADOR")
    @PropertyDescriptor(index = 1, displayName = "Orientador")
    private Funcionario orientador;

    public Lotacao() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Funcionario getOrientador() {
        return orientador;
    }

    public void setOrientador(Funcionario orientador) {
        this.orientador = orientador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lotacao)) {
            return false;
        }
        Lotacao other = (Lotacao) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return sigla + "-" + descricao;
    }
}
