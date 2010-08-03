package faces.backBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import util.convert.I18N;

/**
 * Classe que guarda a SKIN seleconada.
 * @author Fábio Barros
 */
public class LocaleBean implements Serializable {

    private static final List<SelectItem> LOCALES;


    static {
        LOCALES = new ArrayList<SelectItem>(2);

        LOCALES.add(new SelectItem("pt_BR", I18N.getMessage("application.localePtBr","Português")));
        LOCALES.add(new SelectItem("en", I18N.getMessage("application.localeEn","Inglês")));
    }
    private static final long serialVersionUID = -4550876608011329828L;
    private String locale = "pt_BR";    

    public LocaleBean() {
    }

    public LocaleBean(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<SelectItem> getLocaleList() {
        return LOCALES;
    }

    @Override
    public String toString() {
        return locale;
    }
}
