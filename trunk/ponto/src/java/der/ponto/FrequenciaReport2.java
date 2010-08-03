package der.ponto;

import entities.annotations.EntityDescriptor;
import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
@EntityDescriptor(displayName = "Frequência por Funcionário", pluralDisplayName = "Frequência por Funcionário")
public class FrequenciaReport2 implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long ID;
    @Id
    @PropertyDescriptor(hidden = true)
    private Long id;
    @Column(length = 50)
    @PropertyDescriptor(index = 1, hidden = true, displayName = "Funcionário")
    private String nomeFuncionario;
    @Column(length = 10)
    @PropertyDescriptor(index = 2, displayName = "Data")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;
    @Column(length = 8)
    @PropertyDescriptor(index = 3, displayName = "Entrada 1")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date entrada1;
    @Column(length = 8)
    @PropertyDescriptor(index = 4, displayName = "Saída 1")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date saida1;
    @Column(length = 8)
    @PropertyDescriptor(index = 5, displayName = "Entrada 2")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date entrada2;
    @Column(length = 8)
    @PropertyDescriptor(index = 6, displayName = "Saída 2")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date saida2;
    @Column(length = 6)
    @PropertyDescriptor(index = 7, displayName = "Atraso")
    private Short atraso;
    @Column(length = 5)
    @PropertyDescriptor(index = 8, displayName = "Falta")
    private Integer falta;

    public FrequenciaReport2() {
    }

    public FrequenciaReport2(String nomeFuncionario, Date data, Date entrada1, Date saida1, Date entrada2, Date saida2, Short atraso, Integer falta) {
        this.id = Long.valueOf(++ID);
        this.nomeFuncionario = nomeFuncionario;
        this.data = data;
        this.entrada1 = entrada1;
        this.saida1 = saida1;
        this.entrada2 = entrada2;
        this.saida2 = saida2;
        this.atraso = atraso;
        this.falta = falta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getAtraso() {
        return atraso;
    }

    public void setAtraso(Short atraso) {
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

    public Date getEntrada2() {
        return entrada2;
    }

    public void setEntrada2(Date entrada2) {
        this.entrada2 = entrada2;
    }

    public Integer getFalta() {
        return falta;
    }

    public void setFalta(Integer falta) {
        this.falta = falta;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FrequenciaReport2 other = (FrequenciaReport2) obj;
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
