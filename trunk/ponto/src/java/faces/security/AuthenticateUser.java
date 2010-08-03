package faces.security;

import der.acesso.*;
import der.auditoria.LogAcesso;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import faces.backBeans.SessionBean;
import faces.security.ILogin.Status;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AuthenticateUser implements IAuthenticate {

    @Override
    public ILogin checkLogin(String usuario, String senha) {
        ILogin login = new LoginImpl();
        String senhaHash = User.md5(senha);        
    
        try {
            IDAO dao = DAOFactory.getInstance().getDAO(User.class);
            IDAO daoLogAcesso = DAOFactory.getInstance().getDAO(LogAcesso.class);
            List<User> users;
            users = dao.query("User.ByUserName", usuario);
            if (users.size() != 1) {
                login.setStatus(Status.ERRO);
                login.setMessage("Usuário não existe");
            } else if (!users.get(0).getPasswordHash().equals(senhaHash)) {
                login.setStatus(Status.ERRO);
                login.setMessage("Senha inválida");
            } else if (users.size() == 1 && users.get(0).getPasswordHash().equals(senhaHash)) {
                login.setStatus(Status.SUCESS);
                login.setLogin(usuario);
                login.setName(users.get(0).getFirstname());
                login.setMessage("");
            
                LogAcesso logAcesso = new LogAcesso();
                logAcesso.setMatricula(usuario);
                logAcesso.setDataAcesso(new Date());
                logAcesso.setHoraAcesso(Calendar.getInstance().getTime());

                daoLogAcesso.save(logAcesso);

                SessionBean s = SessionBean.getInstance();
                SessionBean.setSessionMapValue("idAcesso", logAcesso.getId());
            }
        } catch (DAOValidationException ex) {
            login.setStatus(Status.FAIL);
            login.setMessage(ex.getMessage());
        } catch (DAOConstraintException ex) {
            login.setStatus(Status.FAIL);
            login.setMessage(ex.getMessage());
        } catch (DAOException ex) {
            login.setStatus(Status.FAIL);
            login.setMessage(ex.getMessage());
        }

        return login;
    }
}
