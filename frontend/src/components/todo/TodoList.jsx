import { Box, TextField, Button } from "@mui/material";
import TodoItem from "./TodoItem";
import { Add } from "@mui/icons-material";
import { useState } from "react";

export default function TodoList({ todos, onAdd, onToggle, onDelete, disabled }) {
  const [newTodo, setNewTodo] = useState("");

  const handleAddClick = () => {
    if (!newTodo.trim()) return;
    onAdd(newTodo.trim());
    setNewTodo("");
  };

  return (
    <Box>
      <Box sx={{ display: "flex", mb: 2 }}>
        <TextField
          label="New Todo"
          value={newTodo}
          onChange={(e) => setNewTodo(e.target.value)}
          disabled={disabled}
          fullWidth
        />
        <Button
          sx={{ ml: 2 }}
          variant="contained"
          onClick={handleAddClick}
          startIcon={<Add />}
          disabled={disabled}
        >
          Add
        </Button>
      </Box>

      {todos.map((todo) => (
        <TodoItem
          key={todo.id}
          todo={todo}
          onToggle={onToggle}
          onDelete={onDelete}
          disabled={disabled}
        />
      ))}
    </Box>
  );
}
