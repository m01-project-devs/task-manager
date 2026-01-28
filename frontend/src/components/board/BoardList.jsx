import BoardItem from "./BoardItem";
import { Box } from "@mui/material";
import { useState } from "react";

export default function BoardList({ boards, onDelete, onSave }) {
  const [editingBoardId, setEditingBoardId] = useState(null);

  const handleEdit = (board) => setEditingBoardId(board.id);
  const handleCancelEdit = () => setEditingBoardId(null);

  const handleSaveLocal = async (boardId, newName) => {
    await onSave(boardId, newName);
    setEditingBoardId(null);
  };

  const handleDeleteLocal = (board) => {
    onDelete(board); 
  };

  return (
    <Box>
      {boards.map((board) => (
        <BoardItem
          key={board.id}
          board={board}
          isEditing={editingBoardId === board.id}
          onEdit={() => handleEdit(board)}
          onCancelEdit={handleCancelEdit}
          onSave={(newName) => handleSaveLocal(board.id, newName)}
          onDelete={() => handleDeleteLocal(board)}
          onClick={() => (window.location.href = `/boards/${board.id}`)}
        />
      ))}
    </Box>
  );
}
