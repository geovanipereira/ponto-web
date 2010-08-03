package der.ponto.backBeans;

import der.ponto.MesProcessado;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.IDAO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import util.convert.Convert;
import util.enums.Mes;
import util.jsf.BackBeanViewPage;

public class EncerrarMesBackBean extends BackBeanViewPage {
    private static final long serialVersionUID = -4925074181202581324L;

    private String mes;
    private String ano;

    private MesProcessado mesProcessado;

    private List<SelectItem> mesLista;

    IDAO daoMesProcessado = DAOFactory.getInstance().getDAO(MesProcessado.class);
    /** Creates a new instance of EncerrarMesBackBean */
    public EncerrarMesBackBean() {
        inicializarForm();
    }

    public void inicializarForm(){
        mesProcessado = new MesProcessado();
        
        Date data = new Date();
        String dataString = Convert.dateToString(data);
        String[] dados = dataString.split("\\/");

        mes = dados[1];
        ano = dados[2];
    }

    public List<SelectItem> getListaMeses(){
        mesLista = new ArrayList<SelectItem>();

        for (Mes mesSeleciona : Mes.values()) {
            SelectItem si = new SelectItem();
            si.setLabel(mesSeleciona.getDescricao());
            si.setValue(mesSeleciona.getValor());
            mesLista.add(si);
        }

        return mesLista;
    }

    public void gravarEncerramentoMes(){
        try {
            Date data = Convert.stringToDate("01/" + mes + "/" + ano);

            mesProcessado.setDataMes(data);

            System.out.println(mesProcessado);

            daoMesProcessado.save(mesProcessado);

            addInfoMessage(null, "info", "Operação realizada com sucesso.");

            inicializarForm();

        } catch (ParseException ex) {
            Logger.getLogger(EncerrarMesBackBean.class.getName()).log(Level.SEVERE, null, ex);
            addErrorMessage(null, "erro", ex.getMessage());
        } catch (Exception ex) {
            addErrorMessage(null, "erro", ex.getMessage());
        }
    }

    public List<MesProcessado> actionListaMesProcessado() throws DAOException {
        List<MesProcessado> lista = daoMesProcessado.query("from der.ponto.MesProcessado order by dataMes desc");

        return lista;
    }


    public void deletaMesProcessado(MesProcessado mesProcessado) throws DAOConstraintException, DAOException {

        daoMesProcessado.delete(mesProcessado);
        addInfoMessage(null, "info", "Operação realizada com sucesso.");
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public List<SelectItem> getMesLista() {
        return mesLista;
    }

    public MesProcessado getMesProcessado() {
        return mesProcessado;
    }

    public void setMesProcessado(MesProcessado mesProcessado) {
        this.mesProcessado = mesProcessado;
    }
}
