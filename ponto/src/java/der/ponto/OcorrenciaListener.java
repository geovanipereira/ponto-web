package der.ponto;

import der.DerException;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.DeleteEventListener;

public class OcorrenciaListener implements DeleteEventListener {

    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {
        if (event.getObject() instanceof Ocorrencia) {
            Ocorrencia ocorrencia = (Ocorrencia) event.getObject();

            if (ocorrencia.getStatus().equals(new StatusValidado())) {
                throw new DerException("Ocorrência validada não pode ser excluída");
            }

            if (ocorrencia.getStatus().equals(new StatusAguardandoValidacao())) {
                throw new DerException("Ocorrência autorizada não pode ser excluída");
            }
        }
    }

    @Override
    public void onDelete(DeleteEvent event, Set set) throws HibernateException {
        if (event.getObject() instanceof Ocorrencia) {
            if (set == null) {
                onDelete(event);
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }
}
