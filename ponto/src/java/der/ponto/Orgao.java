package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 *
 * @author Marcius
 */
@Entity
@Table(name = "ORGAOS", schema = "ponto")
public class Orgao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CD_ORG", nullable = false, length = 5)
    @PropertyDescriptor(displayName = "Código")
    private Short codigo;
    @Basic(optional = false)
    @Length(max=10)
    @NotNull
    @Column(name = "SG_ORG", nullable = false, length = 10)
    @PropertyDescriptor(displayName = "Sigla")
    private String sigla;
    @Basic(optional = false)
    @Length(max=50)
    @NotNull
    @Column(name = "NM_ORG", nullable = false, length = 50)
    @PropertyDescriptor(displayName = "Nome")
    private String nome;

    //TODO Verificar depois
    @Column(name = "TP_ORG", length = 15)
    @PropertyDescriptor(hidden = true, displayName = "Tipo")
    private String tpOrg;

    public Orgao() {
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTpOrg() {
        return tpOrg;
    }

    public void setTpOrg(String tpOrg) {
        this.tpOrg = tpOrg;
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
        if (!(object instanceof Orgao)) {
            return false;
        }
        Orgao other = (Orgao) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo + " " + nome + "(" + sigla + ")";
    }
}
