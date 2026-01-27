import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  TextField,
  TablePagination,
  InputAdornment,
} from "@mui/material";
import { useMemo, useState } from "react";
import SearchIcon from "@mui/icons-material/Search";

import UserRow from "./UserRow";
import ConfirmDialog from "../common/ConfirmDialog";
import { updateUser, deleteUser } from "../../api/userAPI";

export default function UserTable({ users, reload, enqueueSnackbar }) {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedEmail, setSelectedEmail] = useState(null);

  /* ---------- FILTER ---------- */
  const filteredUsers = useMemo(() => {
    return users.filter((u) =>
      `${u.email} ${u.firstName} ${u.lastName} ${u.role}`
        .toLowerCase()
        .includes(search.toLowerCase()),
    );
  }, [users, search]);

  /* ---------- PAGINATION ---------- */
  const paginatedUsers = filteredUsers.slice(
    page * rowsPerPage,
    page * rowsPerPage + rowsPerPage,
  );

  /* ---------- ACTIONS ---------- */
  const handleUpdate = async (email, payload) => {
    await updateUser(email, payload);
    enqueueSnackbar("User updated", { variant: "info" });
    reload();
  };

  const askDelete = (email) => {
    setSelectedEmail(email);
    setConfirmOpen(true);
  };

  const confirmDelete = async () => {
    await deleteUser(selectedEmail);
    enqueueSnackbar("User deleted", { variant: "error" });
    setConfirmOpen(false);
    setSelectedEmail(null);
    reload();
  };

  return (
    <>
      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              {/* SEARCH ROW */}
              <TableRow>
                <TableCell colSpan={6}>
                  <TextField
                    fullWidth
                    size="small"
                    placeholder="Search by email, name or role…"
                    value={search}
                    onChange={(e) => {
                      setSearch(e.target.value);
                      setPage(0);
                    }}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <SearchIcon fontSize="small" />
                        </InputAdornment>
                      ),
                    }}
                  />
                </TableCell>
              </TableRow>

              {/* HEADER */}
              <TableRow>
                <TableCell>№</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>First name</TableCell>
                <TableCell>Last name</TableCell>
                <TableCell>Role</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {paginatedUsers.map((u, index) => (
                <UserRow
                  key={u.email}
                  index={page * rowsPerPage + index}
                  user={u}
                  onUpdate={handleUpdate}
                  onAskDelete={askDelete}
                />
              ))}
            </TableBody>
          </Table>
        </TableContainer>

        {/* PAGINATION */}
        <TablePagination
          component="div"
          count={filteredUsers.length}
          page={page}
          onPageChange={(_, newPage) => setPage(newPage)}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={(e) => {
            setRowsPerPage(parseInt(e.target.value, 10));
            setPage(0);
          }}
          rowsPerPageOptions={[5, 10, 25]}
        />
      </Paper>

      {/* CONFIRM DELETE */}
      <ConfirmDialog
        open={confirmOpen}
        title="Delete user"
        message="Are you sure you want to delete this user?"
        onConfirm={confirmDelete}
        onClose={() => setConfirmOpen(false)}
      />
    </>
  );
}
