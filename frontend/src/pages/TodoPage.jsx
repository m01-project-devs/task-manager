import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getTodos, createTodo, updateTodo, deleteTodo } from "../api/todoAPI";
import TodoList from "../components/todo/TodoList";
import { Box, Typography } from "@mui/material";
import { SnackbarProvider, useSnackbar } from "notistack";

function TodoPageContent() {
  const { boardId } = useParams();
  const [todos, setTodos] = useState([]);
  const { enqueueSnackbar } = useSnackbar();

  const loadTodos = async () => {
    try {
      const data = await getTodos(boardId);
      setTodos(data || []);
    } catch (err) {
      console.error(err);
      enqueueSnackbar("Failed to load todos", { variant: "error" });
    }
  };

  useEffect(() => {
    getTodos(boardId)
      .then((data) => setTodos(data || []))
      .catch(() =>
        enqueueSnackbar("Failed to load todos", { variant: "error" }),
      );
  }, [boardId]);

  const handleAdd = async (title, description) => {
    if (!title.trim()) return;
    try {
      await createTodo(boardId, { title, description });
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

    const handleUpdate = async (todoId, title, description) => {
      const todo = todos.find((t) => t.id === todoId);
      if (!todo) return;

      try {
        // Optimistic UI update
        setTodos(prev =>
          prev.map(t =>
            t.id === todoId ? { ...t, title, description } : t
          )
        );

        await updateTodo(boardId, todoId, {
          title,
          description,
          completed: todo.completed,
        });

      } catch (error) {
        console.error(error);
        enqueueSnackbar("Failed to update todo", { variant: "error" });
        loadTodos(); // rollback
      }
    };


  const handleDelete = async (todoId) => {
    const todo = todos.find((t) => t.id === todoId);
    if (!todo) return;

    try {
      await deleteTodo(boardId, todoId, {
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
        onUpdate={handleUpdate}
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
