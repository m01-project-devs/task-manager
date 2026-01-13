import {
  Typography,
  Box,
  Button,
  TextField,
  Paper,
  InputAdornment,
  TablePagination,
} from "@mui/material";
import { Add, Folder, Search } from "@mui/icons-material";
import { useEffect, useState, useMemo } from "react";
import BoardList from "../components/board/BoardList";
import {
  getBoards,
  createBoard,
  deleteBoard,
  updateBoard,
} from "../api/boardAPI";
import ConfirmDialog from "../components/common/ConfirmDialog";
import { SnackbarProvider, useSnackbar } from "notistack";

function BoardPageContent() {
  const [boards, setBoards] = useState([]);
  const [newBoard, setNewBoard] = useState("");
  const [search, setSearch] = useState("");

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedBoard, setSelectedBoard] = useState(null);

  const { enqueueSnackbar } = useSnackbar();

  const loadBoards = async () => {
    const data = await getBoards();
    setBoards(data || []);
  };

  useEffect(() => {
    const fetchBoards = async () => {
      await loadBoards();
    };
    fetchBoards();
  }, []);

  const filteredBoards = useMemo(() => {
    return boards.filter((b) =>
      b.name.toLowerCase().includes(search.toLowerCase())
    );
  }, [boards, search]);

  const paginatedBoards = useMemo(() => {
    const start = page * rowsPerPage;
    const end = start + rowsPerPage;
    return filteredBoards.slice(start, end);
  }, [filteredBoards, page, rowsPerPage]);

  const handleAdd = async () => {
    if (!newBoard.trim()) return;
    try {
      await createBoard(newBoard.trim());
      enqueueSnackbar("Board added", { variant: "success" });
      setNewBoard("");
      loadBoards();
    } catch {
      enqueueSnackbar("Failed to add board", { variant: "error" });
    }
  };

  const askDelete = (board) => {
    setSelectedBoard(board);
    setConfirmOpen(true);
  };

  const confirmDelete = async () => {
    try {
      await deleteBoard(selectedBoard.id);
      enqueueSnackbar("Board deleted", { variant: "error" });
      setConfirmOpen(false);
      setSelectedBoard(null);
      loadBoards();
    } catch {
      enqueueSnackbar("Failed to delete board", { variant: "error" });
    }
  };

  const handleSave = async (boardId, newName) => {
    if (!newName.trim()) return;
    try {
      await updateBoard(boardId, newName);
      enqueueSnackbar("Board updated", { variant: "info" });
      loadBoards();
    } catch {
      enqueueSnackbar("Failed to update board", { variant: "error" });
    }
  };

  return (
    <Box>
      {/* Header + Search */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 2,
        }}
      >
        <Typography variant="h4">My Boards</Typography>
        <Paper sx={{ display: "flex", p: 1, minWidth: 400 }}>
          <TextField
            sx={{ minWidth: 400 }}
            placeholder="Search boards…"
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);
              setPage(0); // reset page on search
            }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Search />
                </InputAdornment>
              ),
            }}
          />
        </Paper>
      </Box>

      {/* Add new board */}
      <Paper sx={{ display: "flex", gap: 1, mb: 2, p: 2 }}>
        <TextField
          label="New Board"
          value={newBoard}
          onChange={(e) => setNewBoard(e.target.value)}
          fullWidth
        />
        <Button variant="contained" onClick={handleAdd} startIcon={<Add />}>
          Add
        </Button>
      </Paper>

      {/* Boards list */}
      {paginatedBoards.length === 0 ? (
        <Box sx={{ textAlign: "center", py: 10 }}>
          <Folder sx={{ fontSize: 80, color: "text.secondary", mb: 1 }} />
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No boards yet
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Create your first board to start organizing tasks
          </Typography>
        </Box>
      ) : (
        <BoardList
          boards={paginatedBoards}
          onDelete={askDelete}
          onSave={handleSave}
        />
      )}

      {/* Pagination */}
      {filteredBoards.length > rowsPerPage && (
        <TablePagination
          component="div"
          count={filteredBoards.length}
          page={page}
          onPageChange={(_, newPage) => setPage(newPage)}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={(e) => {
            setRowsPerPage(parseInt(e.target.value, 10));
            setPage(0);
          }}
          rowsPerPageOptions={[5, 10, 25]}
        />
      )}

      <ConfirmDialog
        open={confirmOpen}
        title="Delete Board"
        message={`Are you sure you want to delete "${selectedBoard?.name}"?`}
        onConfirm={confirmDelete}
        onClose={() => setConfirmOpen(false)}
      />
    </Box>
  );
}

export default function BoardPage() {
  return (
    <SnackbarProvider
      maxSnack={3}
      anchorOrigin={{ vertical: "top", horizontal: "right" }}
      autoHideDuration={3000}
    >
      <BoardPageContent />
    </SnackbarProvider>
  );
}
