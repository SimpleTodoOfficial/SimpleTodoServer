package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.calltopower.simpletodo.api.model.STDModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model implementation for users
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cache(region = "users", usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode(callSuper = false, exclude = { "roles", "workspaces" })
// @formatter:off
@Table(name = STDUserModel.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(columnNames = "STR_USERNAME"),
        @UniqueConstraint(columnNames = "STR_EMAIL")
    }
)
// @formatter:on
public class STDUserModel implements Serializable, STDModel {

    private static final long serialVersionUID = 4806970452036198396L;

    /**
     * The table name
     */
    public static final String TABLE_NAME = "users";

    /**
     * The role join table name
     */
    public static final String TABLE_NAME_JOIN_ROLE = "user_roles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @UuidGenerator(style = Style.TIME)
    @Column(name = "NR_ID", columnDefinition = "UUID")
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "STR_USERNAME")
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "STR_EMAIL")
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(name = "STR_PASSWORD")
    private String password;

    @NotBlank
    @Column(name = "JSON_DATA")
    private String jsonData;

    @Column(name = "STATUS_VERIFIED")
    private boolean statusVerified;

    @Builder.Default
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = STDUserModel.TABLE_NAME_JOIN_ROLE, joinColumns = @JoinColumn(name = "NR_USER_ID"), inverseJoinColumns = @JoinColumn(name = "NR_ROLE_ID"))
    private Set<STDRoleModel> roles = new HashSet<>();

    @Builder.Default
    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<STDWorkspaceModel> workspaces = new HashSet<>();

    @Override
    public String toString() {
        // @formatter:off
        return String.format(
                "STDUserModel["
                    + "id='%s',"
                    + "username='%s',"
                    + "email='%s'"
                + "]",
                id,
                username,
                email
               );
        // @formatter:on
    }

}
