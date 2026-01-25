import jwt_decode from "jwt-decode";
import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children, requireRole }) {
  const token = localStorage.getItem("token");

  if (!token) return <Navigate to="/login" replace />;

  let role;

  try {
    const decoded = jwt_decode(token);
    role = decoded.role;
  } catch (err) {
    console.error("Invalid token", err);
    return <Navigate to="/login" replace />;
  }

  if (requireRole && role !== requireRole) {
    return <Navigate to="/boards" replace />;
  }

  return children;
}
