package der.ponto;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marcius
 */
public class StatusAguardandoValidacaoTest {

    public StatusAguardandoValidacaoTest() {
    }

    /**
     * Test of autorizar method, of class StatusAguardandoValidacao.
     */
    @Test(expected = StatusException.class)
    public void testAutorizar() throws Exception {
        StatusAguardandoValidacao instance = new StatusAguardandoValidacao(new Ocorrencia());
        instance.autorizar();
    }

    /**
     * Test of validar method, of class StatusAguardandoValidacao.
     */
    @Test
    public void testValidar() throws Exception {
        StatusAguardandoValidacao instance = new StatusAguardandoValidacao(new Ocorrencia());
        instance.validar();
        assertTrue(instance.getOcorrencia().getStatus().equals(new StatusValidado()));
    }

    /**
     * Test of cancelar method, of class StatusAguardandoValidacao.
     */
    @Test(expected = StatusException.class)
    public void testCancelar() throws Exception {
        StatusAguardandoValidacao instance = new StatusAguardandoValidacao(new Ocorrencia());
        instance.cancelar();
    }

    /**
     * Test of voltar method, of class StatusAguardandoValidacao.
     */
    @Test
    public void testVoltar() throws Exception {
        StatusAguardandoValidacao instance = new StatusAguardandoValidacao(new Ocorrencia());
        instance.voltar();
        assertTrue(instance.getOcorrencia().getStatus().equals(new StatusAguardandoAutorizacao()));
    }
}