package de.calltopower.simpletodo.impl.enums;

import java.util.Arrays;
import java.util.Optional;

import de.calltopower.simpletodo.api.enums.STDEnum;
import lombok.Getter;

/**
 * Enum for the role
 */
public enum STDUserRole implements STDEnum {
    // @formatter:off
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");
    // @formatter:on

    public static final String ROLE_PREFIX = "ROLE_";

    @Getter
    private String internalName;

    private STDUserRole(String name) {
        this.internalName = name;
    }

    /**
     * Returns the enum role
     * 
     * @param strRole The role as string
     * @return the enum role
     */
    public static Optional<STDUserRole> get(String strRole) {
        String role = getUnprefixedRoleName(strRole);
        return Arrays.stream(STDUserRole.values()).filter(r -> r.getInternalName().equalsIgnoreCase(role)).findFirst();
    }

    /**
     * Returns an unprefixed role name
     * 
     * @param strRole The possibly prefixed role name
     * @return unprefixed role name
     */
    public static String getUnprefixedRoleName(String strRole) {
        return strRole.toUpperCase().startsWith(ROLE_PREFIX) ? strRole.substring(ROLE_PREFIX.length()) : strRole;
    }

}
