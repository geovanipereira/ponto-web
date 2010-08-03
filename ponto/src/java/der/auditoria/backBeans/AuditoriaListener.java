package der.auditoria.backBeans;

import der.DerException;
import der.auditoria.Auditoria;
import der.ponto.backBeans.LoginFuncionarioBean;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.DeleteEventListener;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;

public class AuditoriaListener implements SaveOrUpdateEventListener, DeleteEventListener {

    private static final long serialVersionUID = 6811169094321743362L;

    IDAO daoAuditoria = DAOFactory.getInstance().getDAO(Auditoria.class);

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) {
        String matricula = LoginFuncionarioBean.getInstance().getMatricula();

        try {
            Auditoria auditoria = new Auditoria();
            auditoria.setMatricula(matricula);
            auditoria.setTransacao(event.getObject().toString());
            auditoria.setEntidade(event.getObject().getClass().getName());
            auditoria.setTipoTransacao("SaveOrUpdate");
            auditoria.setDataTransacao(new Date());
            auditoria.setHoraTransacao(new GregorianCalendar().getTime());

            daoAuditoria.save(auditoria);

            System.out.println("Matrícula: " + matricula + " Transação: " + event.getObject().toString() + " Entidade: " + event.getObject().getClass().getName() + " Tipo: SaveOrUpdate Data: " + new Date() + " Hora: " + new GregorianCalendar().getTime());

        } catch (Exception e) {
            throw new DerException("Erro na auditoria (onSaveOrUpdate)",e);
        }
    }

    @Override
    public void onDelete(DeleteEvent event) {
        String matricula = LoginFuncionarioBean.getInstance().getMatricula();

        try {
            Auditoria auditoria = new Auditoria();
            auditoria.setMatricula(matricula);
            auditoria.setTransacao(event.getObject().toString());
            auditoria.setEntidade(event.getObject().getClass().getName());
            auditoria.setTipoTransacao("Delete");
            auditoria.setDataTransacao(new Date());
            auditoria.setHoraTransacao(new GregorianCalendar().getTime());

            daoAuditoria.save(auditoria);
        } catch (Exception e) {
            throw new DerException("Erro na auditoria (onDelete)",e);
        }        
    }

    @Override
    public void onDelete(DeleteEvent event, Set set) throws HibernateException {
        System.out.println(event.getEntityName());
    }
}
