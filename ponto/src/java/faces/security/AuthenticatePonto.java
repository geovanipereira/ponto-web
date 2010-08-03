package faces.security;

import der.ponto.Funcionario;
import der.ponto.backBeans.LoginFuncionarioBean;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import faces.security.AuthenticateUser;
import faces.security.IAuthenticate;
import faces.security.ILogin;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcius
 */
public class AuthenticatePonto implements IAuthenticate {

    @Override
    public ILogin checkLogin(String username, String password) {
        ILogin login = new AuthenticateUser().checkLogin(username, password);

        try {
            if (login.getStatus().equals(ILogin.Status.SUCESS)) {
                IDAO dao = DAOFactory.getInstance().getDAO(Funcionario.class);
                List<Object[]> matriculas = new ArrayList<Object[]>();

                matriculas = dao.query("Funcionario.NomePorMatricula", username);
//                if (matriculas.size() != 1) {
//                    login.setStatus(ILogin.Status.ERRO);
//                    login.setMessage("Funcionario não cadastrado no sistema de ponto");
//                } else {
                if (matriculas.size() == 1) {
                    login.setName(matriculas.get(0)[1].toString());
                    login.setStatus(ILogin.Status.SUCESS);
                    login.setMessage("");

                    login.setName(matriculas.get(0)[1].toString());
                    LoginFuncionarioBean.getInstance().setMatricula(matriculas.get(0)[0].toString());
                    LoginFuncionarioBean.getInstance().setFuncionario(true);
                }
            }
        } catch (Exception ex) {
            login.setStatus(ILogin.Status.FAIL);
            login.setMessage(ex.getMessage());
        }

        return login;
    }
}
