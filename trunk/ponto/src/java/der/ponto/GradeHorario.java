package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 *
 * @author 39291
 */
@Entity
@Table(name = "GRADE_HORARIOS", schema = "ponto")
public class GradeHorario implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GradeHorarioId id;

    @Column(name = "G_IN1", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(displayName="Entrada 1", index=1)
    private Date entrada1;
    
    @Column(name = "G_OUT1", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(displayName="Saída 1", index=2)
    private Date saida1;

    @Column(name = "G_IN2", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(displayName="Entrada 2", index=3)
    private Date entrada2;

    @Column(name = "G_OUT2", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(displayName="Saída 2", index=4)
    private Date saida2;

    @Column(name = "IN1_13", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(hidden=true)
    private Date entrada113;

    @Column(name = "OUT1_13", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(hidden=true)
    private Date saida113;

    @Column(name = "IN2_13", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(hidden=true)
    private Date entrada213;

    @Column(name = "OUT2_13", length=5)
    @Temporal(TemporalType.TIME)
    @PropertyDescriptor(hidden=true)
    private Date saida213;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOG_12E", nullable = false, length=5)
    @PropertyDescriptor(hidden=true)
    private short log12e;

    public GradeHorario() {
        id = new GradeHorarioId();
    }   

    public GradeHorarioId getId() {
        return id;
    }

    public void setId(GradeHorarioId id) {
        this.id = id;
    }

    public Date getEntrada1() {
        return entrada1;
    }

    public void setEntrada1(Date entrada1) {
        this.entrada1 = entrada1;
    }

    public Date getSaida1() {
        return saida1;
    }

    public void setSaida1(Date saida1) {
        this.saida1 = saida1;
    }

    public Date getEntrada2() {
        return entrada2;
    }

    public void setEntrada2(Date entrada2) {
        this.entrada2 = entrada2;
    }

    public Date getSaida2() {
        return saida2;
    }

    public void setSaida2(Date saida2) {
        this.saida2 = saida2;
    }

    public Date getEntrada113() {
        return entrada113;
    }

    public void setEntrada113(Date entrada113) {
        this.entrada113 = entrada113;
    }

    public Date getSaida113() {
        return saida113;
    }

    public void setSaida113(Date saida113) {
        this.saida113 = saida113;
    }

    public Date getEntrada213() {
        return entrada213;
    }

    public void setEntrada213(Date entrada213) {
        this.entrada213 = entrada213;
    }

    public Date getSaida213() {
        return saida213;
    }

    public void setSaida213(Date saida213) {
        this.saida213 = saida213;
    }

    public short getLog12e() {
        return log12e;
    }

    public void setLog12e(short log12e) {
        this.log12e = log12e;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GradeHorario other = (GradeHorario) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /*
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set

        if (!(object instanceof GradeHorario)) {
            return false;
        }
        GradeHorario other = (GradeHorario) object;

        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    */
    
    @Override
    public String toString() {
        return id + " " + entrada1 + "-" + saida1 + " " + entrada2 + "-" + saida2;
    }

}
