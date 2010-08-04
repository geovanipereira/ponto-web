package der.ponto.backBeans;

import der.ponto.Funcionario;
import der.ponto.FuncionarioReport;
import der.ponto.Lotacao;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import faces.backBeans.LoginBean;
import faces.backBeans.SessionBean;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.jsf.BackBeanViewPage;

public class RelatorioFuncionarioBackBean extends BackBeanViewPage {

    private static final long serialVersionUID = 4992098074973154617L;
    private Lotacao lotacao;
    
    private IDAO daoFuncionario = DAOFactory.getInstance().getDAO(Funcionario.class);

    /** Creates a new instance of RelatorioFrequenciaBackBean */
    public RelatorioFuncionarioBackBean() {
        
    }

    @SuppressWarnings("static-access")
    public void actionReport() {
                
        try {
            String sql = null;
            List<FuncionarioReport> lista = null;
            
            if(lotacao != null) {
                sql = "Select new der.ponto.FuncionarioReport(f.matriculaCompleta, f.nomeCompleto, f.numeroFuncionario, f.lotacao, f.dataInicio, f.batePonto) From der.ponto.Funcionario f Where f.lotacao = :lotacao Order By f.lotacao.descricao, f.nomeCompleto";
                lista = daoFuncionario.query(sql, lotacao);
            }
            else {
                sql = "Select new der.ponto.FuncionarioReport(f.matriculaCompleta, f.nomeCompleto, f.numeroFuncionario, f.lotacao, f.dataInicio, f.batePonto) From der.ponto.Funcionario f Order By f.lotacao.descricao, f.nomeCompleto";
                lista = daoFuncionario.query(sql);
            }
            
            if (lista != null && lista.size() > 0) {                        
                gerarPdf(lista);                
            } else {
                addInfoMessage(null, "info", "Consulta não retornou registros");
            }

        } catch (Exception ex) {
            addErrorMessage(null, "erro", ex.getMessage());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }   

    private void gerarPdf(List<FuncionarioReport> funcionarios) throws IOException, JRException {
        JasperReport report = report = (JasperReport) JRLoader.loadObject(getDiretorioReal("/relatorios/relatorioFuncionario.jasper"));

        LoginBean login = (LoginBean) SessionBean.getSessionMapValue("login");

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("LOGO1", getDiretorioReal("/templates/images/logo1.jpg"));
        map.put("LOGO2", getDiretorioReal("/templates/images/logo2.jpg"));
        map.put("REPORT_LOCALE", new Locale("pt", "br"));
        map.put("USUARIO_EMISSAO", login.getName());
        
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(funcionarios);

        JasperPrint jp = JasperFillManager.fillReport(report, map, ds);
        byte[] bytes = JasperExportManager.exportReportToPdf(jp);

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + report.getName() + ".pdf");
        response.getOutputStream().write(bytes);

        FacesContext.getCurrentInstance().responseComplete();
    }

    private String getDiretorioReal(String diretorio) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getServletContext().getRealPath(diretorio);
    }

    private String getContextPath() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getServletContext().getContextPath();
    }

    public Lotacao getLotacao() {
        return lotacao;
    }

    public void setLotacao(Lotacao lotacao) {
        this.lotacao = lotacao;
    }
    
}
