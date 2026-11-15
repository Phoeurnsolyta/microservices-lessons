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

const Placeholder = ({title}) => (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-8 text-center">
        <h2 className="text-xl font-bold text-slate-800 mb-2">{title}</h2>
        <p className="text-xs text-slate-500">Coming next in Step 5 (Management Suite).</p>
    </div>
);

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
                        <Route path="/admin" element={<Placeholder title="Admin Overview Dashboard"/>}/>
                        <Route path="/admin/users" element={<Placeholder title="User Management"/>}/>
                        <Route path="/admin/roles" element={<Placeholder title="Role Management"/>}/>
                        <Route path="/admin/permissions" element={<Placeholder title="Permission Catalog"/>}/>
                        <Route path="/admin/clients" element={<Placeholder title="OAuth2 Clients"/>}/>
                        <Route path="/admin/audit" element={<Placeholder title="Audit Activity Stream"/>}/>
                        <Route path="/admin/settings" element={<Placeholder title="System Policies"/>}/>
                    </Route>
                </Route>
            </Route>

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/" replace/>}/>
        </Routes>
    );
}