package der.auditoria;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "auditoria", schema = "auditoria")
public class Auditoria implements Serializable {

    private static final long serialVersionUID = -1359424528707182479L;
    @Id
    @GeneratedValue(generator = "Sequence")
    @SequenceGenerator(name = "Sequence", sequenceName = "auditoria.auditoria_id_seq", allocationSize = 5)
    @PropertyDescriptor(hidden=true)
    private Long id;

    @PropertyDescriptor(autoFilter=true, displayName="Matrícula", index= 1)
    @Column(name = "matricula", length = 15)
    private String matricula;

    @PropertyDescriptor(autoFilter=true, displayName="Transação", index= 2)
    @Column(name = "transacao")
    private String transacao;

    @PropertyDescriptor(displayName="Tipo Transação", index= 3)
    @Column(name = "tipo_transacao", length = 30)
    private String tipoTransacao;

    @PropertyDescriptor(displayName="Entidade", index= 4)
    @Column(name = "entidade")
    private String entidade;

    @PropertyDescriptor(autoFilter=true, displayName="Data da Transação", index= 5)
    @Temporal(TemporalType.DATE)
    @Column(name = "data_transacao")
    private Date dataTransacao;

    @PropertyDescriptor(displayName="Hora da Transação", index= 6)
    @Temporal(TemporalType.TIME)
    @Column(name = "hora_transacao")
    private Date horaTransacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public Date getHoraTransacao() {
        return horaTransacao;
    }

    public void setHoraTransacao(Date horaTransacao) {
        this.horaTransacao = horaTransacao;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public String getTransacao() {
        return transacao;
    }

    public void setTransacao(String transacao) {
        if (transacao.length() > 250) {
            this.transacao = transacao.substring(0, 250);
        } else {
            this.transacao = transacao;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Auditoria other = (Auditoria) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "id: " + id + " matricula: " + matricula + " transação: " + transacao;
    }
}
