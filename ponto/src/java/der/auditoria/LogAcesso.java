package der.auditoria;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "log_acesso", schema = "auditoria")
@SequenceGenerator(name="sequenceLogAcesso", sequenceName="auditoria.log_acesso_id_seq", allocationSize=1)
public class LogAcesso implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator="sequenceLogAcesso", strategy=GenerationType.SEQUENCE)
    @PropertyDescriptor(hidden=true)
    private Long id;

    @Basic(optional = false)
    @Column(name = "matricula", nullable = false, length = 20)
    @PropertyDescriptor(autoFilter=true, autoSort=true, displayName="Login", index=1)
    private String matricula;

    @Basic(optional = false)
    @Column(name = "data_acesso", nullable = false)
    @Temporal(TemporalType.DATE)
    @PropertyDescriptor(displayName="Dt. Acesso", index=2)
    private Date dataAcesso;

    @Column(name = "hora_acesso")
    @Temporal(TemporalType.TIMESTAMP)
    @PropertyDescriptor(displayName="Hr. Acesso", index=3)
    private Date horaAcesso;

    @Column(name = "hora_logout")
    @Temporal(TemporalType.TIMESTAMP)
    @PropertyDescriptor(displayName="Hr Logout", index=4)
    private Date horaLogout;

    public LogAcesso() {
    }

    public LogAcesso(Long id) {
        this.id = id;
    }

    public LogAcesso(Long id, String matricula, Date dataAcesso) {
        this.id = id;
        this.matricula = matricula;
        this.dataAcesso = dataAcesso;
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

    public Date getDataAcesso() {
        return dataAcesso;
    }

    public void setDataAcesso(Date dataAcesso) {
        this.dataAcesso = dataAcesso;
    }

    public Date getHoraAcesso() {
        return horaAcesso;
    }

    public void setHoraAcesso(Date horaAcesso) {
        this.horaAcesso = horaAcesso;
    }

    public Date getHoraLogout() {
        return horaLogout;
    }

    public void setHoraLogout(Date horaLogout) {
        this.horaLogout = horaLogout;
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
        if (!(object instanceof LogAcesso)) {
            return false;
        }
        LogAcesso other = (LogAcesso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + "matricula= " + matricula + " data=" + dataAcesso;
    }

}
