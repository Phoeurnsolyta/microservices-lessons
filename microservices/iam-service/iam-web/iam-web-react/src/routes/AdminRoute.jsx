import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';

export default function AdminRoute() {
    const token = useAuthStore((state) => state.accessToken);
    const hasRole = useAuthStore((state) => state.hasRole);
    const hasPermission = useAuthStore((state) => state.hasPermission);

    if (!token) {
        return <Navigate to="/login" replace />;
    }

    const isAuthorized =
        hasRole('ADMIN') ||
        hasPermission('user:read') ||
        hasPermission('role:read') ||
        hasPermission('permission:read');

    if (!isAuthorized) {
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
}