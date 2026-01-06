import { Box } from "@mui/material";

export default function HomeLayout({ children }) {
  return (
    <Box
      sx={{
        minHeight: "100vh",
        padding: 4,
        backgroundColor: "#121212", // örnek
      }}
    >
      {children}
    </Box>
  );
}
