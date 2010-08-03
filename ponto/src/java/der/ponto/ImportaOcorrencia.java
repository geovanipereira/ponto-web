package der.ponto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import util.convert.Convert;

@Entity
public class ImportaOcorrencia implements Serializable{
    private static final long serialVersionUID = 2679560766628325665L;

    @Id
    private Long id;
    private String matricula;
    private String nome;
    private String motivo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicio;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFinal;
    private String descricao;
    private String lotacao;

    private boolean selected;

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLotacao() {
        return lotacao;
    }

    public void setLotacao(String lotacao) {
        this.lotacao = lotacao;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImportaOcorrencia other = (ImportaOcorrencia) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString(){
        //return "<span class='span_importaOcorrencia' style='width:300px;'>" + nome + " </span><span class='span_importaOcorrencia' style='width:150px;'>"+ Convert.dateToString(dataInicio) + " - " + Convert.dateToString(dataFinal) + " </span><span class='span_importaOcorrencia' style='width:100px;'> " + matricula + " </span><span class='span_importaOcorrencia' style='width:350px;'> " + lotacao + "</span>";
        return "Id: " + id + " Nome: " + nome + " Período: " + Convert.dateToString(dataInicio) + " - " + Convert.dateToString(dataFinal) + " Matrícula: " + matricula + " Lotação: " + lotacao;
    }

}
