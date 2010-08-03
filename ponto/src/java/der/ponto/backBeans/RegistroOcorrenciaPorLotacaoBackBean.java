package der.ponto.backBeans;

import der.ponto.Funcionario;
import der.ponto.Grade;
import der.ponto.GradeHorario;
import der.ponto.Lotacao;
import der.ponto.Ocorrencia;
import der.ponto.StatusValidado;
import der.ponto.Motivo;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import util.jsf.BackBeanViewPage;

public class RegistroOcorrenciaPorLotacaoBackBean extends BackBeanViewPage {

    private static final long serialVersionUID = -3953521668117704692L;
    private Date data;
    private boolean entrada1 = false;
    private boolean saida1 = false;
    private boolean entrada2 = false;
    private boolean saida2 = false;
    private String motivo;
    private String descricao;
    private List<Lotacao> lotacaos;
    private List<SelectItem> motivos;
    IDAO daoFuncionario = DAOFactory.getInstance().getDAO(Funcionario.class);
    IDAO daoOcorrencia = DAOFactory.getInstance().getDAO(Ocorrencia.class);
    IDAO daoGradeHorario = DAOFactory.getInstance().getDAO(GradeHorario.class);

    /** Creates a new instance of RegistroOcorrenciaPorLotacaoBackBean */
    public RegistroOcorrenciaPorLotacaoBackBean() {
        lotacaos = new ArrayList<Lotacao>();
    }

    public List getListaMotivos() {
        motivos = new ArrayList();
        for (Motivo motivoItem : Motivo.values()) {
            SelectItem si = new SelectItem();
            si.setLabel(motivoItem.getValor());
            si.setValue(motivoItem.name());
            motivos.add(si);
        }
        return motivos;
    }

    public void actionGravarOcorrencia() {
        for (Lotacao lotacao : lotacaos) {
            try {
                List<Funcionario> funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.lotacao = :valor", lotacao);

                for (Funcionario funcionario : funcionarios) {

                    if (funcionario.getBatePonto() == 0) {
                        Grade gradeHorario = funcionario.getGradeHorario();
                        List<GradeHorario> gradeHorarios = daoGradeHorario.query("From der.ponto.GradeHorario g where g.id.grade = :gradeHorario", gradeHorario);

                        GradeHorario grade = gradeHorarios.get(0);

                        if (isEntrada1() || isSaida1()) {
                            Ocorrencia ocorrencia = new Ocorrencia();
                            ocorrencia.setFuncionario(funcionario);
                            ocorrencia.setDataOcorrencia(data);
                            ocorrencia.setDescricao(descricao);
                            ocorrencia.setMotivo(motivo);
                            ocorrencia.setStatus(new StatusValidado());

                            if (isEntrada1()) {
                                ocorrencia.setSaida(grade.getEntrada1());
                            }
                            if (isSaida1()) {
                                ocorrencia.setRetorno(grade.getSaida1());
                            }

                            if (grade.getEntrada1() != null || grade.getSaida1() != null) {
                                List<Ocorrencia> ocorrencias = daoOcorrencia.query("Select o From der.ponto.Ocorrencia o Where o.funcionario = :funcionario and o.dataOcorrencia = :data and o.saida = :entrada and o.retorno = :saida", funcionario, data, ocorrencia.getSaida(), ocorrencia.getRetorno());

                                if (ocorrencias.size() == 0) {
                                    daoOcorrencia.save(ocorrencia);
                                }
                            }
                        }

                        if (isEntrada2() || isSaida2()) {
                            Ocorrencia ocorrencia = new Ocorrencia();
                            ocorrencia.setFuncionario(funcionario);
                            ocorrencia.setDataOcorrencia(data);
                            ocorrencia.setDescricao(descricao);
                            ocorrencia.setMotivo(motivo);
                            ocorrencia.setStatus(new StatusValidado());

                            if (isEntrada2()) {
                                ocorrencia.setSaida(grade.getEntrada2());
                            }
                            if (isSaida2()) {
                                ocorrencia.setRetorno(grade.getSaida2());
                            }

                            if (grade.getEntrada2() != null || grade.getSaida2() != null) {
                                List<Ocorrencia> ocorrencias = daoOcorrencia.query("Select o From der.ponto.Ocorrencia o Where o.funcionario = :funcionario and o.dataOcorrencia = :data and o.saida = :entrada and o.retorno = :saida and o.status = :status", funcionario, data, ocorrencia.getSaida(), ocorrencia.getRetorno(), ocorrencia.getStatus());
                                //List<Ocorrencia> ocorrencias = daoOcorrencia.query("Select o From der.ponto.Ocorrencia o Where o.funcionario = :funcionario and o.dataOcorrencia = :data and o.saida = :entrada and o.retorno = :saida", funcionario, data, ocorrencia.getSaida(), ocorrencia.getRetorno());

                                if (ocorrencias.size() == 0) {
                                    daoOcorrencia.save(ocorrencia);
                                }
                            }
                        }
                    }
                }
            } catch (DAOValidationException ex) {
                addErrorMessage(null, "error", ex.getMessage());
            } catch (DAOConstraintException ex) {
                addErrorMessage(null, "error", ex.getMessage());
            } catch (DAOException ex) {
                addErrorMessage(null, "error", ex.getMessage());
            }
        }

        addInfoMessage(null, "info", "Ocorrência(s) gravada(s) com sucesso");

        data = null;
        motivo = "";
        descricao = "";
        entrada1 = false;
        saida1 = false;
        entrada2 = false;
        saida2 = false;
        lotacaos = new ArrayList<Lotacao>();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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

    public List<Lotacao> getLotacaos() {
        return lotacaos;
    }

    public void setLotacaos(List<Lotacao> lotacaos) {
        this.lotacaos = lotacaos;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
