package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import de.calltopower.simpletodo.api.model.STDModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Model implementation for todos
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cache(region = "todos", usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode(callSuper = false, exclude = { "list" })
@Table(name = STDTodoModel.TABLE_NAME)
public class STDTodoModel implements Serializable, STDModel {

    private static final long serialVersionUID = -3985241712151192793L;

    /**
     * The table name
     */
    public static final String TABLE_NAME = "todos";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "NR_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "DATE_CREATED")
    private String dateCreated;

    @Column(name = "STR_MSG")
    private String msg;

    @Column(name = "DATE_DUE")
    private String dateDue;

    @Column(name = "STATUS_DONE")
    private boolean statusDone;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NR_LIST_ID", referencedColumnName = "NR_ID")
    private STDListModel list;

    @NotBlank
    @Column(name = "JSON_DATA")
    private String jsonData;

    @Override
    public String toString() {
        // @formatter:off
        return String.format(
                "STDTodoModel["
                    + "id='%s',"
                    + "dateCreated='%s',"
                    + "msg='%s',"
                    + "dateDue='%s',"
                + "]",
                id,
                dateCreated,
                msg,
                dateDue != null ? dateDue : "-"
                );
        // @formatter:on
    }

}
