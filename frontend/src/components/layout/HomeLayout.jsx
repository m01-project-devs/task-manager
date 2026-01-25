import React from "react";
import { Box } from "@mui/material";
import TopBar from "./TopBar";

export default function HomeLayout({ children }) {
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("isAuth");
    window.location.href = "/login";
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
        backgroundColor: "#f5f7fa",
      }}
    >
      <TopBar onLogout={handleLogout} />

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          padding: 4,
        }}
      >
        {children}
      </Box>
    </Box>
  );
}
