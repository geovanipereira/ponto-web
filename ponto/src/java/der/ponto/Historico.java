package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 *
 * @author 39291
 */
@Entity
@Table(name = "HISTORICOS", schema = "ponto")
public class Historico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HistoricoPK historicoPK;

    @Basic(optional = false)
    @NotNull
    @Column(name = "LOG_ATESTADO", nullable = false, length=5)
    @PropertyDescriptor(displayName="Log Atestado")
    private Short logAtestado;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "COD_GRADE", nullable = false, length=5)
    @PropertyDescriptor(displayName="Código Grade")
    private Short codigoGrade;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "BCO_HORAS", nullable = false, length=5)
    private Short bancoHoras;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_13", nullable = false, length=5)
    @PropertyDescriptor(hidden=true)
    private Short qtd13;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "BCO_DESC", nullable = false, length=5)
    private Short bancoDesc;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_PC", nullable = false, length=5)
    @PropertyDescriptor(hidden=true)
    private Short qtdPc;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_PI", nullable = false, length=5)
    @PropertyDescriptor(hidden=true)
    private Short qtdPi;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_FJ", nullable = false, length=5)
    @PropertyDescriptor(hidden=true)
    private Short qtdFj;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_FC", nullable = false, length=5)
    @PropertyDescriptor(hidden=true)
    private Short qtdFc;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_DU", nullable = false, length=5)
    @PropertyDescriptor(hidden=true)
    private Short qtdDu;

    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_FE", nullable = false, length=5)
    @PropertyDescriptor(displayName="Qtd de Férias")
    private Short quantidadeFeria;
    
    @Basic(optional = false)
    @NotNull
    @Length(max=5)
    @Column(name = "QTD_FO", nullable = false, length=5)
    @PropertyDescriptor(displayName="Qtd de Folgas")
    private Short quantidadeFolga;
    
    public Historico() {
    }    

    public HistoricoPK getHistoricoPK() {
        return historicoPK;
    }

    public void setHistoricoPK(HistoricoPK historicoPK) {
        this.historicoPK = historicoPK;
    }

    public Short getLogAtestado() {
        return logAtestado;
    }

    public void setLogAtestado(Short logAtestado) {
        this.logAtestado = logAtestado;
    }

    public Short getCodigoGrade() {
        return codigoGrade;
    }

    public void setCodigoGrade(Short codigoGrade) {
        this.codigoGrade = codigoGrade;
    }

    public Short getBancoHoras() {
        return bancoHoras;
    }

    public void setBancoHoras(Short bancoHoras) {
        this.bancoHoras = bancoHoras;
    }

    public Short getQtd13() {
        return qtd13;
    }

    public void setQtd13(Short qtd13) {
        this.qtd13 = qtd13;
    }

    public Short getBancoDesc() {
        return bancoDesc;
    }

    public void setBancoDesc(Short bancoDesc) {
        this.bancoDesc = bancoDesc;
    }

    public Short getQtdPc() {
        return qtdPc;
    }

    public void setQtdPc(Short qtdPc) {
        this.qtdPc = qtdPc;
    }

    public Short getQtdPi() {
        return qtdPi;
    }

    public void setQtdPi(Short qtdPi) {
        this.qtdPi = qtdPi;
    }

    public Short getQtdFj() {
        return qtdFj;
    }

    public void setQtdFj(Short qtdFj) {
        this.qtdFj = qtdFj;
    }

    public Short getQtdFc() {
        return qtdFc;
    }

    public void setQtdFc(Short qtdFc) {
        this.qtdFc = qtdFc;
    }

    public Short getQtdDu() {
        return qtdDu;
    }

    public void setQtdDu(Short qtdDu) {
        this.qtdDu = qtdDu;
    }

    public Short getQuantidadeFeria() {
        return quantidadeFeria;
    }

    public void setQuantidadeFeria(Short quantidadeFeria) {
        this.quantidadeFeria = quantidadeFeria;
    }

    public Short getQuantidadeFolga() {
        return quantidadeFolga;
    }

    public void setQuantidadeFolga(Short quantidadeFolga) {
        this.quantidadeFolga = quantidadeFolga;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historicoPK != null ? historicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historico)) {
            return false;
        }
        Historico other = (Historico) object;
        if ((this.historicoPK == null && other.historicoPK != null) || (this.historicoPK != null && !this.historicoPK.equals(other.historicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return historicoPK + " " + logAtestado + " " + bancoHoras;
    }

}
