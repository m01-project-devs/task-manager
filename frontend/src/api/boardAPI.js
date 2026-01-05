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

// // --------- Todo API ---------
// export async function getTodos(boardId) {
//   const res = await fetch(`${API_BASE_URL}/boards/${boardId}/todos`);
//   return res.json();
// }

// export async function createTodo(boardId, title) {
//   const res = await fetch(`${API_BASE_URL}/boards/${boardId}/todos`, {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify({ title }),
//   });
//   return res.json();
// }

// export async function updateTodo(boardId, todoId, data) {
//   const res = await fetch(`${API_BASE_URL}/boards/${boardId}/todos/${todoId}`, {
//     method: "PUT",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify(data),
//   });
//   return res.json();
// }

// export async function deleteTodo(boardId, todoId) {
//   const res = await fetch(`${API_BASE_URL}/boards/${boardId}/todos/${todoId}`, {
//     method: "DELETE",
//   });
//   return res.ok;
// }
