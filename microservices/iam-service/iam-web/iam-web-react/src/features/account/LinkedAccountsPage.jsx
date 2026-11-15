import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {userService} from '../../services/userService';
import {Link2, Unlink, CheckCircle2, AlertCircle, Loader2} from 'lucide-react';

export default function LinkedAccountsPage() {
    const {t} = useTranslation();
    const [identities, setIdentities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const providers = [
        {key: 'google', name: 'Google', authUrl: 'http://localhost:9000/oauth2/authorization/google'},
        {key: 'github', name: 'GitHub', authUrl: 'http://localhost:9000/oauth2/authorization/github'},
        {key: 'facebook', name: 'Facebook', authUrl: 'http://localhost:9000/oauth2/authorization/facebook'},
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

    const handleUnlink = async (identityUuid) => {
        if (!window.confirm(t('account.unlinkConfirm'))) return;
        try {
            await userService.unlinkIdentity(identityUuid);
            setSuccess('Account unlinked successfully.');
            reloadIdentities();
            setTimeout(() => setSuccess(''), 3000);
        } catch (err) {
            setError(err.message || 'Cannot unlink the only login method.');
            setTimeout(() => setError(''), 4000);
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
                    className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs rounded-xl flex items-center gap-2">
                    <CheckCircle2 className="w-4 h-4 shrink-0"/>
                    <span>{success}</span>
                </div>
            )}

            {error && (
                <div
                    className="p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0"/>
                    <span>{error}</span>
                </div>
            )}

            <div
                className="bg-white rounded-2xl border border-slate-200 divide-y divide-slate-100 shadow-sm overflow-hidden">
                {providers.map((p) => {
                    const linked = identities.find((id) => id.provider?.toLowerCase() === p.key);
                    return (
                        <div key={p.key} className="p-5 flex items-center justify-between">
                            <div className="flex items-center gap-4">
                                <div
                                    className="w-10 h-10 rounded-xl bg-slate-100 flex items-center justify-center text-slate-700 font-bold text-sm">
                                    {p.name[0]}
                                </div>
                                <div>
                                    <div className="text-sm font-bold text-slate-800">{p.name}</div>
                                    <div className="text-xs text-slate-400">
                                        {linked ? (
                                            <span className="text-emerald-600 font-medium">
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
                                    onClick={() => handleUnlink(linked.uuid)}
                                    className="px-3 py-1.5 text-xs font-semibold text-red-600 border border-red-200 rounded-xl hover:bg-red-50 transition flex items-center gap-1.5"
                                >
                                    <Unlink className="w-3.5 h-3.5"/>
                                    <span>{t('account.unlink')}</span>
                                </button>
                            ) : (
                                <a
                                    href={p.authUrl}
                                    className="px-3 py-1.5 text-xs font-semibold text-primary-700 border border-primary-200 rounded-xl hover:bg-primary-50 transition flex items-center gap-1.5"
                                >
                                    <Link2 className="w-3.5 h-3.5"/>
                                    <span>{t('account.link')}</span>
                                </a>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
}