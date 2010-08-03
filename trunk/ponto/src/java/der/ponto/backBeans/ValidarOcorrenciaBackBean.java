package der.ponto.backBeans;

import der.ponto.Lotacao;
import der.ponto.Ocorrencia;
import der.ponto.StatusException;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.ajax4jsf.model.KeepAlive;
import util.jsf.BackBeanViewPage;

@KeepAlive
public class ValidarOcorrenciaBackBean extends BackBeanViewPage {

    private static final long serialVersionUID = -2022850742591201697L;
    private List<Ocorrencia> listaOcorrencia = new ArrayList<Ocorrencia>();
    private List<Ocorrencia> ocorrenciaSelecionadas;
    IDAO daoOcorrencia = DAOFactory.getInstance().getDAO(Ocorrencia.class);
    private Lotacao lotacao;
    private String motivo;

    /** Creates a new instance of ValidarOcorrenciaBackBean */
    public ValidarOcorrenciaBackBean() {
    }

    public void actionListaOcorrencia() {
        StringBuilder sql = new StringBuilder("from der.ponto.Ocorrencia o where o.status.id = 2");
        List parametros = new LinkedList();

        try {
            if (lotacao != null && !lotacao.getCodigo().equals("")) {
                sql.append(" and o.funcionario.lotacao = :lotacao");
                parametros.add(lotacao);
            }

            if (motivo != null) {
                sql.append(" and o.motivo = :motivo");
                parametros.add(motivo);
            }

            listaOcorrencia = daoOcorrencia.query(sql.toString(), parametros.toArray());

            if (listaOcorrencia.size() > 0) {
                addInfoMessage("", "info", "Foram encontrados {0} ocorrências", listaOcorrencia.size());
            } else {
                addInfoMessage("", "info", "Nenhuma ocorrência encontrada");
            }
            
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }
    }

    public void actionMarcarTodasOcorrencias() {
        for (Ocorrencia ocorrencia : listaOcorrencia) {
            ocorrencia.setSelected(true);
        }
    }

    public void actionValidarListaOcorrenciaSelecionado(){
        try {
            ocorrenciaSelecionadas = new ArrayList<Ocorrencia>();

            for (Ocorrencia ocorrencia : listaOcorrencia) {
                if (ocorrencia.isSelected()) {
                    ocorrencia.validar();
                    ocorrenciaSelecionadas.add(ocorrencia);
                }
            }

            if(ocorrenciaSelecionadas.size() > 0){
                daoOcorrencia.save(ocorrenciaSelecionadas.toArray());
                addInfoMessage("", "info", "Ocorrências foram validadas");
            }
            else {
                addInfoMessage("", "info", "Nenhuma ocorrência foi selecionada");
            }

        } catch (StatusException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOValidationException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOConstraintException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }
        actionListaOcorrencia();
    }

    public void actionDevolverListaOcorrenciaSelecionado(){
        try {
            ocorrenciaSelecionadas = new ArrayList<Ocorrencia>();

            for (Ocorrencia ocorrencia : listaOcorrencia) {
                if (ocorrencia.isSelected()) {
                    ocorrencia.voltar();
                    ocorrenciaSelecionadas.add(ocorrencia);
                }
            }

            if(ocorrenciaSelecionadas.size() > 0){
                daoOcorrencia.save(ocorrenciaSelecionadas.toArray());
                addInfoMessage("", "info", "Ocorrências foram devolvidas");
            }
            else {
                addInfoMessage("", "info", "Nenhuma ocorrência foi selecionada");
            }

        } catch (StatusException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOValidationException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOConstraintException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }
        actionListaOcorrencia();

    }

    public void validarOcorrencia(Ocorrencia ocorrencia) {
        try {
            String obs = ocorrencia.getObservacaoRH();
            daoOcorrencia.load(ocorrencia);
            ocorrencia.setObservacaoRH(obs);
            ocorrencia.validar();
            daoOcorrencia.save(ocorrencia);
            addInfoMessage("", "info", "Ocorrência foi validada.");
        } catch (StatusException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOValidationException ex) {
            addErrorMessage("", "error", ex.getMessage());
        } catch (DAOConstraintException ex) {
            addErrorMessage("", "error", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "error", ex.getMessage());
        }

        actionListaOcorrencia();
    }

    public void voltarOcorrencia(Ocorrencia ocorrencia) {

        try {
            String obs = ocorrencia.getObservacaoRH();
            daoOcorrencia.load(ocorrencia);
            ocorrencia.setObservacaoRH(obs);
            ocorrencia.voltar();
            daoOcorrencia.save(ocorrencia);
            addInfoMessage("", "info", "Status da ocorrência alterado com sucesso.");
        } catch (StatusException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOValidationException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOConstraintException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }

        actionListaOcorrencia();
    }

    public List<Ocorrencia> getListaOcorrencia() {
        return listaOcorrencia;
    }

    public void setListaOcorrencia(List<Ocorrencia> listaOcorrencia) {
        this.listaOcorrencia = listaOcorrencia;
    }

    public Lotacao getLotacao() {
        return lotacao;
    }

    public void setLotacao(Lotacao lotacao) {
        this.lotacao = lotacao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public List<Ocorrencia> getOcorrenciaSelecionadas() {
        return ocorrenciaSelecionadas;
    }

    public void setOcorrenciaSelecionadas(List<Ocorrencia> ocorrenciaSelecionadas) {
        this.ocorrenciaSelecionadas = ocorrenciaSelecionadas;
    }

}
