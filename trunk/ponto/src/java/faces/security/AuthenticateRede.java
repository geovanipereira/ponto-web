package faces.security;

import faces.security.ILogin.Status;
import java.net.UnknownHostException;
import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;
import util.jsf.JSFContextHelper;

/**
 * Autenticação pela rede AD. O hostname e o dominio da rede devem ser informador no web.xml :
 *
 *   <!-- Parametro para Autenticacao via rede -->
 *   <context-param>
 *       <description>IP do hostname da rede para autenticação</description>
 *       <param-name>Authentication_hostname</param-name>
 *       <param-value>172.25.144.10</param-value>
 *   </context-param>
 *
 *   <context-param>
 *       <description>Nome do dominio de rede para autenticação</description>
 *       <param-name>Authentication_domain</param-name>
 *       <param-value>DER;</param-value>
 *   </context-param>
 *
 */
public class AuthenticateRede implements IAuthenticate {

    @Override
    public ILogin checkLogin(String usuario, String senha) {
        LoginImpl login = new LoginImpl();
        String hostname = JSFContextHelper.getContextParam("Authentication_hostname");
        String domain = JSFContextHelper.getContextParam("Authentication_domain");

        if (hostname == null) {
            login.setStatus(Status.FAIL);
            login.setMessage("Authentication_hostname não configurado no web.xml");
        } else if (domain == null) {
            login.setStatus(Status.FAIL);
            login.setMessage("Authentication_domain não configurado no web.xml");
        } else {
            UniAddress dc;
            try {
                dc = UniAddress.getByName(hostname);
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain + usuario + ":" + senha);
                SmbSession.logon(dc, auth);

                login.setLogin(usuario);
                login.setStatus(Status.SUCESS);
                login.setMessage("");
            } catch (UnknownHostException ex) {
                login.setStatus(Status.FAIL);
                login.setMessage(ex.getMessage());
            } catch (SmbException ex) {
                login.setStatus(Status.FAIL);
                login.setMessage(ex.getMessage());
            }
        }
        return login;
    }
}
