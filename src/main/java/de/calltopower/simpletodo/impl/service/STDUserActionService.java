package de.calltopower.simpletodo.impl.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.impl.db.repository.STDUserForgotPasswordTokensRepository;
import de.calltopower.simpletodo.impl.db.repository.STDUserVerificationTokensRepository;
import de.calltopower.simpletodo.impl.exception.STDGeneralException;
import de.calltopower.simpletodo.impl.model.STDUserForgotPasswordTokenModel;
import de.calltopower.simpletodo.impl.model.STDUserModel;
import de.calltopower.simpletodo.impl.model.STDUserVerificationTokenModel;

/**
 * Service for user actions
 */
@Service
public class STDUserActionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(STDUserActionService.class);

	private STDEmailService emailService;
	private STDUserVerificationTokensRepository userActivationTokensRepository;
	private STDUserForgotPasswordTokensRepository userForgotPasswordTokensRepository;

	/**
	 * Initializes the service
	 * 
	 * @param emailService                       Email service
	 * @param userActivationTokensRepository     user activation tokens repository
	 * @param userForgotPasswordTokensRepository The user forgot password tokens
	 *                                           repository
	 */
	@Autowired
	public STDUserActionService(STDEmailService emailService,
			STDUserVerificationTokensRepository userActivationTokensRepository,
			STDUserForgotPasswordTokensRepository userForgotPasswordTokensRepository) {
		this.emailService = emailService;
		this.userActivationTokensRepository = userActivationTokensRepository;
		this.userForgotPasswordTokensRepository = userForgotPasswordTokensRepository;
	}

	protected void sendEmailAccountCreated(STDUserModel user) {
		LOGGER.debug("Sending account created email");
		try {
			emailService.sendAccountCreatedEmail(user.getEmail(), user);
		} catch (Exception ex) {
			LOGGER.error("Something went wrong with the email service: ", ex);
		}
	}

	protected void sendEmailVerifyEmailAddress(STDUserModel user, String newEmail,
			STDUserVerificationTokenModel model) {
		LOGGER.debug("Sending verify email address email");
		try {
			emailService.sendVerifyEmailAddressEmail(newEmail, user, model);
		} catch (Exception ex) {
			LOGGER.error("Something went wrong with the email service: ", ex);
		}
	}

	protected void sendEmailForgotPassword(STDUserModel user, STDUserForgotPasswordTokenModel model) {
		LOGGER.debug("Sending forgot password email");
		try {
			emailService.sendPasswordForgotEmail(user.getEmail(), model);
		} catch (Exception ex) {
			LOGGER.error("Something went wrong with the email service: ", ex);
		}
	}

	protected void sendEmailNewPasswordGenerated(STDUserModel user, String newPassword) {
		LOGGER.debug("Sending new password generated email");
		try {
			emailService.sendNewPasswordGeneratedEmail(user.getEmail(), newPassword);
		} catch (Exception ex) {
			LOGGER.error("Something went wrong with the email service: ", ex);
		}
	}

	protected void sendEmailEmailAddressVerified(STDUserModel user) {
		LOGGER.debug("Sending email address verified email");
		try {
			emailService.sendEmailAddressverifiedEmail(user.getEmail());
		} catch (Exception ex) {
			LOGGER.error("Something went wrong with the email service: ", ex);
		}
	}

	protected void deleteAllUserActivationTokensForUserId(UUID userId) {
		try {
			for (STDUserVerificationTokenModel token : userActivationTokensRepository.findAllByUserId(userId)) {
				userActivationTokensRepository.deleteById(token.getId());
			}
		} catch (Exception ex) {
			String errMsg = String.format(
					"Something went wrong deleting all user activation tokens for user with username \"%s\"", userId);
			LOGGER.error(errMsg);
			throw new STDGeneralException(errMsg);
		}
	}

	protected void deleteAllUserForgotPasswordTokensForUserId(UUID userId) {
		try {
			for (STDUserForgotPasswordTokenModel token : userForgotPasswordTokensRepository.findAllByUserId(userId)) {
				userForgotPasswordTokensRepository.deleteById(token.getId());
			}
		} catch (Exception ex) {
			String errMsg = String.format(
					"Something went wrong deleting all forgot password tokens for user with username \"%s\"", userId);
			LOGGER.error(errMsg);
			throw new STDGeneralException(errMsg);
		}
	}

}
