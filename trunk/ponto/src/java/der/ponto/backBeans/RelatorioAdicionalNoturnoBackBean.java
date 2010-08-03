package der.ponto.backBeans;

import der.ponto.Escala;
import der.ponto.Frequencia;
import der.ponto.FrequenciaAdicionalNoturno;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import faces.backBeans.LoginBean;
import faces.backBeans.SessionBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
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

public class RelatorioAdicionalNoturnoBackBean extends BackBeanViewPage {

    IDAO daoEscala = DAOFactory.getInstance().getDAO(Escala.class);
    IDAO daoFreqencia = DAOFactory.getInstance().getDAO(Frequencia.class);
    private Date dataInicial;
    private Date dataFinal;

    /** Creates a new instance of AdicionalNoturnoBackBean */
    public RelatorioAdicionalNoturnoBackBean() {
    }

    public String subtraiHora(String horaSaida, String horaEntrada) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        long min_1 = getMinutos(horaSaida, formatter);
        long min_2 = getMinutos(horaEntrada, formatter);
        long result = (min_1 - min_2) * 60 * 1000;
        Date data = new Date(result);
        return formatter.format(data);
    }

    private long getMinutos(String hora, SimpleDateFormat formatter) {
        Date data;
        try {
            data = formatter.parse(hora);
        } catch (ParseException e) {
            return 0;
        }
        long minutos = data.getTime() / 1000 / 60;
        return minutos;
    }

    public BigDecimal retornaQuantidadeHorasAdicionais(String horaSaida, String horaEntrada) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date horaEntradaDate = formatter.parse(horaEntrada);
        Date horaSaidaDate = formatter.parse(horaSaida);
        Date horaEntradaPadrao = formatter.parse("22:00");
        Date horaSaidaPadrao = formatter.parse("05:00");

        if (horaEntradaDate.before(horaEntradaPadrao)) {
            horaEntrada = "22:00";
        }

        if (horaSaidaDate.after(horaSaidaPadrao)) {
            horaSaida = "05:00";
            }

        String quantidadeHoras = subtraiHora(horaSaida, horaEntrada);

        Calendar hour = Calendar.getInstance();
        hour.setTime(formatter.parse(quantidadeHoras));

        BigDecimal segundos = new BigDecimal(hour.getTimeInMillis() / 1000);

        BigDecimal res = segundos.divide(new BigDecimal("0.875"), 3, RoundingMode.UP);
        //BigDecimal res = segundos.divide(new BigDecimal("52.5"), 2 , RoundingMode.UP);

        return res;
    }

    public void actionRelatorioHorasNoturnas() {
        try {
            SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formatterData = new SimpleDateFormat("dd/MM/yyyy");

            String dataInicialString = formatterData.format(dataInicial);
            String dataFinalString = formatterData.format(dataFinal);

            String matriculas = "'64200100747416', '6420010111641X', '64200100794414', '64200101106619', '6420010077121X'";

            StringBuilder sql = new StringBuilder();
            sql.append("Select e From der.ponto.Escala e Where (e.id.dataEscala Between '" + dataInicialString + "' and '" + dataFinalString + "') ");
            sql.append("and e.id.funcionario.matriculaCompleta IN(" + matriculas + ") order by e.id.funcionario.nomeCompleto, e.id.dataEscala");

            List<Escala> escalas = daoEscala.query(sql.toString());

            int horaControle = 1;

            List<FrequenciaAdicionalNoturno> adicionalNoturnos = new ArrayList<FrequenciaAdicionalNoturno>();

            String horaEntrada = "";
            String horaSaida = "";

            FrequenciaAdicionalNoturno frequenciaAdicionalNoturno = null;
            for (Escala escala : escalas) {
                Frequencia frequencia = (Frequencia) daoFreqencia.query("Select fre From der.ponto.Frequencia fre Where fre.id.funcionario = :funcionario and fre.id.data = :data", escala.getId().getFuncionario(), escala.getId().getDataEscala()).get(0);

                if (escala.getEntrada1() != null && frequencia.getEntrada1() != null && formatterHora.format(escala.getEntrada1()).equals("18:00") && horaControle == 1) {
                    frequenciaAdicionalNoturno = new FrequenciaAdicionalNoturno();

                    frequenciaAdicionalNoturno.setNome(escala.getId().getFuncionario().getNomeCompleto());
                    frequenciaAdicionalNoturno.setLotacao(escala.getId().getFuncionario().getLotacao().getDescricao());

                    if (frequencia.getEntrada1().after(escala.getEntrada1())) {
                        horaEntrada = formatterHora.format(frequencia.getEntrada1());
                    } else {
                        horaEntrada = formatterHora.format(escala.getEntrada1());
                    }

                    frequenciaAdicionalNoturno.setDataEntrada(frequencia.getId().getData());
                    frequenciaAdicionalNoturno.setHoraEntrada(frequencia.getEntrada1());
                    horaControle = 2;
                }

                if (escala.getSaida1() != null && formatterHora.format(escala.getSaida1()).equals("06:00") && horaControle == 2) {

                    if (frequencia.getSaida1() != null) {
                        if (frequencia.getSaida1().after(escala.getSaida1())) {
                            horaSaida = formatterHora.format(escala.getSaida1());
                        } else {
                            horaSaida = formatterHora.format(frequencia.getSaida1());
                        }

                        // Recebe a hora em formato string e transforma em minutos
                        String[] hora = subtraiHora(horaSaida, horaEntrada).toString().split(":");
                        Long minuto = (Long.parseLong(hora[0]) * 60) + Long.parseLong(hora[1]);
                        frequenciaAdicionalNoturno.setQuantidadeMinutos(minuto);

                        BigDecimal quantidadeAdicionalNoturno = retornaQuantidadeHorasAdicionais(horaSaida, horaEntrada);
                        frequenciaAdicionalNoturno.setQuantidadeSegundoAdicionalNoturno(quantidadeAdicionalNoturno);

                        frequenciaAdicionalNoturno.setDataSaida(frequencia.getId().getData());
                        frequenciaAdicionalNoturno.setHoraSaida(frequencia.getSaida1());

                        adicionalNoturnos.add(frequenciaAdicionalNoturno);
                        horaControle = 1;
                    } else {
                        horaControle = 1;
                    }
                }
            }

            if (adicionalNoturnos.size() > 0) {
                gerarPdf(adicionalNoturnos);
            } else {
                addInfoMessage(null, "info", "Não existem registros para visualizar");
            }

        } catch (IOException ex) {
            addErrorMessage(null, "erro", ex.getMessage());
        } catch (JRException ex) {
            addErrorMessage(null, "erro", ex.getMessage());
        } catch (ParseException ex) {
            addErrorMessage(null, "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage(null, "erro", ex.getMessage());
        }
    }

    private void gerarPdf(List<FrequenciaAdicionalNoturno> frequencias) throws IOException, JRException {
        JasperReport report = report = (JasperReport) JRLoader.loadObject(getDiretorioReal("/relatorios/relatorioFrequenciaAdicionalNoturno.jasper"));

        LoginBean login = (LoginBean) SessionBean.getSessionMapValue("login");

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("LOGO1", getDiretorioReal("/relatorios/logo1.jpg"));
        map.put("LOGO2", getDiretorioReal("/relatorios/logo2.jpg"));
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
}
