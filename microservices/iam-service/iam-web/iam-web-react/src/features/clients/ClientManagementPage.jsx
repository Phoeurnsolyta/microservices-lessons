import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {adminService} from '../../services/adminService';
import {
    Search,
    Plus,
    Trash2,
    Eye,
    EyeOff,
    Copy,
    Check,
    Globe,
    Key,
    AlertCircle,
    CheckCircle2,
    Loader2,
    X,
} from 'lucide-react';

export default function ClientManagementPage() {
    const {t} = useTranslation();
    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [revealedSecrets, setRevealedSecrets] = useState(new Set());
    const [copiedId, setCopiedId] = useState(null);
    const [createModalOpen, setCreateModalOpen] = useState(false);
    const [notification, setNotification] = useState(null);

    const showNotification = (type, message) => {
        setNotification({type, message});
        setTimeout(() => setNotification(null), 3500);
    };

    const refreshClients = () => {
        adminService.getClients()
            .then((data) => setClients(data || []))
            .catch((err) => showNotification('error', err.message || 'Failed to fetch clients'));
    };

    useEffect(() => {
        let isMounted = true;
        // Clean out any old mock data from previous runs
        localStorage.removeItem('istad_clients');

        adminService.getClients()
            .then((data) => {
                if (isMounted) setClients(data || []);
            })
            .catch((err) => {
                if (isMounted) showNotification('error', err.message || 'Failed to load clients');
            })
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, []);

    const toggleRevealSecret = (id) => {
        const next = new Set(revealedSecrets);
        if (next.has(id)) {
            next.delete(id);
        } else {
            next.add(id);
        }
        setRevealedSecrets(next);
    };

    const handleCopy = (text, id) => {
        navigator.clipboard.writeText(text);
        setCopiedId(id);
        setTimeout(() => setCopiedId(null), 2000);
    };

    const handleDeleteClient = async (client) => {
        if (!window.confirm(`Are you sure you want to delete client [${client.clientName}]?`)) return;
        try {
            await adminService.deleteClient(client.id);
            showNotification('success', `Client [${client.clientName}] removed.`);
            refreshClients();
        } catch (err) {
            showNotification('error', err.message);
        }
    };

    const filteredClients = clients.filter(
        (c) =>
            (c.clientName && c.clientName.toLowerCase().includes(search.toLowerCase())) ||
            (c.clientId && c.clientId.toLowerCase().includes(search.toLowerCase()))
    );

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('nav.clients')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Administer OAuth 2.1 & OpenID Connect registered client applications and credentials
                    </p>
                </div>
                <button
                    onClick={() => setCreateModalOpen(true)}
                    className="flex items-center gap-1.5 px-3.5 py-2 bg-primary-600 hover:bg-primary-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
                >
                    <Plus className="w-4 h-4"/>
                    <span>{t('management.registerClient')}</span>
                </button>
            </div>

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

            <div className="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm">
                <div className="relative">
                    <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5"/>
                    <input
                        type="text"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        placeholder="Search clients by ID or application name..."
                        className="w-full pl-9 pr-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                </div>
            </div>

            {loading ? (
                <div className="py-24 text-center text-slate-400 text-xs">
                    <Loader2 className="w-5 h-5 animate-spin mx-auto mb-2"/>
                    <span>{t('common.loading')}</span>
                </div>
            ) : filteredClients.length === 0 ? (
                <div className="bg-white rounded-2xl border border-slate-200 p-12 text-center text-slate-400 text-xs">
                    No registered OAuth2 clients found in the database.
                </div>
            ) : (
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
                    {filteredClients.map((client) => {
                        const isRevealed = revealedSecrets.has(client.id);
                        return (
                            <div
                                key={client.id}
                                className="bg-white rounded-2xl border border-slate-200 p-5 shadow-sm space-y-4 hover:border-primary-200 transition"
                            >
                                <div className="flex items-start justify-between">
                                    <div>
                                        <h3 className="font-bold text-sm text-slate-900">{client.clientName}</h3>
                                        <div className="font-mono text-xs text-primary-700 mt-0.5">
                                            client_id: <strong>{client.clientId}</strong>
                                        </div>
                                    </div>
                                    <button
                                        onClick={() => handleDeleteClient(client)}
                                        className="p-1.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition"
                                        title="Delete Client"
                                    >
                                        <Trash2 className="w-4 h-4"/>
                                    </button>
                                </div>

                                <div
                                    className="p-2.5 bg-slate-50 border border-slate-100 rounded-xl flex items-center justify-between text-xs font-mono">
                                    <div className="flex items-center gap-2 truncate">
                                        <Key className="w-3.5 h-3.5 text-slate-400 shrink-0"/>
                                        <span className="text-slate-600 truncate">
                      {isRevealed ? client.clientSecret : '••••••••••••••••••••••••••••••••'}
                    </span>
                                    </div>
                                    <div className="flex items-center gap-1 shrink-0 ml-2">
                                        <button
                                            onClick={() => toggleRevealSecret(client.id)}
                                            className="p-1 text-slate-500 hover:text-slate-700 rounded transition"
                                            title={isRevealed ? 'Hide Secret' : 'Reveal Secret'}
                                        >
                                            {isRevealed ? <EyeOff className="w-3.5 h-3.5"/> :
                                                <Eye className="w-3.5 h-3.5"/>}
                                        </button>
                                        <button
                                            onClick={() => handleCopy(client.clientSecret, client.id)}
                                            className="p-1 text-slate-500 hover:text-slate-700 rounded transition"
                                            title="Copy Secret"
                                        >
                                            {copiedId === client.id ?
                                                <Check className="w-3.5 h-3.5 text-emerald-600"/> :
                                                <Copy className="w-3.5 h-3.5"/>}
                                        </button>
                                    </div>
                                </div>

                                {/* Grant Types & Scopes */}
                                <div className="space-y-2 text-xs">
                                    <div>
                    <span className="font-semibold text-slate-500 text-[11px] uppercase tracking-wider block mb-1">
                      Grant Types
                    </span>
                                        <div className="flex flex-wrap gap-1.5">
                                            {(client.grantTypes || []).map((gt) => (
                                                <span key={gt}
                                                      className="px-2 py-0.5 bg-slate-100 text-slate-700 rounded-md font-mono text-[10px]">
                          {gt}
                        </span>
                                            ))}
                                        </div>
                                    </div>

                                    <div>
                    <span className="font-semibold text-slate-500 text-[11px] uppercase tracking-wider block mb-1">
                      Scopes
                    </span>
                                        <div className="flex flex-wrap gap-1.5">
                                            {(client.scopes || []).map((s) => (
                                                <span key={s}
                                                      className="px-2 py-0.5 bg-primary-50 text-primary-700 rounded-md font-mono text-[10px] font-semibold">
                          {s}
                        </span>
                                            ))}
                                        </div>
                                    </div>

                                    <div>
                    <span className="font-semibold text-slate-500 text-[11px] uppercase tracking-wider block mb-1">
                      Redirect URIs
                    </span>
                                        <div className="space-y-1">
                                            {(client.redirectUris || []).map((uri) => (
                                                <div key={uri}
                                                     className="flex items-center gap-1.5 text-slate-600 font-mono text-[11px] truncate">
                                                    <Globe className="w-3 h-3 text-slate-400 shrink-0"/>
                                                    <span className="truncate">{uri}</span>
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}

            {createModalOpen && (
                <RegisterClientModal
                    onClose={() => setCreateModalOpen(false)}
                    onSuccess={() => {
                        setCreateModalOpen(false);
                        showNotification('success', 'OAuth2 client registered directly in database.');
                        refreshClients();
                    }}
                />
            )}
        </div>
    );
}

function RegisterClientModal({onClose, onSuccess}) {
    const [clientName, setClientName] = useState('');
    const [clientId, setClientId] = useState('');
    const [clientSecret, setClientSecret] = useState('');
    const [redirectUris, setRedirectUris] = useState('http://localhost:3000/login/oauth2/code/custom');
    const [grantTypes, setGrantTypes] = useState(['authorization_code', 'refresh_token']);
    const [scopes, setScopes] = useState(['openid', 'profile', 'email']);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const generateRandomSecret = () => {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_#@';
        let res = '';
        for (let i = 0; i < 32; i++) {
            res += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        setClientSecret(res);
    };

    const toggleGrant = (gt) => {
        if (grantTypes.includes(gt)) {
            setGrantTypes(grantTypes.filter((g) => g !== gt));
        } else {
            setGrantTypes([...grantTypes, gt]);
        }
    };

    const toggleScope = (sc) => {
        if (scopes.includes(sc)) {
            setScopes(scopes.filter((s) => s !== sc));
        } else {
            setScopes([...scopes, sc]);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!clientSecret) {
            setError('Please generate or provide a client secret');
            return;
        }
        setLoading(true);
        setError('');

        const uris = redirectUris.split(',').map((u) => u.trim()).filter(Boolean);

        try {
            await adminService.registerClient({
                clientId: clientId.trim(),
                clientName: clientName.trim(),
                clientSecret,
                grantTypes,
                redirectUris: uris,
                scopes,
            });
            onSuccess();
        } catch (err) {
            setError(err.response?.data?.message || err.message || 'Failed to register client');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl max-w-lg w-full border border-slate-200 shadow-xl overflow-hidden">
                <div className="flex items-center justify-between p-4 border-b border-slate-100">
                    <h3 className="font-bold text-slate-900 text-sm">Register OAuth2 / OIDC Client</h3>
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

                <form onSubmit={handleSubmit} className="p-4 space-y-3 text-xs max-h-[75vh] overflow-y-auto">
                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Application Name *</label>
                        <input
                            type="text"
                            required
                            value={clientName}
                            onChange={(e) => setClientName(e.target.value)}
                            placeholder="e.g. Mobile Application, LMS Frontend"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Client ID *</label>
                        <input
                            type="text"
                            required
                            value={clientId}
                            onChange={(e) => setClientId(e.target.value)}
                            placeholder="e.g. istad-mobile-client"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500 font-mono"
                        />
                    </div>

                    <div>
                        <div className="flex items-center justify-between mb-1">
                            <label className="font-semibold text-slate-700">Client Secret *</label>
                            <button
                                type="button"
                                onClick={generateRandomSecret}
                                className="text-primary-600 hover:underline font-semibold"
                            >
                                Generate Key
                            </button>
                        </div>
                        <input
                            type="text"
                            required
                            value={clientSecret}
                            onChange={(e) => setClientSecret(e.target.value)}
                            placeholder="Click 'Generate Key' or enter a strong secret"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500 font-mono text-slate-700"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1">Redirect URIs
                            (comma-separated)</label>
                        <input
                            type="text"
                            required
                            value={redirectUris}
                            onChange={(e) => setRedirectUris(e.target.value)}
                            placeholder="http://localhost:3000/callback, https://app.example.com/oauth"
                            className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500 font-mono"
                        />
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1.5">Grant Types</label>
                        <div className="grid grid-cols-2 gap-2">
                            {['authorization_code', 'refresh_token', 'client_credentials'].map((gt) => (
                                <label key={gt}
                                       className="flex items-center gap-2 cursor-pointer font-mono text-[11px] text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={grantTypes.includes(gt)}
                                        onChange={() => toggleGrant(gt)}
                                        className="rounded text-primary-600 focus:ring-primary-500"
                                    />
                                    <span>{gt}</span>
                                </label>
                            ))}
                        </div>
                    </div>

                    <div>
                        <label className="font-semibold text-slate-700 block mb-1.5">Scopes</label>
                        <div className="grid grid-cols-2 gap-2">
                            {['openid', 'profile', 'email', 'offline_access'].map((sc) => (
                                <label key={sc}
                                       className="flex items-center gap-2 cursor-pointer font-mono text-[11px] text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={scopes.includes(sc)}
                                        onChange={() => toggleScope(sc)}
                                        className="rounded text-primary-600 focus:ring-primary-500"
                                    />
                                    <span>{sc}</span>
                                </label>
                            ))}
                        </div>
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
                            {loading ? 'Registering...' : 'Register Client'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}