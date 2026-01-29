import {
  Card,
  CardContent,
  Typography,
  IconButton,
  Box,
  TextField,
} from "@mui/material";
import { Edit, Delete, Save, Close, CalendarMonth } from "@mui/icons-material";
import { useState } from "react";

const formatDate = (date) =>
  new Date(date).toLocaleDateString("de-DE");

export default function BoardItem({
  board,
  onClick,
  onEdit,
  onDelete,
  isEditing,
  onSave,
  onCancelEdit,
}) {
  const [name, setName] = useState(board.name);

  const handleSave = () => {
    const trimmed = name.trim();
    if (!trimmed || trimmed === board.name) return onCancelEdit();
    onSave(trimmed);
  };

  return (
    <Card
      sx={{
        height: "100%",
        display: "flex",
        flexDirection: "column",
        transition: "0.2s",
        "&:hover": { transform: "scale(1.02)", boxShadow: 6 },
        borderLeft: "6px solid #2196f3",
      }}
    >
      <CardContent
        sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}
      >
        {isEditing ? (
          <Box sx={{ display: "flex", gap: 1, flexGrow: 1 }}>
            <TextField
              autoFocus
              size="small"
              value={name}
              onChange={(e) => setName(e.target.value)}
              onBlur={handleSave}
              fullWidth
            />
            <IconButton size="small" color="primary" onMouseDown={(e) => e.preventDefault()} onClick={handleSave}>
              <Save fontSize="small" />
            </IconButton>
            <IconButton size="small" onMouseDown={(e) => e.preventDefault()} onClick={onCancelEdit}>
              <Close fontSize="small" />
            </IconButton>
          </Box>
        ) : (
          <>
            <Box sx={{ flexGrow: 1, cursor: "pointer" }} onClick={onClick}>
              <Typography variant="subtitle1" fontWeight="bold" sx={{ wordBreak: "break-word" }}>
                {board.name}
              </Typography>

              <Box sx={{ display: "flex", alignItems: "center", gap: 0.5, mt: 0.5 }}>
                <CalendarMonth sx={{ fontSize: 14, color: "text.secondary" }} />
                <Typography variant="caption" color="text.secondary">
                  {formatDate(board.createdAt)}
                </Typography>
              </Box>
            </Box>

            <Box sx={{ display: "flex", gap: 0.5 }}>
              <IconButton size="small" onClick={(e) => { e.stopPropagation(); onEdit(board); }}>
                <Edit fontSize="small" />
              </IconButton>
              <IconButton size="small" color="error" onClick={(e) => { e.stopPropagation(); onDelete(); }}>
                <Delete fontSize="small" />
              </IconButton>
            </Box>
          </>
        )}
      </CardContent>
    </Card>
  );
}
