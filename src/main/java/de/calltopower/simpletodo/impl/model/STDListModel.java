package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.ManyToOne;
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
 * Model implementation for lists
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cache(region = "lists", usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode(callSuper = false, exclude = { "workspace", "todos" })
@Table(name = STDListModel.TABLE_NAME)
public class STDListModel implements Serializable, STDModel {

    private static final long serialVersionUID = 6989979312962940987L;

    /**
     * The table name
     */
    public static final String TABLE_NAME = "lists";

    /**
     * The users join table name
     */
    public static final String TABLE_NAME_JOIN_TODOS = "list_todos";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @GenericGenerator(name = "uuid2")
    @Column(name = "NR_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATED")
    private Date createdDate;

    @NotBlank
    @Size(max = 100)
    @Column(name = "STR_NAME")
    private String name;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NR_WORKSPACE_ID", referencedColumnName = "NR_ID")
    private STDWorkspaceModel workspace;

    @NotBlank
    @Column(name = "JSON_DATA")
    private String jsonData;

    @Builder.Default
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = STDListModel.TABLE_NAME_JOIN_TODOS, joinColumns = @JoinColumn(name = "NR_LIST_ID"), inverseJoinColumns = @JoinColumn(name = "NR_TODO_ID"))
    private Set<STDTodoModel> todos = new HashSet<>();

    @Override
    public String toString() {
        // @formatter:off
        return String.format(
                "STDListModel["
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
