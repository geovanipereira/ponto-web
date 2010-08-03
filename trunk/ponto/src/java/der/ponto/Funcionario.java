package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

/**
 * Entidade Frequência representa um dia de batida do funcionário
 *
 * @author Marcius
 * @Version 1.0
 * @since 09/12/2008
 * @see
 */
@Entity
@Table(name = "FUNCIONARIOS", schema = "ponto")
@NamedQueries({@NamedQuery(name = "Funcionario.Todos", query = "from der.ponto.Funcionario"),
@NamedQuery(name="Funcionario.LikeMatricula",query="Select f.matricula, f.nomeCompleto from der.ponto.Funcionario f where f.matriculaCompleta like :matricula"),
@NamedQuery(name="Funcionario.NomePorMatricula",query="Select f.matricula, f.nomeCompleto from der.ponto.Funcionario f where f.matricula = :matricula")})
public class Funcionario implements Serializable {
    //Matrícula completa do Funcionário
    @Id    
    @NotNull
    @Length(max = 14)
    @Basic(optional = false)
    @Column(name = "MAT_FUNC", nullable = false, length = 14)
    @Pattern(regex = "[0-9]{13}[0-9,A-Z]{1}", message = "Matrícula inválida")
    @PropertyDescriptor(index = 1, displayName = "Matrícula Completa", summary = true)
    private String matriculaCompleta;
    
    //Matrícula Reduzida do Funcionário, usada no relógio
    @Length(max = 10)
    @Column(name = "MAT_FUNC_REDUZIDA", nullable = false, length = 10)    
    @PropertyDescriptor(index = 2, summary = true, autoSort = true, autoFilter = true)
    private String matricula;

    @NotNull
    @Length(max = 50)
    @Column(name = "NOM_FUNC", nullable = false, length = 50)
    @PropertyDescriptor(index = 3, displayName = "Nome Completo", autoFilter = true, autoSort = true)
    private String nomeCompleto;

    @NotNull
    @Column(name = "NUM_FUNC", length = 5)
    @PropertyDescriptor(index = 6, displayName = "Nº Funcionário")
    private Short numeroFuncionario;

    @Column(name = "ORG_ORIGEM", length = 5)
    @PropertyDescriptor(index = 8, displayName = "Orgão")
    private Short orgaoOrigem;

    @NotNull
    @Column(name = "CARGA_HORARIA", length = 5)    
    @PropertyDescriptor(index = 7, displayName = "Carga Horária")
    private Short cargaHoraria;

    @NotNull
    @Column(name = "LOG_REGISTRO", length = 5)
    @PropertyDescriptor(index = 9, displayName = "Bate Ponto?")
    private Short batePonto;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DAT_INI", length = 10)        
    @PropertyDescriptor(index = 10, displayName = "Data Início", summary = true)
    private Date dataInicio;

    @Column(name = "DAT_FIM", length = 10)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(index = 11, displayName = "Data Fim", summary = false)
    private Date dataFim;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(optional = false)
    @JoinColumn(name = "LOTACAO", referencedColumnName = "COD_LOTACAO", nullable = false)
    @PropertyDescriptor(index = 4, displayName = "Lotação")    
    private Lotacao lotacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "COD_GRADE", referencedColumnName = "COD_GRADE", nullable = false)
    @PropertyDescriptor(index = 5, displayName = "Grade", autoSort = true, autoFilter = true)
    @Fetch(FetchMode.JOIN)
    private Grade gradeHorario;

//    @Column(length=20)
//    private String login;

    // <editor-fold defaultstate="collapsed" desc="Get´s e Set´s">
    public String getMatriculaCompleta() {
        return matriculaCompleta;
    }

    public void setMatriculaCompleta(String matriculaCompleta) {
        this.matriculaCompleta = matriculaCompleta;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Short getNumeroFuncionario() {
        return numeroFuncionario;
    }

    public void setNumeroFuncionario(Short numeroFuncionario) {
        this.numeroFuncionario = numeroFuncionario;
    }

    public Short getOrgaoOrigem() {
        return orgaoOrigem;
    }

    public void setOrgaoOrigem(Short orgaoOrigem) {
        this.orgaoOrigem = orgaoOrigem;
    }

    public Short getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Short cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Short getBatePonto() {
        return batePonto;
    }

    public void setBatePonto(Short batePonto) {
        this.batePonto = batePonto;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Lotacao getLotacao() {
        return lotacao;
    }

    public void setLotacao(Lotacao lotacao) {
        this.lotacao = lotacao;
    }

    public Grade getGradeHorario() {
        return gradeHorario;
    }

    public void setGradeHorario(Grade gradeHorario) {
        this.gradeHorario = gradeHorario;
    }

//    public String getLogin() {
//        return login;
//    }
//
//    public void setLogin(String login) {
//        this.login = login;
//    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="equals e hashcode">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matriculaCompleta != null ? matriculaCompleta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.matriculaCompleta == null && other.matriculaCompleta != null) || (this.matriculaCompleta != null && !this.matriculaCompleta.equals(other.matriculaCompleta))) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    
    @Override
    public String toString() {
        return nomeCompleto + " [" + matricula + "]";
    }
}
