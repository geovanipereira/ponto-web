package faces.security;

/**
 * Define a interface a ser implementada pelas classes responsáveis por verificar o login e senha
 *
 * @author Marcius
 */
public interface IAuthenticate {

    ILogin checkLogin(String username, String password);

}
