import { Box, TextField, Button } from "@mui/material";
import TodoItem from "./TodoItem";
import { Add } from "@mui/icons-material";
import { useState } from "react";

export default function TodoList({
  todos,
  onAdd,
  onToggle,
  onDelete,
  onUpdate,
  disabled
}) {
  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");

  const handleAddClick = () => {
    onAdd(newTitle, newDescription);

    setNewTitle("");
    setNewDescription("");
  };

  return (
    <Box>
      {/* NEW TODO FORM */}
      <Box sx={{ display: "flex", flexDirection: "column", gap: 1, mb: 2 }}>
        <TextField
          label="New Todo Title"
          value={newTitle}
          onChange={(e) => setNewTitle(e.target.value)}
          disabled={disabled}
          fullWidth
        />

        <TextField
          label="Description (optional)"
          value={newDescription}
          onChange={(e) => setNewDescription(e.target.value)}
          fullWidth
          multiline
          minRows={2}
        />

        <Button
          variant="contained"
          onClick={handleAddClick}
          startIcon={<Add />}
          sx={{ alignSelf: "flex-end" }}
          disabled={disabled}
        >
          Add
        </Button>
      </Box>

      {/* TODO ITEMS */}
      {todos.map((todo) => (
        <TodoItem
          key={todo.id}
          todo={todo}
          onToggle={onToggle}
          onDelete={onDelete}
          onUpdate={onUpdate}
          disabled={disabled}
        />
      ))}
    </Box>
  );
}
