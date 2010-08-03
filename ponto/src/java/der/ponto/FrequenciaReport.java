package der.ponto;

import entities.annotations.EntityDescriptor;
import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import util.convert.Convert;

@Entity
@EntityDescriptor(displayName = "Frequência por Funcionário", pluralDisplayName = "Frequência por Funcionário")
public class FrequenciaReport implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long ID;
    @Id
    @PropertyDescriptor(hidden = true)
    private Long id;
    @PropertyDescriptor(hidden = true)
    private String matricula;
    @PropertyDescriptor(index = 10, displayName = "Funcionário", displayWidth = 50, summary = false)
    private String nomeFuncionario;
    @PropertyDescriptor(index = 20, displayName = "Data", displayWidth = 12)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;
    @PropertyDescriptor(index = 25, displayWidth = 3)
    private String dia;
    @PropertyDescriptor(index = 30, displayName = "Entrada", displayWidth = 8)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date entrada1;
    @PropertyDescriptor(index = 35, displayName = "Mot.", displayWidth = 3)
    private String motivo1;
    @PropertyDescriptor(index = 40, displayName = "Saída", displayWidth = 8)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date saida1;
    @PropertyDescriptor(index = 45, displayName = "Mot. ", displayWidth = 3)
    private String motivo2;
    @PropertyDescriptor(index = 50, displayName = "Entrada", displayWidth = 8)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date entrada2;
    @PropertyDescriptor(index = 55, displayName = "Mot.", displayWidth = 3)
    private String motivo3;
    @PropertyDescriptor(index = 60, displayName = "Saída", displayWidth = 8)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date saida2;
    @PropertyDescriptor(index = 65, displayName = "Mot.", displayWidth = 3)
    private String motivo4;
    @PropertyDescriptor(index = 70, displayName = "Atraso", displayWidth = 5)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date atraso;
    @PropertyDescriptor(index = 80, displayName = "Falta", displayWidth = 5, hidden = true)
    private Integer falta;
    @PropertyDescriptor(index = 85, displayName = "Feriado", displayWidth = 5, hidden = true)
    private Short feriado;
    @PropertyDescriptor(index = 90, displayName = "Incomp.", displayWidth = 5, hidden = true)
    private Integer pontoIncompleto;
    @PropertyDescriptor(index = 95, displayName = "Observação", displayWidth = 20)
    private String observacao;
    @PropertyDescriptor(hidden = true)
    private String lotacao;
    @PropertyDescriptor(hidden = true)
    private String grade;
    @PropertyDescriptor(hidden = true)
    private String funcaoFuncionario;
    @PropertyDescriptor(hidden = true)
    private Integer minuto;

    public FrequenciaReport() {
    }

    public FrequenciaReport(String matricula, String nomeFuncionario, Date data, Date entrada1, String motivo1, Date saida1, String motivo2, Date entrada2, String motivo3, Date saida2, String motivo4, Date atraso, Integer falta, Short feriado, Integer pontoIncompleto, String observacao, Lotacao lotacao, Grade gradeHorario) {
        this.id = Long.valueOf(++ID);
        this.matricula = matricula;
        this.nomeFuncionario = nomeFuncionario;
        this.data = data;
        this.entrada1 = entrada1;
        this.motivo1 = motivo1;
        this.saida1 = saida1;
        this.motivo2 = motivo2;
        this.entrada2 = entrada2;
        this.motivo3 = motivo3;
        this.saida2 = saida2;
        this.motivo4 = motivo4;
        this.atraso = atraso;
        this.falta = falta;
        this.feriado = feriado;
        this.pontoIncompleto = pontoIncompleto;
        this.observacao = observacao;
        this.lotacao = lotacao.toString();
        this.grade = gradeHorario.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Date getAtraso() {
        return atraso;
    }

    public void setAtraso(Date atraso) {
        this.atraso = atraso;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getEntrada1() {
        return entrada1;
    }

    public void setEntrada1(Date entrada1) {
        this.entrada1 = entrada1;
    }

    public String getMotivo1() {
        return motivo1;
    }

    public void setMotivo1(String motivo1) {
        this.motivo1 = motivo1;
    }

    public Date getEntrada2() {
        return entrada2;
    }

    public void setEntrada2(Date entrada2) {
        this.entrada2 = entrada2;
    }

    public String getMotivo2() {
        return motivo2;
    }

    public void setMotivo2(String motivo2) {
        this.motivo2 = motivo2;
    }

    public String getMotivo3() {
        return motivo3;
    }

    public void setMotivo3(String motivo3) {
        this.motivo3 = motivo3;
    }

    public String getMotivo4() {
        return motivo4;
    }

    public void setMotivo4(String motivo4) {
        this.motivo4 = motivo4;
    }

    public Integer getFalta() {
        return falta;
    }

    public void setFalta(Integer falta) {
        this.falta = falta;
    }

    public String getNomeFuncionario() {
        return matricula.substring(6) + "-" + nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public Date getSaida1() {
        return saida1;
    }

    public void setSaida1(Date saida1) {
        this.saida1 = saida1;
    }

    public Date getSaida2() {
        return saida2;
    }

    public void setSaida2(Date saida2) {
        this.saida2 = saida2;
    }

    public Short getFeriado() {
        return feriado;
    }

    public void setFeriado(Short feriado) {
        this.feriado = feriado;
    }

    public String getDia() {
        if (dia == null) {
            dia = Convert.dateToString(data, "E", Convert.LocaleBR);
        }
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Integer getPontoIncompleto() {
        return pontoIncompleto;
    }

    public void setPontoIncompleto(Integer pontoIncompleto) {
        this.pontoIncompleto = pontoIncompleto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLotacao() {
        return lotacao;
    }

    public void setLotacao(String lotacao) {
        this.lotacao = lotacao;
    }

    public String getFuncaoFuncionario() {
        return funcaoFuncionario;
    }

    public void setFuncaoFuncionario(String funcaoFuncionario) {
        this.funcaoFuncionario = funcaoFuncionario;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FrequenciaReport other = (FrequenciaReport) obj;
        if ((this.nomeFuncionario == null) ? (other.nomeFuncionario != null) : !this.nomeFuncionario.equals(other.nomeFuncionario)) {
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
        hash = 79 * hash + (this.nomeFuncionario != null ? this.nomeFuncionario.hashCode() : 0);
        hash = 79 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }
}
