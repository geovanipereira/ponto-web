package der.auditoria.backBeans;

import der.auditoria.Auditoria;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ajax4jsf.model.KeepAlive;
import util.jsf.BackBeanViewPage;

@KeepAlive
public class AuditoriaBackBean extends BackBeanViewPage{
    private static final long serialVersionUID = 1722829860445263450L;

    private Date dataInicial;
    private Date dataFinal;
    private List<Auditoria> auditorias;

    IDAO daoAuditoria = DAOFactory.getInstance().getDAO(Auditoria.class);

    /** Creates a new instance of AuditoriaBackBean */
    public AuditoriaBackBean() {
        auditorias = new ArrayList<Auditoria>();
    }
    
    public void actionConsultarAuditoria(){
        try {
            auditorias = daoAuditoria.query("From der.auditoria.Auditoria a Where a.dataTransacao between :dataInicial and :dataFinal", dataInicial, dataFinal);

            addInfoMessage(null, "info", "Total de Registros {0}", auditorias.size());
        } catch (DAOException ex) {
            addErrorMessage(null, "error", ex.getMessage());
        }
    }

    public List<Auditoria> getAuditorias() {
        return auditorias;
    }

    public void setAuditorias(List<Auditoria> auditorias) {
        this.auditorias = auditorias;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }


}
