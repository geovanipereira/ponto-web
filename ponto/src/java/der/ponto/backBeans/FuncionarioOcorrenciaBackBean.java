package der.ponto.backBeans;

import der.ponto.Funcionario;
import der.ponto.GradeHorario;
import der.ponto.Ocorrencia;
import der.ponto.StatusOcorrencia;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.ajax4jsf.model.KeepAlive;
import util.jsf.BackBeanViewPage;

@KeepAlive
public class FuncionarioOcorrenciaBackBean extends BackBeanViewPage {

    private static final long serialVersionUID = -3998770009492783167L;
    private Ocorrencia ocorrenciaForm;
    private Date dataInicial;
    private Date dataFinal;
    private StatusOcorrencia statusOcorrencia;
    private List<Ocorrencia> ocorrencias;
    private boolean entrada1 = false;
    private boolean saida1 = false;
    private boolean entrada2 = false;
    private boolean saida2 = false;
    private StringBuffer sql;
    IDAO daoOcorrencia = DAOFactory.getInstance().getDAO(Ocorrencia.class);
    IDAO daoFuncionario = DAOFactory.getInstance().getDAO(Funcionario.class);

    /** Creates a new instance of FuncionarioOcorrenciaBackBean */
    public FuncionarioOcorrenciaBackBean() {
        ocorrenciaForm = new Ocorrencia();

        ocorrencias = new ArrayList<Ocorrencia>();
    }

    public void actionPesquisaOcorrencia() {
        try {
            String matricula = LoginFuncionarioBean.getInstance().getMatricula();

            sql = new StringBuffer();
            sql.append("From der.ponto.Ocorrencia o Where o.funcionario.matricula = '" + matricula + "'");

            if (dataInicial != null && dataFinal != null) {
                sql.append(" and (o.dataOcorrencia between '" + dataInicial + "' and '" + dataFinal + "') ");
            }

            if (statusOcorrencia != null) {
                sql.append(" and o.status.descricao = '" + statusOcorrencia + "'  ");
            }

            sql.append(" order by o.dataOcorrencia, o.saida, o.retorno ");

            ocorrencias = daoOcorrencia.query(sql.toString());

            if (ocorrencias.size() > 0) {
                addInfoMessage(null, "info", "Foram retornados {0} registros", ocorrencias.size());
            } else {
                addInfoMessage(null, "info", "Não retornou registros");
            }

        } catch (DAOException ex) {
            addErrorMessage(null, "error", ex.getMessage());
        }

    }

    public static int diferencaEmDias(Calendar c1, Calendar c2) {
        long m1 = c1.getTimeInMillis();
        long m2 = c2.getTimeInMillis();
        return (int) ((m2 - m1) / (24 * 60 * 60 * 1000));
    }

