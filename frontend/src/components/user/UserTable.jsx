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
import SortableTableHead from "../common/SortableTableHead.jsx";

export default function UserTable({
  users,
  reload,
  enqueueSnackbar,
  page,
  rowsPerPage,
  onPageChange,
  onRowsPerPageChange,
  sortConfig,
  onSort,
  searchValue = "",
  onSearchChange,
}) {

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedEmail, setSelectedEmail] = useState(null);


    /* ---------- COLUMNS CONFIG ---------- */
    const columns = [
        { field: 'index', label: '№', sortable: false },
        { field: 'email', label: 'Email', sortable: true },
        { field: 'firstName', label: 'First name', sortable: true },
        { field: 'lastName', label: 'Last name', sortable: true },
        { field: 'role', label: 'Role', sortable: true },
        { field: 'actions', label: 'Actions', sortable: false, align: 'right' },
    ];

  /* ---------- ACTIONS ---------- */
  const handleUpdate = async (email, payload) => {
      try {
          await updateUser(email, payload);
          enqueueSnackbar("User updated", {variant: "info"});
          reload();
      }catch(error) {
          console.error(error);
          enqueueSnackbar("Failed to update user", { variant: "error" });
      }
  };

  const askDelete = (email) => {
    setSelectedEmail(email);
    setConfirmOpen(true);
  };

  const confirmDelete = async () => {
      try{
        await deleteUser(selectedEmail);
          enqueueSnackbar("User deleted", { variant: "success" });
          setConfirmOpen(false);
        setSelectedEmail(null);
        reload();
      }catch (err) {
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
              {/* SEARCH ROW */}
              <TableRow>
                <TableCell colSpan={6}>
                  <TextField
                    fullWidth
                    size="small"
                    placeholder="Search by email, name or role…"
                    value={searchValue}
                    onChange={(e) => {
                      onSearchChange(e.target.value);
                      onPageChange(0);
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

                {/* SORTABLE HEADER */}
                <SortableTableHead
                    columns={columns}
                    sortConfig={sortConfig}
                    onSort={onSort}
                />
            </TableHead>

              <TableBody>
                  {(users.content || []).map((u, index) => (
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
