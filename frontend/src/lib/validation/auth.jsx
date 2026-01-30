import { z } from 'zod';
import { MIN_LENGTH, MAX_LENGTH, REGEX, ERROR_MESSAGE } from "../../components/constants/passwordConstraints";

export const registerSchema = z.object({
    email: z.string().email('Invalid email address'),
    password: z
        .string()
        .min(MIN_LENGTH, ERROR_MESSAGE)
        .max(MAX_LENGTH, ERROR_MESSAGE)
        .regex(REGEX, ERROR_MESSAGE),
    confirmPassword: z.string(),
    firstName: z.string().min(2).max(50),
    lastName: z.string().min(2).max(50),
}).refine((data) => data.password === data.confirmPassword, {
    message: 'Passwords do not match',
    path: ['confirmPassword'],
});