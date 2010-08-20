package der.acesso;

import entities.annotations.ActionDescriptor;
import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.jboss.seam.annotations.security.management.UserEnabled;
import org.jboss.seam.annotations.security.management.UserFirstName;
import org.jboss.seam.annotations.security.management.UserLastName;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserRoles;
import util.jsf.BackBeanViewPage;

@Entity
@Table(name = "users", schema = "security")
@NamedQueries(
@NamedQuery(name = "User.ByUserName",
    query = "from der.acesso.User u where u.username = :username")
)

public class User implements Serializable {

    @Id
    @UserPrincipal
    @Column(name = "matricula", length = 14)
    @PropertyDescriptor(index = 1, autoFilter = true, autoSort = true)
    private String username;

    @Column(name = "nome_completo", length = 50)
    @PropertyDescriptor(index = 2, autoFilter = true, autoSort = true)
    private String nomeCompleto;

    @Column(name = "senha", length = 32)
    @PropertyDescriptor(displayName = "Senha", index = 3, secret = true)
    private String passwordHash;

    @Transient
    @UserFirstName
    @PropertyDescriptor(hidden = true)
    @Column(name = "nome_referencia", length = 20)
    private String firstname;

    @Transient
    @UserLastName
    @PropertyDescriptor(hidden = true)
    private String lastname;

    @UserEnabled
    @Column(name = "id_situacao")
    @PropertyDescriptor(index = 3, hidden = true)
    private boolean enabled = true;

    @UserRoles
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(schema = "security", name = "users_x_roles",
    joinColumns = @JoinColumn(name = "id_user"),
    inverseJoinColumns = @JoinColumn(name = "id_role"))
    @PropertyDescriptor(hidden = true)
    private List<Role> roles = new ArrayList<Role>();

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public User() {
    }

    public User(String username, String passwordHash, String nomeCompleto, boolean enabled) {
        this.username = username;
        this.passwordHash = md5(passwordHash);
        this.nomeCompleto = nomeCompleto;
        this.enabled = enabled;
    }// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Get´s e Set´s">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        String senhaHash = md5(passwordHash);
        this.passwordHash = senhaHash;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getFirstname() {
        return nomeCompleto.split(" ")[0];
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }// </editor-fold>

    /**
     * Cria hash da senha informada
     * @param senha
     * @return
     */
    @ActionDescriptor(hidden=true)
    public static String md5(String senha) {
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        sen = hash.toString(16);
        return sen;
    }

    @ActionDescriptor(hidden=true)
    public static String getCurrentUserName() {
        return (String) BackBeanViewPage.evalEL("#{SessionBean.login.username}");
    }

    // <editor-fold defaultstate="collapsed" desc="equals()/hashCode() e ToString()">
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return username;
    }// </editor-fold>
}
