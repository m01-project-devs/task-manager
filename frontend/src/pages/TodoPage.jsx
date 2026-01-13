import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getTodos } from "../api/todoAPI";
import TodoList from "../components/todo/TodoList";
import { Box, Typography } from "@mui/material";

export default function TodoPage() {
  const { boardId } = useParams();
  const [todos, setTodos] = useState([]);

  const loadTodos = async () => {
    const data = await getTodos(boardId);
    setTodos(data || []);
  };

  useEffect(() => {
    const fetchTodos = async () => {
      await loadTodos();
    };
    fetchTodos();
  }, [boardId]);

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>
        My Todo List
      </Typography>

      <TodoList
        boardId={boardId}
        todos={todos}
        refreshTodos={loadTodos}
      />
    </Box>
  );
}
