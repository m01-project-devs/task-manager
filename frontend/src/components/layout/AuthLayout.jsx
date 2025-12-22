import { Box } from "@mui/material";

export default function AuthLayout({ children }) {
  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundImage: "url('./public/background.png')",
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      {children}
    </Box>
  );
}
