package der.ponto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import entities.annotations.PropertyDescriptor;

/**
 * Entidade Frequência representa um dia de batida do funcionário
 *
 * @author Marcius
 * @Version 1.0
 * @since 09/12/2008
 * @see
 */
@Entity
@Table(name = "vw_frequencias", schema = "ponto")
@NamedQueries({
    @NamedQuery(name = "Frequencia.FrequenciaNoPeriodo",
    query = "From der.ponto.Frequencia fq " +
            "Where fq.id.funcionario.matricula = :matricula " +
            "And fq.id.data Between :dataInicial and :dataFinal")})
public class Frequencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @PropertyDescriptor(index=0)
    protected FrequenciaPK id;

    transient private String dia;

    //Hora de Entrada do 1o. turno
    @Column(name = "IN1", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(index=1, displayName="Entrada 1")
    private Date entrada1;

    //nulo, A, J, P, S,T,X,R,L,E,F
    @PropertyDescriptor(index=2, hidden=true, displayName="Log Entrada 1")
    @Column(name = "LOG_IN1", length = 1)
    private String logEntrada1;

    //Hora de Saída do 1o. turno
    @Column(name = "OUT1", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(index=3, displayName="Saída 1")
    private Date saida1;

    //nulo, A, J, P, S,T,X,R,L,E,F
    @PropertyDescriptor(index=4,hidden=true, displayName="Log Saída 1")
    @Column(name = "LOG_OUT1", length = 1)
    private String logSaida1;

    //Hora de Entrada do 2o. turno
    @Column(name = "IN2", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(index=5, displayName="Entrada 2")
    private Date entrada2;

    //nulo, A, J, P, S,T,X,R,L,E,F
    @PropertyDescriptor(index=6,hidden=true, displayName="Log Entrada 2")
    @Column(name = "LOG_IN2", length = 1)
    private String logEntrada2;

    //Hora de Saída do 2o. turno
    @Column(name = "OUT2", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(index=7, displayName="Saída 2")
    private Date saida2;

    //nulo, A, J, P, S,T,X,R,L,E,F
    @PropertyDescriptor(index=8,hidden=true, displayName="Log Saída 2")
    @Column(name = "LOG_OUT2", length = 1)
    private String logSaida2;

    //Desconto em minutos efetuado no dia
    @Temporal(TemporalType.TIME)
    @Column(name = "BCO_DESC", length=5)
    @PropertyDescriptor(index=9, displayName="Atraso")
    private Date atraso;

    //Quantidade 1/3 efetuados
    @Column(name = "LOG_13", length=5)
    @PropertyDescriptor(index=10, hidden=true)
    private Short log13;

    //Número do Dia da Semana
    @PropertyDescriptor(index=11,hidden=true)
    @Column(name = "NUM_DIA", length=5)
    private Short diaDaSemana;

    //Se é ou não Feriado
    @PropertyDescriptor(index=12,hidden=true)
    @Column(name = "LOG_FERIADO", length=5)
    private Short feriado;

    //Se é ou não Falta
    @Column(name = "LOG_FALTA", length=1)
    @PropertyDescriptor(index=13)
    private Integer falta;

    //Se é ou não Falta
    @Column(name = "PONTO_INCOMPLETO", length=1)
    @PropertyDescriptor(index=14)
    private Integer pontoIncompleto;

    @Column(name = "OBSERVACAO", length=40)
    @PropertyDescriptor(index = 95, displayName = "Observação", displayWidth = 20)
    private String observacao;

    public Frequencia() {
    }

    public FrequenciaPK getId() {
        return id;
    }

    public void setId(FrequenciaPK Id) {
        this.id = Id;
    }

    public String getDia() {
        return "dia";
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Date getEntrada1() {
        return entrada1;
    }

    public void setEntrada1(Date entrada) {
        this.entrada1 = entrada;
    }

    public String getLogEntrada1() {
        return logEntrada1;
    }

    public void setLogEntrada1(String logEntrada1) {
        this.logEntrada1 = logEntrada1;
    }

    public Date getSaida1() {
        return saida1;
    }

    public void setSaida1(Date saida) {
        this.saida1 = saida;
    }

    public String getLogSaida1() {
        return logSaida1;
    }

    public void setLogSaida1(String logSaida1) {
        this.logSaida1 = logSaida1;
    }

    public Date getEntrada2() {
        return entrada2;
    }

    public void setEntrada2(Date entrada) {
        this.entrada2 = entrada;
    }

    public String getLogEntrada2() {
        return logEntrada2;
    }

    public void setLogEntrada2(String logEntrada2) {
        this.logEntrada2 = logEntrada2;
    }

    public Date getSaida2() {
        return saida2;
    }

    public void setSaida2(Date saida) {
        this.saida2 = saida;
    }

     public String getLogSaida2() {
        return logSaida2;
    }

    public void setLogSaida2(String logSaida2) {
        this.logSaida2 = logSaida2;
    }

    public Date getAtraso() {
        return atraso;
    }

    public void setAtraso(Date atraso) {
        this.atraso = atraso;
    }

    public Short getLog13() {
        return log13;
    }

    public void setLog13(Short log13) {
        this.log13 = log13;
    }

    public Short getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(Short diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public Short getFeriado() {
        return feriado;
    }

    public void setFeriado(Short feriado) {
        this.feriado = feriado;
    }

    public Integer getFalta() {
        return falta;
    }

    public void setFalta(Integer falta) {
        this.falta = falta;
    }

    public Integer getPontoIncompleto() {
        return pontoIncompleto;
    }

    public void setPontoIncompleto(Integer ponto_incompleto) {
        this.pontoIncompleto = ponto_incompleto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Frequencia)) {
            return false;
        }
        Frequencia other = (Frequencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " : " + entrada1 + " | " + saida1 + " | " + entrada2 + " | " + saida2;
    }
}
