package der.ponto.backBeans;

import der.ponto.Grade;
import der.ponto.GradeHorario;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GradesBackBean implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer codigoGrade;
    private Boolean exibeModal = false;
    private List<GradeHorario> horarios = new ArrayList<GradeHorario>();

    IDAO daoGrade = DAOFactory.getInstance().getDAO(Grade.class);
    IDAO daoGradeHorario = DAOFactory.getInstance().getDAO(GradeHorario.class);
    /** Creates a new instance of GradesBackBean */
    public GradesBackBean() {
        
    }

    public List<Grade> actionConsultaGrade() throws DAOException {
        List<Grade> lista = new ArrayList<Grade>();

        lista = daoGrade.query("der.obras.Grade");

        return lista;
    }
    
    public List<GradeHorario> actionConsultaGradeHorario() throws DAOException {
        List<GradeHorario> lista = new ArrayList<GradeHorario>();

        lista = daoGradeHorario.query("From der.obras.GradeHorario gh where gh.id.grade.id = :valor", codigoGrade);
        exibeModal = true;
        return lista;
    }

    public List<GradeHorario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<GradeHorario> horarios) {
        this.horarios = horarios;
    }

    public Integer getCodigoGrade() {
        return codigoGrade;
    }

    public void setCodigoGrade(Integer codigoGrade) {
        this.codigoGrade = codigoGrade;
    }

    public Boolean getExibeModal() {
        return exibeModal;
    }

    public void setExibeModal(Boolean exibeModal) {
        this.exibeModal = exibeModal;
    }
    
}
