import { Card, CardContent, Typography } from "@mui/material";

export default function BoardItem({ board, onClick }) {
  return (
    <Card
      sx={{ mb: 2, cursor: "pointer" }}
      onClick={onClick}
    >
      <CardContent>
        <Typography variant="h6">
          {board.name}
        </Typography>
      </CardContent>
    </Card>
  );
}
