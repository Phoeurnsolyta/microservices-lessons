import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';

export default function AdminRoute() {
    const token = useAuthStore((state) => state.accessToken);
    const hasRole = useAuthStore((state) => state.hasRole);
    if (!token) {
        return <Navigate to="/login" replace />;
    }
    const isAuthorized = hasRole('ADMIN');
    if (!isAuthorized) {
        return <Navigate to="/" replace />;
    }
    return <Outlet />;
}