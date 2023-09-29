package de.calltopower.simpletodo.impl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.calltopower.simpletodo.api.controller.STDController;

/**
 * Connection controller
 */
@RestController
@RequestMapping(path = STDConnectionController.PATH)
public class STDConnectionController implements STDController {

    /**
     * The controller path
     */
    public static final String PATH = APIPATH + "/connection";

    private static final Logger LOGGER = LoggerFactory.getLogger(STDConnectionController.class);

    /**
     * Initializes the controller
     */
    @Autowired
    public STDConnectionController() {
        // Nothing to see here...
    }

    @GetMapping(path = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean checkAvailability(@AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested check availability");
        }

        return true;
    }

    @GetMapping(path = "/authorized", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean checkAuthorized(@AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested check authorized");
        }

        return true;
    }

    @Override
    public String getPath() {
        return PATH;
    }

}
