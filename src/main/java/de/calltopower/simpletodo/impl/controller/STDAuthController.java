package de.calltopower.simpletodo.impl.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.calltopower.simpletodo.api.controller.STDController;
import de.calltopower.simpletodo.impl.dto.STDTokenDto;
import de.calltopower.simpletodo.impl.dto.STDUserDto;
import de.calltopower.simpletodo.impl.dtoservice.STDTokenDtoService;
import de.calltopower.simpletodo.impl.dtoservice.STDUserDtoService;
import de.calltopower.simpletodo.impl.requestbody.STDSigninRequestBody;
import de.calltopower.simpletodo.impl.requestbody.STDSignupRequestBody;
import de.calltopower.simpletodo.impl.service.STDAuthService;

/**
 * Authentication controller
 */
@RestController
@RequestMapping(path = STDAuthController.PATH)
public class STDAuthController implements STDController {

    /**
     * The controller path
     */
    public static final String PATH = APIPATH + "/auth";

    private static final Logger LOGGER = LoggerFactory.getLogger(STDAuthController.class);

    private STDAuthService authService;
    private STDUserDtoService userDtoService;
    private STDTokenDtoService tokenDtoService;

    /**
     * Initializes the controller
     * 
     * @param userService     Injected user service
     * @param userDtoService  Injected user DTO service
     * @param tokenDtoService Injected token DTO service
     */
    @Autowired
    public STDAuthController(STDAuthService userService, STDUserDtoService userDtoService,
            STDTokenDtoService tokenDtoService) {
        this.authService = userService;
        this.userDtoService = userDtoService;
        this.tokenDtoService = tokenDtoService;
    }

    @SuppressWarnings("javadoc")
    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public STDUserDto registerUser(@Valid @RequestBody STDSignupRequestBody requestBody,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested signup new user");
        }

        return userDtoService.convert(authService.signup(requestBody, userDetails));
    }

    @SuppressWarnings("javadoc")
    @PostMapping(path = "/signin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public STDTokenDto authenticateUser(@Valid @RequestBody STDSigninRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested signing in user");
        }

        return tokenDtoService.convert(authService.signin(requestBody));
    }

    @Override
    public String getPath() {
        return PATH;
    }

}
