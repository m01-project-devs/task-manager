import { API_BASE_URL } from "../config/apiConfig";

const authHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${localStorage.getItem("token")}`,
});

// --------- Board API ---------
export async function getBoards() {
  const res = await fetch(`${API_BASE_URL}/api/boards`, {
    headers: authHeaders(),
  });
  const data = await res.json();
  return data.content; 
}

export async function createBoard(name) {
  const res = await fetch(`${API_BASE_URL}/api/boards`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ name }),
  });
  return res.json();
}

export async function deleteBoard(boardId) {
  await fetch(`${API_BASE_URL}/api/boards/${boardId}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
}

export async function updateBoard(boardId, name) {
  const res = await fetch(`${API_BASE_URL}/api/boards/${boardId}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify({ name }),
  });
  return res.json();
}

