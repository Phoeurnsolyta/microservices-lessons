import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {adminService} from '../../services/adminService';
import {
    Search,
    Plus,
    Trash2,
    Edit2,
    CheckCircle,
    XCircle,
    AlertCircle,
    CheckCircle2,
    Loader2,
    X,
    Lock,
} from 'lucide-react';

export default function PermissionManagementPage() {
    const {t} = useTranslation();
    const [permissions, setPermissions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [resourceFilter, setResourceFilter] = useState('');
    const [notification, setNotification] = useState(null);

    const [createModalOpen, setCreateModalOpen] = useState(false);
    const [editModalPerm, setEditModalPerm] = useState(null);

    const showNotification = (type, message) => {
        setNotification({type, message});
        setTimeout(() => setNotification(null), 3500);
    };

    const refreshPermissions = () => {
        adminService.getPermissions({
            search,
            resource: resourceFilter,
            size: 100,
        })
            .then((data) => {
                setPermissions(data.content || []);
            })
            .catch((err) => {
                showNotification('error', err.message || 'Failed to fetch permissions');
            });
    };

    useEffect(() => {
        let isMounted = true;
        adminService.getPermissions({
            search,
            resource: resourceFilter,
            size: 100,
        })
            .then((data) => {
                if (isMounted) setPermissions(data.content || []);
            })
            .catch((err) => {
                if (isMounted) showNotification('error', err.message || 'Failed to load permissions');
            })
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, [search, resourceFilter]);

    const handleToggleActive = async (perm) => {
        if (perm.isSystem) {
            showNotification('error', 'System permissions are immutable and cannot be toggled.');
            return;
        }
        try {
            if (perm.active) {
                await adminService.deactivatePermission(perm.uuid);
                showNotification('success', `Permission [${perm.name}] deactivated.`);
            } else {
                await adminService.activatePermission(perm.uuid);
                showNotification('success', `Permission [${perm.name}] activated.`);
            }
            refreshPermissions();
        } catch (err) {
            showNotification('error', err.message);
        }
    };

    const handleDeletePermission = async (perm) => {
        if (perm.isSystem) {
            showNotification('error', 'System permissions cannot be deleted.');
            return;
        }
        if (!window.confirm(`Are you sure you want to delete permission [${perm.name}]?`)) return;
        try {
            await adminService.deletePermission(perm.uuid);
            showNotification('success', `Permission [${perm.name}] deleted.`);
            refreshPermissions();
        } catch (err) {
            showNotification('error', err.message);
        }
    };

    const resources = ['ALL', 'user', 'role', 'permission', 'client'];

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('nav.permissions')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Resource-based access authority catalog and security boundaries
                    </p>
                </div>
                <button
                    onClick={() => setCreateModalOpen(true)}
                    className="flex items-center gap-1.5 px-3.5 py-2 bg-primary-600 hover:bg-primary-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
                >
                    <Plus className="w-4 h-4"/>
                    <span>{t('management.createPermission')}</span>
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

            {/* Resource Pill Filters */}
            <div className="flex items-center gap-2 overflow-x-auto pb-1">
                {resources.map((res) => {
                    const isSelected = res === 'ALL' ? resourceFilter === '' : resourceFilter === res;
                    return (
                        <button
                            key={res}
                            onClick={() => setResourceFilter(res === 'ALL' ? '' : res)}
                            className={`px-3 py-1.5 rounded-xl text-xs font-semibold transition ${
                                isSelected
                                    ? 'bg-primary-600 text-white shadow-xs'
                                    : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'
                            }`}
                        >
                            {res === 'ALL' ? 'All Resources' : res.toUpperCase()}
                        </button>
                    );
                })}
            </div>

            {/* Search Toolbar */}
            <div className="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm">
                <div className="relative">
                    <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5"/>
                    <input
                        type="text"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        placeholder="Search permissions by name or description..."
                        className="w-full pl-9 pr-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                </div>
            </div>

            {/* Table */}
            <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full text-left text-xs">
                        <thead
                            className="bg-slate-50 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
                        <tr>
                            <th className="py-3 px-4">Authority Name</th>
                            <th className="py-3 px-4">Resource</th>
                            <th className="py-3 px-4">Action</th>
                            <th className="py-3 px-4">Description</th>
                            <th className="py-3 px-4">Status</th>
                            <th className="py-3 px-4 text-right">Actions</th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-100 text-slate-700">
                        {loading ? (
                            <tr>
                                <td colSpan="6" className="py-12 text-center text-slate-400">
                                    <Loader2 className="w-5 h-5 animate-spin mx-auto mb-2"/>
                                    <span>{t('common.loading')}</span>
                                </td>
                            </tr>
                        ) : permissions.length === 0 ? (
                            <tr>
                                <td colSpan="6" className="py-12 text-center text-slate-400">
                                    No permissions match your filter.
                                </td>
                            </tr>
                        ) : (
                            permissions.map((perm) => (
                                <tr key={perm.uuid} className="hover:bg-slate-50/70 transition">
                                    <td className="py-3 px-4 font-mono font-bold text-slate-900 flex items-center gap-1.5">
                                        <span>{perm.name}</span>
                                        {perm.isSystem && (
                                            <span title="System Authority" className="p-0.5 text-slate-400">
                          <Lock className="w-3 h-3"/>
                        </span>
                                        )}
                                    </td>
                                    <td className="py-3 px-4">
                      <span className="px-2 py-0.5 bg-slate-100 text-slate-700 rounded-md font-semibold text-[11px]">
                        {perm.resource}
                      </span>
                                    </td>
                                    <td className="py-3 px-4">
                      <span className="px-2 py-0.5 bg-primary-50 text-primary-700 rounded-md font-semibold text-[11px]">
                        {perm.action}
                      </span>
                                    </td>
                                    <td className="py-3 px-4 text-slate-500 max-w-xs truncate">
                                        {perm.description || '-'}
                                    </td>
                                    <td className="py-3 px-4">
                      <span
                          className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold ${
                              perm.active
                                  ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                                  : 'bg-slate-100 text-slate-500 border border-slate-200'
                          }`}
                      >
                        {perm.active ? <CheckCircle className="w-3 h-3"/> : <XCircle className="w-3 h-3"/>}
                          {perm.active ? 'Active' : 'Inactive'}
                      </span>
                                    </td>
                                    <td className="py-3 px-4 text-right">
                                        <div className="flex items-center justify-end gap-1">
                                            <button
                                                title="Edit Description"
                                                onClick={() => setEditModalPerm(perm)}
                                                className="p-1.5 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-lg transition"
                                            >
                                                <Edit2 className="w-3.5 h-3.5"/>
                                            </button>
                                            <button
                                                title={perm.active ? 'Deactivate' : 'Activate'}
                                                disabled={perm.isSystem}
                                                onClick={() => handleToggleActive(perm)}
                                                className="p-1.5 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-lg transition disabled:opacity-30"
                                            >
                                                {perm.active ? <XCircle className="w-3.5 h-3.5"/> :
                                                    <CheckCircle className="w-3.5 h-3.5 text-emerald-600"/>}
                                            </button>
                                            <button
                                                title="Delete Permission"
                                                disabled={perm.isSystem}
                                                onClick={() => handleDeletePermission(perm)}
                                                className="p-1.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition disabled:opacity-30"
                                            >
                                                <Trash2 className="w-3.5 h-3.5"/>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>

            {createModalOpen && (
                <CreatePermissionModal
                    onClose={() => setCreateModalOpen(false)}
                    onSuccess={() => {
                        setCreateModalOpen(false);
                        showNotification('success', 'Permission created successfully.');
                        refreshPermissions();
                    }}
                />
            )}

            {editModalPerm && (
                <EditPermissionModal
                    perm={editModalPerm}
                    onClose={() => setEditModalPerm(null)}
                    onSuccess={() => {
                        setEditModalPerm(null);
                        showNotification('success', 'Permission updated successfully.');
                        refreshPermissions();
                    }}
                />
            )}
        </div>
    );
}

function CreatePermissionModal({onClose, onSuccess}) {
    const [resource, setResource] = useState('user');
    const [action, setAction] = useState('');
    const [description, setDescription] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        try {
            await adminService.createPermission({
                resource: resource.toLowerCase().trim(),
                action: action.toLowerCase().trim(),
                description: description.trim(),
            });
            onSuccess();
        } catch (err) {
            setError(err.message || 'Failed to create permission');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <h3 className="font-bold text-slate-900 text-sm">Create New Permission</h3>
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
                        <label className="font-semibold text-slate-700 block mb-1">Resource *</label>
                        <input
                            type="text"
                            required
                            value={resource}
                            onChange={(e) => setResource(e.target.value)}
                            placeholder="e.g. user, role, course, invoice"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500 font-mono"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Action *</label>
                        <input
                            type="text"
                            required
                            value={action}
                            onChange={(e) => setAction(e.target.value)}
                            placeholder="e.g. read, create, update, delete, export"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500 font-mono"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Description</label>
                        <textarea
                            rows={3}
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder="Describe access authority granted..."
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                        />
                    </div>

                    <div className="p-2.5 rounded-xl bg-slate-50 border border-slate-100 font-mono text-slate-600">
                        Canonical
                        Name: <strong>{resource ? resource.toLowerCase().trim() : 'resource'}:{action ? action.toLowerCase().trim() : 'action'}</strong>
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
                            {loading ? 'Creating...' : 'Create Permission'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

function EditPermissionModal({perm, onClose, onSuccess}) {
    const [description, setDescription] = useState(perm.description || '');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        try {
            await adminService.updatePermission(perm.uuid, {description});
            onSuccess();
        } catch (err) {
            setError(err.message || 'Failed to update permission');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-md w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <div>
                        <h3 className="font-bold text-slate-900 text-sm">Edit Permission</h3>
                        <p className="text-[11px] text-slate-400 font-mono">{perm.name}</p>
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