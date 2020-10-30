package de.calltopower.simpletodo.impl.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import de.calltopower.simpletodo.api.utils.STDUtils;
import lombok.NoArgsConstructor;

/**
 * Date utilities
 */
@Component
@NoArgsConstructor
public class STDDateUtils implements STDUtils {

    public Date getDateMinusMinutes(long minutes) {
        return new Date(System.currentTimeMillis() - (Math.abs(minutes) * 60 * 1000));
    }

    public Date getDatePlusMinutes(long minutes) {
        return new Date(System.currentTimeMillis() + (Math.abs(minutes) * 60 * 1000));
    }

}
