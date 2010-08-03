package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.validator.NotNull;

@Embeddable
public class GradeHorarioId implements Serializable {
    private static final long serialVersionUID = 1L;
    @ManyToOne
    @Basic(optional = false)
    @NotNull
    @JoinColumn(name="COD_GRADE")
    @PropertyDescriptor(index=0)
    private Grade grade;

    @Basic(optional = false)
    @NotNull
    @Column(name = "NUM_DIA", nullable = false, length = 5)
    @PropertyDescriptor(index=1, displayName="N.º Dia")
    private Short numDia;

    public GradeHorarioId() {
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Short getNumDia() {
        return numDia;
    }

    public void setNumDia(Short numDia) {
        this.numDia = numDia;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GradeHorarioId other = (GradeHorarioId) obj;
        if (this.grade != other.grade && (this.grade == null || !this.grade.equals(other.grade))) {
            return false;
        }
        if (this.numDia != other.numDia && (this.numDia == null || !this.numDia.equals(other.numDia))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.grade != null ? this.grade.hashCode() : 0);
        hash = 11 * hash + (this.numDia != null ? this.numDia.hashCode() : 0);
        return hash;
    }

    //todo
    @Override
    public String toString() {
        return grade + ", numDia=" + numDia;
    }
}
