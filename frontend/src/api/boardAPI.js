import api from "./axios";

// --------- Board API ---------

export async function getBoards() {
  const res = await api.get("/boards");
  return res.data.content;
}

export async function createBoard(title) {
  const res = await api.post("/boards", { title });
  return res.data;
}

export async function deleteBoard(boardId) {
  await api.delete(`/boards/${boardId}`);
}

export async function updateBoard(boardId, name) {
  const res = await api.put(`/boards/${boardId}`, { name });
  return res.data;
}
