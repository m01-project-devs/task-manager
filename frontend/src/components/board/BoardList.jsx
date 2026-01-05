import { Box, Button, TextField } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import BoardItem from "./BoardItem";
import { createBoard } from "../../api/boardAPI";

export default function BoardList({ boards, refreshBoards }) {
  const [newBoard, setNewBoard] = useState("");
  const navigate = useNavigate();

  const handleAdd = async () => {
    if (!newBoard.trim()) return;
    await createBoard(newBoard);
    setNewBoard("");
    refreshBoards();
  };

  return (
    <Box>
      <Box sx={{ display: "flex", mb: 2 }}>
        <TextField
          label="New Board"
          value={newBoard}
          onChange={(e) => setNewBoard(e.target.value)}
          fullWidth
        />
        <Button sx={{ ml: 2 }} variant="contained" onClick={handleAdd}>
          Add
        </Button>
      </Box>

      {boards.map((board) => (
        <BoardItem
          key={board.id}
          board={board}
          onClick={() => navigate(`/boards/${board.id}`)}
        />
      ))}
    </Box>
  );
}
