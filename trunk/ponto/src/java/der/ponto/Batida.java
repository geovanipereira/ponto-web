package der.ponto;

import entities.annotations.ParameterDescriptor;
import entities.annotations.PropertyDescriptor;
import entities.annotations.View;
import entities.annotations.Views;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BATIDAS", schema = "ponto")
@Views({
    @View(name = "ImportarBatidas",
    title = "Importar Batidas",
    filters = "id.numero,id.data",
    header = "importarBatidas()",
    members = "id.data,id.hora,id.numero",
    rows = 10,
    template = "@PAGER+@FILTER")})
public class Batida implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private BatidaId id;
    @Column(name = "NUM_RELOGIO", length = 5)
    @PropertyDescriptor(displayName = "Nº Relógio")
    private Short numeroRelogio;

    public Batida() {
        id = new BatidaId();
    }

    public Batida(BatidaId id, Short numeroRelogio) {
        this.id = id;
        this.numeroRelogio = numeroRelogio;
    }

    public BatidaId getId() {
        return id;
    }

    public void setId(BatidaId id) {
        this.id = id;
    }

    public Short getNumeroRelogio() {
        return numeroRelogio;
    }

    public void setNumeroRelogio(Short numeroRelogio) {
        this.numeroRelogio = numeroRelogio;
    }

    static public String importarBatidas(
            @ParameterDescriptor(displayName = "Arquivo") InputStream stream)
            throws IOException, ParseException, DAOValidationException, DAOConstraintException, DAOException {
        Set<Batida> batidas = new HashSet<Batida>();
        BufferedReader leitor = null;

        try {
            InputStreamReader streamReader = new InputStreamReader(stream);
            //BufferedReader reader = new BufferedReader(streamReader);
            //leitor = new BufferedReader(new FileReader(file));
            leitor = new BufferedReader(streamReader);

            while (leitor.ready()) {
                String linha = leitor.readLine();

                if (linha != null && !linha.equals("")) {
                    String[] linhas = linha.split("\\,");

                    Batida batida = new Batida();

                    BatidaId batidaPK = new BatidaId();

                    DateFormat formatterData = new SimpleDateFormat("MM/dd/yy");
                    Date date = formatterData.parse(linhas[0]);
                    batidaPK.setData(date);

                    DateFormat formatterHora = new SimpleDateFormat("HH:mm");
                    Date hora = (Date) formatterHora.parse(linhas[1]);
                    batidaPK.setHora(hora);

                    if (linhas[2].length() == 12) {
                        batidaPK.setNumero(Integer.parseInt(linhas[2].substring(0, 10)));
                        batida.setNumeroRelogio(Short.parseShort(linhas[2].substring(10, 12)));
                    } else {
                        batidaPK.setNumero(Integer.parseInt(linhas[2].substring(0, 10)));
                        batida.setNumeroRelogio(Short.valueOf("3"));
                    }
                    batida.setId(batidaPK);
                    batidas.add(batida);
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            leitor.close();
        }

        IDAO dao = DAOFactory.getInstance().getDAO(Batida.class);
        dao.save(batidas.toArray());
        return "Arquivo inportado com sucesso (" + batidas.size() + " batidas importadas)";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Batida other = (Batida) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
