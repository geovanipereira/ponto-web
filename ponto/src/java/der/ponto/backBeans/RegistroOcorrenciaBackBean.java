package der.ponto.backBeans;

import der.ponto.Funcionario;
import der.ponto.Ocorrencia;
import der.ponto.StatusException;
import der.ponto.StatusValidado;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import der.ponto.Motivo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import util.jsf.BackBeanViewPage;

public class RegistroOcorrenciaBackBean extends BackBeanViewPage implements Serializable {

    private static final long serialVersionUID = 3738145639680057372L;
    private Ocorrencia ocorrenciaForm;
    private List<SelectItem> motivos;
    private String matricula;
    private String matriculaCompleta;
    private String descricaoGrade;
    private Date periodoInicial;
    private Date periodoFinal;
    private List<String> diaSemanas;
    private List<SelectItem> listaOcorrencias;
    private List<String> idOcorrenciaSelecionadas;
    IDAO daoFuncionario = DAOFactory.getInstance().getDAO(Funcionario.class);
    IDAO daoOcorrencia = DAOFactory.getInstance().getDAO(Ocorrencia.class);

    public List getListaMotivos() {
        motivos = new ArrayList();
        for (Motivo motivo : Motivo.values()) {
            SelectItem si = new SelectItem();
            si.setLabel(motivo.getValor());
            si.setValue(motivo.name());
            motivos.add(si);
        }
        return motivos;
    }

    /** Creates a new instance of RegistroOcorrenciaBackBean */
    public RegistroOcorrenciaBackBean() {
        inicializaForm();
    }

    public List<Funcionario> autoCompleteServidor(Object o) throws DAOException {
        List<Funcionario> servidores = daoFuncionario.query("from der.ponto.Funcionario where nomeCompleto like '" + o.toString() + "%' Order By nomeCompleto", 0, 10);
        return servidores;
    }

    public void inicializaForm() {
        Funcionario funcionario = new Funcionario();

        ocorrenciaForm = new Ocorrencia();
        ocorrenciaForm.setFuncionario(funcionario);
        ocorrenciaForm.setStatus(new StatusValidado());

        setPeriodoInicial(null);
        setPeriodoFinal(null);

        listaOcorrencias = new ArrayList<SelectItem>();
        idOcorrenciaSelecionadas = new ArrayList<String>();

        diaSemanas = new ArrayList<String>();
        diaSemanas.add("0");
        diaSemanas.add("6");

        matricula = "";
    }

    public void actionConsultaOcorrencia() throws DAOException {
        List<Ocorrencia> lista = daoOcorrencia.query("From der.ponto.Ocorrencia o Where o.funcionario.matricula = :matricula And o.dataOcorrencia Between :dataInicial and :dataFinal", matricula, periodoInicial, periodoFinal);

        for (Ocorrencia ocorrencia : lista) {
            SelectItem item = new SelectItem();
            item.setValue(ocorrencia.getId().toString());
            item.setLabel(ocorrencia.toString());
            listaOcorrencias.add(item);
        }

        //todo i18n
        if (listaOcorrencias.size() == 0) {
            addInfoMessage(null, "info", "Não existem ocorrências");
        }
    }

    public void actionAddOcorrenciaAll() {
        idOcorrenciaSelecionadas = new ArrayList<String>();
        for (SelectItem item : listaOcorrencias) {
            idOcorrenciaSelecionadas.add(item.getValue().toString());
        }
    }

    public void actionGravaOcorrencia() throws DAOValidationException, DAOConstraintException, DAOException, StatusException {

        Object[] ocorrencias = adicionaOcorrencia().toArray();
        //todo i18n
        if (ocorrencias.length > 0) {
            daoOcorrencia.save(ocorrencias);
            addInfoMessage(null, "info", "{0} ocorrências cadastradas", ocorrencias.length);
        } else {
            addWarnMessage(null, "warn", "Nenhuma ocorrência cadastrada");
        }

        inicializaForm();

    }

