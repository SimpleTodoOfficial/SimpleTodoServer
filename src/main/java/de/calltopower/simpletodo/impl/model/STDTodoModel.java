package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import com.fasterxml.jackson.annotation.JsonBackReference;

import de.calltopower.simpletodo.api.model.STDModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
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
    @UuidGenerator(style = Style.TIME)
    @Column(name = "NR_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATED")
    private Date createdDate;

    @Column(name = "STR_MSG")
    private String msg;

    @Column(name = "STR_URL")
    private String url;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_DUE")
    private Date dueDate;

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
                    + "createdDate='%s',"
                    + "msg='%s',"
                    + "url='%s',"
                    + "dateDue='%s',"
                + "]",
                id,
                createdDate,
                msg,
                url != null ? url : "",
                dueDate != null ? dueDate : "-"
                );
        // @formatter:on
    }

}
