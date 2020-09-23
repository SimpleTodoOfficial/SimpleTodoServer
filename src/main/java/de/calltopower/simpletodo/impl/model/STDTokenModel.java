package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import de.calltopower.simpletodo.api.model.STDModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model implementation for tokens
 */
@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class STDTokenModel implements Serializable, STDModel {

    private static final long serialVersionUID = 3734859122797868303L;

    private String token;
    private STDUserModel user;

    @Override
    public String toString() {
        // @formatter:off
        return String.format(
                "STDTokenModel["
                    + "jwt='%s',"
                    + "user='%s'"
                + "]",
                token,
                user
               );
        // @formatter:on
    }

}
