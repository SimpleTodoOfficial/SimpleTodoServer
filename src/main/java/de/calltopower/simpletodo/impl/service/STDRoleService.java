package de.calltopower.simpletodo.impl.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.db.repository.STDUserRoleRepository;
import de.calltopower.simpletodo.impl.enums.STDUserRole;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.model.STDRoleModel;

/**
 * Service for role results
 */
@Service
public class STDRoleService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDRoleService.class);

    private STDUserRoleRepository roleRepository;

    /**
     * Initializes the service
     * 
     * @param roleRepository The role repository
     */
    @Autowired
    public STDRoleService(STDUserRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Converts given roles as string to STDRoleModel
     * 
     * @param roles The roles as string
     * @return converted roles
     */
    public Set<STDRoleModel> convertRoles(Set<String> roles) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Converting roles \"%s\"", roles));
        }
        if (roles == null) {
            return new HashSet<>();
        }

        Set<STDRoleModel> allRoles = roleRepository.findAll().stream().collect(Collectors.toSet());

        // @formatter:off
        return roles.stream()
                .map(String::toUpperCase)
                .map(role -> {
                    Optional<STDUserRole> userRoleOpt = STDUserRole.get(role);
                    if (!userRoleOpt.isPresent()) {
                        throw new STDNotFoundException(String.format("Role \"%s\" not found.", role));
                    }
                    
                    return userRoleOpt;
                })
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get())
                .map(role -> {
                    Optional<STDRoleModel> mappedRole = allRoles.stream()
                    .filter(r -> r.getName().getInternalName().equalsIgnoreCase(role.getInternalName()))
                    .findFirst();
                    if (!mappedRole.isPresent()) {
                        throw new STDNotFoundException(String.format("Role \"%s\" not found in database.", role));
                    }
                    
                    return mappedRole;
                })
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get())
                .collect(Collectors.toSet());
        // @formatter:on
    }

    /**
     * Returns the standard user role
     * 
     * @return the standard user role
     */
    public STDRoleModel getStandardUserRole() {
        return getRole(STDUserRole.ROLE_USER);
    }

    /**
     * Returns the admin user role
     * 
     * @return the admin user role
     */
    public STDRoleModel getAdminUserRole() {
        return getRole(STDUserRole.ROLE_ADMIN);
    }

    private STDRoleModel getRole(STDUserRole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> new STDNotFoundException(String.format("Role \"%s\" not found.", role)));
    }

}
