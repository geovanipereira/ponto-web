package der.ponto.backBeans;

import der.ponto.Ocorrencia;
import der.ponto.StatusException;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import java.util.ArrayList;
import java.util.List;
import org.ajax4jsf.model.KeepAlive;
import util.jsf.BackBeanViewPage;

@KeepAlive
public class AutorizarOcorrenciaBackBean extends BackBeanViewPage {

    private static final long serialVersionUID = 1393425272256822060L;
    private List<Ocorrencia> listaOcorrencia = new ArrayList<Ocorrencia>();
    private List<Ocorrencia> listaOcorrenciaSelecionados;
    private String motivo;
    IDAO daoOcorrencia = DAOFactory.getInstance().getDAO(Ocorrencia.class);

    /** Creates a new instance of AutorizarOcorrenciaBackBean */
    public AutorizarOcorrenciaBackBean() {
    }

    public void listaOcorrencia() {
        try {
            String valor = LoginFuncionarioBean.getInstance().getMatricula();

            if (motivo != null && !motivo.equals("")) {
                listaOcorrencia = daoOcorrencia.query("from der.ponto.Ocorrencia o where o.status.id = 1 and o.funcionario.lotacao.orientador.matricula = :valor and o.motivo = :motivo", valor, motivo);
            } else {
                listaOcorrencia = daoOcorrencia.query("from der.ponto.Ocorrencia o where o.status.id = 1 and o.funcionario.lotacao.orientador.matricula = :valor", valor);
            }

            if(listaOcorrencia.size() > 0){
                addInfoMessage(null, "info", "Existem {0} ocorrências para autorizar", listaOcorrencia.size());
            }
            else {
                addInfoMessage(null, "info", "Não existem ocorrências para autorizar");
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

    public void actionSalvarListaOcorrenciaSelecionado() {
        try {
            listaOcorrenciaSelecionados = new ArrayList<Ocorrencia>();

            for (Ocorrencia ocorrencia : listaOcorrencia) {
                if (ocorrencia.isSelected()) {
                    ocorrencia.autorizar();
                    listaOcorrenciaSelecionados.add(ocorrencia);
                }
            }

            if (listaOcorrenciaSelecionados.size() > 0) {
                daoOcorrencia.save(listaOcorrenciaSelecionados.toArray());
                addInfoMessage("", "info", "Ocorrências foram transferidas para validação");
            } else {
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

        listaOcorrencia();
    }

    public void actionCancelarListaOcorrenciaSelecionado() {
        try {
            listaOcorrenciaSelecionados = new ArrayList<Ocorrencia>();

            for (Ocorrencia ocorrencia : listaOcorrencia) {
                if (ocorrencia.isSelected()) {
                    ocorrencia.cancelar();
                    listaOcorrenciaSelecionados.add(ocorrencia);
                }
            }

            if (listaOcorrenciaSelecionados.size() > 0) {
                daoOcorrencia.save(listaOcorrenciaSelecionados.toArray());
                addInfoMessage("", "info", "Ocorrências foram canceladas");
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

        listaOcorrencia();
    }

    public void autorizarOcorrencia(Ocorrencia ocorrencia) {
        try {
            String descricao = ocorrencia.getDescricao();
            String motivoAtual = ocorrencia.getMotivo();
            daoOcorrencia.load(ocorrencia);
            ocorrencia.setDescricao(descricao);
            ocorrencia.setMotivo(motivoAtual);
            ocorrencia.autorizar();
            daoOcorrencia.save(ocorrencia);
            addInfoMessage("", "info", "Ocorrência foi transferida para validação");
        } catch (StatusException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOValidationException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOConstraintException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }

        listaOcorrencia();
    }

    public void cancelarOcorrencia(Ocorrencia ocorrencia) {
        try {
            String descricao = ocorrencia.getDescricao();
            daoOcorrencia.load(ocorrencia);
            ocorrencia.setDescricao(descricao);
            ocorrencia.cancelar();
            daoOcorrencia.save(ocorrencia);
            addInfoMessage("", "info", "Ocorrência foi cancelada.");
        } catch (StatusException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOValidationException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOConstraintException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (DAOException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }

        listaOcorrencia();
    }

    public List<Ocorrencia> getListaOcorrencia() {
        return listaOcorrencia;
    }

    public void setListaOcorrencia(List<Ocorrencia> listaOcorrencia) {
        this.listaOcorrencia = listaOcorrencia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
