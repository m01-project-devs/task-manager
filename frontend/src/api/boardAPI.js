import api from "./axios";

// --------- Board API ---------

export async function getBoards(page, size) {
  const res = await api.get("/boards", {
    params: { page, size }
  });
  return res.data;
}

export async function createBoard(title) {
  const res = await api.post("/boards", { title });
  return res.data;
}

export async function deleteBoard(boardId) {
  await api.delete(`/boards/${boardId}`);
}

export async function updateBoard(boardId, title) {
  const res = await api.put(`/boards/${boardId}`, { title });
  return res.data;
}
