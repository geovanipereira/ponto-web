package der.auditoria.backBeans;

import der.auditoria.LogAcesso;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import faces.backBeans.SessionBean;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounter implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Acessando o sistema");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            Long idAcesso = (Long) SessionBean.getSessionMapValue("idAcesso");
            IDAO daoLogAcesso = DAOFactory.getInstance().getDAO(LogAcesso.class);
            List<LogAcesso> lista = daoLogAcesso.query("Select la From der.auditoria.LogAcesso la Where la.id = :idAcesso", idAcesso);

            LogAcesso logAcesso = lista.get(0);

            logAcesso.setHoraLogout(Calendar.getInstance().getTime());

            daoLogAcesso.save(logAcesso);
            System.out.println("Saindo do sistema");
        } catch (DAOValidationException ex) {
            ex.printStackTrace();
        } catch (DAOConstraintException ex) {
            ex.printStackTrace();
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

}
