import api from "./axios";

// --------- Todo API ---------

export async function getTodos(boardId) {
  const res = await api.get(`/todo/${boardId}`);
  return res.data.content;
}

export async function createTodo(boardId, data) {
  const res = await api.post(`/todo/${boardId}`, data);
  return res.data;
}

export async function updateTodo(boardId, todoId, data) {
  const res = await api.patch(`/todo/${boardId}/${todoId}`, data);
  return res.data;
}

export async function deleteTodo(boardId, todoId) {
  await api.delete(`/todo/${boardId}/${todoId}`);
  return true;
}
