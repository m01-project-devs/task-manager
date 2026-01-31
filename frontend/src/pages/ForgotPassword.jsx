import { useState } from "react";
import { Link } from "react-router-dom";
import { forgotPassword } from "../api/auth";
import { Box, TextField, Button, Typography, Snackbar, Alert, Container } from "@mui/material";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  async function onSubmit(e) {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!email) {
      setError("Please enter your email address.");
      return;
    }

    let passwordResetSuccessMsg = "Password reset link will be sent in few minutes.";
    try {
      setLoading(true);
      await forgotPassword(email);
      setSuccess(passwordResetSuccessMsg);
    } catch {
      setSuccess(passwordResetSuccessMsg);
    } finally {
      setLoading(false);
    }
  }

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          p: 4,
          mt: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          boxShadow: 3,
          borderRadius: 2,
          backgroundColor: "rgba(255,255,255,0.2)",
          backdropFilter: "blur(20px)"
        }}
      >
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600, textAlign: "center" }}>
          Forgot Password
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 3, textAlign: "center" }}>
          Enter your email and we’ll send you a reset link.
        </Typography>

        <Box component="form" onSubmit={onSubmit} sx={{ width: "100%" }}>
          <TextField
            type="email"
            label="Email address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            fullWidth
            margin="normal"
            required
            disabled={loading}
          />

          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{ mt: 2, py: 1.5, fontWeight: 600 }}
            disabled={loading}
          >
            {loading ? "Sending..." : "Send reset link"}
          </Button>
        </Box>

        <Typography align="center" sx={{ mt: 3 }}>
          <Link to="/login" style={{ textDecoration: "none", color: "#0b5ed7", fontWeight: 600 }}>
            Back to login
          </Link>
        </Typography>
      </Box>

      <Snackbar open={!!error} autoHideDuration={6000} onClose={() => setError("")} anchorOrigin={{ vertical: "top", horizontal: "center" }}>
        <Alert severity="error" onClose={() => setError("")}>{error}</Alert>
      </Snackbar>

      <Snackbar open={!!success} autoHideDuration={3000} onClose={() => setSuccess("")} anchorOrigin={{ vertical: "top", horizontal: "center" }}>
        <Alert severity="success">{success}</Alert>
      </Snackbar>
    </Container>
  );
}
