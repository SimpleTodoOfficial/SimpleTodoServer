ALTER TABLE users RENAME COLUMN STATUS_ACTIVATED TO STATUS_VERIFIED;

ALTER TABLE useractivationtokens RENAME TO user_verification_tokens;

ALTER TABLE forgotpasswordtokens RENAME TO user_forgot_password_tokens;