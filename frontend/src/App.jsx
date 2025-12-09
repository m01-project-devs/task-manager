import { AppBar, Toolbar, Typography, Button, Container } from "@mui/material";
import HomePage from "./pages/HomePage";
import { API_BASE_URL } from './config/apiConfig'

function App() {
  console.log('API_BASE_URL:', API_BASE_URL );

  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            My React + MUI App
          </Typography>
          <Button color="inherit" sx={{ background: "green", }}>Login</Button>
        </Toolbar>
      </AppBar>

      <Container sx={{ background: '#DBE9F4', minHeight: "100vh" }} maxWidth={false}>
        <Typography variant="h3" gutterBottom>
          Welcome to Frontend, Abiler!
        </Typography>
         <Typography>
             API base URL: {API_BASE_URL}
         </Typography>
        <Button variant="contained">MUI Works!</Button>
        <HomePage/>
      </Container>
      
    </>
  );
}

export default App;
