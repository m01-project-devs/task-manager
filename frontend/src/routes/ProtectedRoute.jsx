import jwt_decode from "jwt-decode"; // default import
import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children, requireRole }) {
  const token = localStorage.getItem("token");

  if (!token) return <Navigate to="/login" replace />;

  let role;

  try {
    const decoded = jwt_decode(token); // straight call, .default керек эмес
    role = decoded.role; // бекенд payloadда role болушу керек
  } catch (err) {
    console.error("Invalid token", err);
    return <Navigate to="/login" replace />;
  }

  if (requireRole && role !== requireRole) {
    return <Navigate to="/boards" replace />;
  }

  return children;
}
