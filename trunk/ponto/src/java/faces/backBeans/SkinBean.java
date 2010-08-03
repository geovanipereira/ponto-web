package faces.backBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 * Classe que guarda a SKIN seleconada.
 * @author Fábio Barros
 */
public class SkinBean implements Serializable {

    private static List<SelectItem> SKINS;


    static {
        SKINS = new ArrayList<SelectItem>(10);

        SKINS.add(new SelectItem("blueSky", "BlueSky"));
        SKINS.add(new SelectItem("classic", "Classic"));
        SKINS.add(new SelectItem("ruby", "Ruby"));
        SKINS.add(new SelectItem("wine", "Wine"));
        SKINS.add(new SelectItem("deepMarine", "DeepMarine"));
        SKINS.add(new SelectItem("emeraldTown", "EmeraldTown"));
        SKINS.add(new SelectItem("japanCherry", "Sakura"));
        SKINS.add(new SelectItem("plain", "Plain"));
        SKINS.add(new SelectItem("DEFAULT", "Default"));
        SKINS.add(new SelectItem("NULL", "Null"));
    }
    private String skin = "wine";

    public SkinBean() {
    }

    public SkinBean(String skin) {
        this.skin = skin;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public List<SelectItem> getSkinsList() {
        return SKINS;
    }

    @Override
    public String toString() {
        return skin;
    }
}
