import axios from "./axios";

const API_BASE = import.meta.env.VITE_API_URL;

export async function resetPassword(token, newPassword) {
  const res = await fetch(`${API_BASE}/api/auth/reset-password`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ token, newPassword }),
  });

  if (!res.ok) {
    let msg = "Reset link is invalid, expired, or already used.";
    try {
      const data = await res.json();
      msg = data.message || data.error || msg;
    } catch (_) {}
    throw new Error(msg);
  }
}

export function forgotPassword(email) {
  return axios.post("/auth/forgot-password", { email });
}
