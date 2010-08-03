/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package der.ponto;

import entities.dao.DAOFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author 39291
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({der.ponto.TestModelPonto.class,
    der.ponto.StatusAguardandoAutorizacaoTest.class,
    der.ponto.StatusAguardandoValidacaoTest.class,
    der.ponto.StatusCanceladoTest.class,
    der.ponto.StatusValidadoTest.class})
public class PontoSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
        DAOFactory.getInstance().addMapFiles("postgres.hibernate.cfg.xml");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}