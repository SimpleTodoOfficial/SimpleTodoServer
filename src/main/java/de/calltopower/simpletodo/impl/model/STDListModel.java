package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.calltopower.simpletodo.api.model.STDModel;
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
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "NR_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "DATE_CREATED")
    public String createdDate;

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
