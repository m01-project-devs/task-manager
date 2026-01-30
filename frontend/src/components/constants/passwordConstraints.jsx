/**
 * Common password constraints
 * SYNC with backend: PasswordConstraints.java
 */

export const MIN_LENGTH = 6;
export const MAX_LENGTH = 24;
export const SPECIAL_CHARS = "@$!%*?&";

export const REGEX = new RegExp(
    `^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[${SPECIAL_CHARS}])[A-Za-z\\d${SPECIAL_CHARS}]{${MIN_LENGTH},${MAX_LENGTH}}$`
);

export const ERROR_MESSAGE =
    `Password must be ${MIN_LENGTH}–${MAX_LENGTH} characters long ` +
    `and contain at least one uppercase letter, one lowercase letter, one number, ` +
    `and one special character (${SPECIAL_CHARS})`;

export const HINT = `Min ${MIN_LENGTH} chars: 1 upper, 1 lower, 1 number, 1 special (${SPECIAL_CHARS})`;