package der.acesso;

import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.jboss.seam.annotations.security.management.RoleConditional;
import org.jboss.seam.annotations.security.management.RoleGroups;
import org.jboss.seam.annotations.security.management.RoleName;

@Entity
@Table(name = "roles", schema = "security")
@NamedQueries(@NamedQuery(name = "Roles.DistinctRoles", query = "Select distinct r from der.acesso.Role r"))
public class Role implements Serializable {

    @Id
    @Column(name = "id_role")
    @PropertyDescriptor(index = 1)
    private Integer roleId;

    @RoleName
    @PropertyDescriptor(index = 2)
    @Column(name = "role_name", length = 100)
    private String rolename;

    @RoleConditional
    @PropertyDescriptor(hidden = true)
    private boolean conditional = true;

    @RoleGroups
    @ManyToMany()
    @JoinTable(schema = "security", name = "roles_x_groups",
    joinColumns = @JoinColumn(name = "id_role"),
    inverseJoinColumns = @JoinColumn(name = "id_group"))
    @PropertyDescriptor(hidden = true)
    private List<Role> groups = new ArrayList<Role>();

    public Role() {
    }

    public Role(String rolename) {
        this.rolename = rolename;
    }

    public Role(Integer roleId, String rolename) {
        this.roleId = roleId;
        this.rolename = rolename;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public boolean isConditional() {
        return conditional;
    }

    public void setConditional(boolean conditional) {
        this.conditional = conditional;
    }

    public List<Role> getGroups() {
        return groups;
    }

    public void setGroups(List<Role> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role other = (Role) obj;
        if ((this.rolename == null) ? (other.rolename != null) : !this.rolename.equals(other.rolename)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.rolename != null ? this.rolename.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return rolename;
    }
}