    public void actionGravaOcorrencia() {
        try {
            String valor = LoginFuncionarioBean.getInstance().getMatricula();
            List<Funcionario> funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", valor);
            Funcionario funcionario = funcionarios.get(0);

            GradeHorario grade = (GradeHorario) daoFuncionario.query("From der.ponto.GradeHorario g where g.id.grade = :id", funcionario.getGradeHorario()).get(0);

            Calendar diaInicial = Calendar.getInstance();
            diaInicial.setTime(dataInicial);

            Calendar diaAtual = Calendar.getInstance();
            diaAtual.setTime(new Date());

            Calendar diaFinal = Calendar.getInstance();
            diaFinal.setTime(dataFinal);
            diaFinal.add(Calendar.DAY_OF_MONTH, 1);

            if (diferencaEmDias(diaInicial, diaAtual) > 45) {
                addInfoMessage(null, "info", "A data inicial não pode ser menor que 45 dias da data de hoje");

            } else if (diferencaEmDias(diaAtual, diaFinal) > 0) {
                addInfoMessage(null, "info", "Não é possível gravar uma ocorrência para uma data futura");

            } else {
                while (diaInicial.before(diaFinal)) {
                    if (isEntrada1() || isSaida1()) {
                        Ocorrencia ocorrencia = new Ocorrencia();
                        ocorrencia.setFuncionario(funcionario);
                        ocorrencia.setDataOcorrencia(diaInicial.getTime());
                        ocorrencia.setDescricao(ocorrenciaForm.getDescricao());
                        ocorrencia.setMotivo(ocorrenciaForm.getMotivo());
                        if (isEntrada1()) {
                            ocorrencia.setSaida(grade.getEntrada1());
                        }
                        if (isSaida1()) {
                            ocorrencia.setRetorno(grade.getSaida1());
                        }

                        List<Ocorrencia> ocorrenciasTeste = daoOcorrencia.query("Select o From der.ponto.Ocorrencia o Where o.funcionario = :funcionario and o.dataOcorrencia = :data and o.saida = :entrada and o.retorno = :saida", funcionario, diaInicial.getTime(), ocorrencia.getSaida(), ocorrencia.getRetorno());

                        if (ocorrenciasTeste.size() == 0) {
                            daoOcorrencia.save(ocorrencia);
                        }
                    }

                    if (isEntrada2() || isSaida2()) {
                        Ocorrencia ocorrencia = new Ocorrencia();
                        ocorrencia.setFuncionario(funcionario);
                        ocorrencia.setDataOcorrencia(diaInicial.getTime());
                        ocorrencia.setDescricao(ocorrenciaForm.getDescricao());
                        ocorrencia.setMotivo(ocorrenciaForm.getMotivo());
                        if (isEntrada2()) {
                            ocorrencia.setSaida(grade.getEntrada2());
                        }
                        if (isSaida2()) {
                            ocorrencia.setRetorno(grade.getSaida2());
                        }

                        List<Ocorrencia> ocorrenciasTeste = daoOcorrencia.query("Select o From der.ponto.Ocorrencia o Where o.funcionario = :funcionario and o.dataOcorrencia = :data and o.saida = :entrada and o.retorno = :saida", funcionario, diaInicial.getTime(), ocorrencia.getSaida(), ocorrencia.getRetorno());

                        if (ocorrenciasTeste.size() == 0) {
                            daoOcorrencia.save(ocorrencia);
                        }
                    }

                    diaInicial.add(Calendar.DAY_OF_MONTH, 1);
                }
                addInfoMessage(null, "info", "Ocorrência(s) gravada(s) com sucesso");
                ocorrenciaForm = new Ocorrencia();
                dataInicial = null;
                dataFinal = null;

                entrada1 = false;
                saida1 = false;
                entrada2 = false;
                saida2 = false;

            }

        } catch (DAOValidationException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOConstraintException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }

    }

    public void actionExcluirOcorrencia(Ocorrencia ocorrencia) {
        try {
            daoOcorrencia.delete(ocorrencia);

            addInfoMessage(null, "info", "Ocorrência excluída com sucesso");

            ocorrencias = daoOcorrencia.query(sql.toString());

            if (ocorrencias.size() > 0) {
                addInfoMessage(null, "info", "Foram retornados {0} registros", ocorrencias.size());
            } else {
                addInfoMessage(null, "info", "Não retornou registros");
            }

        } catch (DAOConstraintException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }
    }

    public Ocorrencia getOcorrenciaForm() {
        return ocorrenciaForm;
    }

    public void setOcorrenciaForm(Ocorrencia ocorrenciaForm) {
        this.ocorrenciaForm = ocorrenciaForm;
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

    public StatusOcorrencia getStatusOcorrencia() {
        return statusOcorrencia;
    }

    public void setStatusOcorrencia(StatusOcorrencia statusOcorrencia) {
        this.statusOcorrencia = statusOcorrencia;
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(List<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public StringBuffer getSql() {
        return sql;
    }

    public void setSql(StringBuffer sql) {
        this.sql = sql;
    }

    public boolean isEntrada1() {
        return entrada1;
    }

    public void setEntrada1(boolean entrada1) {
        this.entrada1 = entrada1;
    }

    public boolean isEntrada2() {
        return entrada2;
    }

    public void setEntrada2(boolean entrada2) {
        this.entrada2 = entrada2;
    }

    public boolean isSaida1() {
        return saida1;
    }

    public void setSaida1(boolean saida1) {
        this.saida1 = saida1;
    }

    public boolean isSaida2() {
        return saida2;
    }

    public void setSaida2(boolean saida2) {
        this.saida2 = saida2;
    }
}
