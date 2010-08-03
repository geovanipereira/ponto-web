package faces.listeners;

import faces.backBeans.SessionBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import util.jsf.BackBeanViewPage;

/**
 * PhaseListener para controle de autenticacao.
 *
 * @author Marcius Brandão
 */
public class LoginPhaseListener implements PhaseListener {

    /** Serial Version UID. */
    private static final long serialVersionUID = 8403402696846718508L;

    /**
     * Verifica se o usuario esta logado no sistema.
     * @param e {@link PhaseEvent}
     * @see PhaseListener#beforePhase(PhaseEvent)
     */
    @Override
    public void afterPhase(PhaseEvent e) {
        SessionBean usuarioBean = SessionBean.getInstance();
        
        FacesContext context = e.getFacesContext();
        String viewId = "";
        if (context.getViewRoot() != null) {
            viewId = context.getViewRoot().getViewId();
        }

        //Verifica se há usuario authenticado
        if (e.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {
            if (e.getFacesContext().getExternalContext().getSession(false) == null) {
                System.out.println("sessionExpired");
                e.getFacesContext().getApplication().getNavigationHandler().handleNavigation(
                        e.getFacesContext(), "", "sessionExpired");
            // /public/login.jsf
            }

            if (!viewId.startsWith("/public/") && !usuarioBean.isUserLogged()) {
                BackBeanViewPage.gotoPage("/public/login.jsf");
            }
        }

        if (e.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
            if (viewId.contains("/logout.") || viewId.contains("/login.")) {
                usuarioBean.logout();
            } else if (!viewId.startsWith("/public/") && !usuarioBean.isUserLogged()) {
                BackBeanViewPage.gotoPage("/public/login.jsf");
            }
        }
    }

    /**
     * Metodo da interface PhaseListener. Nao utilizado.
     * @param e {@link PhaseEvent}
     * @see PhaseListener#beforePhase(PhaseEvent)
     */
    @Override
    public void beforePhase(PhaseEvent event) {
    }

    /**
     * A que fase queremos "escutar". No caso {@link PhaseId#RESTORE_VIEW}.
     * @return {@link PhaseId#RESTORE_VIEW}
     * @see PhaseListener#getPhaseId()
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
