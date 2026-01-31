import { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Grid,
  Paper,
  TextField,
  Button,
  Select,
  MenuItem,
} from "@mui/material";
import {
  People as PeopleIcon,
  AdminPanelSettings as AdminPanelSettingsIcon,
  Person as PersonIcon,
} from "@mui/icons-material";
import { getUsersOnly, getAdminsOnly, createUser } from "../api/userAPI";
import UserTable from "../components/user/userTable";
import SectionDivider from "../components/common/SectionDivider";
import { SnackbarProvider, useSnackbar } from "notistack";
import { useSortableTable } from "../hooks/useSortableTable.js"

function AdminUserPageContent() {
    const [users, setUsers] = useState({ content: [], totalElements: 0 });
    const [admins, setAdmins] = useState({ content: [], totalElements: 0 });
    const [loadingUsers, setLoadingUsers] = useState(true);
    const [loadingAdmins, setLoadingAdmins] = useState(true);

    const [usersSearch, setUsersSearch] = useState("");
    const [adminsSearch, setAdminsSearch] = useState("");




  const [newUser, setNewUser] = useState({
    email: "",
    firstName: "",
    lastName: "",
    password: "",
    role: "USER",
  });

  const { enqueueSnackbar } = useSnackbar();

  const adminsTable = useSortableTable(5);
  const usersTable = useSortableTable(5);

  // Triggered when the component is mounted

    useEffect(() => {
        loadUsers();
    }, [
        usersTable.page,
        usersTable.rowsPerPage,
        usersTable.sort,
        usersSearch
    ]);

    useEffect(() => {
        loadAdmins();
    }, [
        adminsTable.page,
        adminsTable.rowsPerPage,
        adminsTable.sort,
        adminsSearch
    ]);
  //Pre-ready functions to be used dynamically

  const loadUsers = async () => {
    try {
      setLoadingUsers(true);
      const data = await getUsersOnly({
        page: usersTable.page,
        size: usersTable.rowsPerPage,
        sort: usersTable.sort,
      });
      setUsers(data);
    } catch (error) {
      console.error("Users cannot be loaded:", error);
      enqueueSnackbar("Failed to load users", {variant: "error"});
    } finally {
      setLoadingUsers(false);
    }
  };

  const loadAdmins = async () => {
    try {
      setLoadingAdmins(true);
      const data = await getAdminsOnly({
        page: adminsTable.page,
        size: adminsTable.rowsPerPage,
        sort: adminsTable.sort,
      });
      setAdmins(data);
    } catch (error) {
      console.error("Admins cannot be loaded:", error);
      enqueueSnackbar("Failed to load admins", {variant: "error"});
    } finally {
      setLoadingAdmins(false);
    }
  };

  const handleCreate = async () => {
      try{
          await createUser(newUser);
          enqueueSnackbar("User created", { variant: "success" });
          setNewUser({
              email: "",
              firstName: "",
              lastName: "",
              password: "",
              role: "USER",
          });
          usersTable.handleChangePage(0);
          loadUsers();
      }catch (err){
          enqueueSnackbar("Error creating user", {variant: "error"});
      }
  };

  const total = (users.totalElements || 0) + (admins.totalElements || 0);
  const adminUsers = admins.totalElements || 0;
  const regulars = users.totalElements || 0;

  if (loadingUsers || loadingAdmins) return <Typography>Loading...</Typography>;

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" sx={{ mb: 3 }}>
        User Management
      </Typography>

      {/* STATS */}
      <Grid container spacing={2} sx={{ mb: 3 }}>
        {[
          { label: "Total Users", value: total, icon: <PeopleIcon /> },
          { label: "Admins", value: adminUsers, icon: <AdminPanelSettingsIcon /> },
          { label: "User", value: regulars, icon: <PersonIcon /> },
        ].map(({ label, value, icon }) => (
          <Grid item xs={12} md={4} key={label}>
            <Paper
              sx={{
                p: 2,
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                minWidth: 300,
              }}
            >
              {/* LEFT */}
              <Box>
                <Typography variant="subtitle2">{label}</Typography>
                <Typography variant="h4">{value}</Typography>
              </Box>

              {/* RIGHT ICON */}
              <Box
                sx={{
                  width: 48,
                  height: 48,
                  borderRadius: "50%",
                  bgcolor: "primary.main",
                  color: "white",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                }}
              >
                {icon}
              </Box>
            </Paper>
          </Grid>
        ))}
      </Grid>

      {/* CREATE USER */}
      <Paper sx={{ p: 2, mb: 3, display: "flex", gap: 1, flexWrap: "wrap" }}>
        <TextField
          label="Email"
          value={newUser.email}
          onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
        />
        <TextField
          label="First Name"
          value={newUser.firstName}
          onChange={(e) =>
            setNewUser({ ...newUser, firstName: e.target.value })
          }
        />
        <TextField
          label="Last Name"
          value={newUser.lastName}
          onChange={(e) => setNewUser({ ...newUser, lastName: e.target.value })}
        />
        <TextField
          label="Password"
          type="password"
          value={newUser.password}
          onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
        />
        <Select
          value={newUser.role}
          onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
        >
          <MenuItem value="USER">USER</MenuItem>
          {/*<MenuItem value="ADMIN">ADMIN</MenuItem>*/}
        </Select>
        <Button variant="contained" onClick={handleCreate}>
          Add
        </Button>
      </Paper>

      <SectionDivider label="ADMINS" />

      {/* ADMIN TABLE */}
      <UserTable
        users={admins}
        reload={loadAdmins}
        enqueueSnackbar={enqueueSnackbar}
        page={adminsTable.page}
        rowsPerPage={adminsTable.rowsPerPage}
        onPageChange={adminsTable.handleChangePage}
        onRowsPerPageChange={adminsTable.handleChangeRowsPerPage}
        sortConfig={adminsTable.sortConfig}
        onSort={adminsTable.handleSort}
        searchValue={adminsSearch}
        onSearchChange={setAdminsSearch}
      />
      <SectionDivider label="USERS" />

      {/* USER TABLE */}
      <UserTable
        users={users}
        reload={loadUsers}
        enqueueSnackbar={enqueueSnackbar}
        page={usersTable.page}
        rowsPerPage={usersTable.rowsPerPage}
        onPageChange={usersTable.handleChangePage}
        onRowsPerPageChange={usersTable.handleChangeRowsPerPage}
        sortConfig={usersTable.sortConfig}
        onSort={usersTable.handleSort}
        searchValue={usersSearch}
        onSearchChange={setUsersSearch}
      />
    </Box>
  );
}

export default function AdminUserPage() {
  return (
    <SnackbarProvider
      maxSnack={3}
      anchorOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      autoHideDuration={3000}
    >
      <AdminUserPageContent />
    </SnackbarProvider>
  );
}
