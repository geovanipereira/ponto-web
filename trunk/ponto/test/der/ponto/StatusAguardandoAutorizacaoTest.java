package der.ponto;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marcius
 */
public class StatusAguardandoAutorizacaoTest {

    public StatusAguardandoAutorizacaoTest() {
    }

    /**
     * Test of autorizar method, of class StatusAguardandoAutorizacao.
     */
    @Test
    public void testAutorizar() throws Exception {
        StatusAguardandoAutorizacao instance = new StatusAguardandoAutorizacao(new Ocorrencia());
        instance.autorizar();
        assertTrue(instance.getOcorrencia().getStatus().equals(new StatusAguardandoValidacao()));
    }

    /**
     * Test of validar method, of class StatusAguardandoAutorizacao.
     */
    @Test(expected = StatusException.class)
    public void testValidar() throws Exception {
        StatusAguardandoAutorizacao instance = new StatusAguardandoAutorizacao(new Ocorrencia());
        instance.validar();
    }

    /**
     * Test of cancelar method, of class StatusAguardandoAutorizacao.
     */
    @Test
    public void testCancelar() throws Exception {
        StatusAguardandoAutorizacao instance = new StatusAguardandoAutorizacao(new Ocorrencia());
        instance.cancelar();
        assertTrue(instance.getOcorrencia().getStatus().equals(new StatusCancelado()));
    }

    /**
     * Test of voltar method, of class StatusAguardandoAutorizacao.
     */
    @Test(expected = StatusException.class)
    public void testVoltar() throws Exception {
        StatusAguardandoAutorizacao instance = new StatusAguardandoAutorizacao(new Ocorrencia());
        instance.voltar();
    }
}