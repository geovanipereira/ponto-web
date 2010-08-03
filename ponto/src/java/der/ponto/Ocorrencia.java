package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import util.convert.Convert;

@Entity
@Table(name = "OCORRENCIAS", schema = "ponto")
@NamedQueries({
    @NamedQuery(name = "Ocorrencias.porId",
    query = "from der.ponto.Ocorrencia o where o.id = :id"),
    @NamedQuery(name = "Ocorrencias.OcorrenciasNoPeriodo",
    query = "from der.ponto.Ocorrencia o " +
    "where o.funcionario.matricula = :matricula " +
    "and o.dataOcorrencia between :dataInicial and :dataFinal"),
    @NamedQuery(name = "Ocorrencias.aguardandoAutorizacao",
    query = "from der.ponto.Ocorrencia o where o.status.id = 1"),
    @NamedQuery(name = "Ocorrencias.aguardandoValidacao",
    query = "from der.ponto.Ocorrencia o where o.status.id = 2")})
public class Ocorrencia implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @PropertyDescriptor(hidden = true)
    @GeneratedValue(generator = "Sequence")
    @SequenceGenerator(name = "Sequence", sequenceName = "ponto.id_ocorrencias_seq", allocationSize = 5)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "MAT_FUNC")
    @PropertyDescriptor(index = 1, displayName = "Funcionário", summary = false)
    private Funcionario funcionario;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DAT_OCORR", nullable = false)
    @PropertyDescriptor(index = 2, displayName = "Data", autoSort=true,autoFilter=true)
    private Date dataOcorrencia;

    @Column(name = "SAI_OCORR")
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(index = 3, displayName = "Entrada")
    private Date saida;

    @Column(name = "RET_OCORR")
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(index = 4, displayName = "Saída")
    private Date retorno;

    //TODO : por o enum motivo
    @NotNull
    @Length(min = 1, max = 1)
    @PropertyDescriptor(index = 5, displayName="Motivo", autoFilter=true, autoSort=true)
    @Column(name = "MOTIVO", length = 1, nullable = false)
    private String motivo;

    @Lob
    @NotNull
    @Column(name = "DSC_OCORR", length = 32700)
    @PropertyDescriptor(index = 6, displayName = "Descrição", autoFilter=true, autoSort=true)
    private String descricao;

    @Lob
    @Column(name = "DSC_OCORR_RH", length = 32700)
    @PropertyDescriptor(index = 7, displayName = "Observação RH", hidden=true, autoFilter=true, autoSort=true)
    private String observacaoRH;

    @Column(name = "LOG_BCO_HORAS")
    @PropertyDescriptor(summary = false, index = 8, displayName = "Banco de Horas?", hidden=true)
    private Short bancoDeHoras;

    @Transient
    private boolean selected;

    @ManyToOne(optional = false,fetch=FetchType.EAGER)
    @JoinColumn(name = "ID_STATUS")
    @PropertyDescriptor(index = 9, displayName="Situação", autoFilter=true, autoSort=true)
    private StatusOcorrencia status = new StatusAguardandoAutorizacao(this);

    // <editor-fold defaultstate="collapsed" desc="Get´s e Set´s">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(Date data) {
        this.dataOcorrencia = data;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public Date getRetorno() {
        return retorno;
    }

    public void setRetorno(Date retorno) {
        this.retorno = retorno;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacaoRH() {
        return observacaoRH;
    }

    public void setObservacaoRH(String observacaoRH) {
        this.observacaoRH = observacaoRH;
    }

    public Short getBancoDeHoras() {
        return bancoDeHoras;
    }

    public void setBancoDeHoras(Short bancoDeHoras) {
        this.bancoDeHoras = bancoDeHoras;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
        this.status.setOcorrencia(this);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    public void autorizar() throws StatusException {
        this.status.setOcorrencia(this);
        status.autorizar();
    }

    public void cancelar() throws StatusException {
        this.status.setOcorrencia(this);
        status.cancelar();
    }

    public void validar() throws StatusException {
        this.status.setOcorrencia(this);
        status.validar();
    }

    public void voltar() throws StatusException {
        this.status.setOcorrencia(this);
        status.voltar();
    }
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
        final Ocorrencia other = (Ocorrencia) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    // </editor-fold>

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (saida != null || retorno != null) {
            s.append(" : ");
            if (saida != null) {
                s.append(saida);
                s.append(" ");
            }
            if (retorno != null) {
                s.append(retorno);
                s.append(" ");
            }
        }

        return "[" + id + "] "+ funcionario + " - " + Convert.dateToString(dataOcorrencia) + s.toString() + "-" + descricao + " ("+status.getDescricao()+")";
        //return "[" + id + "] "                      + Convert.dateToString(dataOcorrencia) + s.toString() + "-" + descricao + " ("+status.getDescricao()+")";
    }
}
