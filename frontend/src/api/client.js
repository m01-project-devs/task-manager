import api from "./axios";

export async function pingBackend() {
  try {
    const res = await api.get("/users");
    console.log("Backend ping status:", res.status);
    return res.status >= 200 && res.status < 300;
  } catch (err) {
    if (err.response) {
      console.error("Backend ping failed:", err.response.status);
    } else {
      console.error("Backend ping failed:", err.message);
    }
    return false;
  }
}
