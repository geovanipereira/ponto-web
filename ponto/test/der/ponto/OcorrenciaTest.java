package der.ponto;

import entities.dao.DAOFactory;
import entities.dao.IDAO;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 39291
 */
public class OcorrenciaTest {

    public OcorrenciaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DAOFactory.getInstance().addMapFiles("postgres.hibernate.cfg.xml");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of autorizar method, of class Ocorrencia.
     */
    @Test
    public void testAutorizar() throws Exception {
        IDAO dao = DAOFactory.getInstance().getDAO(Ocorrencia.class);
        List<Ocorrencia> ocorrencias = dao.query("from der.ponto.Ocorrencia o where o.status.id = 1");

        for (Ocorrencia ocorrencia : ocorrencias) {
            ocorrencia.autorizar();
        }
    }

    /**
     * Test of cancelar method, of class Ocorrencia.
     */
    @Test
    public void testCancelar() throws Exception {
        IDAO dao = DAOFactory.getInstance().getDAO(Ocorrencia.class);
        List<Ocorrencia> ocorrencias = dao.query("from der.ponto.Ocorrencia o where o.status.id = 1");

        for (Ocorrencia ocorrencia : ocorrencias) {
            ocorrencia.cancelar();
        }
    }

    /**
     * Test of validar method, of class Ocorrencia.
     */
    @Test
    public void testValidar() throws Exception {
        IDAO dao = DAOFactory.getInstance().getDAO(Ocorrencia.class);
        List<Ocorrencia> ocorrencias = dao.query("from der.ponto.Ocorrencia o where o.status.id = 2");

        assertTrue(ocorrencias.size()>0);
        for (Ocorrencia ocorrencia : ocorrencias) {
            ocorrencia.validar();
        }
    }

    /**
     * Test of voltar method, of class Ocorrencia.
     */
    @Test
    public void testVoltar() throws Exception {
        IDAO dao = DAOFactory.getInstance().getDAO(Ocorrencia.class);
        List<Ocorrencia> ocorrencias = dao.query("from der.ponto.Ocorrencia o where o.status.id = 2");

        for (Ocorrencia ocorrencia : ocorrencias) {
            ocorrencia.voltar();
        }
    }

}