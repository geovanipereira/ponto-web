package der.ponto;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name="GRADES", schema="ponto")
public class Grade implements Serializable {
    private static final long serialVersionUID = 8068694693331939507L;
    
    @Id    
    @Column(name="COD_GRADE")
    @PropertyDescriptor(index=1,displayName="Código")
    private Short id;

    @NotNull
    @Length(max=50)
    @Column(name="DESCRICAO", nullable = false, length=50)
    @PropertyDescriptor(index=2,displayName="Descrição")
    private String descricao;   

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }  
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Grade other = (Grade) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString(){
        return id + " : "+descricao;
    }    
}
