import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {userService} from '../../services/userService';
import ConfirmModal from '../../components/common/ConfirmModal';
import {Link2, Unlink, CheckCircle2, AlertCircle, Loader2} from 'lucide-react';

export default function LinkedAccountsPage() {
    const {t} = useTranslation();
    const [identities, setIdentities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [unlinkModalIdentity, setUnlinkModalIdentity] = useState(null);
    const [unlinking, setUnlinking] = useState(false);

    const providers = [
        {
            key: 'google',
            name: 'Google',
            authUrl: 'http://localhost:9000/oauth2/authorization/google',
            icon: (
                <svg className="w-5 h-5" viewBox="0 0 24 24">
                    <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                    <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                    <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
                    <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
                </svg>
            ),
        },
        {
            key: 'github',
            name: 'GitHub',
            authUrl: 'http://localhost:9000/oauth2/authorization/github',
            icon: (
                <svg className="w-5 h-5 fill-current text-slate-800" viewBox="0 0 24 24">
                    <path fillRule="evenodd" clipRule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"/>
                </svg>
            ),
        },
        {
            key: 'facebook',
            name: 'Facebook',
            authUrl: 'http://localhost:9000/oauth2/authorization/facebook',
            icon: (
                <svg className="w-5 h-5 fill-current text-[#1877F2]" viewBox="0 0 24 24">
                    <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                </svg>
            ),
        },
        {
            key: 'telegram',
            name: 'Telegram',
            authUrl: 'http://localhost:9000/login/telegram',
            icon: (
                <svg className="w-5 h-5 fill-current text-[#229ED9]" viewBox="0 0 24 24">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm4.64 6.8c-.15 1.58-.8 5.42-1.13 7.19-.14.75-.42 1-.68 1.03-.58.05-1.02-.38-1.58-.75-.88-.58-1.38-.94-2.23-1.5-.99-.65-.35-1.01.22-1.59.15-.15 2.71-2.48 2.76-2.69a.2.2 0 00-.05-.18c-.06-.05-.14-.03-.21-.02-.09.02-1.49.95-4.22 2.79-.4.27-.76.41-1.08.4-.36-.01-1.04-.2-1.55-.37-.63-.2-1.12-.31-1.08-.66.02-.18.27-.36.74-.55 2.92-1.27 4.86-2.11 5.83-2.51 2.78-1.16 3.35-1.36 3.73-1.36.08 0 .27.02.39.12.1.08.13.19.14.27-.01.06.01.24 0 .38z"/>
                </svg>
            ),
        },
    ];

    const reloadIdentities = () => {
        userService.getIdentities()
            .then((data) => setIdentities(data || []))
            .catch(() => setIdentities([]));
    };

    useEffect(() => {
        let isMounted = true;
        userService.getIdentities()
            .then((data) => {
                if (isMounted) setIdentities(data || []);
            })
            .catch(() => {
                if (isMounted) setIdentities([]);
            })
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, []);

    const confirmUnlink = async () => {
        if (!unlinkModalIdentity) return;
        setUnlinking(true);
        try {
            await userService.unlinkIdentity(unlinkModalIdentity.uuid);
            setSuccess(`Successfully unlinked ${unlinkModalIdentity.provider || 'account'}.`);
            setUnlinkModalIdentity(null);
            reloadIdentities();
            setTimeout(() => setSuccess(''), 3500);
        } catch (err) {
            setError(err.message || 'Cannot unlink the only authentication method.');
            setUnlinkModalIdentity(null);
            setTimeout(() => setError(''), 4000);
        } finally {
            setUnlinking(false);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center py-24 text-slate-400 gap-2">
                <Loader2 className="w-5 h-5 animate-spin"/>
                <span className="text-sm font-medium">{t('common.loading')}</span>
            </div>
        );
    }

    return (
        <div className="max-w-3xl space-y-6">
            <div>
                <h1 className="text-xl font-bold text-slate-900">{t('account.linkedTitle')}</h1>
                <p className="text-xs text-slate-500 mt-0.5">{t('account.linkedSubtitle')}</p>
            </div>

            {success && (
                <div
                    className="p-3.5 bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs rounded-2xl flex items-center gap-2">
                    <CheckCircle2 className="w-4 h-4 shrink-0"/>
                    <span>{success}</span>
                </div>
            )}

            {error && (
                <div
                    className="p-3.5 bg-red-50 border border-red-200 text-red-700 text-xs rounded-2xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0"/>
                    <span>{error}</span>
                </div>
            )}

            <div
                className="bg-white rounded-3xl border border-slate-200 divide-y divide-slate-100 shadow-sm overflow-hidden">
                {providers.map((p) => {
                    const linked = identities.find((id) => id.provider?.toLowerCase() === p.key);
                    return (
                        <div key={p.key} className="p-5 flex items-center justify-between">
                            <div className="flex items-center gap-4">
                                <div
                                    className="w-11 h-11 rounded-2xl bg-[#F8FAFC] border border-slate-200/80 flex items-center justify-center shadow-2xs">
                                    {p.icon}
                                </div>
                                <div>
                                    <div className="text-sm font-bold text-slate-800">{p.name}</div>
                                    <div className="text-xs text-slate-400 mt-0.5">
                                        {linked ? (
                                            <span className="text-[#4E6B56] font-semibold flex items-center gap-1.5">
                                                <span className="w-1.5 h-1.5 rounded-full bg-emerald-500"></span>
                                                Connected as {linked.displayName || linked.email || 'Linked'}
                                            </span>
                                        ) : (
                                            'Not connected'
                                        )}
                                    </div>
                                </div>
                            </div>

                            {linked ? (
                                <button
                                    type="button"
                                    onClick={() => setUnlinkModalIdentity(linked)}
                                    className="px-3.5 py-1.5 text-xs font-bold text-red-600 border border-red-200 rounded-xl hover:bg-red-50 transition flex items-center gap-1.5 cursor-pointer"
                                >
                                    <Unlink className="w-3.5 h-3.5"/>
                                    <span>{t('account.unlink')}</span>
                                </button>
                            ) : (
                                <a
                                    href={p.authUrl}
                                    className="px-3.5 py-1.5 text-xs font-bold text-[#4E6B56] border border-[#D0E2D6] bg-[#EBF3EE] hover:bg-[#dceade] rounded-xl transition flex items-center gap-1.5"
                                >
                                    <Link2 className="w-3.5 h-3.5"/>
                                    <span>{t('account.link')}</span>
                                </a>
                            )}
                        </div>
                    );
                })}
            </div>

            {/* In-App Confirmation Modal */}
            <ConfirmModal
                isOpen={!!unlinkModalIdentity}
                title="Disconnect Account"
                message={`Are you sure you want to disconnect your ${unlinkModalIdentity?.provider || 'social'} account? You can reconnect it at any time.`}
                confirmText={unlinking ? 'Disconnecting...' : 'Disconnect'}
                cancelText="Keep Connected"
                variant="danger"
                onConfirm={confirmUnlink}
                onClose={() => setUnlinkModalIdentity(null)}
            />
        </div>
    );
}