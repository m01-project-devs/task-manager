import BoardItem from "./BoardItem";
import { Box } from "@mui/material";
import { useState } from "react";

export default function BoardList({ boards, onDelete, onSave }) {
  const [editingBoardId, setEditingBoardId] = useState(null);

  return (
    <Box
      sx={{
        display: "grid",
        gap: 2,
        gridTemplateColumns: {
          xs: "1fr",
          sm: "repeat(2, 1fr)",
          md: "repeat(3, 1fr)",
          lg: "repeat(4, 1fr)",
        },
      }}
    >
      {boards.map((board) => (
        <BoardItem
          key={board.id}
          board={board}
          isEditing={editingBoardId === board.id}
          onEdit={() => setEditingBoardId(board.id)}
          onCancelEdit={() => setEditingBoardId(null)}
          onSave={(newName) => {
            onSave(board.id, newName);
            setEditingBoardId(null);
          }}
          onDelete={() => onDelete(board)}
          onClick={() => (window.location.href = `/boards/${board.id}`)}
        />
      ))}
    </Box>
  );
}
