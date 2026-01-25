import React from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  InputBase,
  Button,
  Avatar,
} from "@mui/material";

const TopBar = ({ onLogout }) => {
  const email = localStorage.getItem("email");
  return (
    <AppBar position="static" color="primary">
      <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <Typography variant="h6" component="div" sx={{ fontWeight: "bold" }}>
            Task Manager
          </Typography>
        </Box>

        <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
          <Avatar sx={{ width: 32, height: 32, bgcolor: "#26a69a" }}>
            {email ? email[0].toUpperCase() : "?"}
          </Avatar>
          <Typography
            variant="body2"
            sx={{ display: { xs: "none", sm: "block" } }}
          >
            {email || ""}
          </Typography>
          <Button
            color="inherit"
            onClick={onLogout}
            sx={{ bgcolor: "#f44336" }}
          >
            Logout
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default TopBar;
