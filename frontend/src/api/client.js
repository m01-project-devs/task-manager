import { API_BASE_URL } from '../config/apiConfig';

export async function pingBackend() {
  try {
    const res = await fetch(`${API_BASE_URL}/api/users`); 
    console.log('Backend ping status:', res.status);
    return res.ok;
  } catch (err) {
    console.error('Backend ping failed:', err);
    return false;
  }
}