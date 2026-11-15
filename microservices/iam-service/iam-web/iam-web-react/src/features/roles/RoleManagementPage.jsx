import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {adminService} from '../../services/adminService';
import {
    Search,
    Plus,
    Shield,
    Trash2,
    Edit2,
    CheckCircle,
    XCircle,
    AlertCircle,
    CheckCircle2,
    Loader2,
    X,
    Check,
    Lock,
} from 'lucide-react';

export default function RoleManagementPage() {
    const {t} = useTranslation();
    const [roles, setRoles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [activeFilter, setActiveFilter] = useState('');
    const [notification, setNotification] = useState(null);

    const [createModalOpen, setCreateModalOpen] = useState(false);
    const [editModalRole, setEditModalRole] = useState(null);
    const [permMatrixRole, setPermMatrixRole] = useState(null);

    const showNotification = (type, message) => {
        setNotification({type, message});
        setTimeout(() => setNotification(null), 3500);
    };

    const refreshRoles = () => {
        adminService.getRoles({
            search,
            active: activeFilter,
            size: 50,
        })
            .then((data) => {
                setRoles(data.content || []);
            })
            .catch((err) => {
                showNotification('error', err.message || 'Failed to fetch roles');
            });
    };

    useEffect(() => {
        let isMounted = true;
        adminService.getRoles({
            search,
            active: activeFilter,
            size: 50,
        })
            .then((data) => {
                if (isMounted) setRoles(data.content || []);
            })
            .catch((err) => {
                if (isMounted) showNotification('error', err.message || 'Failed to load roles');
            })
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, [search, activeFilter]);

    const handleToggleActive = async (role) => {
        if (role.isSystem) {
            showNotification('error', 'System roles cannot be deactivated.');
            return;
        }
        try {
            if (role.active) {
                await adminService.deactivateRole(role.uuid);
                showNotification('success', `Role [${role.name}] deactivated.`);
            } else {
                await adminService.activateRole(role.uuid);
                showNotification('success', `Role [${role.name}] activated.`);
            }
            refreshRoles();
        } catch (err) {
            showNotification('error', err.message);
        }
    };

    const handleDeleteRole = async (role) => {
        if (role.isSystem) {
            showNotification('error', 'System roles are immutable and cannot be deleted.');
            return;
        }
        if (!window.confirm(`Are you sure you want to delete role [${role.name}]?`)) return;
        try {
            await adminService.deleteRole(role.uuid);
            showNotification('success', `Role [${role.name}] deleted.`);
            refreshRoles();
        } catch (err) {
            showNotification('error', err.message);
        }
    };

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('nav.roles')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Define role-based access control (RBAC), assign authority scopes, and guard sensitive resources
                    </p>
                </div>
                <button
                    onClick={() => setCreateModalOpen(true)}
                    className="flex items-center gap-1.5 px-3.5 py-2 bg-primary-600 hover:bg-primary-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
                >
                    <Plus className="w-4 h-4"/>
                    <span>{t('management.createRole')}</span>
                </button>
            </div>

            {/* Notification Toast */}
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

            {/* Filter & Search Bar */}
            <div
                className="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm flex flex-col sm:flex-row items-center gap-3">
                <div className="relative flex-1 w-full">
                    <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5"/>
                    <input
                        type="text"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        placeholder="Search roles by name or description..."
                        className="w-full pl-9 pr-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                </div>
                <select
                    value={activeFilter}
                    onChange={(e) => setActiveFilter(e.target.value)}
                    className="px-3 py-2 text-xs rounded-xl border border-slate-200 bg-white focus:outline-none focus:ring-2 focus:ring-primary-500 w-full sm:w-auto"
                >
                    <option value="">All Statuses</option>
                    <option value="true">Active Roles</option>
                    <option value="false">Inactive Roles</option>
                </select>
            </div>

            {/* Roles Cards Grid */}
            {loading ? (
                <div className="py-24 text-center text-slate-400 text-xs">
                    <Loader2 className="w-5 h-5 animate-spin mx-auto mb-2"/>
                    <span>{t('common.loading')}</span>
                </div>
            ) : roles.length === 0 ? (
                <div className="bg-white rounded-2xl border border-slate-200 p-12 text-center text-slate-400 text-xs">
                    No roles found matching your search.
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {roles.map((role) => (
                        <div
                            key={role.uuid}
                            className="bg-white rounded-2xl border border-slate-200 p-5 shadow-sm hover:border-primary-200 transition flex flex-col justify-between"
                        >
                            <div>
                                <div className="flex items-center justify-between mb-3">
                                    <div className="flex items-center gap-2">
                    <span className="font-bold text-sm text-slate-900 tracking-wide font-mono">
                      {role.name}
                    </span>
                                        {role.isSystem && (
                                            <span
                                                className="inline-flex items-center gap-1 text-[10px] bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full font-bold">
                        <Lock className="w-2.5 h-2.5"/>
                        SYSTEM
                      </span>
                                        )}
                                    </div>
                                    <span
                                        className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold ${
                                            role.active
                                                ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                                                : 'bg-slate-100 text-slate-500 border border-slate-200'
                                        }`}
                                    >
                    {role.active ? <CheckCircle className="w-3 h-3"/> : <XCircle className="w-3 h-3"/>}
                                        {role.active ? 'Active' : 'Inactive'}
                  </span>
                                </div>

                                <p className="text-xs text-slate-500 line-clamp-2 min-h-8">
                                    {role.description || 'No description provided for this role.'}
                                </p>
                            </div>

                            <div className="pt-4 mt-4 border-t border-slate-100 flex items-center justify-between">
                                <button
                                    onClick={() => setPermMatrixRole(role)}
                                    className="flex items-center gap-1.5 text-xs text-primary-600 hover:text-primary-800 font-semibold"
                                >
                                    <Shield className="w-3.5 h-3.5"/>
                                    <span>Configure Permissions</span>
                                </button>

                                <div className="flex items-center gap-1">
                                    <button
                                        title="Edit Description"
                                        onClick={() => setEditModalRole(role)}
                                        className="p-1.5 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-lg transition"
                                    >
                                        <Edit2 className="w-3.5 h-3.5"/>
                                    </button>
                                    <button
                                        title={role.active ? 'Deactivate' : 'Activate'}
                                        disabled={role.isSystem}
                                        onClick={() => handleToggleActive(role)}
                                        className="p-1.5 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-lg transition disabled:opacity-30"
                                    >
                                        {role.active ? <XCircle className="w-3.5 h-3.5"/> :
                                            <CheckCircle className="w-3.5 h-3.5 text-emerald-600"/>}
                                    </button>
                                    <button
                                        title="Delete Role"
                                        disabled={role.isSystem}
                                        onClick={() => handleDeleteRole(role)}
                                        className="p-1.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition disabled:opacity-30"
                                    >
                                        <Trash2 className="w-3.5 h-3.5"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {createModalOpen && (
                <CreateRoleModal
                    onClose={() => setCreateModalOpen(false)}
                    onSuccess={() => {
                        setCreateModalOpen(false);
                        showNotification('success', 'Role created successfully.');
                        refreshRoles();
                    }}
                />
            )}

            {editModalRole && (
                <EditRoleModal
                    role={editModalRole}
                    onClose={() => setEditModalRole(null)}
                    onSuccess={() => {
                        setEditModalRole(null);
                        showNotification('success', 'Role updated successfully.');
                        refreshRoles();
                    }}
                />
            )}

            {permMatrixRole && (
                <RolePermissionsMatrixModal
                    role={permMatrixRole}
                    onClose={() => setPermMatrixRole(null)}
                    onSuccess={() => {
                        setPermMatrixRole(null);
                        showNotification('success', `Permissions updated for role [${permMatrixRole.name}].`);
                        refreshRoles();
                    }}
                />
            )}
        </div>
    );
}

function CreateRoleModal({onClose, onSuccess}) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        try {
            await adminService.createRole({
                name: name.toUpperCase().trim(),
                description: description.trim(),
                permissionUuids: [],
            });
            onSuccess();
        } catch (err) {
            setError(err.message || 'Failed to create role');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <h3 className="font-bold text-slate-900 text-sm">Create New RBAC Role</h3>
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
                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Role Name *</label>
                        <input
                            type="text"
                            required
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="e.g. MANAGER, AUDITOR, INSTRUCTOR"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500 font-mono uppercase"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Description</label>
                        <textarea
                            rows={3}
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder="Describe access privileges granted by this role..."
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
                            {loading ? 'Creating...' : 'Create Role'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

function EditRoleModal({role, onClose, onSuccess}) {
    const [description, setDescription] = useState(role.description || '');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        try {
            await adminService.updateRole(role.uuid, {description});
            onSuccess();
        } catch (err) {
            setError(err.message || 'Failed to update role');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div>
                        <h3 className="font-bold text-slate-900 text-sm">Edit Role</h3>
                        <p className="text-[11px] text-slate-400 font-mono">[{role.name}]</p>
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

                <form onSubmit={handleSubmit} className="p-4 space-y-3 text-xs">
                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Description</label>
                        <textarea
                            rows={4}
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
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
                            {loading ? 'Saving...' : 'Save Changes'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

function RolePermissionsMatrixModal({role, onClose, onSuccess}) {
    const [allPermissions, setAllPermissions] = useState([]);
    const [selectedUuids, setSelectedUuids] = useState(new Set());
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        let isMounted = true;
        Promise.all([
            adminService.getPermissions({size: 100}),
            adminService.getRole(role.uuid),
        ])
            .then(([permsRes, roleDetail]) => {
                if (isMounted) {
                    setAllPermissions(permsRes.content || []);
                    const assigned = new Set((roleDetail.permissions || []).map((p) => p.uuid));
                    setSelectedUuids(assigned);
                }
            })
            .catch((err) => console.error(err))
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, [role.uuid]);

    const togglePermission = (uuid) => {
        const next = new Set(selectedUuids);
        if (next.has(uuid)) {
            next.delete(uuid);
        } else {
            next.add(uuid);
        }
        setSelectedUuids(next);
    };

    const handleSave = async () => {
        setSaving(true);
        try {
            await adminService.assignPermissionsToRole(role.uuid, {
                permissionUuids: Array.from(selectedUuids),
            });
            onSuccess();
        } catch (err) {
            alert(err.message || 'Failed to update role permissions');
        } finally {
            setSaving(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-lg w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div>
                        <h3 className="font-bold text-slate-900 text-sm">Role Authority Permissions</h3>
                        <p className="text-[11px] text-slate-400 font-mono">Role: [{role.name}]</p>
                    </div>
                    <button onClick={onClose} className="p-1 text-slate-400 hover:text-slate-600 rounded-lg">
                        <X className="w-4 h-4"/>
                    </button>
                </div>

                <div className="p-4 space-y-2 max-h-80 overflow-y-auto">
                    {loading ? (
                        <div className="py-8 text-center text-slate-400 text-xs">Loading permission catalog...</div>
                    ) : (
                        allPermissions.map((perm) => {
                            const checked = selectedUuids.has(perm.uuid);
                            return (
                                <div
                                    key={perm.uuid}
                                    onClick={() => togglePermission(perm.uuid)}
                                    className={`p-3 rounded-xl border cursor-pointer transition flex items-center justify-between text-xs ${
                                        checked
                                            ? 'bg-primary-50 border-primary-300 text-primary-900'
                                            : 'border-slate-100 hover:bg-slate-50 text-slate-700'
                                    }`}
                                >
                                    <div>
                                        <div className="font-mono font-bold">{perm.name}</div>
                                        <div
                                            className="text-[11px] text-slate-400 mt-0.5">{perm.description || `Resource: ${perm.resource}`}</div>
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
                        {saving ? 'Saving...' : 'Apply Permissions'}
                    </button>
                </div>
            </div>
        </div>
    );
}