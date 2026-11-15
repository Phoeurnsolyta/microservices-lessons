import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';

export default function ProtectedRoute() {
    const token = useAuthStore((state) => state.accessToken);
    const location = useLocation();

    if (!token) {
        return <Navigate to="/login" state={
            { from: location }
        } replace
        />;
    }

    return <Outlet />;
}