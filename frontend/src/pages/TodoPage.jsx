import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getBoards } from "../api/boardAPI";
import { getTodos, createTodo, updateTodo, deleteTodo } from "../api/todoAPI";
import TodoList from "../components/todo/TodoList";
import { Box, Typography, Button } from "@mui/material";
import { SnackbarProvider, useSnackbar, closeSnackbar } from "notistack";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import ConfirmDialog from "../components/common/ConfirmDialog";

function TodoPageContent() {
  const { boardTitle } = useParams();
  const decodedTitle = decodeURIComponent(boardTitle);
  const navigate = useNavigate();

  const [todos, setTodos] = useState([]);
  const [currentBoardId, setCurrentBoardId] = useState(null);

  const [todoToDelete, setTodoToDelete] = useState(null);
  const [confirmOpen, setConfirmOpen] = useState(false);

  const { enqueueSnackbar } = useSnackbar();

  const loadTodos = async () => {
    try {
      const boardsData = await getBoards(0, 1000);
      const board = boardsData.content.find((b) => b.name === decodedTitle);
      if (!board) throw new Error("Board not found");

      setCurrentBoardId(board.id);

      const todosData = await getTodos(board.id);
      setTodos(todosData || []);
    } catch (err) {
      console.error(err);
      enqueueSnackbar("Failed to load todos", { variant: "error" });
    }
  };

  useEffect(() => {
    loadTodos();
  }, [decodedTitle]);

  const handleAdd = async (title, description) => {
    if (!title || !title.trim()) {
      enqueueSnackbar("Title is required", { variant: "warning" });
      return;
    }

    if (!currentBoardId) {
      enqueueSnackbar("Board not loaded yet", { variant: "error" });
      return;
    }

    try {
      await createTodo(currentBoardId, { title: title.trim(), description });
      enqueueSnackbar("Todo added", { variant: "success" });
      loadTodos();
    } catch (error) {
      console.error(error);
      enqueueSnackbar("Failed to add todo", { variant: "error" });
    }
  };

  const handleToggle = async (todoId) => {
    if (!currentBoardId) return;
    const todo = todos.find((t) => t.id === todoId);
    if (!todo) return;

    try {
      await updateTodo(currentBoardId, todoId, {
        title: todo.title,
        completed: !todo.completed,
      });

      enqueueSnackbar(
        todo.completed ? "Todo marked incomplete" : "Todo completed",
        { variant: "info" }
      );

      loadTodos();
    } catch (error) {
      console.error(error);
      enqueueSnackbar("Failed to update todo", { variant: "error" });
    }
  };

  const handleUpdate = async (todoId, title, description) => {
    if (!currentBoardId) {
      enqueueSnackbar("Board not loaded yet", { variant: "error" });
      return;
    }

    if (!title || !title.trim()) {
      enqueueSnackbar("Title is required", { variant: "warning" });
      return;
    }

    const todo = todos.find((t) => t.id === todoId);
    if (!todo) return;

    try {
      setTodos((prev) =>
        prev.map((t) =>
          t.id === todoId ? { ...t, title: title.trim(), description } : t
        )
      );

      await updateTodo(currentBoardId, todoId, {
        title: title.trim(),
        description,
        completed: todo.completed,
      });

      enqueueSnackbar("Todo updated", { variant: "success" });
    } catch (error) {
      console.error(error);
      enqueueSnackbar("Failed to update todo", { variant: "error" });
      loadTodos();
    }
  };

  const handleRequestDelete = (todoId) => {
    const todo = todos.find((t) => t.id === todoId);
    if (!todo) return;
    setTodoToDelete(todo);
    setConfirmOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!currentBoardId || !todoToDelete) return;

    try {
      await deleteTodo(currentBoardId, todoToDelete.id, {
        title: todoToDelete.title,
        deleted: true,
      });

      enqueueSnackbar("Todo deleted", { variant: "warning" });
      loadTodos();
    } catch (error) {
      console.error(error);
      enqueueSnackbar("Failed to delete todo", { variant: "error" });
    } finally {
      setConfirmOpen(false);
      setTodoToDelete(null);
    }
  };

  return (
    <Box>
      <Box sx={{ display: "flex", alignItems: "center", justifyContent: "space-between", mb: 2 }}>
        <Typography variant="h4">My Todo List</Typography>
        <Button
          variant="contained"
          onClick={() => navigate("/boards")}
        >
          Back
        </Button>
      </Box>

      <TodoList
        boardId={currentBoardId}
        todos={todos}
        onAdd={handleAdd}
        onToggle={handleToggle}
        onDelete={handleRequestDelete} 
        onUpdate={handleUpdate}
      />

      <ConfirmDialog
        open={confirmOpen}
        title="Delete Todo"
        message={`Are you sure you want to delete "${todoToDelete?.title}"?`}
        onConfirm={handleConfirmDelete}
        onClose={() => {
          setConfirmOpen(false);
          setTodoToDelete(null);
        }}
      />
    </Box>
  );
}

export default function TodoPage() {
  return (
    <SnackbarProvider
      maxSnack={3}
      anchorOrigin={{ vertical: "top", horizontal: "right" }}
      autoHideDuration={3000}
      action={(snackbarId) => (
        <IconButton
          size="small"
          aria-label="close"
          color="inherit"
          onClick={(event) => {
            event.stopPropagation();
            closeSnackbar(snackbarId);
          }}
        >
          <CloseIcon fontSize="small" />
        </IconButton>
      )}
    >
      <TodoPageContent />
    </SnackbarProvider>
  );
}
