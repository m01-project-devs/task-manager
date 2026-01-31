import { TableRow, TableCell, IconButton, TextField } from "@mui/material";
import { Edit, Delete, Save, Close } from "@mui/icons-material";
import { useState } from "react";

export default function UserRow({ user, index, onUpdate, onAskDelete }) {
  const currentEmail = localStorage.getItem("email");
  const [editing, setEditing] = useState(false);
  const [data, setData] = useState({
    firstName: user.firstName || "",
    lastName: user.lastName || "",
  });

  const save = () => {
    onUpdate(user.email, {
      firstName: data.firstName,
      lastName: data.lastName,
    });
    setEditing(false);
  };

  return (
    <TableRow>
      <TableCell>{index + 1}</TableCell>
      <TableCell>{user.email}</TableCell>
      <TableCell>
        {editing ? (
          <TextField
            size="small"
            value={data.firstName}
            onChange={(e) => setData({ ...data, firstName: e.target.value })}
          />
        ) : (
          user.firstName
        )}
      </TableCell>
      <TableCell>
        {editing ? (
          <TextField
            size="small"
            value={data.lastName}
            onChange={(e) => setData({ ...data, lastName: e.target.value })}
          />
        ) : (
          user.lastName
        )}
      </TableCell>
      <TableCell>{user.role}</TableCell>
      <TableCell align="right">
        {editing ? (
          <>
            <IconButton color="primary" onClick={save}>
              <Save />
            </IconButton>
            <IconButton color="error" onClick={() => setEditing(false)}>
              <Close />
            </IconButton>
          </>
        ) : (
          <>
            <IconButton onClick={() => setEditing(true)}>
              <Edit />
            </IconButton>
            <IconButton
              color="error"
              disabled={user.email === currentEmail}
              onClick={() => onAskDelete(user.email)}
            >
              <Delete />
            </IconButton>
          </>
        )}
      </TableCell>
    </TableRow>
  );
}
