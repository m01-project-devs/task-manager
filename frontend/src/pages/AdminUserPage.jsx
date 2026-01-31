import { useEffect, useState } from "react";
import { Box, Typography, Grid, Paper, TextField, Button } from "@mui/material";
import {
  People as PeopleIcon,
  AdminPanelSettings as AdminPanelSettingsIcon,
  Person as PersonIcon,
  Add,
} from "@mui/icons-material";
import { getUsersOnly, getAdminsOnly, createUser, searchUsers } from "../api/userAPI";
import UserTable from "../components/user/UserTable";
import SectionDivider from "../components/common/SectionDivider";
import { SnackbarProvider, useSnackbar } from "notistack";
import { useSortableTable } from "../hooks/useSortableTable.js"

function AdminUserPageContent() {
  const [users, setUsers] = useState({ content: [], totalElements: 0 });
  const [admins, setAdmins] = useState({ content: [], totalElements: 0 });
  const [loadingUsers, setLoadingUsers] = useState(false);
  const [loadingAdmins, setLoadingAdmins] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");

  const [newUser, setNewUser] = useState({
    email: "",
    firstName: "",
    lastName: "",
    password: "",
  });

  const { enqueueSnackbar } = useSnackbar();

  const adminsTable = useSortableTable(5);
  const usersTable = useSortableTable(5);

  useEffect(() => {
    const fetchUsers = async () => {
      setLoadingUsers(true);
      try {
        const data = searchQuery.trim()
          ? await searchUsers({
              query: searchQuery,
              page: usersTable.page,
              size: usersTable.rowsPerPage,
              sort: usersTable.sort,
            })
          : await getUsersOnly({
              page: usersTable.page,
              size: usersTable.rowsPerPage,
              sort: usersTable.sort,
            });
        setUsers(data);
      } catch (error) {
        console.error("Users cannot be loaded:", error);
        enqueueSnackbar("Failed to load users", { variant: "error" });
      } finally {
        setLoadingUsers(false);
      }
    };
    fetchUsers();
  }, [
      usersTable.page,
      usersTable.rowsPerPage,
      usersTable.sort,
      searchQuery,
      enqueueSnackbar]);

  useEffect(() => {
    const fetchAdmins = async () => {
      setLoadingAdmins(true);
      try {
        const data = await getAdminsOnly({
            page: adminsTable.page,
            size: adminsTable.rowsPerPage,
            sort: adminsTable.sort,
        });
        setAdmins(data);
      } catch (error) {
        console.error("Admins cannot be loaded:", error);
        enqueueSnackbar("Failed to load admins", { variant: "error" });
      } finally {
        setLoadingAdmins(false);
      }
    };
    fetchAdmins();
  }, [
      adminsTable.page,
      adminsTable.rowsPerPage,
      adminsTable.sort,
      enqueueSnackbar,
  ]);

  const handleCreate = async () => {
    if (
      !newUser.email ||
      !newUser.firstName ||
      !newUser.lastName ||
      !newUser.password
    )
      return;

    try {
      await createUser({
        ...newUser,
        role: "USER",
      });
      enqueueSnackbar("User created", { variant: "success" });
      setNewUser({ email: "", firstName: "", lastName: "", password: "" });
      usersTable.handleChangePage(0);
      setLoadingUsers(true);
      const data = await getUsersOnly({
        page: 0,
        size: usersTable.rowsPerPage,
        sort: usersTable.sort,
      });
      setUsers(data);
    } catch (err) {
      console.error(err);
      enqueueSnackbar("Failed to create user", { variant: "error" });
    } finally {
      setLoadingUsers(false);
    }
  };

  const handleSearch = (query) => {
    setSearchQuery(query);
    usersTable.handleChangePage(0);
  };

  const totalUsers = (users.totalElements || 0) + (admins.totalElements || 0);
  const adminUsers = admins.totalElements || 0;
  const regularUsers = users.totalElements || 0;

  if (loadingUsers || loadingAdmins) return <Typography>Loading...</Typography>;

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" sx={{ mb: 3 }}>
        User Management
      </Typography>

      <Grid container spacing={2} sx={{ mb: 3 }}>
        {[
          { label: "Total Users", value: totalUsers, icon: <PeopleIcon /> },
          {
            label: "Admins",
            value: adminUsers,
            icon: <AdminPanelSettingsIcon />,
          },
          { label: "User", value: regularUsers, icon: <PersonIcon /> },
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
              <Box>
                <Typography variant="subtitle2">{label}</Typography>
                <Typography variant="h4">{value}</Typography>
              </Box>
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
        <Typography
          variant="body1"
          sx={{
            display: "flex",
            alignItems: "center",
            px: 2,
            height: 56,
            bgcolor: "grey.100",
            borderRadius: 1,
          }}
        >
          USER
        </Typography>
        <Button variant="contained" onClick={handleCreate} startIcon={<Add />}>
          Add
        </Button>
      </Paper>

      <SectionDivider label="ADMINS" />
      <UserTable
        users={admins}
        reload={async () => {
          setLoadingAdmins(true);
          try {
            const data = await getAdminsOnly({
                page: adminsTable.page,
                size: adminsTable.rowsPerPage,
                sort: adminsTable.sort,
            });
            setAdmins(data);
          } finally {
            setLoadingAdmins(false);
          }
        }}
        enqueueSnackbar={enqueueSnackbar}
        page={adminsTable.page}
        rowsPerPage={adminsTable.rowsPerPage}
        onPageChange={adminsTable.handleChangePage}
        onRowsPerPageChange={adminsTable.handleChangeRowsPerPage}
        sortConfig={adminsTable.sortConfig}
        onSort={adminsTable.handleSort}
        showSearch={false}
      />

      <SectionDivider label="USERS" />
      <UserTable
        users={users}
        reload={async () => {
          setLoadingUsers(true);
          try {
            const data = searchQuery.trim()
              ? await searchUsers({
                  query: searchQuery,
                  page: usersTable.page,
                  size: usersTable.rowsPerPage,
                  sort: usersTable.sort,
                })
              : await getUsersOnly({
                  page: usersTable.page,
                  size: usersTable.rowsPerPage,
                  sort: usersTable.sort,
                });
            setUsers(data);
          } finally {
            setLoadingUsers(false);
          }
        }}
        enqueueSnackbar={enqueueSnackbar}
        page={usersTable.page}
        rowsPerPage={usersTable.rowsPerPage}
        onPageChange={usersTable.handleChangePage}
        onRowsPerPageChange={usersTable.handleChangeRowsPerPage}
        sortConfig={usersTable.sortConfig}
        onSort={usersTable.handleSort}
        showSearch={true}
        searchQuery={searchQuery}
        onSearch={handleSearch}
      />
    </Box>
  );
}

export default function AdminUserPage() {
  return (
    <SnackbarProvider
      maxSnack={3}
      anchorOrigin={{ vertical: "top", horizontal: "right" }}
      autoHideDuration={3000}
    >
      <AdminUserPageContent />
    </SnackbarProvider>
  );
}
