import {Routes, Route, Navigate} from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import AdminRoute from './AdminRoute';

const Placeholder = ({title}) => (
    <div className="p-8 max-w-4xl mx-auto">
        <div className="bg-white rounded-xl shadow-sm border border-slate-200 p-8 text-center">
            <h2 className="text-2xl font-bold text-slate-800 mb-2">{title}</h2>
            <p className="text-slate-500">Feature will be rendered here in the next step.</p>
        </div>
    </div>
);

export default function AppRoutes() {
    return (
        <Routes>
            {/* Public Routes */}
            <Route path="/login" element={<Placeholder title="Login Page"/>}/>
            <Route path="/register" element={<Placeholder title="Registration Page"/>}/>

            <Route element={<ProtectedRoute/>}>
                <Route path="/" element={<Placeholder title="Profile Overview"/>}/>
                <Route path="/profile" element={<Placeholder title="Personal Profile Edit"/>}/>
                <Route path="/security" element={<Placeholder title="Security & Password"/>}/>
                <Route path="/linked-accounts" element={<Placeholder title="Linked Social Accounts"/>}/>
                <Route path="/sessions" element={<Placeholder title="Active Sessions"/>}/>
                <Route path="/login-history" element={<Placeholder title="Login History"/>}/>
                <Route path="/activity" element={<Placeholder title="Account Activity Log"/>}/>
                <Route path="/deactivate" element={<Placeholder title="Account Deactivation"/>}/>

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

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/" replace/>}/>
        </Routes>
    );
}