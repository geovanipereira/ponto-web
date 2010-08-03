package faces.backBeans;

import faces.security.IAuthenticate;
import faces.security.ILogin;
import der.acesso.Role;
import der.acesso.User;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import java.io.Serializable;
import java.util.List;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import util.jsf.MessageHelper;
import util.jsf.JSFContextHelper;
import util.reflection.ObjectHelper;

/**
 * Autentica usuário no sistema.
 *
 * Para definir qual a implementação do login a ser utilizada pelo sistema
 * basta definir a classe que implementa IAuthenticate no web.xml :
 *   <!-- Controle de Acesso -->
 *   <context-param>
 *       <description>Classe de autenticação do usuário : login/senho</description>
 *       <param-name>IAuthenticate</param-name>
 *       <param-value>nome.da.classe.que.implementa.IAuthenticate</param-value>
 *   </context-param>
 *
 * @author Marcius
 * @Version 2.0
 * @since 09/12/2008  
 */
@Name("loginMB")
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 4982939822058347864L;
    @In
    Credentials credentials;
    @In
    Identity identity;
    private String name;
    @NotNull
    @NotEmpty
    private String username;

    // <editor-fold defaultstate="collapsed" desc="Get´s e Set´s">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // </editor-fold>

    /**
     * Este méthodo é invocado quando o JBossSeam Security precisa verificar se tem algum
     * usuário autenticado no sistema.
     *
     * @return
     * @throws entities.dao.DAOException
     */
    public boolean authenticate() {
        boolean authenticate = false;
        try {
            if (credentials.getUsername().equals("admin") && credentials.getPassword().equals("@dministr@dor2009")) {
                //A senha do administrador permite acesso apenas ao cadastro de usuario e roles
                identity.addRole("ADMIN");
                setName(credentials.getUsername());
                setUsername(credentials.getUsername());
                authenticate = true;
            } else if (credentials.getUsername().equals("super") && credentials.getPassword().equals("super@dministr@dor2009")) {
                //A senha super permite acesso com todas as Roles cadastradas
                setName(credentials.getUsername());
                setUsername(credentials.getUsername());

                IDAO dao = DAOFactory.getInstance().getDAO(Role.class);
                List<Role> roles = dao.query("Roles.DistinctRoles");
                for (Role role : roles) {
                    identity.addRole(role.getRolename());
                }

                authenticate = true;
            } else {
                //Autenticação 
                ILogin login = null;
                IAuthenticate iAuthenticate = null;
                String authenticateClassName = JSFContextHelper.getContextParam("IAuthenticate");

                if (authenticateClassName == null) {
                    MessageHelper.getInstance().addErrorMessage(null, "error", "Parametro IAuthenticate não configurado no web.xml");
                    return false;
                }

                try {
                    iAuthenticate = (IAuthenticate) ObjectHelper.newInstance(authenticateClassName);
                } catch (Exception ex) {
                    MessageHelper.getInstance().addErrorMessage(null, "error", ex.getMessage());
                    return false;
                }

                //Verifica se o usuario/senha é válido
                login = iAuthenticate.checkLogin(credentials.getUsername(), credentials.getPassword());

                if (login.getStatus().equals(ILogin.Status.SUCESS)) {
                    setName(login.getName());
                    setUsername(login.getLogin());

                    //TODO
                    SessionBean s = SessionBean.getInstance();
                    SessionBean.setSessionMapValue("login", this);//todo usar contexthelper?
                    //SessionBean.setSessionMapValue("userName", login.getLogin());//todo usar contexthelper?
                    //SessionBean.setSessionMapValue("name", login.getName());//todo usar contexthelper?
                    s.setLogin(this);

                    IDAO dao = DAOFactory.getInstance().getDAO(User.class);
                    List<User> users = dao.query("User.ByUserName", credentials.getUsername());
                    if (users.size() > 0) {
                        if (login.getName() == null) {
                            setName(users.get(0).getFirstname());
                        }

                        for (Role role : users.get(0).getRoles()) {
                            identity.addRole(role.getRolename());
                        }
                    }

                    MessageHelper.getInstance().addInfoMessage(null, "{page.login.userLogged}", login.getMessage(), getName());
                    authenticate = true;
                } else {
                    if (login.getStatus().equals(ILogin.Status.FAIL)) {
                        MessageHelper.getInstance().addFatalMessage(null, "erro", login.getMessage());
                    } else {
                        MessageHelper.getInstance().addErrorMessage(null, "erro", login.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            MessageHelper.getInstance().addExceptionMessage(ex);
            authenticate = false;
        }

        return authenticate;
    }

    @Override
    public String toString() {
        return super.toString() + " ("+username + "/" + name + ")";
    }
}
