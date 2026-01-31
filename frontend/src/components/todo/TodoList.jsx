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
}) {
  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");

  const handleAddClick = () => {
    if (!newTitle.trim()) return;

    onAdd(newTitle.trim(), newDescription.trim());
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
        />
      ))}
    </Box>
  );
}
