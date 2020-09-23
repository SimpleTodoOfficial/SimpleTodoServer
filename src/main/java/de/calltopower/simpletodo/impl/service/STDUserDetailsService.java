package de.calltopower.simpletodo.impl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.db.repository.STDUserRepository;
import de.calltopower.simpletodo.impl.exception.STDUserException;
import de.calltopower.simpletodo.impl.model.STDUserDetailsImpl;
import de.calltopower.simpletodo.impl.model.STDUserModel;

/**
 * UserDetailsService implementation
 */
@Service
public class STDUserDetailsService implements UserDetailsService, STDService {

    @Autowired
    STDUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws STDUserException {
        // @formatter:off
        STDUserModel user = userRepository.findByUsername(username)
                                          .orElseThrow(() -> new STDUserException(String.format("User with username \"%s\" not found", username)));
        // @formatter:on

        return STDUserDetailsImpl.build(user);
    }

}
