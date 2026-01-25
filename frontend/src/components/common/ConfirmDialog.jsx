import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography
} from "@mui/material";

export default function ConfirmDialog({
  open,
  title = "Confirm action",
  message = "Are you sure?",
  onConfirm,
  onClose,
}) {
  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle>{title}</DialogTitle>

      <DialogContent>
        <Typography>{message}</Typography>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose} variant="outlined">
          No
        </Button>
        <Button onClick={onConfirm} variant="contained" color="error">
          Yes
        </Button>
      </DialogActions>
    </Dialog>
  );
}
