import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import HomePage from "./pages/HomePage";
import ResetPassword from "./pages/ResetPassword";

import ProtectedRoute from "./routes/ProtectedRoute";
import AuthLayout from "./components/layout/AuthLayout";
import HomeLayout from "./components/layout/HomeLayout";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />

        {/* AUTH PAGES */}
        <Route
          path="/login"
          element={
            <AuthLayout>
              <LoginPage />
            </AuthLayout>
          }
        />

        <Route
          path="/register"
          element={
            <AuthLayout>
              <RegisterPage />
            </AuthLayout>
          }
        />

        <Route
           path="/resetpassword"
           element={
             <AuthLayout>
               <ResetPassword />
             </AuthLayout>
           }
        />

        {/* HOME */}
        <Route
          path="/home"
          element={
            <ProtectedRoute>
              <HomeLayout>
                <HomePage />
              </HomeLayout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
