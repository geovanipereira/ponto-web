package faces.listeners;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * PhaseListener de teste.
 * 
 * @author Fábio Barros
 */
public class LogPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 7700487457990644826L;

    /**
     * Metodo executado antes da fase.
     */
    @Override
    public void beforePhase(PhaseEvent event) {
        try {
            System.out.println(">>>Antes da fase: " + event.getPhaseId());
            System.out.println(">>>View Id:" + event.getFacesContext().getViewRoot().getViewId());
        } catch (Exception e) {
        }
    }

    /**
     * Metodo executado depois da fase.
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        try {
            System.out.println(">>>Depois da fase: " + event.getPhaseId());
            System.out.println(">>>View Id:" + event.getFacesContext().getViewRoot().getViewId());
        } catch (Exception e) {
        }
    }

    /**
     * Metodo que indica qual fase sera "escutada".
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
