import { Box, Typography, Checkbox, IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";

export default function TodoItem({ todo, onToggle, onDelete }) {
  if (todo.deleted) return null;

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        mb: 1,
        gap: 1,
      }}
    >
      <Checkbox checked={todo.completed} onChange={() => onToggle(todo.id)} />

      <Typography
        sx={{
          flex: 1,
          textDecoration: todo.completed ? "line-through" : "none",
        }}
      >
        {todo.title}
      </Typography>

      <IconButton color="error" onClick={() => onDelete(todo.id)}>
        <DeleteIcon />
      </IconButton>
    </Box>
  );
}
