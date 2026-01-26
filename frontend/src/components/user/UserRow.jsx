import {
  TableRow,
  TableCell,
  IconButton,
  TextField,
  Select,
  MenuItem,
} from "@mui/material";
import { Edit, Delete, Save, Close } from "@mui/icons-material";
import { useState } from "react";

export default function UserRow({ user, index, onUpdate, onAskDelete }) {
  const currentEmail = localStorage.getItem("email");
  const [editing, setEditing] = useState(false);
  const [data, setData] = useState({
    email: user.email,
    firstName: user.firstName,
    lastName: user.lastName,
    role: user.role,
  });

  const save = () => {
    const payload = {
      firstName: data.firstName,
      lastName: data.lastName,
      role: data.role,
    };
    onUpdate(user.email, payload);
    setEditing(false);
  };

  return (
    <TableRow>
      <TableCell>{index + 1}</TableCell>

      <TableCell>
        {editing ? (
          <TextField
            size="small"
            value={data.email}
            onChange={(e) => setData({ ...data, email: e.target.value })}
          />
        ) : (
          user.email
        )}
      </TableCell>
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

      <TableCell>
        {editing ? (
          <Select
            size="small"
            value={data.role}
            onChange={(e) => setData({ ...data, role: e.target.value })}
          >
            <MenuItem value="USER">USER</MenuItem>
            <MenuItem value="ADMIN">ADMIN</MenuItem>
          </Select>
        ) : (
          user.role
        )}
      </TableCell>

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
