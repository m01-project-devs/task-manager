import { API_BASE_URL } from "../config/apiConfig";

const authHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${localStorage.getItem("token")}`,
});

// --------- Todo API ---------
export async function getTodos(boardId) {
  const res = await fetch(`${API_BASE_URL}/api/boards/${boardId}/todos`, {
    headers: authHeaders(),
  });
  return res.json();
}

export async function createTodo(boardId, title) {
  const res = await fetch(`${API_BASE_URL}/api/boards/${boardId}/todos`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ title }),
  });
  return res.json();
}

export async function updateTodo(boardId, todoId, data) {
  const res = await fetch(
    `${API_BASE_URL}/api/boards/${boardId}/todos/${todoId}`,
    {
      method: "PUT",
      headers: authHeaders(),
      body: JSON.stringify(data),
    }
  );
  return res.json();
}

export async function deleteTodo(boardId, todoId) {
  const res = await fetch(
    `${API_BASE_URL}/api/boards/${boardId}/todos/${todoId}`,
    {
      method: "DELETE",
      headers: authHeaders(),
    }
  );
  return res.ok;
}