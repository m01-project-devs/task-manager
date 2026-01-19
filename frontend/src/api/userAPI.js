import { API_BASE_URL } from "../config/apiConfig";

const authHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${localStorage.getItem("token")}`,
});

export async function getUsers() {
  const res = await fetch(`${API_BASE_URL}/api/users`, {
    headers: authHeaders(),
  });
  console.log(res)
  if (!res.ok) throw new Error("Failed to fetch users");
  return res.json();
}

export async function createUser(payload) {
  const res = await fetch(`${API_BASE_URL}/api/users`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Failed to create user");
}

export async function updateUser(email, payload) {
  const res = await fetch(`${API_BASE_URL}/api/users/${email}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Failed to update user");
}

export async function deleteUser(email) {
  const res = await fetch(`${API_BASE_URL}/api/users/${email}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  if (!res.ok) throw new Error("Failed to delete user");
}
