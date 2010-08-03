package faces.security;

/**
 * Define a interface de autenticação do usuário no sistema
 *
 * @author Marcius
 */
public interface ILogin {

    public enum Status {
        NONE,SUCESS,ERRO,FAIL
    }

    //TODO renomear para username
    String getLogin();
    void setLogin(String login);

    String getName();
    void setName(String name);

    Status getStatus();
    void setStatus(Status status);

    String getMessage();
    void setMessage(String message);
    
}
