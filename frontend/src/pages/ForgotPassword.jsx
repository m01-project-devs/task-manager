import { useState } from "react";
import { Link } from "react-router-dom";
import { forgotPassword } from "../api/auth";
import "./ForgotPassword.css";

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
      setSuccess(
          passwordResetSuccessMsg
      );
    } catch {
      setSuccess(
        passwordResetSuccessMsg
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="forgot-page">
      <div className="forgot-card">
        <h2>Forgot Password</h2>
        <p>Enter your email and we’ll send you a reset link.</p>

        <form onSubmit={onSubmit}>
          <input
            className="forgot-input"
            type="email"
            placeholder="Email address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            disabled={loading}
          />

          <button className="forgot-button" type="submit" disabled={loading}>
            {loading ? "Sending..." : "Send reset link"}
          </button>
        </form>

        {error && <div className="error">{error}</div>}
        {success && <div className="success">{success}</div>}

        <div style={{ marginTop: 16 }}>
          <Link to="/login">Back to login</Link>
        </div>
      </div>
    </div>
  );
}
