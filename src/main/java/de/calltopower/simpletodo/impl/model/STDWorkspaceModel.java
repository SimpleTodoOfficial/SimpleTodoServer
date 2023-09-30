package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.calltopower.simpletodo.api.model.STDModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model implementation for workspaces
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cache(region = "workspaces", usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode(callSuper = false, exclude = { "users", "lists" })
@Table(name = STDWorkspaceModel.TABLE_NAME)
public class STDWorkspaceModel implements Serializable, STDModel {

    private static final long serialVersionUID = 2057035435477510762L;

    /**
     * The table name
     */
    public static final String TABLE_NAME = "workspaces";

    /**
     * The users join table name
     */
    public static final String TABLE_NAME_JOIN_USERS = "workspace_users";

    /**
     * The lists join table name
     */
    public static final String TABLE_NAME_JOIN_LISTS = "workspace_lists";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @UuidGenerator(style = Style.TIME)
    @Column(name = "NR_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATED")
    private Date createdDate;

    @NotBlank
    @Size(max = 100)
    @Column(name = "STR_NAME")
    private String name;

    @NotBlank
    @Column(name = "JSON_DATA")
    private String jsonData;

    @Builder.Default
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = STDWorkspaceModel.TABLE_NAME_JOIN_USERS, joinColumns = @JoinColumn(name = "NR_WORKSPACE_ID"), inverseJoinColumns = @JoinColumn(name = "NR_USER_ID"))
    private Set<STDUserModel> users = new HashSet<>();

    @Builder.Default
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = STDWorkspaceModel.TABLE_NAME_JOIN_LISTS, joinColumns = @JoinColumn(name = "NR_WORKSPACE_ID"), inverseJoinColumns = @JoinColumn(name = "NR_LIST_ID"))
    private Set<STDListModel> lists = new HashSet<>();

    @Override
    public String toString() {
        // @formatter:off
        return String.format(
                "STDWorkspaceModel["
                    + "id='%s',"
                    + "createdDate='%s',"
                    + "name='%s'"
                + "]",
                id,
                createdDate,
                name
               );
        // @formatter:on
    }

}
