import api from "./axios";

// --------- Todo API ---------

export async function getTodos(boardId) {
  const res = await api.get(`/boards/${boardId}/todos`);
  return res.data;
}

export async function createTodo(boardId, title) {
  const res = await api.post(`/boards/${boardId}/todos`, { title });
  return res.data;
}

export async function updateTodo(boardId, todoId, data) {
  const res = await api.put(`/boards/${boardId}/todos/${todoId}`, data);
  return res.data;
}

export async function deleteTodo(boardId, todoId) {
  await api.delete(`/boards/${boardId}/todos/${todoId}`);
  return true;
}
