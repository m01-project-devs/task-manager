import { Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function HomePage() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("isAuth");
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <>
      <Typography sx={{ mt: 5, color: "yellow" }} variant="h4">
        This is Home Page
      </Typography>

      <Button
        variant="contained"
        sx={{ mt: 2 }}
        onClick={logout}
      >
        Logout
      </Button>
    </>
  );
}
