package der.ponto;

import entities.annotations.EntityDescriptor;
import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
@EntityDescriptor(displayName = "Frequência por Funcionário", pluralDisplayName = "Frequência por Funcionário")
public class FrequenciaReport1 implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long ID;
    @Id
    @PropertyDescriptor(hidden = true)
    private Long id;
    @Column(length = 50)
    @PropertyDescriptor(index = 1, displayName = "Funcionário")
    private String nomeFuncionario;
    private List<NestedClass> freqs = new ArrayList<NestedClass>();

    /**
     * @return the freqs
     */
    public List<NestedClass> getFreqs() {
        return freqs;
    }

    /**
     * @param freqs the freqs to set
     */
    public void setFreqs(List<NestedClass> freqs) {
        this.freqs = freqs;
    }

    static public class NestedClass {

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

        /**
         * @return the data
         */
        public Date getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(Date data) {
            this.data = data;
        }

        /**
         * @return the entrada1
         */
        public Date getEntrada1() {
            return entrada1;
        }

        /**
         * @param entrada1 the entrada1 to set
         */
        public void setEntrada1(Date entrada1) {
            this.entrada1 = entrada1;
        }

        /**
         * @return the saida1
         */
        public Date getSaida1() {
            return saida1;
        }

        /**
         * @param saida1 the saida1 to set
         */
        public void setSaida1(Date saida1) {
            this.saida1 = saida1;
        }

        /**
         * @return the entrada2
         */
        public Date getEntrada2() {
            return entrada2;
        }

        /**
         * @param entrada2 the entrada2 to set
         */
        public void setEntrada2(Date entrada2) {
            this.entrada2 = entrada2;
        }

        /**
         * @return the saida2
         */
        public Date getSaida2() {
            return saida2;
        }

        /**
         * @param saida2 the saida2 to set
         */
        public void setSaida2(Date saida2) {
            this.saida2 = saida2;
        }

        /**
         * @return the atraso
         */
        public Short getAtraso() {
            return atraso;
        }

        /**
         * @param atraso the atraso to set
         */
        public void setAtraso(Short atraso) {
            this.atraso = atraso;
        }

        /**
         * @return the falta
         */
        public Integer getFalta() {
            return falta;
        }

        /**
         * @param falta the falta to set
         */
        public void setFalta(Integer falta) {
            this.falta = falta;
        }

        public NestedClass(Date data, Date entrada1, Date saida1, Date entrada2, Short atraso, Integer falta) {
            this.data = data;
            this.entrada1 = entrada1;
            this.saida1 = saida1;
            this.entrada2 = entrada2;
            this.atraso = atraso;
            this.falta = falta;
        }
    }

    public FrequenciaReport1() {
    }

    public FrequenciaReport1(String nomeFuncionario, Date data, Date entrada1, Date saida1, Date entrada2, Date saida2, Short atraso, Integer falta) {
        this.id = Long.valueOf(++ID);
        this.nomeFuncionario = nomeFuncionario;
        this.freqs.add(new NestedClass(data, entrada1, saida1, entrada2, atraso, falta));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FrequenciaReport1 other = (FrequenciaReport1) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
