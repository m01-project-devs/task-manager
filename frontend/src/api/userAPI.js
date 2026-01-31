import api from "./axios";

// --------- User API ---------

export async function searchUsers({ query, page, size, sort } = {}) {
  try {
    const res = await api.get("/users/search", {
      params: { query, page, size, sort },
    });
    return res.data;
  } catch (error) {
    console.error("SEARCH USERS ERROR:", error.response?.data);
    throw error;
  }
}

export async function getUsersOnly({ page, size, sort } = {}) {
  try {
    const res = await api.get("/users/users-only", {
      params: { page, size, sort },
    });
    return res.data;
  } catch (error) {
    console.error("GET USERS ONLY ERROR:", error.response?.data);
    throw error;
  }
}

export async function getAdminsOnly({ page, size, sort } = {}) {
  try {
    const res = await api.get("/users/admins-only", {
      params: { page, size, sort },
    });
    return res.data;
  } catch (error) {
    console.error("GET ADMINS ONLY ERROR:", error.response?.data);
    throw error;
  }
}

export async function createUser(payload) {
  try {
    const res = await api.post("/users", payload);
    return res.data;
  } catch (error) {
    console.error("CREATE USER ERROR:", error.response?.data);
    throw error;
  }
}

export async function updateUser(email, payload) {
  try {
    const res = await api.put(`/users/${encodeURIComponent(email)}`, payload);
    return res.data;
  } catch (error) {
    console.error("UPDATE USER ERROR:", error.response?.data);
    throw error;
  }
}

export async function deleteUser(email) {
  try {
    const res = await api.delete(`/users/${encodeURIComponent(email)}`);
    return res.data;
  } catch (error) {
    console.error("DELETE USER ERROR:", error.response?.data);
    throw error;
  }
}
