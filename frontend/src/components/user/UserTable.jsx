import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  TablePagination,
  TextField,
  InputAdornment,
} from "@mui/material";
import { useState, useMemo } from "react";
import SearchIcon from "@mui/icons-material/Search";

import UserRow from "./UserRow";
import ConfirmDialog from "../common/ConfirmDialog";
import { updateUser, deleteUser } from "../../api/userAPI";
import SortableTableHead from "../common/SortableTableHead.jsx";

export default function UserTable({
  users = { content: [], totalElements: 0 },
  enqueueSnackbar,
  page,
  rowsPerPage,
  onPageChange,
  onRowsPerPageChange,
  sortConfig,
  onSort,
  searchValue = "",
  onSearchChange,
  showSearch = true,
}) {
  const [search, setSearch] = useState("");
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedEmail, setSelectedEmail] = useState(null);

  const [localUsers, setLocalUsers] = useState(() => [...users.content]);


    const columns = [
        { field: 'index', label: '№', sortable: false },
        { field: 'email', label: 'Email', sortable: true },
        { field: 'firstName', label: 'First name', sortable: true },
        { field: 'lastName', label: 'Last name', sortable: true },
        { field: 'role', label: 'Role', sortable: true },
        { field: 'actions', label: 'Actions', sortable: false, align: 'right' },
    ];

  const filteredUsers = useMemo(() => {
    if (!showSearch) return localUsers;
    return localUsers.filter((u) =>
      `${u.email} ${u.firstName} ${u.lastName} ${u.role}`
        .toLowerCase()
        .includes(search.toLowerCase())
    );
  }, [localUsers, search, showSearch]);

  const handleUpdate = async (email, payload) => {
    try {
      await updateUser(email, payload);
      enqueueSnackbar("User updated", { variant: "info" });

      setLocalUsers(prev =>
        prev.map(u =>
          u.email === email
            ? { ...u, firstName: payload.firstName, lastName: payload.lastName }
            : u
        )
      );
    } catch {
      enqueueSnackbar("Failed to update user", { variant: "error" });
    }
  };

  const askDelete = (email) => {
    setSelectedEmail(email);
    setConfirmOpen(true);
  };

  const confirmDelete = async () => {
    try {
      await deleteUser(selectedEmail);
      enqueueSnackbar("User deleted", { variant: "success" });

      setLocalUsers(prev => prev.filter(u => u.email !== selectedEmail));
      setConfirmOpen(false);
      setSelectedEmail(null);
    } catch (err) {
        console.error(err);
      enqueueSnackbar("Failed to delete user", { variant: "error" });
    } finally {
        setConfirmOpen(false);
        setSelectedEmail(null);
        reload();
    }
  };

  return (
    <>
      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              {showSearch && (
                <TableRow>
                  <TableCell colSpan={6}>
                    <TextField
                      fullWidth
                      size="small"
                      placeholder="Search by email, name or role…"
                      value={search}
                      onChange={(e) => setSearch(e.target.value)}
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
              )}
                <SortableTableHead
                    columns={columns}
                    sortConfig={sortConfig}
                    onSort={onSort}
                />
            </TableHead>
            <TableBody>
              {filteredUsers.map((u, idx) => (
                <UserRow
                  key={u.email}
                  index={page * rowsPerPage + idx}
                  user={u}
                  onUpdate={handleUpdate}
                  onAskDelete={askDelete}
                />
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          component="div"
          count={users.totalElements || 0}
          page={page}
          onPageChange={(_, newPage) => onPageChange(newPage)}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={(e) => {
            onRowsPerPageChange(parseInt(e.target.value, 10));
            onPageChange(0);
          }}
          rowsPerPageOptions={[5, 10, 25]}
        />
      </Paper>

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
