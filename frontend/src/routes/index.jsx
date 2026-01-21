import { Routes, Route } from "react-router-dom";
import ResetPassword from "../pages/ResetPassword";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/resetpassword" element={<ResetPassword />} />
    </Routes>
  );
}
