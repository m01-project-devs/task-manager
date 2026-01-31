import React, { useState, useMemo } from "react";
import {
  Container,
  Box,
  TextField,
  Button,
  Typography,
  Link,
  Alert,
  Snackbar,
  IconButton,
  InputAdornment,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Collapse,
} from "@mui/material";
import { Visibility, VisibilityOff, Check, Circle } from "@mui/icons-material";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import { API_BASE_URL } from "../config/apiConfig";
import { registerSchema } from "../lib/validation/auth.jsx";
import {
  MIN_LENGTH,
  MAX_LENGTH,
  SPECIAL_CHARS,
} from "../components/constants/passwordConstraints.jsx";

const RegisterPage = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [errors, setErrors] = useState({});
  const [passwordsMatch, setPasswordsMatch] = useState(true);
  const [loading, setLoading] = useState(false);
  const [serverError, setServerError] = useState("");
  const [success, setSuccess] = useState(false);

  const passwordChecks = useMemo(() => {
    const pwd = formData.password;
    return {
      length: pwd.length >= MIN_LENGTH,
      uppercase: /[A-Z]/.test(pwd),
      lowercase: /[a-z]/.test(pwd),
      number: /\d/.test(pwd),
      special: new RegExp(`[${SPECIAL_CHARS}]`).test(pwd),
      maxLength: pwd.length <= MAX_LENGTH,
    };
  }, [formData.password]);

  const isPasswordValid = useMemo(
    () => Object.values(passwordChecks).every((c) => c),
    [passwordChecks],
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: null }));
    if (name === "password") {
      setPasswordsMatch(
        value === formData.confirmPassword || formData.confirmPassword === "",
      );
    } else if (name === "confirmPassword") {
      setPasswordsMatch(value === formData.password);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setServerError("");

    if (formData.password !== formData.confirmPassword) {
      setPasswordsMatch(false);
      setErrors((prev) => ({
        ...prev,
        confirmPassword: "Passwords do not match",
      }));
      return;
    }

    const result = registerSchema.safeParse(formData);
    if (!result.success) {
      const fieldErrors = {};
      result.error.errors.forEach((err) => {
        fieldErrors[err.path[0]] = err.message;
      });
      setErrors(fieldErrors);
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
          firstName: formData.firstName,
          lastName: formData.lastName,
        }),
      });

      if (!response.ok) {
        const data = await response.json().catch(() => ({}));
        throw new Error(
          data.message || `Registration failed (${response.status})`,
        );
      }

      setSuccess(true);
      setTimeout(() => navigate("/login"), 2000);
    } catch (err) {
      setServerError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const RequirementItem = ({ met, text }) => (
    <ListItem dense sx={{ py: 0 }}>
      <ListItemIcon sx={{ minWidth: 30 }}>
        {met ? (
          <Check sx={{ color: "success.main", fontSize: 20 }} />
        ) : (
          <Circle sx={{ color: "grey.400", fontSize: 8 }} />
        )}
      </ListItemIcon>
      <ListItemText
        primary={text}
        sx={{
          color: met ? "success.main" : "text.secondary",
          "& .MuiListItemText-primary": { fontSize: "0.875rem" },
        }}
      />
    </ListItem>
  );

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          p: 4,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          boxShadow: 3,
          borderRadius: 2,
          backgroundColor: "rgba(255, 255, 255, 0.2)",
          backdropFilter: "blur(20px)",
        }}
      >
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600 }}>
          Create Account
        </Typography>

        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ mt: 1, width: "100%" }}
          noValidate
        >
          <Box sx={{ display: { sm: "flex" }, gap: 2 }}>
            <TextField
              name="firstName"
              label="First Name"
              value={formData.firstName}
              onChange={handleChange}
              error={!!errors.firstName}
              helperText={errors.firstName}
              fullWidth
              margin="normal"
              required
            />
            <TextField
              name="lastName"
              label="Last Name"
              value={formData.lastName}
              onChange={handleChange}
              error={!!errors.lastName}
              helperText={errors.lastName}
              fullWidth
              margin="normal"
              required
            />
          </Box>

          <TextField
            name="email"
            label="Email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            error={!!errors.email}
            helperText={errors.email}
            fullWidth
            margin="normal"
            required
          />

          <TextField
            name="password"
            label="Password"
            type={showPassword ? "text" : "password"}
            value={formData.password}
            onChange={handleChange}
            error={
              !!errors.password &&
              formData.password.length > 0 &&
              !isPasswordValid
            }
            helperText={errors.password}
            fullWidth
            margin="normal"
            required
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setShowPassword(!showPassword)}
                    edge="end"
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />

          {formData.password.length > 0 && (
            <Box sx={{ mt: 1, mb: 2, pl: 1 }}>
              <Typography
                variant="caption"
                color="text.secondary"
                sx={{ mb: 0.5, display: "block" }}
              >
                Password requirements:
              </Typography>
              <List dense disablePadding>
                <RequirementItem
                  met={passwordChecks.length}
                  text={`At least ${MIN_LENGTH} characters`}
                />
                <RequirementItem
                  met={passwordChecks.uppercase}
                  text="One uppercase letter (A-Z)"
                />
                <RequirementItem
                  met={passwordChecks.lowercase}
                  text="One lowercase letter (a-z)"
                />
                <RequirementItem
                  met={passwordChecks.number}
                  text="One number (0-9)"
                />
                <RequirementItem
                  met={passwordChecks.special}
                  text={`One special character (${SPECIAL_CHARS})`}
                />
                <RequirementItem
                  met={passwordChecks.maxLength}
                  text={`Maximum ${MAX_LENGTH} characters`}
                />
              </List>
            </Box>
          )}

          <TextField
            name="confirmPassword"
            label="Confirm Password"
            type={showPassword ? "text" : "password"}
            value={formData.confirmPassword}
            onChange={handleChange}
            error={!passwordsMatch || !!errors.confirmPassword}
            helperText={
              !passwordsMatch && formData.confirmPassword !== ""
                ? "⚠️ Passwords do not match!"
                : errors.confirmPassword
            }
            fullWidth
            margin="normal"
            required
            sx={{
              "& .MuiFormHelperText-root": {
                color:
                  !passwordsMatch && formData.confirmPassword !== ""
                    ? "error.main"
                    : "inherit",
                fontWeight:
                  !passwordsMatch && formData.confirmPassword !== ""
                    ? "bold"
                    : "normal",
              },
            }}
          />

          <Collapse in={!passwordsMatch && formData.confirmPassword !== ""}>
            <Alert severity="error" sx={{ mt: 2, mb: 2 }}>
              ❌ Passwords do not match! Please check both fields.
            </Alert>
          </Collapse>

          <Button
            type="submit"
            variant="contained"
            fullWidth
            size="large"
            sx={{ mt: 2, py: 1.5, fontWeight: 600 }}
            disabled={
              loading ||
              (formData.password.length > 0 && !isPasswordValid) ||
              !passwordsMatch
            }
          >
            {loading ? "Creating Account..." : "Register"}
          </Button>
        </Box>

        <Typography align="center" sx={{ mt: 3 }}>
          Already have an account?{" "}
          <Link component={RouterLink} to="/login" underline="hover">
            Sign In
          </Link>
        </Typography>
      </Box>

      <Snackbar
        open={!!serverError}
        autoHideDuration={6000}
        onClose={() => setServerError("")}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert severity="error" onClose={() => setServerError("")}>
          {serverError}
        </Alert>
      </Snackbar>

      <Snackbar
        open={success}
        autoHideDuration={2000}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert severity="success">
          Registration successful! Redirecting to login...
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default RegisterPage;
