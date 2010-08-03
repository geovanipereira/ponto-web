package faces.security;

/**
 * Implementação default de ILogin
 *
 * @author Marcius
 */
public class LoginImpl implements ILogin {

    private String login;
    private String nome = null;
    private String msgGeral = "Usuário não logado";
    private ILogin.Status status = Status.NONE;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getName() {
        return nome;
    }

    @Override
    public void setName(String nome) {
        this.nome = nome;
    }

    @Override
    public String getMessage() {
        return msgGeral;
    }

    @Override
    public void setMessage(String msgGeral) {
        this.msgGeral = msgGeral;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Login: " + login + " Nome: " + nome + " Msg de Erro: " + msgGeral;
    }

}
