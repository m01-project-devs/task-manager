import { useEffect, useState } from "react";
import { getTodos } from "../api/boardAPI";
import TodoList from "../components/todo/TodoList";
import { Box, Typography } from "@mui/material";
import { useParams } from "react-router-dom";

export default function TodoPage() {
  const { boardId } = useParams();
  const [todos, setTodos] = useState([]);

  useEffect(() => {
    const fetchTodos = async () => {
      try {
        const data = await getTodos(boardId);
        setTodos(data);
      } catch (err) {
        console.error("Error fetching todos:", err);
      }
    };

    fetchTodos();
  }, [boardId]);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" sx={{ mb: 2 }}>
        Todos
      </Typography>
      <TodoList boardId={boardId} todos={todos} refreshTodos={() => {
        (async () => {
          try {
            const data = await getTodos(boardId);
            setTodos(data);
          } catch (err) {
            console.error(err);
          }
        })();
      }} />
    </Box>
  );
}
