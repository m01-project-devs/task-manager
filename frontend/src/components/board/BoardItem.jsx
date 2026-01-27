import {
  Card,
  CardContent,
  Typography,
  IconButton,
  Box,
  TextField,
} from "@mui/material";
import { Edit, Delete, Save, Close } from "@mui/icons-material";
import { useState } from "react";

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
    if (!name.trim() || name === board.name) {
      onCancelEdit();
      return;
    }
    onSave(name.trim());
  };

  return (
    <Card sx={{ mb: 2 }}>
      <CardContent
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
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
            <IconButton
              color="primary"
              onMouseDown={(e) => e.preventDefault()}
              onClick={handleSave}
            >
              <Save />
            </IconButton>
            <IconButton
              onMouseDown={(e) => e.preventDefault()}
              onClick={onCancelEdit}
            >
              <Close />
            </IconButton>
          </Box>
        ) : (
          <>
            <Typography
              variant="h6"
              sx={{ cursor: "pointer" }}
              onClick={onClick}
            >
              {board.name}
            </Typography>
            <Box>
              <IconButton
                size="small"
                onClick={(e) => {
                  e.stopPropagation();
                  onEdit(board);
                }}
              >
                <Edit />
              </IconButton>
              <IconButton
                size="small"
                color="error"
                onClick={(e) => {
                  e.stopPropagation();
                  onDelete();
                }}
              >
                <Delete />
              </IconButton>
            </Box>
          </>
        )}
      </CardContent>
    </Card>
  );
}
