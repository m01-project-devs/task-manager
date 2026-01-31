import React, { useState } from "react";
import {
  Container,
  Box,
  TextField,
  Button,
  Typography,
  Link,
  Snackbar,
  Alert,
} from "@mui/material";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import jwt_decode from "jwt-decode";
import { authTextFieldSx } from "../components/form/TextFieldStyles";
import { API_BASE_URL } from "../config/apiConfig";

const LoginPage = () => {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!email.match(/^\S+@\S+\.\S+$/)) {
      setError("Please enter a valid email.");
      return;
    }

    if (password.length < 4) {
      setError("Password must be at least 4 characters.");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error("Invalid credentials");
      }

      const data = await response.json();

      // ===== SAVE TOKEN =====
      localStorage.setItem("token", data.token);
      localStorage.setItem("email", email);

      // ===== DECODE TOKEN =====
      const decoded = jwt_decode(data.token);
      const role = decoded.role;

      setSuccess(true);

      // ===== ROLE BASED REDIRECT =====
      setTimeout(() => {
        if (role === "ADMIN") {
          navigate("/users");
        } else {
          navigate("/boards");
        }
      }, 1000);
    } catch (err) {
      setError(err.message || "Something went wrong.");
    } finally {
      setLoading(false);
    }
  };

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
        <Typography variant="h4" gutterBottom>
          Login
        </Typography>

        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ mt: 1, width: "100%" }}
        >
          <TextField
            label="Email"
            type="email"
            fullWidth
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            sx={authTextFieldSx}
          />

          <TextField
            label="Password"
            type="password"
            fullWidth
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            sx={authTextFieldSx}
          />

          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{ mt: 2 }}
            disabled={loading}
          >
            {loading ? "Logging in..." : "Login"}
          </Button>
        </Box>

        <Typography variant="body2" sx={{ mt: 2 }}>
          Don't have an account?{" "}
          <Link component={RouterLink} to="/register">
            Register
          </Link>
        </Typography>

        <Typography variant="body2" sx={{ mt: 1, color: "#1f2937" }}>
          Forgot your password?{" "}
          <Link
            component={RouterLink}
            to="/forgot-password"
            sx={{ color: "#2196f3" }}
            underline="always"
          >
            Reset it
          </Link>
        </Typography>
      </Box>

      {/* ERROR */}
      <Snackbar
        open={!!error}
        autoHideDuration={6000}
        onClose={() => setError("")}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert severity="error" onClose={() => setError("")}>
          {error}
        </Alert>
      </Snackbar>

      {/* SUCCESS */}
      <Snackbar
        open={success}
        autoHideDuration={3000}
        onClose={() => setSuccess(false)}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert severity="success">Login successful!</Alert>
      </Snackbar>
    </Container>
  );
};

export default LoginPage;
