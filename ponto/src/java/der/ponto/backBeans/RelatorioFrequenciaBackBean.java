package der.ponto.backBeans;

import der.ponto.Frequencia;
import der.ponto.FrequenciaReport;
import der.ponto.Lotacao;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import faces.backBeans.LoginBean;
import faces.backBeans.SessionBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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

public class RelatorioFrequenciaBackBean extends BackBeanViewPage {

    private static final long serialVersionUID = 4992098074973154617L;
    private String matricula;
    private Date dataInicial;
    private Date dataFinal;
    private IDAO daoFrequencia = DAOFactory.getInstance().getDAO(Frequencia.class);

    /** Creates a new instance of RelatorioFrequenciaBackBean */
    public RelatorioFrequenciaBackBean() {
        params.put("frequencia", "todas");
    }

    @SuppressWarnings("static-access")
    public void actionReport() {
        /*
        BaseReport runReport = new BaseReport();

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

        String realPath = servletContext.getRealPath("/" + "relatorios") + "/";
         */
        List parametros = new LinkedList();

        Lotacao lotacao = (Lotacao) params.get("lotacao");
        try {
            StringBuilder sql = new StringBuilder("Select new der.ponto.FrequenciaReport(fq.id.funcionario.matriculaCompleta,fq.id.funcionario.nomeCompleto,fq.id.data,fq.entrada1,fq.logEntrada1,fq.saida1,fq.logSaida1,fq.entrada2,fq.logEntrada2,fq.saida2,fq.logSaida2,fq.atraso,fq.falta,fq.feriado,fq.pontoIncompleto,fq.observacao,fq.id.funcionario.lotacao,fq.id.funcionario.gradeHorario) From der.ponto.Frequencia fq ");
            sql.append(" Where fq.id.data Between :dataInicial and :dataFinal ");
            parametros.add(dataInicial);
            parametros.add(dataFinal);

            if (matricula != null && !matricula.equals("")) {
                sql.append(" and fq.id.funcionario.matricula = :matricula");
                parametros.add(matricula);
            } else if (lotacao != null) {
                sql.append(" and fq.id.funcionario.lotacao.codigo = :orgao");
                parametros.add(lotacao.getCodigo());
            }

            if (!params.get("frequencia").equals("todas")) {
                sql.append(" and fq." + params.get("frequencia") + " = '1'");
            }

            sql.append(" Order By fq.id.funcionario.lotacao.descricao, fq.id.funcionario.nomeCompleto, fq.id.data");
            List<FrequenciaReport> lista = daoFrequencia.query(sql.toString(), parametros.toArray());

            if (lista != null && lista.size() > 0) {

                List<FrequenciaReport> listaFrequencias = new ArrayList<FrequenciaReport>();

                for (FrequenciaReport frequencia : lista) {
                    String[] minutos = frequencia.getAtraso().toString().split(":");
                    Integer minuto = (Integer.parseInt(minutos[0]) * 60) + Integer.parseInt(minutos[1]);
                    
                    frequencia.setMinuto(minuto);
                    listaFrequencias.add(frequencia);
                }

                gerarPdf(listaFrequencias);
            /*
            runReport.runReport(lista, realPath, false, runReport.PAISAGEM);

            response.addHeader("Cache-Control", "cache, must-revalidate");
            response.addHeader("Pragma", "public");
            response.setContentType("application/pdf");

            byte x1[] = JasperExportManager.exportReportToPdf(runReport.jp);
            response.getOutputStream().write(x1);

            FacesContext.getCurrentInstance().responseComplete();
             */
            } else {
                addInfoMessage(null, "info", "Consulta não retornou registros");
            }

        } catch (Exception ex) {
            addErrorMessage(null, "erro", ex.getMessage());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void actionReportFrequenciaFuncionario() throws IOException, JRException, DAOException {
        matricula = LoginFuncionarioBean.getInstance().getMatricula();

        List<FrequenciaReport> lista = daoFrequencia.query("Select new der.ponto.FrequenciaReport(fq.id.funcionario.matriculaCompleta,fq.id.funcionario.nomeCompleto,fq.id.data,fq.entrada1,fq.logEntrada1,fq.saida1,fq.logSaida1,fq.entrada2,fq.logEntrada2,fq.saida2,fq.logSaida2,fq.atraso,fq.falta,fq.feriado,fq.pontoIncompleto,fq.observacao,fq.id.funcionario.lotacao, fq.id.funcionario.gradeHorario) From der.ponto.Frequencia fq Where fq.id.funcionario.matricula = :userName And fq.id.data Between :dataInicial and :dataFinal Order By fq.id.data", matricula, dataInicial, dataFinal);

        if (lista != null && lista.size() > 0) {

            List<FrequenciaReport> listaFrequencias = new ArrayList<FrequenciaReport>();

            for (FrequenciaReport frequencia : lista) {
                String[] minutos = frequencia.getAtraso().toString().split(":");
                Integer minuto = (Integer.parseInt(minutos[0]) * 60) + Integer.parseInt(minutos[1]);
                
                frequencia.setMinuto(minuto);
                listaFrequencias.add(frequencia);
            }

            gerarPdf(listaFrequencias);

        } else {
            addInfoMessage(null, "info", "Consulta não retornou registros");
        }
    }

    private void gerarPdf(List<FrequenciaReport> frequencias) throws IOException, JRException {
        JasperReport report = report = (JasperReport) JRLoader.loadObject(getDiretorioReal("/relatorios/relatorioFrequencia.jasper"));

        LoginBean login = (LoginBean) SessionBean.getSessionMapValue("login");

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("LOGO1", getDiretorioReal("/templates/images/logo1.jpg"));
        map.put("LOGO2", getDiretorioReal("/templates/images/logo2.jpg"));
        map.put("REPORT_LOCALE", new Locale("pt", "br"));
        map.put("USUARIO_EMISSAO", login.getName());
        map.put("DATA_INICIAL", dataInicial);
        map.put("DATA_FINAL", dataFinal);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(frequencias);

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

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
