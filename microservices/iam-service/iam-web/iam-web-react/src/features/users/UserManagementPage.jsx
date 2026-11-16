import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {adminService} from '../../services/adminService';
import ConfirmModal from '../../components/common/ConfirmModal';
import {
    Search,
    Plus,
    Shield,
    Key,
    Lock,
    Unlock,
    CheckCircle,
    XCircle,
    Laptop,
    History,
    Trash2,
    KeyRound,
    AlertCircle,
    CheckCircle2,
    Loader2,
    X,
    Check,
} from 'lucide-react';

const STATIC_USER_SESSIONS = [
    {
        id: 'sess-1',
        device: 'Windows 11 / Chrome 124',
        ipAddress: '192.168.1.105',
        location: 'Phnom Penh, Cambodia',
        lastActive: '2026-11-15T10:30:00.000Z',
        isCurrent: true,
    },
    {
        id: 'sess-2',
        device: 'iPhone 15 Pro / Safari',
        ipAddress: '110.74.214.88',
        location: 'Siem Reap, Cambodia',
        lastActive: '2026-11-15T09:15:00.000Z',
        isCurrent: false,
    },
];

export default function UserManagementPage() {
    const {t} = useTranslation();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [statusFilter, setStatusFilter] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [notification, setNotification] = useState(null);

    // Active Modals
    const [createModalOpen, setCreateModalOpen] = useState(false);
    const [roleModalUser, setRoleModalUser] = useState(null);
    const [permModalUser, setPermModalUser] = useState(null);
    const [passwordModalUser, setPasswordModalUser] = useState(null);
    const [sessionsModalUser, setSessionsModalUser] = useState(null);
    const [historyModalUser, setHistoryModalUser] = useState(null);
    const [deleteModalUser, setDeleteModalUser] = useState(null);
    const [deleting, setDeleting] = useState(false);

    const showNotification = (type, message) => {
        setNotification({type, message});
        setTimeout(() => setNotification(null), 3500);
    };

    const refreshUsers = () => {
        adminService.getUsers({
            search,
            enabled: statusFilter,
            page,
            size: 10,
        })
            .then((data) => {
                setUsers(data.content || []);
                setTotalPages(data.page?.totalPages || 1);
            })
            .catch((err) => {
                showNotification('error', err.message || 'Failed to fetch users');
            });
    };

    useEffect(() => {
        let isMounted = true;
        adminService.getUsers({
            search,
            enabled: statusFilter,
            page,
            size: 10,
        })
            .then((data) => {
                if (isMounted) {
                    setUsers(data.content || []);
                    setTotalPages(data.page?.totalPages || 1);
                }
            })
            .catch((err) => {
                if (isMounted) showNotification('error', err.message || 'Failed to fetch users');
            })
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, [search, statusFilter, page]);

    // Account Status Handlers
    const handleToggleEnable = async (user) => {
        try {
            if (user.enabled) {
                await adminService.disableUser(user.uuid);
                showNotification('success', `User @${user.username} has been disabled.`);
            } else {
                await adminService.enableUser(user.uuid);
                showNotification('success', `User @${user.username} has been activated.`);
            }
            refreshUsers();
        } catch (err) {
            showNotification('error', err.message);
        }
    };

    const handleToggleLock = async (user) => {
        try {
            if (user.locked) {
                await adminService.unlockUser(user.uuid);
                showNotification('success', `User @${user.username} unlocked.`);
            } else {
                await adminService.lockUser(user.uuid);
                showNotification('success', `User @${user.username} locked.`);
            }
            refreshUsers();
        } catch (err) {
            showNotification('error', err.message);
        }
    };

    // Custom Modal Delete Handler
    const handleConfirmDelete = async () => {
        if (!deleteModalUser) return;
        setDeleting(true);
        try {
            await adminService.deleteUser(deleteModalUser.uuid);
            showNotification('success', `User @${deleteModalUser.username} deleted.`);
            setDeleteModalUser(null);
            refreshUsers();
        } catch (err) {
            showNotification('error', err.message);
        } finally {
            setDeleting(false);
        }
    };

    return (
        <div className="space-y-6">
            {/* Page Title & Actions */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('nav.users')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Administer user accounts, security credentials, roles, and granular direct permissions
                    </p>
                </div>
                <button
                    onClick={() => setCreateModalOpen(true)}
                    className="flex items-center gap-1.5 px-3.5 py-2 bg-primary-600 hover:bg-primary-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
                >
                    <Plus className="w-4 h-4"/>
                    <span>{t('management.createUser')}</span>
                </button>
            </div>

            {/* Notifications */}
            {notification && (
                <div
                    className={`p-3 rounded-xl border text-xs flex items-center gap-2 ${
                        notification.type === 'success'
                            ? 'bg-emerald-50 border-emerald-200 text-emerald-800'
                            : 'bg-red-50 border-red-200 text-red-800'
                    }`}
                >
                    {notification.type === 'success' ? (
                        <CheckCircle2 className="w-4 h-4 shrink-0"/>
                    ) : (
                        <AlertCircle className="w-4 h-4 shrink-0"/>
                    )}
                    <span>{notification.message}</span>
                </div>
            )}

            {/* Filters & Search Toolbar */}
            <div
                className="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm flex flex-col sm:flex-row items-center gap-3">
                <div className="relative flex-1 w-full">
                    <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5"/>
                    <input
                        type="text"
                        value={search}
                        onChange={(e) => {
                            setSearch(e.target.value);
                            setPage(0);
                        }}
                        placeholder="Search by username, full name, or email..."
                        className="w-full pl-9 pr-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                </div>
                <div className="flex items-center gap-2 w-full sm:w-auto">
                    <select
                        value={statusFilter}
                        onChange={(e) => {
                            setStatusFilter(e.target.value);
                            setPage(0);
                        }}
                        className="px-3 py-2 text-xs rounded-xl border border-slate-200 bg-white focus:outline-none focus:ring-2 focus:ring-primary-500 w-full sm:w-auto"
                    >
                        <option value="">All Statuses</option>
                        <option value="true">Active / Enabled</option>
                        <option value="false">Disabled</option>
                    </select>
                </div>
            </div>

            {/* Users Table */}
            <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full text-left text-xs">
                        <thead
                            className="bg-slate-50 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
                        <tr>
                            <th className="py-3 px-4">User</th>
                            <th className="py-3 px-4">Contact</th>
                            <th className="py-3 px-4">Status</th>
                            <th className="py-3 px-4">Security</th>
                            <th className="py-3 px-4 text-right">Actions</th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-100 text-slate-700">
                        {loading ? (
                            <tr>
                                <td colSpan="5" className="py-12 text-center text-slate-400">
                                    <Loader2 className="w-5 h-5 animate-spin mx-auto mb-2"/>
                                    <span>{t('common.loading')}</span>
                                </td>
                            </tr>
                        ) : users.length === 0 ? (
                            <tr>
                                <td colSpan="5" className="py-12 text-center text-slate-400">
                                    No users match the search criteria.
                                </td>
                            </tr>
                        ) : (
                            users.map((user) => (
                                <tr key={user.uuid} className="hover:bg-slate-50/70 transition">
                                    {/* USER COLUMN WITH AVATAR */}
                                    <td className="py-3 px-4">
                                        <div className="flex items-center gap-3">
                                            <div
                                                className="w-9 h-9 rounded-xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-xs border border-primary-200 shrink-0 overflow-hidden shadow-2xs">
                                                {user.pictureUrl ? (
                                                    <img
                                                        src={user.pictureUrl}
                                                        alt={user.name || user.username}
                                                        className="w-full h-full object-cover"
                                                    />
                                                ) : (
                                                    <span>{(user.name || user.username || 'U')[0].toUpperCase()}</span>
                                                )}
                                            </div>
                                            <div>
                                                <div className="font-semibold text-slate-900 leading-tight">
                                                    {user.name || user.username}
                                                </div>
                                                <div className="text-[11px] text-slate-400 font-mono mt-0.5">
                                                    @{user.username}
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="py-3 px-4">
                                        <div>{user.email}</div>
                                    </td>
                                    <td className="py-3 px-4">
                                        <span
                                            className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold ${
                                                user.enabled
                                                    ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                                                    : 'bg-slate-100 text-slate-600 border border-slate-200'
                                            }`}
                                        >
                                            {user.enabled ? <CheckCircle className="w-3 h-3"/> :
                                                <XCircle className="w-3 h-3"/>}
                                            {user.enabled ? t('common.enabled') : t('common.disabled')}
                                        </span>
                                    </td>
                                    <td className="py-3 px-4">
                                        <span
                                            className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold ${
                                                user.locked
                                                    ? 'bg-red-50 text-red-700 border border-red-200'
                                                    : 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                                            }`}
                                        >
                                            {user.locked ? <Lock className="w-3 h-3"/> : <Unlock className="w-3 h-3"/>}
                                            {user.locked ? 'Locked' : 'Unlocked'}
                                        </span>
                                    </td>
                                    <td className="py-3 px-4 text-right">
                                        <div className="flex items-center justify-end gap-1">
                                            <button
                                                title="Assign Roles"
                                                onClick={() => setRoleModalUser(user)}
                                                className="p-1.5 hover:bg-slate-100 text-slate-600 rounded-lg transition"
                                            >
                                                <Key className="w-4 h-4"/>
                                            </button>
                                            <button
                                                title="Grant Permissions"
                                                onClick={() => setPermModalUser(user)}
                                                className="p-1.5 hover:bg-slate-100 text-slate-600 rounded-lg transition"
                                            >
                                                <Shield className="w-4 h-4"/>
                                            </button>
                                            <button
                                                title="Inspect Active Sessions"
                                                onClick={() => setSessionsModalUser(user)}
                                                className="p-1.5 hover:bg-slate-100 text-slate-600 rounded-lg transition"
                                            >
                                                <Laptop className="w-4 h-4"/>
                                            </button>
                                            <button
                                                title="User Login History"
                                                onClick={() => setHistoryModalUser(user)}
                                                className="p-1.5 hover:bg-slate-100 text-slate-600 rounded-lg transition"
                                            >
                                                <History className="w-4 h-4"/>
                                            </button>
                                            <button
                                                title="Reset Password"
                                                onClick={() => setPasswordModalUser(user)}
                                                className="p-1.5 hover:bg-slate-100 text-amber-600 rounded-lg transition"
                                            >
                                                <KeyRound className="w-4 h-4"/>
                                            </button>
                                            <button
                                                title={user.locked ? 'Unlock Account' : 'Lock Account'}
                                                onClick={() => handleToggleLock(user)}
                                                className="p-1.5 hover:bg-slate-100 text-slate-600 rounded-lg transition"
                                            >
                                                {user.locked ? <Unlock className="w-4 h-4 text-emerald-600"/> :
                                                    <Lock className="w-4 h-4 text-slate-500"/>}
                                            </button>
                                            <button
                                                title={user.enabled ? 'Deactivate User' : 'Activate User'}
                                                onClick={() => handleToggleEnable(user)}
                                                className="p-1.5 hover:bg-slate-100 text-slate-600 rounded-lg transition"
                                            >
                                                {user.enabled ? <XCircle className="w-4 h-4 text-slate-400"/> :
                                                    <CheckCircle className="w-4 h-4 text-emerald-600"/>}
                                            </button>
                                            <button
                                                title="Delete User"
                                                onClick={() => setDeleteModalUser(user)}
                                                className="p-1.5 hover:bg-red-50 text-red-600 rounded-lg transition"
                                            >
                                                <Trash2 className="w-4 h-4"/>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>

                {/* Pagination Bar */}
                <div className="p-4 border-t border-slate-100 flex items-center justify-between text-xs text-slate-500">
                    <span>
                        Page {page + 1} of {Math.max(totalPages, 1)}
                    </span>
                    <div className="flex gap-2">
                        <button
                            disabled={page === 0}
                            onClick={() => setPage((p) => Math.max(p - 1, 0))}
                            className="px-3 py-1.5 rounded-lg border border-slate-200 hover:bg-slate-50 disabled:opacity-40 transition font-medium"
                        >
                            Previous
                        </button>
                        <button
                            disabled={page + 1 >= totalPages}
                            onClick={() => setPage((p) => p + 1)}
                            className="px-3 py-1.5 rounded-lg border border-slate-200 hover:bg-slate-50 disabled:opacity-40 transition font-medium"
                        >
                            Next
                        </button>
                    </div>
                </div>
            </div>

            {/* Modal Components */}
            {createModalOpen && (
                <CreateUserModal
                    onClose={() => setCreateModalOpen(false)}
                    onSuccess={() => {
                        setCreateModalOpen(false);
                        showNotification('success', 'User account created successfully.');
                        refreshUsers();
                    }}
                />
            )}

            {roleModalUser && (
                <UserRoleAssignmentModal
                    user={roleModalUser}
                    onClose={() => setRoleModalUser(null)}
                    onSuccess={() => {
                        setRoleModalUser(null);
                        showNotification('success', `Roles updated for @${roleModalUser.username}`);
                    }}
                />
            )}

            {permModalUser && (
                <UserPermissionAssignmentModal
                    user={permModalUser}
                    onClose={() => setPermModalUser(null)}
                    onSuccess={() => {
                        setPermModalUser(null);
                        showNotification('success', `Direct permissions updated for @${permModalUser.username}`);
                    }}
                />
            )}

            {passwordModalUser && (
                <AdminResetPasswordModal
                    user={passwordModalUser}
                    onClose={() => setPasswordModalUser(null)}
                    onSuccess={() => {
                        setPasswordModalUser(null);
                        showNotification('success', `Password reset for @${passwordModalUser.username}`);
                    }}
                />
            )}

            {sessionsModalUser && (
                <UserSessionsInspectorModal
                    user={sessionsModalUser}
                    onClose={() => setSessionsModalUser(null)}
                />
            )}

            {historyModalUser && (
                <UserLoginHistoryInspectorModal
                    user={historyModalUser}
                    onClose={() => setHistoryModalUser(null)}
                />
            )}

            {/* Custom Modal for User Deletion */}
            <ConfirmModal
                isOpen={Boolean(deleteModalUser)}
                onClose={() => setDeleteModalUser(null)}
                onConfirm={handleConfirmDelete}
                title="Delete User Account"
                message={`Are you sure you want to delete user @${deleteModalUser?.username}? This action is irreversible.`}
                confirmText="Delete User"
                cancelText="Cancel"
                variant="danger"
                loading={deleting}
            />
        </div>
    );
}

function CreateUserModal({onClose, onSuccess}) {
    const [form, setForm] = useState({
        username: '',
        email: '',
        password: '',
        givenName: '',
        familyName: '',
        phoneNumber: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        try {
            await adminService.createUser({
                username: form.username,
                email: form.email,
                password: form.password,
                givenName: form.givenName,
                familyName: form.familyName,
                phoneNumber: form.phoneNumber || null,
                locale: 'en',
                zoneinfo: 'Asia/Phnom_Penh',
            });
            onSuccess();
        } catch (err) {
            setError(err.message || 'Failed to create user');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <h3 className="font-bold text-slate-900 text-sm">Create New User Account</h3>
                    <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-600 rounded-lg">
                        <X className="w-4 h-4"/>
                    </button>
                </div>

                {error && (
                    <div
                        className="m-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
                        <AlertCircle className="w-4 h-4 shrink-0"/>
                        <span>{error}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} className="p-4 space-y-3 text-xs">
                    <div className="grid grid-cols-2 gap-3">
                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">First Name *</label>
                            <input
                                type="text"
                                required
                                value={form.givenName}
                                onChange={(e) => setForm({...form, givenName: e.target.value})}
                                className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                            />
                        </div>
                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">Last Name *</label>
                            <input
                                type="text"
                                required
                                value={form.familyName}
                                onChange={(e) => setForm({...form, familyName: e.target.value})}
                                className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                            />
                        </div>
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Username *</label>
                        <input
                            type="text"
                            required
                            value={form.username}
                            onChange={(e) => setForm({...form, username: e.target.value})}
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Email Address *</label>
                        <input
                            type="email"
                            required
                            value={form.email}
                            onChange={(e) => setForm({...form, email: e.target.value})}
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Password *</label>
                        <input
                            type="password"
                            required
                            minLength={8}
                            value={form.password}
                            onChange={(e) => setForm({...form, password: e.target.value})}
                            placeholder="Min 8 characters"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Phone Number</label>
                        <input
                            type="text"
                            value={form.phoneNumber}
                            onChange={(e) => setForm({...form, phoneNumber: e.target.value})}
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                        />
                    </div>

                    <div className="flex justify-end gap-2 pt-3 border-t border-slate-100">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 font-semibold"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={loading}
                            className="px-4 py-2 bg-primary-600 text-white rounded-xl hover:bg-primary-700 font-semibold disabled:opacity-50"
                        >
                            {loading ? 'Creating...' : 'Create Account'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

function UserRoleAssignmentModal({user, onClose, onSuccess}) {
    const [allRoles, setAllRoles] = useState([]);
    const [assignedRoleUuids, setAssignedRoleUuids] = useState(new Set());
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        let isMounted = true;
        Promise.all([
            adminService.getRoles({size: 100}),
            adminService.getUserAuthorities(user.uuid),
        ])
            .then(([rolesRes, userAuthRes]) => {
                if (isMounted) {
                    setAllRoles(rolesRes.content || []);
                    const currentUuids = new Set(
                        (userAuthRes.roles || [])
                            .map((r) => r.uuid || r.roleUuid)
                            .filter(Boolean)
                    );
                    setAssignedRoleUuids(currentUuids);
                }
            })
            .catch((err) => console.error(err))
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, [user.uuid]);

    const toggleRole = (uuid) => {
        if (!uuid) return;
        const next = new Set(assignedRoleUuids);
        if (next.has(uuid)) {
            next.delete(uuid);
        } else {
            next.add(uuid);
        }
        setAssignedRoleUuids(next);
    };

    const handleSave = async () => {
        const validRoleUuids = Array.from(assignedRoleUuids).filter(Boolean);
        if (validRoleUuids.length === 0) {
            alert('Please select at least one role to assign.');
            return;
        }

        setSaving(true);
        try {
            await adminService.assignRolesToUser(user.uuid, {
                roleUuids: validRoleUuids,
            });
            onSuccess();
        } catch (err) {
            alert(err.message || 'Failed to save roles');
        } finally {
            setSaving(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div className="flex items-center gap-3">
                        <div
                            className="w-8 h-8 rounded-xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-xs border border-primary-200 shrink-0 overflow-hidden">
                            {user.pictureUrl ? (
                                <img src={user.pictureUrl} alt={user.username} className="w-full h-full object-cover"/>
                            ) : (
                                <span>{(user.name || user.username || 'U')[0].toUpperCase()}</span>
                            )}
                        </div>
                        <div>
                            <h3 className="font-bold text-slate-900 text-sm">Assign Roles</h3>
                            <p className="text-[11px] text-slate-400">@{user.username}</p>
                        </div>
                    </div>
                    <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-600 rounded-lg">
                        <X className="w-4 h-4"/>
                    </button>
                </div>

                <div className="p-4 space-y-2 max-h-72 overflow-y-auto">
                    {loading ? (
                        <div className="py-6 text-center text-slate-400 text-xs">Loading roles...</div>
                    ) : (
                        allRoles.map((role) => {
                            const checked = assignedRoleUuids.has(role.uuid);
                            return (
                                <div
                                    key={role.uuid}
                                    onClick={() => toggleRole(role.uuid)}
                                    className={`p-3 rounded-xl border cursor-pointer transition flex items-center justify-between ${
                                        checked
                                            ? 'bg-primary-50 border-primary-300 text-primary-900'
                                            : 'border-slate-200 hover:bg-slate-50 text-slate-700'
                                    }`}
                                >
                                    <div>
                                        <div className="text-xs font-bold">{role.name}</div>
                                        <div className="text-[11px] text-slate-500 mt-0.5">{role.description}</div>
                                    </div>
                                    <div
                                        className={`w-5 h-5 rounded-md flex items-center justify-center border transition ${
                                            checked
                                                ? 'bg-primary-600 border-primary-600 text-white'
                                                : 'border-slate-300 bg-white'
                                        }`}
                                    >
                                        {checked && <Check className="w-3.5 h-3.5"/>}
                                    </div>
                                </div>
                            );
                        })
                    )}
                </div>

                <div className="p-4 border-t border-slate-100 flex justify-end gap-2 bg-slate-50">
                    <button
                        onClick={onClose}
                        className="px-4 py-2 border border-slate-200 text-slate-600 rounded-xl hover:bg-white text-xs font-semibold"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleSave}
                        disabled={saving}
                        className="px-4 py-2 bg-primary-600 text-white rounded-xl hover:bg-primary-700 text-xs font-semibold disabled:opacity-50"
                    >
                        {saving ? 'Saving...' : 'Save Role Assignments'}
                    </button>
                </div>
            </div>
        </div>
    );
}

function UserPermissionAssignmentModal({user, onClose, onSuccess}) {
    const [allPerms, setAllPerms] = useState([]);
    const [selectedUuids, setSelectedUuids] = useState(new Set());
    const [effect, setEffect] = useState('GRANT');
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        let isMounted = true;
        adminService.getPermissions({size: 100})
            .then((permsRes) => {
                if (isMounted) setAllPerms(permsRes.content || []);
            })
            .catch((err) => console.error(err))
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, []);

    const togglePerm = (uuid) => {
        const next = new Set(selectedUuids);
        if (next.has(uuid)) {
            next.delete(uuid);
        } else {
            next.add(uuid);
        }
        setSelectedUuids(next);
    };

    const handleGrant = async () => {
        if (selectedUuids.size === 0) {
            alert('Please select at least one permission.');
            return;
        }
        setSaving(true);
        try {
            await adminService.grantPermissionsToUser(user.uuid, {
                permissionUuids: Array.from(selectedUuids),
                effect,
                reason: 'Direct permission granted by administrator',
            });
            onSuccess();
        } catch (err) {
            alert(err.message || 'Failed to grant permissions');
        } finally {
            setSaving(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-lg w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div className="flex items-center gap-3">
                        <div
                            className="w-8 h-8 rounded-xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-xs border border-primary-200 shrink-0 overflow-hidden">
                            {user.pictureUrl ? (
                                <img src={user.pictureUrl} alt={user.username} className="w-full h-full object-cover"/>
                            ) : (
                                <span>{(user.name || user.username || 'U')[0].toUpperCase()}</span>
                            )}
                        </div>
                        <div>
                            <h3 className="font-bold text-slate-900 text-sm">Direct Permission Grant</h3>
                            <p className="text-[11px] text-slate-400">@{user.username}</p>
                        </div>
                    </div>
                    <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-600 rounded-lg">
                        <X className="w-4 h-4"/>
                    </button>
                </div>

                <div className="p-4 space-y-3">
                    <div className="flex items-center gap-3">
                        <span className="text-xs font-semibold text-slate-700">Effect:</span>
                        <div className="flex gap-2">
                            <button
                                type="button"
                                onClick={() => setEffect('GRANT')}
                                className={`px-3 py-1 rounded-lg text-xs font-bold transition ${
                                    effect === 'GRANT'
                                        ? 'bg-emerald-600 text-white'
                                        : 'border border-slate-200 text-slate-600 hover:bg-slate-50'
                                }`}
                            >
                                GRANT (Allow)
                            </button>
                            <button
                                type="button"
                                onClick={() => setEffect('DENY')}
                                className={`px-3 py-1 rounded-lg text-xs font-bold transition ${
                                    effect === 'DENY'
                                        ? 'bg-red-600 text-white'
                                        : 'border border-slate-200 text-slate-600 hover:bg-slate-50'
                                }`}
                            >
                                DENY (Explicit Block)
                            </button>
                        </div>
                    </div>

                    <div className="space-y-1.5 max-h-64 overflow-y-auto border border-slate-100 rounded-xl p-2">
                        {loading ? (
                            <div className="py-6 text-center text-slate-400 text-xs">Loading permissions...</div>
                        ) : (
                            allPerms.map((perm) => {
                                const checked = selectedUuids.has(perm.uuid);
                                return (
                                    <div
                                        key={perm.uuid}
                                        onClick={() => togglePerm(perm.uuid)}
                                        className={`p-2.5 rounded-lg border text-xs cursor-pointer flex items-center justify-between transition ${
                                            checked
                                                ? 'bg-primary-50 border-primary-300 text-primary-900'
                                                : 'border-slate-100 hover:bg-slate-50 text-slate-700'
                                        }`}
                                    >
                                        <div>
                                            <span className="font-mono font-semibold">{perm.name}</span>
                                            <span className="text-[11px] text-slate-400 ml-2">({perm.resource})</span>
                                        </div>
                                        <div
                                            className={`w-4 h-4 rounded flex items-center justify-center border transition ${
                                                checked
                                                    ? 'bg-primary-600 border-primary-600 text-white'
                                                    : 'border-slate-300 bg-white'
                                            }`}
                                        >
                                            {checked && <Check className="w-3.5 h-3.5"/>}
                                        </div>
                                    </div>
                                );
                            })
                        )}
                    </div>
                </div>

                <div className="p-4 border-t border-slate-100 flex justify-end gap-2 bg-slate-50">
                    <button
                        onClick={onClose}
                        className="px-4 py-2 border border-slate-200 text-slate-600 rounded-xl hover:bg-white text-xs font-semibold"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleGrant}
                        disabled={saving}
                        className="px-4 py-2 bg-primary-600 text-white rounded-xl hover:bg-primary-700 text-xs font-semibold disabled:opacity-50"
                    >
                        {saving ? 'Applying...' : 'Apply Direct Permissions'}
                    </button>
                </div>
            </div>
        </div>
    );
}

function AdminResetPasswordModal({user, onClose, onSuccess}) {
    const [newPassword, setNewPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        try {
            await adminService.resetUserPassword(user.uuid, newPassword);
            onSuccess();
        } catch (err) {
            setError(err.message || 'Failed to reset password');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-sm w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div className="flex items-center gap-3">
                        <div
                            className="w-8 h-8 rounded-xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-xs border border-primary-200 shrink-0 overflow-hidden">
                            {user.pictureUrl ? (
                                <img src={user.pictureUrl} alt={user.username} className="w-full h-full object-cover"/>
                            ) : (
                                <span>{(user.name || user.username || 'U')[0].toUpperCase()}</span>
                            )}
                        </div>
                        <div>
                            <h3 className="font-bold text-slate-900 text-sm">Force Password Reset</h3>
                            <p className="text-[11px] text-slate-400">@{user.username}</p>
                        </div>
                    </div>
                    <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-600 rounded-lg">
                        <X className="w-4 h-4"/>
                    </button>
                </div>

                {error && (
                    <div
                        className="m-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
                        <AlertCircle className="w-4 h-4 shrink-0"/>
                        <span>{error}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} className="p-4 space-y-4 text-xs">
                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">New Password *</label>
                        <input
                            type="password"
                            required
                            minLength={8}
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            placeholder="Enter new strong password"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                        />
                    </div>

                    <div className="flex justify-end gap-2 pt-2 border-t border-slate-100">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-3.5 py-1.5 border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 font-semibold"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={loading}
                            className="px-3.5 py-1.5 bg-amber-600 text-white rounded-xl hover:bg-amber-700 font-semibold disabled:opacity-50"
                        >
                            {loading ? 'Updating...' : 'Update Password'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

function UserSessionsInspectorModal({user, onClose}) {
    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div className="flex items-center gap-3">
                        <div
                            className="w-8 h-8 rounded-xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-xs border border-primary-200 shrink-0 overflow-hidden">
                            {user.pictureUrl ? (
                                <img src={user.pictureUrl} alt={user.username} className="w-full h-full object-cover"/>
                            ) : (
                                <span>{(user.name || user.username || 'U')[0].toUpperCase()}</span>
                            )}
                        </div>
                        <div>
                            <h3 className="font-bold text-slate-900 text-sm">Active User Sessions</h3>
                            <p className="text-[11px] text-slate-400">@{user.username}</p>
                        </div>
                    </div>
                    <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-600 rounded-lg">
                        <X className="w-4 h-4"/>
                    </button>
                </div>

                <div className="p-4 space-y-3">
                    {STATIC_USER_SESSIONS.map((sess) => (
                        <div key={sess.id}
                             className="p-3 bg-slate-50 rounded-xl border border-slate-200 text-xs space-y-1">
                            <div className="flex items-center justify-between font-semibold text-slate-900">
                                <span>{sess.device}</span>
                                {sess.isCurrent && (
                                    <span
                                        className="text-[10px] bg-emerald-100 text-emerald-800 px-1.5 py-0.5 rounded font-bold">
                                        CURRENT
                                    </span>
                                )}
                            </div>
                            <div className="text-slate-500 flex items-center justify-between">
                                <span>{sess.ipAddress} • {sess.location}</span>
                                <button
                                    onClick={() => alert(`Terminated session ${sess.id}`)}
                                    className="text-red-600 hover:underline font-semibold"
                                >
                                    Revoke
                                </button>
                            </div>
                        </div>
                    ))}
                </div>

                <div className="p-3 border-t border-slate-100 flex justify-end bg-slate-50">
                    <button
                        onClick={onClose}
                        className="px-3.5 py-1.5 bg-slate-200 text-slate-700 rounded-xl hover:bg-slate-300 text-xs font-semibold"
                    >
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
}

// REAL REDIS USER LOGIN HISTORY INSPECTOR
function UserLoginHistoryInspectorModal({user, onClose}) {
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        let isMounted = true;
        adminService.getUserLoginHistory(user.uuid)
            .then((data) => {
                if (isMounted) setHistory(data || []);
            })
            .catch((err) => {
                console.error('Failed to load user login history:', err);
                if (isMounted) setHistory([]);
            })
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, [user.uuid]);

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div className="flex items-center gap-3">
                        <div
                            className="w-8 h-8 rounded-xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-xs border border-primary-200 shrink-0 overflow-hidden">
                            {user.pictureUrl ? (
                                <img src={user.pictureUrl} alt={user.username} className="w-full h-full object-cover"/>
                            ) : (
                                <span>{(user.name || user.username || 'U')[0].toUpperCase()}</span>
                            )}
                        </div>
                        <div>
                            <h3 className="font-bold text-slate-900 text-sm">User Login History & Security</h3>
                            <p className="text-[11px] text-slate-400">@{user.username}</p>
                        </div>
                    </div>
                    <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-600 rounded-lg">
                        <X className="w-4 h-4"/>
                    </button>
                </div>

                <div className="p-4 space-y-2.5 max-h-72 overflow-y-auto">
                    {loading ? (
                        <div className="py-8 text-center text-slate-400 text-xs flex items-center justify-center gap-2">
                            <Loader2 className="w-4 h-4 animate-spin"/>
                            <span>Loading real login logs...</span>
                        </div>
                    ) : history.length === 0 ? (
                        <div className="py-8 text-center text-slate-400 text-xs">
                            No login activity recorded yet for this user.
                        </div>
                    ) : (
                        history.map((item) => (
                            <div key={item.id}
                                 className="p-2.5 rounded-xl border border-slate-200 text-xs flex items-center justify-between">
                                <div>
                                    <div className="font-semibold text-slate-800">{item.device}</div>
                                    <div className="text-[11px] text-slate-400 font-mono">
                                        {item.ipAddress} • {item.reason || 'Normal sign in'}
                                    </div>
                                    <div className="text-[10px] text-slate-400 mt-0.5">
                                        {new Date(item.timestamp).toLocaleString()}
                                    </div>
                                </div>
                                <span
                                    className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${
                                        item.status === 'SUCCESS'
                                            ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                                            : 'bg-red-50 text-red-700 border border-red-200'
                                    }`}
                                >
                                    {item.status}
                                </span>
                            </div>
                        ))
                    )}
                </div>

                <div className="p-3 border-t border-slate-100 flex justify-end bg-slate-50">
                    <button
                        onClick={onClose}
                        className="px-3.5 py-1.5 bg-slate-200 text-slate-700 rounded-xl hover:bg-slate-300 text-xs font-semibold"
                    >
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
}