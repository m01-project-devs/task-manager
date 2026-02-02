import { useMemo, useState } from "react";
import { useLocation, useNavigate, Link } from "react-router-dom";
import { resetPassword } from "../api/auth";
import {
  MIN_LENGTH,
  MAX_LENGTH,
  SPECIAL_CHARS,
} from "../components/constants/passwordConstraints.jsx";
import "./ResetPassword.css";

export default function ResetPassword() {
  const location = useLocation();
  const navigate = useNavigate();

  const token = useMemo(
    () => new URLSearchParams(location.search).get("token"),
    [location.search]
  );

  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const passwordChecks = useMemo(() => {
    const pwd = password;
    return {
      length: pwd.length >= MIN_LENGTH,
      uppercase: /[A-Z]/.test(pwd),
      lowercase: /[a-z]/.test(pwd),
      number: /\d/.test(pwd),
      special: new RegExp(`[${SPECIAL_CHARS}]`).test(pwd),
      maxLength: pwd.length <= MAX_LENGTH,
    };
  }, [password]);

  const isPasswordValid = useMemo(
    () => Object.values(passwordChecks).every((c) => c),
    [passwordChecks],
  );

  const passwordsMatch = useMemo(
    () => password === confirm || confirm === "",
    [password, confirm],
  );

  async function onSubmit(e) {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!token) {
      setError("Invalid reset link.");
      return;
    }
    if (!isPasswordValid) {
      setError("Password does not meet all requirements.");
      return;
    }
    if (password !== confirm) {
      setError("Passwords do not match.");
      return;
    }

    try {
      setLoading(true);
      await resetPassword(token, password);
      setSuccess("Password changed successfully. Redirecting to login...");
      setTimeout(() => navigate("/login"), 1200);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="reset-page">
      <div className="reset-card">
        <h2>Reset Password</h2>
        <p>This link expires after 15 minutes.</p>

        <form onSubmit={onSubmit}>
          <input
            className="reset-input"
            type="password"
            placeholder="New password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            disabled={loading}
          />

          <input
            className="reset-input"
            type="password"
            placeholder="Confirm password"
            value={confirm}
            onChange={(e) => setConfirm(e.target.value)}
            disabled={loading}
          />

          {password.length > 0 && (
            <div style={{ marginTop: "12px", marginBottom: "12px", textAlign: "left" }}>
              <div style={{ fontSize: "0.85rem", color: "#666", marginBottom: "6px" }}>
                Password requirements:
              </div>
              <ul style={{ margin: 0, paddingLeft: "20px", fontSize: "0.85rem" }}>
                <li style={{ color: passwordChecks.length ? "#2e7d32" : "#999" }}>
                  {passwordChecks.length ? "✓" : "○"} At least {MIN_LENGTH} characters
                </li>
                <li style={{ color: passwordChecks.uppercase ? "#2e7d32" : "#999" }}>
                  {passwordChecks.uppercase ? "✓" : "○"} One uppercase letter (A-Z)
                </li>
                <li style={{ color: passwordChecks.lowercase ? "#2e7d32" : "#999" }}>
                  {passwordChecks.lowercase ? "✓" : "○"} One lowercase letter (a-z)
                </li>
                <li style={{ color: passwordChecks.number ? "#2e7d32" : "#999" }}>
                  {passwordChecks.number ? "✓" : "○"} One number (0-9)
                </li>
                <li style={{ color: passwordChecks.special ? "#2e7d32" : "#999" }}>
                  {passwordChecks.special ? "✓" : "○"} One special character ({SPECIAL_CHARS})
                </li>
                <li style={{ color: passwordChecks.maxLength ? "#2e7d32" : "#999" }}>
                  {passwordChecks.maxLength ? "✓" : "○"} Maximum {MAX_LENGTH} characters
                </li>
              </ul>
            </div>
          )}

          {!passwordsMatch && confirm !== "" && (
            <div style={{ color: "#d32f2f", fontSize: "0.875rem", marginBottom: "12px" }}>
              ⚠️ Passwords do not match!
            </div>
          )}

          <button
            className="reset-button"
            type="submit"
            disabled={loading || (password.length > 0 && !isPasswordValid) || !passwordsMatch}
          >
            {loading ? "Resetting..." : "Reset Password"}
          </button>
        </form>

        {error && (
          <div className="error">
            {error}
            <br />
            <Link to="/forgot-password">Request a new link</Link>
          </div>
        )}

        {success && <div className="success">{success}</div>}
      </div>
    </div>
  );
}