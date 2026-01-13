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
import { getUsers, createUser } from "../api/userAPI";
import UserTable from "../components/user/userTable";
import { SnackbarProvider, useSnackbar } from "notistack";

function AdminUserPageContent() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newUser, setNewUser] = useState({
    email: "",
    firstName: "",
    lastName: "",
    password: "",
    role: "USER",
  });

  const { enqueueSnackbar } = useSnackbar();

  const loadUsers = async () => {
    const data = await getUsers();
    setUsers(data.content || []);
    setLoading(false);
  };

  useEffect(() => {
    (async () => {
      try {
        const data = await getUsers();
        setUsers(data.content || []);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const handleCreate = async () => {
    await createUser(newUser);
    enqueueSnackbar("User created", { variant: "success" });
    setNewUser({
      email: "",
      firstName: "",
      lastName: "",
      password: "",
      role: "USER",
    });
    loadUsers();
  };

  const total = users.length;
  const admins = users.filter((u) => u.role === "ADMIN").length;
  const regulars = users.filter((u) => u.role === "USER").length;

  if (loading) return <Typography>Loading...</Typography>;

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" sx={{ mb: 3 }}>
        User Management
      </Typography>

      {/* STATS */}
      <Grid container spacing={2} sx={{ mb: 3 }}>
        {[
          { label: "Total Users", value: total, icon: <PeopleIcon /> },
          { label: "Admins", value: admins, icon: <AdminPanelSettingsIcon /> },
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
          <MenuItem value="ADMIN">ADMIN</MenuItem>
        </Select>
        <Button variant="contained" onClick={handleCreate}>
          Add
        </Button>
      </Paper>

      {/* TABLE */}
      <UserTable
        users={users}
        reload={loadUsers}
        enqueueSnackbar={enqueueSnackbar}
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
