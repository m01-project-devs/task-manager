import { Typography, Box, Button } from "@mui/material";
import { Add, Folder } from "@mui/icons-material";
import { useEffect, useState } from "react";
import BoardList from "../components/board/BoardList";
import { getBoards } from "../api/boardAPI";

export default function BoardPage() {
  const [boards, setBoards] = useState([]);

  useEffect(() => {
    (async () => {
      const data = await getBoards();
      setBoards(data || []);
    })();
  }, []);

  const refreshBoards = async () => {
    const data = await getBoards();
    setBoards(data || []);
  };

  return (
    <Box>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 4,
        }}
      >
        <Typography variant="h4" sx={{ mb: 2 }}>
          My Boards
        </Typography>
        <Button
          variant="contained"
          startIcon={<Add sx={{ mb: 0.3 }} />}
          size="large"
        >
          Create Board
        </Button>
      </Box>

      {boards.length === 0 ? (
        <Box sx={{ textAlign: "center", py: 8, px: 2 }}>
          <Folder sx={{ fontSize: 80, color: "text.secondary", mb: 2 }} />
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No boards yet
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Create your first board to start organizing tasks
          </Typography>
          <Button variant="contained" startIcon={<Add />}>
            Create Board
          </Button>
        </Box>
      ) : (
        <BoardList boards={boards} refreshBoards={refreshBoards} />
      )}
    </Box>
  );
}
