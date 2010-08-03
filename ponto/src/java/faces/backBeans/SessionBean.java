package faces.backBeans;

import entities.dao.DAOFactory;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.jboss.seam.security.Identity;
import util.jsf.BackBeanViewPage;

/*
 * @author Marcius
 * @Version 1.0
 * @since 09/12/2008
 * @see
 */
public class SessionBean extends BackBeanViewPage {

    private static final long serialVersionUID = 5473401848344117609L;

    static {
        DAOFactory.getInstance().
                addMapFiles("hibernate.cfg.xml").
                addMapFiles("auditoria.hibernate.cfg.xml");
    }
    private SkinBean skin = new SkinBean();
    private LocaleBean locale = new LocaleBean();

    public SessionBean() {
        setLocale(locale.getLocale());
    }

    /**
     * Get´s e Set´s
     */
    public LoginBean getLogin() {
        LoginBean sessionLogin = (LoginBean) getSessionMapValue("login");
        return sessionLogin;
    }

    public void setLogin(LoginBean login) {

    }

    public String getSkin() {
        return skin.toString();
    }

    public void setSkin(String skin) {
        this.skin.setSkin(skin);
    }

    public List<SelectItem> getSkinsList() {
        return this.skin.getSkinsList();
    }

    public String getLocale() {
        return locale.getLocale();
    }

    public void setLocale(String locale) {
        this.locale.setLocale(locale);

        Locale.setDefault(new Locale(getLocale()));
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale(getLocale()));
    }

    public List<SelectItem> getLocaleList() {
        return this.locale.getLocaleList();
    }

    public void changeLocale(ValueChangeEvent event) {
        setLocale(event.getNewValue().toString());
    }

    /**
     * Métodos
     */
    public String logout() {
        setSessionMapValue("login", null);
        setLogin(null);
        return "logout";
    }

    public boolean isUserLogged() {
        return (getLogin() != null);
    }

    /**
     * Métodos auxiliares
     */
    static public SessionBean getInstance() {
        return (SessionBean) evalEL("#{" + SessionBean.class.getSimpleName() + "}");
    }

    //TODO: Está fixo, por em algum xml ou ini (usar convensão)
    public String getPathTemplates() {
        return "/templates";
    }

//    //TODO: Está fixo, por em algum xml ou ini (usar convensão)
//    public String getPathTemplatesImages() {
//        return "/templates/images";
//    }

    //TODO: Está fixo, por em algum xml ou ini (usar convensão)
    public String getTemplateViewId() {
        return "/templates/template.xhtml";
    }

    public String getViewId() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    public String getInitialPageDerCe() {
        String page = null;
        page = "derDerCeInicial";
        return page;
    }

    public String getInitialPageProEstradas() {
        String page = null;
        if (Identity.instance().hasRole("RODOVIAS.EMPRESAS")) {
            page = "proSelecionarObras";
        } else if (Identity.instance().hasRole("RODOVIAS.CONSTRUTORA")) {
            page = "proSelecionarObrasConstrutora";
        } else {
            page = "proMapa";
        }
        return page;
    }
}
