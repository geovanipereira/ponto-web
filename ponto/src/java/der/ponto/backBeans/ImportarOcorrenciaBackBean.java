package der.ponto.backBeans;

import der.ponto.ImportaOcorrencia;
import der.ponto.Funcionario;
import der.ponto.Ocorrencia;
import der.ponto.StatusValidado;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import org.ajax4jsf.model.KeepAlive;
import util.jsf.BackBeanViewPage;

@KeepAlive
public class ImportarOcorrenciaBackBean extends BackBeanViewPage implements Serializable {

    private static final long serialVersionUID = 6972209715861487038L;
    private Date dataInicial;
    private Date dataFinal;
    private String motivo;
    private String lotacao;
    List<ImportaOcorrencia> importaOcorrencias;
    IDAO daoOcorrencia = DAOFactory.getInstance().getDAO(Ocorrencia.class);
    IDAO daoFuncionario = DAOFactory.getInstance().getDAO(Funcionario.class);

    /** Creates a new instance of ImportarOcorrenciaBackBean */
    public ImportarOcorrenciaBackBean() {
    }

    public Connection actionConectaBanco() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@172.25.144.24:1521:dertpro", "dert", "floresta");

        } catch (SQLException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }
        return con;
    }

    public List<SelectItem> actionListaSetorSRH() {
        List<SelectItem> listaItems = null;
        try {
            listaItems = new ArrayList<SelectItem>();

            Connection con = actionConectaBanco();
            Statement stmt = con.createStatement();

            String sql = "select * from dert.TB_SET_SETOR setor order by setor.SET_NOM";

            ResultSet rs = stmt.executeQuery(sql.toString());

            while (rs.next()) {
                SelectItem item = new SelectItem();
                item.setLabel(rs.getString(2));
                item.setValue(rs.getString(1));

                listaItems.add(item);
            }
            con.close();
        } catch (SQLException ex) {
            addErrorMessage("", "erro", ex.getMessage());
        }

        return listaItems;

    }

    public void actionListaOcorrenciaSRH() {
        consultaSRH();

        if (importaOcorrencias.size() > 0) {
            addInfoMessage("", "info", "Foram encontrados {0} afastamentos no sistema SRH", importaOcorrencias.size());
        } else {
            addInfoMessage("", "info", "Nenhum afastamento encontrada");
        }
    }

    public void actionMarcarTodasOcorrencias() {
        for (ImportaOcorrencia importaOcorrencia : importaOcorrencias) {
            importaOcorrencia.setSelected(true);
        }
    }

    public void consultaSRH() {
        try {
            Connection con = actionConectaBanco();

            Statement stmt = con.createStatement();

            DateFormat formatterData = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatadaInicial = formatterData.format(dataInicial);
            String dataFormatadaFinal = formatterData.format(dataFinal);

            StringBuffer sql = new StringBuffer();

            if (motivo.equals("V")) {
                sql.append("select f.FUN_NOM, f.FUN_MATRICULA, d.PDF_DATA_INICIAL, d.PDF_DATA_FINAL, d.PDF_COMPL_SERVICO, s.SET_NOM ");
                sql.append(" from dert.TB_PDF_PEDIDO_DIARIO_FUNCI d, dert.TB_FUN_FUNCIONARIO f, dert.TB_SET_SETOR s ");
                sql.append(" where d.FUN_MATRICULA = f.FUN_MATRICULA ");
                sql.append("and d.PDF_DATA_INICIAL >= to_date('" + dataFormatadaInicial + "','dd/mm/yyyy') ");
                sql.append(" and d.PDF_DATA_FINAL <= to_date('" + dataFormatadaFinal + "','dd/mm/yyyy') ");
                sql.append(" and s.SET_COD = f.SET_COD ");

                if (lotacao != null && !lotacao.equals("")) {
                    sql.append(" and s.SET_COD = '" + lotacao + "' ");
                }

                sql.append(" order by f.FUN_NOM, d.PDF_DATA_INICIAL asc");

                ResultSet rs = stmt.executeQuery(sql.toString());
                Long valor = 1L;

                importaOcorrencias =
                        new ArrayList<ImportaOcorrencia>();

                while (rs.next()) {
                    ImportaOcorrencia importaOcorrencia = new ImportaOcorrencia();
                    importaOcorrencia.setMatricula(rs.getString(2));
                    importaOcorrencia.setNome(rs.getString(1));
                    importaOcorrencia.setMotivo("V");
                    importaOcorrencia.setDataInicio(rs.getDate(3));
                    importaOcorrencia.setDataFinal(rs.getDate(4));
                    importaOcorrencia.setDescricao(rs.getString(5));
                    importaOcorrencia.setLotacao(rs.getString(6));
                    importaOcorrencia.setId(valor);

                    valor = valor + 1L;

                    importaOcorrencias.add(importaOcorrencia);
                }

            } else {
                sql.append("SELECT FUN.FUN_MATRICULA, ");
                sql.append(" FUN.FUN_NOM, ");
                sql.append(" AFF.TAF_CODIGO, ");
                sql.append(" NVL(INTERVALODTINICIO(DERT.COMPARADATAS(AFF.AFF_DT_INICIO, AFF.AFF_DT_FIM, to_date('" + dataFormatadaInicial + "','dd/mm/yyyy'), to_date('" + dataFormatadaFinal + "','dd/mm/yyyy'))), AFF.AFF_DT_INICIO) INICIO, ");
                sql.append(" NVL(INTERVALODTFIM(DERT.COMPARADATAS(AFF.AFF_DT_INICIO, AFF.AFF_DT_FIM, to_date('" + dataFormatadaInicial + "','dd/mm/yyyy'),  to_date('" + dataFormatadaFinal + "','dd/mm/yyyy'))), AFF.AFF_DT_FIM) FIM, ");
                sql.append(" TAF.TAF_DESCRICAO, SETOR.SET_NOM ");
                sql.append(" FROM DERT.TB_AFF_AFASTAMENTO_FUNCIONARIO AFF, ");
                sql.append(" DERT.TB_FUN_FUNCIONARIO             FUN, ");
                sql.append(" DERT.TB_TAF_TIPO_AFASTAMENTO        TAF, ");
                sql.append(" DERT.TB_SET_SETOR                   SETOR ");
                sql.append(" WHERE AFF.FUN_MATRICULA = FUN.FUN_MATRICULA ");
                sql.append(" AND AFF.TAF_CODIGO = TAF.TAF_CODIGO ");
                sql.append(" AND FUN.SET_COD = SETOR.SET_COD ");
                sql.append(" AND DERT.COMPARADATAS(AFF.AFF_DT_INICIO, AFF.AFF_DT_FIM, to_date('" + dataFormatadaInicial + "','dd/mm/yyyy'), to_date('" + dataFormatadaFinal + "','dd/mm/yyyy')) IS NOT NULL ");

                if (lotacao != null && !lotacao.equals("")) {
                    sql.append(" and SETOR.SET_COD = '" + lotacao + "' ");
                }

                sql.append(" AND AFF.TAF_CODIGO = '" + motivo + "' order by FUN.FUN_NOM, AFF.AFF_DT_INICIO asc");

                ResultSet rs = stmt.executeQuery(sql.toString());
                Long valor = 1L;

                importaOcorrencias =
                        new ArrayList<ImportaOcorrencia>();

                while (rs.next()) {
                    ImportaOcorrencia importaOcorrencia = new ImportaOcorrencia();
                    importaOcorrencia.setMatricula(rs.getString(1));
                    importaOcorrencia.setNome(rs.getString(2));
                    importaOcorrencia.setMotivo(rs.getString(3));
                    importaOcorrencia.setDataInicio(rs.getDate(4));
                    importaOcorrencia.setDataFinal(rs.getDate(5));
                    importaOcorrencia.setDescricao(rs.getString(6));
                    importaOcorrencia.setLotacao(rs.getString(7));
                    importaOcorrencia.setId(valor);

                    valor =
                            valor + 1L;

                    importaOcorrencias.add(importaOcorrencia);
                }

            }

            con.close();
        } catch (Exception e) {
            addErrorMessage("", "erro", e.getMessage());
        }

    }

    public void actionImportaOcorrenciaSRH() throws DAOException {
        List<ImportaOcorrencia> itemSelecionados = new ArrayList<ImportaOcorrencia>();

        for (ImportaOcorrencia importaOcorrencia : importaOcorrencias) {
            if (importaOcorrencia.isSelected()) {
                itemSelecionados.add(importaOcorrencia);
            }

        }

        for (ImportaOcorrencia importaOcorrencia : itemSelecionados) {

            Calendar diaInicial = Calendar.getInstance();
            diaInicial.setTime(importaOcorrencia.getDataInicio());

            Calendar diaFinal = Calendar.getInstance();
            diaFinal.setTime(importaOcorrencia.getDataFinal());
            diaFinal.add(Calendar.DAY_OF_MONTH, 1);

            boolean temFuncionario = true;

            List<Funcionario> funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", importaOcorrencia.getMatricula());

            if (funcionarios.size() == 0) {
                funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", importaOcorrencia.getMatricula().substring(0, 7));

                if (funcionarios.size() == 0) {
                    funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", importaOcorrencia.getMatricula().substring(1, 8));

                    if (funcionarios.size() == 0) {
                        funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", importaOcorrencia.getMatricula().substring(1, 7));

                        if (funcionarios.size() == 0) {
                            funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", importaOcorrencia.getMatricula().substring(2, 8));

                            if (funcionarios.size() == 0) {
                                funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", importaOcorrencia.getMatricula().substring(1, 6));

                                if (funcionarios.size() == 0) {
                                    funcionarios = daoFuncionario.query("From der.ponto.Funcionario f Where f.matricula = :valor", importaOcorrencia.getMatricula().substring(2, 6));

                                    if (funcionarios.size() == 0) {
                                        System.out.println("Funcionário: " + importaOcorrencia.getNome() + " não cadastrado no sistema do ponto.");
                                        temFuncionario = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (temFuncionario) {
                while (diaInicial.before(diaFinal)) {
                    try {
                        Ocorrencia ocorrencia = new Ocorrencia();
                        Funcionario funcionario = funcionarios.get(0);
                        ocorrencia.setFuncionario(funcionario);
                        ocorrencia.setMotivo(importaOcorrencia.getMotivo());
                        ocorrencia.setDataOcorrencia(diaInicial.getTime());
                        ocorrencia.setDescricao(importaOcorrencia.getDescricao());
                        ocorrencia.setStatus(new StatusValidado());
                        daoOcorrencia.save(ocorrencia);
                        diaInicial.add(Calendar.DAY_OF_MONTH, 1);
                    } catch (DAOValidationException ex) {
                        addErrorMessage("", "erro", ex.getMessage());
                    } catch (DAOConstraintException ex) {
                        addErrorMessage("", "erro", ex.getMessage());
                    }

                }
            }
        }

        if (itemSelecionados.size() > 0) {
            addInfoMessage("", "info", "Foram importados {0} afastamentos do SRH", itemSelecionados.size());
        } else {
            addInfoMessage("", "info", "Nenhum afastamento selecionado");
        }

        importaOcorrencias = new ArrayList<ImportaOcorrencia>();
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

    public List<ImportaOcorrencia> getImportaOcorrencias() {
        return importaOcorrencias;
    }

    public void setImportaOcorrencias(List<ImportaOcorrencia> importaOcorrencias) {
        this.importaOcorrencias = importaOcorrencias;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getLotacao() {
        return lotacao;
    }

    public void setLotacao(String lotacao) {
        this.lotacao = lotacao;
    }
}
