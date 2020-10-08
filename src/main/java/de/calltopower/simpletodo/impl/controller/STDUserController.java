package de.calltopower.simpletodo.impl.controller;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.calltopower.simpletodo.api.controller.STDController;
import de.calltopower.simpletodo.impl.dto.STDUserDto;
import de.calltopower.simpletodo.impl.dtoservice.STDUserDtoService;
import de.calltopower.simpletodo.impl.requestbody.STDForgotPasswordRequestBody;
import de.calltopower.simpletodo.impl.requestbody.STDUserRequestBody;
import de.calltopower.simpletodo.impl.service.STDAuthService;
import de.calltopower.simpletodo.impl.service.STDUserService;

/**
 * User controller
 */
@RestController
@RequestMapping(path = STDUserController.PATH)
public class STDUserController implements STDController {

    /**
     * The controller path
     */
    public static final String PATH = APIPATH + "/users";

    private static final Logger LOGGER = LoggerFactory.getLogger(STDUserController.class);

    private STDUserDtoService userDtoService;
    private STDUserService userService;
    private STDAuthService authService;

    /**
     * Initializes the controller
     * 
     * @param userDtoService Injected DTO service
     * @param userService    Injected user service
     * @param authService    Injected authentication service
     */
    @Autowired
    public STDUserController(STDUserDtoService userDtoService, STDUserService userService, STDAuthService authService) {
        this.userDtoService = userDtoService;
        this.userService = userService;
        this.authService = authService;
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Set<STDUserDto> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested all users");
        }

        return userDtoService.convert(userService.getAllUsers(userDetails));
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public STDUserDto getUser(@NotNull @PathVariable(name = "id") String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested specific users");
        }

        if (authService.isAdminOrRequestedUser(userDetails, id)) {
            return userDtoService.convert(userService.getUser(userDetails, id));
        } else {
            return userDtoService.convertAbridged(userService.getUser(userDetails, id));
        }
    }

    @SuppressWarnings("javadoc")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public STDUserDto updateUser(@NotNull @PathVariable(name = "id") String id,
            @NotNull @RequestBody STDUserRequestBody requestBody, @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested updating user");
        }

        return userDtoService.convert(userService.updateUser(userDetails, id, requestBody));
    }

    @SuppressWarnings("javadoc")
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteUser(@NotNull @PathVariable(name = "id") String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested deleting user");
        }

        userService.deleteUser(userDetails, id);
    }

    @SuppressWarnings("javadoc")
    @DeleteMapping(path = "")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested deleting all users");
        }

        userService.deleteAllUsers(userDetails);
    }

    @SuppressWarnings("javadoc")
    @PostMapping(path = "/password/forgot", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void forgotPassword(@Valid @RequestBody STDForgotPasswordRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested forgot password");
        }

        userService.forgotPassword(requestBody);
    }

    @SuppressWarnings("javadoc")
    @PutMapping(path = "/password/reset/{id}")
    public void resetPassword(@NotNull @PathVariable(name = "id") String id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested forgot password");
        }

        userService.resetPassword(id);
    }

    @SuppressWarnings("javadoc")
    @PutMapping(path = "/activate/resend")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void activate(@AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested resend activation");
        }

        userService.resendActivation(userDetails);
    }

    @SuppressWarnings("javadoc")
    @PutMapping(path = "/activate/{id}")
    public void activate(@NotNull @PathVariable(name = "id") String id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested activation");
        }

        userService.activate(id);
    }

    @Override
    public String getPath() {
        return PATH;
    }

}