    public void actionExcluiOcorrencia() throws DAOConstraintException, DAOException, DAOValidationException {

        List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();

        for (String idOcorrencia : idOcorrenciaSelecionadas) {
            Ocorrencia ocorrencia = (Ocorrencia) daoOcorrencia.query("Ocorrencias.porId", Long.parseLong(idOcorrencia)).get(0);
            try {
                ocorrencia.cancelar();
                ocorrencias.add(ocorrencia);
            } catch (StatusException ex) {
                addErrorMessage(null, "erro", ex.getMessage());
            }
        }
        daoOcorrencia.save(ocorrencias.toArray());

        //todo i18n
        addInfoMessage(null, "info", "{0} ocorrência(s) cancelada(s)", ocorrencias.size());

        inicializaForm();
    }

    public Collection<Ocorrencia> adicionaOcorrencia() throws DAOException, StatusException {
        Calendar diaInicial = Calendar.getInstance();
        diaInicial.setTime(periodoInicial);

        Calendar diaFinal = Calendar.getInstance();
        diaFinal.setTime(periodoFinal);
        diaFinal.add(Calendar.DAY_OF_MONTH, 1);

        List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();

        Funcionario funcionario = (Funcionario) daoFuncionario.query("from der.ponto.Funcionario where matriculaCompleta = :id", matriculaCompleta).get(0);

        while (diaInicial.before(diaFinal)) {
            String diaSemana = String.valueOf(diaInicial.getTime().getDay());

            if (!diaSemanas.contains(diaSemana)) {
                Ocorrencia ocorrencia = new Ocorrencia();
                ocorrencia.setStatus(new StatusValidado());
                ocorrencia.setDataOcorrencia(diaInicial.getTime());
                ocorrencia.setFuncionario(funcionario);
                ocorrencia.setDescricao(ocorrenciaForm.getDescricao());
                ocorrencia.setMotivo(ocorrenciaForm.getMotivo());
                ocorrencia.setSaida(ocorrenciaForm.getSaida());
                ocorrencia.setRetorno(ocorrenciaForm.getRetorno());

                List<Ocorrencia> ocorrenciasTeste = daoOcorrencia.query("Select o From der.ponto.Ocorrencia o Where o.funcionario = :funcionario and o.dataOcorrencia = :data and o.saida = :entrada and o.retorno = :saida and o.status = :status", funcionario, diaInicial.getTime(), ocorrencia.getSaida(), ocorrencia.getRetorno(), ocorrencia.getStatus());
                //List<Ocorrencia> ocorrenciasTeste = daoOcorrencia.query("Select o From der.ponto.Ocorrencia o Where o.funcionario = :funcionario and o.dataOcorrencia = :data and o.saida = :entrada and o.retorno = :saida", funcionario, diaInicial.getTime(), ocorrencia.getSaida(), ocorrencia.getRetorno());

                if (ocorrenciasTeste.size() == 0) {
                    ocorrencias.add(ocorrencia);
                }
            }
            diaInicial.add(Calendar.DAY_OF_MONTH, 1);
        }

        return ocorrencias;
    }

    public Ocorrencia getOcorrenciaForm() {
        return ocorrenciaForm;
    }

    public void setOcorrenciaForm(Ocorrencia ocorrencia) {
        this.ocorrenciaForm = ocorrencia;
    }

    public List<SelectItem> getMotivos() {
        return motivos;
    }

    public void setMotivos(List<SelectItem> motivos) {
        this.motivos = motivos;
    }

    public Date getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public Date getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Date periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public List<String> getDiaSemanas() {
        return diaSemanas;
    }

    public void setDiaSemanas(List<String> diaSemanas) {
        this.diaSemanas = diaSemanas;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<SelectItem> getListaOcorrencias() {
        return listaOcorrencias;
    }

    public void setListaOcorrencias(List<SelectItem> listaOcorrencias) {
        this.listaOcorrencias = listaOcorrencias;
    }

    public List<String> getIdOcorrenciaSelecionadas() {
        return idOcorrenciaSelecionadas;
    }

    public void setIdOcorrenciaSelecionadas(List<String> idOcorrenciaSelecionadas) {
        this.idOcorrenciaSelecionadas = idOcorrenciaSelecionadas;
    }

    public String getMatriculaCompleta() {
        return matriculaCompleta;
    }

    public void setMatriculaCompleta(String matriculaCompleta) {
        this.matriculaCompleta = matriculaCompleta;
    }

    public String getDescricaoGrade() {
        return descricaoGrade;
    }

    public void setDescricaoGrade(String descricaoGrade) {
        this.descricaoGrade = descricaoGrade;
    }
}
