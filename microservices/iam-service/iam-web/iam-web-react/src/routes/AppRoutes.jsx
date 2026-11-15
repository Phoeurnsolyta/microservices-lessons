import {Routes, Route, Navigate} from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import AdminRoute from './AdminRoute';
import AuthLayout from '../layouts/AuthLayout';
import AppLayout from '../layouts/AppLayout';
import LoginPage from '../features/auth/LoginPage';
import RegisterPage from '../features/auth/RegisterPage';
import ProfilePage from '../features/account/ProfilePage';
import SecurityPage from '../features/account/SecurityPage';
import LinkedAccountsPage from '../features/account/LinkedAccountsPage';
import SessionsPage from '../features/account/SessionsPage';
import LoginHistoryPage from '../features/account/LoginHistoryPage';
import AccountActivityPage from '../features/account/AccountActivityPage';
import DeactivatePage from '../features/account/DeactivatePage';
import AdminOverviewPage from '../features/dashboard/AdminOverviewPage';
import UserManagementPage from '../features/users/UserManagementPage';
import RoleManagementPage from '../features/roles/RoleManagementPage';
import PermissionManagementPage from '../features/permissions/PermissionManagementPage';
import ClientManagementPage from '../features/clients/ClientManagementPage';
import AuditLogsPage from '../features/audit/AuditLogsPage';
import SystemSettingsPage from '../features/settings/SystemSettingsPage';

export default function AppRoutes() {
    return (
        <Routes>
            {/* Public Auth Routes */}
            <Route element={<AuthLayout/>}>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
            </Route>

            <Route element={<ProtectedRoute/>}>
                <Route element={<AppLayout/>}>
                    <Route path="/" element={<ProfilePage/>}/>
                    <Route path="/profile" element={<ProfilePage/>}/>
                    <Route path="/security" element={<SecurityPage/>}/>
                    <Route path="/linked-accounts" element={<LinkedAccountsPage/>}/>
                    <Route path="/sessions" element={<SessionsPage/>}/>
                    <Route path="/login-history" element={<LoginHistoryPage/>}/>
                    <Route path="/activity" element={<AccountActivityPage/>}/>
                    <Route path="/deactivate" element={<DeactivatePage/>}/>

                    <Route element={<AdminRoute/>}>
                        <Route path="/admin" element={<AdminOverviewPage/>}/>
                        <Route path="/admin/users" element={<UserManagementPage/>}/>
                        <Route path="/admin/roles" element={<RoleManagementPage/>}/>
                        <Route path="/admin/permissions" element={<PermissionManagementPage/>}/>
                        <Route path="/admin/clients" element={<ClientManagementPage/>}/>
                        <Route path="/admin/audit" element={<AuditLogsPage/>}/>
                        <Route path="/admin/settings" element={<SystemSettingsPage/>}/>
                    </Route>
                </Route>
            </Route>

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/" replace/>}/>
        </Routes>
    );
}