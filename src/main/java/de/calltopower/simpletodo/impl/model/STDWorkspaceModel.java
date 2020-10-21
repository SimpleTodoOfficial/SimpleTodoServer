package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.calltopower.simpletodo.api.model.STDModel;
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
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
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
