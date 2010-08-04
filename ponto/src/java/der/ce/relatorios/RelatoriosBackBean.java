package der.ce.relatorios;

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

public class RelatoriosBackBean {

    /**
     * Gera um arquivo PDF apartir do dados fornecidos
     * -----------------------------------------------------
     * @param lista - Coletion contendo os dados que irão popular o relatório
     * @param LocalPath - Data Source Fonte do relatório
     */
    public void GerarReport(List lista, String LocalPath) throws IOException, JRException {
        JasperReport report = report = (JasperReport) JRLoader.loadObject(getDiretorioReal(LocalPath));
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
        gerarReportPdf(report, ds);
    }

    public void GerarReportMapParams(List lista, String LocalPath, HashMap<String, Object> map) throws IOException, JRException {
        JasperReport report = report = (JasperReport) JRLoader.loadObject(getDiretorioReal(LocalPath));
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
        gerarReportPdfMapParams(report, ds, map);
    }

    public void GerarReportComTitulo(List lista, String LocalPath, String titulo) throws IOException, JRException {
        JasperReport report = report = (JasperReport) JRLoader.loadObject(getDiretorioReal(LocalPath));
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
        gerarReportPdfComTitulo(report, ds, titulo);
    }

    /**
     * Gera um arquivo PDF apartir do dados fornecidos
     * -----------------------------------------------------
     * @param report - relatorio a ser gerado no formato PDF
     * @param ds - Data Source Fonte do relatório
     */
    private void gerarReportPdf(JasperReport report, JRBeanCollectionDataSource ds) throws IOException, JRException {
        // captura o login que está em seção
        LoginBean login = (LoginBean) SessionBean.getSessionMapValue("login");
        // faz confiurações do relatório
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("LogoDER", getDiretorioReal("/templates/images/logo1.jpg"));
        map.put("REPORT_LOCALE", new Locale("pt", "br"));
        map.put("USUARIO_EMISSAO", login.getName());

        // preenche relatório com dados e gera saida
        JasperPrint jp = JasperFillManager.fillReport(report, map, ds);
        byte[] bytes = JasperExportManager.exportReportToPdf(jp);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + report.getName() + ".pdf");
        response.getOutputStream().write(bytes);

        FacesContext.getCurrentInstance().responseComplete();
    }

    private void gerarReportPdfMapParams(JasperReport report, JRBeanCollectionDataSource ds, HashMap<String, Object> map) throws IOException, JRException {
        // captura o login que está em seção
        LoginBean login = (LoginBean) SessionBean.getSessionMapValue("login");

        map.put("LogoDER", getDiretorioReal("/templates/images/logo1.jpg"));
        map.put("REPORT_LOCALE", new Locale("pt", "br"));
        map.put("USUARIO_EMISSAO", login.getName());

        // preenche relatório com dados e gera saida
        JasperPrint jp = JasperFillManager.fillReport(report, map, ds);
        byte[] bytes = JasperExportManager.exportReportToPdf(jp);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + report.getName() + ".pdf");
        response.getOutputStream().write(bytes);

        FacesContext.getCurrentInstance().responseComplete();
    }

    private void gerarReportPdfComTitulo(JasperReport report, JRBeanCollectionDataSource ds, String titulo) throws IOException, JRException {
        // captura o login que está em seção
        LoginBean login = (LoginBean) SessionBean.getSessionMapValue("login");
        // faz confiurações do relatório
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("LogoDER", getDiretorioReal("/templates/images/logo1.jpg"));
        map.put("REPORT_LOCALE", new Locale("pt", "br"));
        map.put("USUARIO_EMISSAO", login.getName());
        map.put("TITULO", titulo);

        // preenche relatório com dados e gera saida
        JasperPrint jp = JasperFillManager.fillReport(report, map, ds);
        byte[] bytes = JasperExportManager.exportReportToPdf(jp);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + report.getName() + ".pdf");
        response.getOutputStream().write(bytes);

        FacesContext.getCurrentInstance().responseComplete();
    }
    // recupera diretorio fisico no HD

    private String getDiretorioReal(String diretorio) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getServletContext().getRealPath(diretorio);
    }

    private String getContextPath() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getServletContext().getContextPath();
    }
}
