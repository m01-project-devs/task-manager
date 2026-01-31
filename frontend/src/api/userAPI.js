import api from "./axios";

// --------- User API ---------

export async function getUsers() {
  try {
    const res = await api.get("/users");
    console.log(res);
    return res.data;
  } catch {
    throw new Error("Failed to fetch users");
  }
}

export async function getUsersOnly({ page, size, sort } = {}) {
  try {
    const res = await api.get("/users/users-only", {
      params: {
        page,
        size,
        sort,
      },
    });
    console.log(res);
    return res.data;
  } catch {
    throw new Error("Failed to fetch users");
  }
}

export async function getAdminsOnly({ page, size, sort } = {}) {
  try {
    const res = await api.get("/users/admins-only", {
      params: {
        page,
        size,
        sort,
      },
    });
    console.log(res);
    return res.data;
  } catch {
    throw new Error("Failed to fetch admins");
  }
}

export async function createUser(payload) {
  try {
    await api.post("/users", payload);
  } catch {
    throw new Error("Failed to create user");
  }
}

export async function updateUser(email, payload) {
  try {
      await api.put(`/users/${encodeURIComponent(email)}`, payload);
  } catch {
      throw new Error("Failed to update user");
  }
}

export async function deleteUser(email) {
  try {
      await api.delete(`/users/${encodeURIComponent(email)}`);
  } catch {
      throw new Error("Failed to delete user");
  }
}
