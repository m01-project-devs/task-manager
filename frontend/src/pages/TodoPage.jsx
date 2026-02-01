import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTodos, createTodo, updateTodo } from "../api/todoAPI";
import TodoList from "../components/todo/TodoList";
import { Box, Typography } from "@mui/material";
import { SnackbarProvider, useSnackbar } from "notistack";

function TodoPageContent() {
  const { boardId } = useParams();
  const [todos, setTodos] = useState([]);
  const [loading, setLoading] = useState(true);
  const { enqueueSnackbar } = useSnackbar();
  const navigate = useNavigate();

  const loadTodos = async () => {
    try {
      setLoading(true);
      const data = await getTodos(boardId);
      setTodos(data || []);
    } catch (err) {
      console.error(err);
      const status = err?.response?.status;
      if (status === 404 || status === 403) {
        enqueueSnackbar("Board not found or access denied", {
          variant: "error",
        });
        navigate("/boards", { replace: true });
        return;
      }
      enqueueSnackbar("Failed to load todos", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTodos();
  }, [boardId]);

  const handleAdd = async (title) => {
    if (!title.trim()) return;
    try {
      await createTodo(boardId, title);
      enqueueSnackbar("Todo added", { variant: "success" });
      loadTodos();
    } catch {
      enqueueSnackbar("Failed to add todo", { variant: "error" });
    }
  };

  const handleToggle = async (todoId) => {
    const todo = todos.find((t) => t.id === todoId);
    if (!todo) return;

    try {
      await updateTodo(boardId, todoId, {
        title: todo.title,
        completed: !todo.completed,
      });

      enqueueSnackbar(
        todo.completed ? "Todo marked incomplete" : "Todo completed",
        { variant: "info" },
      );

      loadTodos();
    } catch (error) {
      console.log(error);
      enqueueSnackbar("Failed to update todo", { variant: "error" });
    }
  };

  const handleDelete = async (todoId) => {
    const todo = todos.find((t) => t.id === todoId);
    if (!todo) return;

    try {
      await updateTodo(boardId, todoId, {
        title: todo.title,
        deleted: true,
      });

      enqueueSnackbar("Todo deleted", { variant: "error" });
      loadTodos();
    } catch (error) {
      console.log(error);
      enqueueSnackbar("Failed to delete todo", { variant: "error" });
    }
  };

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>
        My Todo List
      </Typography>

      <TodoList
        boardId={boardId}
        todos={todos}
        onAdd={handleAdd}
        onToggle={handleToggle}
        onDelete={handleDelete}
        disabled={loading}
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
    >
      <TodoPageContent />
    </SnackbarProvider>
  );
}
