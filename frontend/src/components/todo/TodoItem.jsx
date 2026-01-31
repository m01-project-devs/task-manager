import { Box, Typography, Checkbox, IconButton, TextField } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import SaveIcon from "@mui/icons-material/Save";
import CloseIcon from "@mui/icons-material/Close";
import { useState } from "react";

export default function TodoItem({ todo, onToggle, onDelete, onUpdate }) {
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState(todo.title);
  const [description, setDescription] = useState(todo.description || "");

  if (todo.deleted) return null;

  const handleSave = () => {
    if (!title.trim()) return;
    onUpdate(todo.id, title, description);
    setIsEditing(false);
  };

  return (
    <Box sx={{ display: "flex", flexDirection: "column", mb: 1, gap: 1 }}>
      <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
        <Checkbox checked={todo.completed} onChange={() => onToggle(todo.id)} />

        {isEditing ? (
          <TextField
            size="small"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            fullWidth
            autoFocus
          />
        ) : (
          <Typography
            sx={{ flex: 1, textDecoration: todo.completed ? "line-through" : "none" }}
          >
            {title}
          </Typography>
        )}

        <IconButton size="small" color="error" onClick={() => onDelete(todo.id)}>
          <DeleteIcon />
        </IconButton>

        {isEditing ? (
          <>
            <IconButton size="small" onClick={handleSave}>
              <SaveIcon />
            </IconButton>
            <IconButton size="small" onClick={() => setIsEditing(false)}>
              <CloseIcon />
            </IconButton>
          </>
        ) : (
          <IconButton size="small" onClick={() => setIsEditing(true)}>
            <EditIcon />
          </IconButton>
        )}
      </Box>

      {isEditing && (
        <TextField
          size="small"
          placeholder="Description (optional)"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          fullWidth
          multiline
        />
      )}

      {!isEditing && description && (
        <Typography sx={{ ml: 4, color: "text.secondary" }}>
          {description}
        </Typography>
      )}
    </Box>
  );
}