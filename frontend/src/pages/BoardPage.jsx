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
  const [totalElements, setTotalElements] = useState(0);
  const [newBoard, setNewBoard] = useState("");
  const [search, setSearch] = useState("");

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(20);

  const [selectedBoard, setSelectedBoard] = useState(null);
  const [confirmOpen, setConfirmOpen] = useState(false);

  const { enqueueSnackbar } = useSnackbar();

  const loadBoards = async (pageNumber, pageSize) => {
    try {
      const data = await getBoards(pageNumber, pageSize);
      setBoards(data.content || []);
      setTotalElements(data.totalElements || 0);
    } catch {
      enqueueSnackbar("Failed to load boards", { variant: "error" });
    }
  };

  useEffect(() => {
    const fetchBoards = async () => {
      await loadBoards(page, rowsPerPage);
    };
    fetchBoards();
  }, [page, rowsPerPage]);

  const filteredBoards = useMemo(
    () =>
      boards.filter((b) => b.name.toLowerCase().includes(search.toLowerCase())),
    [boards, search],
  );

  const handleAdd = async () => {
    if (!newBoard.trim()) return;
    try {
      await createBoard(newBoard.trim());
      enqueueSnackbar("Board added", { variant: "success" });
      setNewBoard("");
      setPage(0);
      loadBoards(0, rowsPerPage);
    } catch {
      enqueueSnackbar("Failed to add board", { variant: "error" });
    }
  };

  const askDelete = (board) => {
    setSelectedBoard(board);
    setConfirmOpen(true);
  };

  const confirmDelete = async () => {
    if (!selectedBoard) return;
    try {
      await deleteBoard(selectedBoard.id);
      enqueueSnackbar("Board deleted", { variant: "error" });
      setConfirmOpen(false);
      setSelectedBoard(null);
      loadBoards(page, rowsPerPage);
    } catch {
      enqueueSnackbar("Failed to delete board", { variant: "error" });
    }
  };

  const handleSave = async (boardId, newTitle) => {
    if (!newTitle.trim()) return;
    try {
      await updateBoard(boardId, newTitle);
      enqueueSnackbar("Board updated", { variant: "info" });
      loadBoards(page, rowsPerPage);
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
          flexDirection: { xs: "column", sm: "row" },
          justifyContent: "space-between",
          alignItems: { xs: "stretch", sm: "center" },
          mb: 2,
          gap: 1,
        }}
      >
        <Typography variant="h4">My Boards</Typography>

        <Paper
          sx={{ display: "flex", p: 1, width: { xs: "100%", sm: "auto" } }}
        >
          <TextField
            sx={{ width: { xs: "100%", sm: 400 } }}
            placeholder="Search boards…"
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);
              setPage(0);
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
      {filteredBoards.length === 0 ? (
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
          boards={filteredBoards}
          onDelete={askDelete}
          onSave={handleSave}
        />
      )}

      {/* Pagination */}
      {totalElements > rowsPerPage && (
        <TablePagination
          component="div"
          count={totalElements}
          page={page}
          onPageChange={(_, newPage) => setPage(newPage)}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={(e) => {
            setRowsPerPage(parseInt(e.target.value, 10));
            setPage(0);
          }}
          rowsPerPageOptions={[20, 30, 40]}
        />
      )}

      {/* Confirm Delete Dialog */}
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
