package der.ponto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Abstração do Pattern State do Status da Ocorrência
 * @author Marcius Brandão
 *
 */
@Entity
@Table(name = "status", schema = "ponto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO",
discriminatorType = DiscriminatorType.STRING)
public abstract class StatusOcorrencia implements Serializable {

    @Id
    private Short id;

    @Column(length = 40, nullable = false)
    private String descricao;

    @Transient
    protected Ocorrencia ocorrencia;

    // <editor-fold defaultstate="collapsed" desc="Get´s e Set´s">
    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    public abstract void autorizar() throws StatusException;

    public abstract void cancelar() throws StatusException;

    public abstract void validar() throws StatusException;

    public abstract void voltar() throws StatusException;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="equals e hashcode">
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatusOcorrencia other = (StatusOcorrencia) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 11 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
        return hash;
    }
    // </editor-fold>

    @Override
    public String toString(){
        return this.id == null ? "<Novo>" : descricao;
    }
}
