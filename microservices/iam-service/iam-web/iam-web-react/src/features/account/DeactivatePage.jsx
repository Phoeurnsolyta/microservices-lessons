import {useState} from 'react';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {useNavigate} from 'react-router-dom';
import {AlertTriangle, Lock, ShieldAlert, Loader2} from 'lucide-react';

export default function DeactivatePage() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const logout = useAuthStore((state) => state.logout);

    const [password, setPassword] = useState('');
    const [confirmed, setConfirmed] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleDeactivate = async (e) => {
        e.preventDefault();
        if (!confirmed) {
            setError('Please acknowledge the terms before proceeding.');
            return;
        }

        setLoading(true);
        setError('');

        try {
            // Deactivate workflow
            await new Promise((res) => setTimeout(res, 1000));
            alert('Your account deactivation request has been processed.');
            logout();
            navigate('/login');
        } catch (err) {
            setError(err.message || 'Deactivation failed.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl space-y-6">
            <div>
                <h1 className="text-xl font-bold text-red-700 flex items-center gap-2">
                    <AlertTriangle className="w-5 h-5 text-red-600"/>
                    {t('account.deactivateTitle')}
                </h1>
                <p className="text-xs text-slate-500 mt-0.5">{t('account.deactivateSubtitle')}</p>
            </div>

            <div className="bg-red-50 border border-red-200 rounded-2xl p-5 text-xs text-red-800 space-y-2">
                <div className="font-bold flex items-center gap-2 text-sm text-red-900">
                    <ShieldAlert className="w-4 h-4"/>
                    Warning: Irreversible Account Status Change
                </div>
                <p>{t('account.deactivateWarning')}</p>
            </div>

            {error && (
                <div className="p-3 bg-red-100 border border-red-300 text-red-800 text-xs rounded-xl">
                    {error}
                </div>
            )}

            <form onSubmit={handleDeactivate}
                  className="bg-white rounded-2xl border border-slate-200 p-6 space-y-4 shadow-sm">
                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        Confirm Your Password
                    </label>
                    <div className="relative">
                        <Lock className="w-4 h-4 text-slate-400 absolute left-3 top-3"/>
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full pl-9 pr-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-red-500"
                            placeholder="••••••••"
                        />
                    </div>
                </div>

                <div className="flex items-start gap-2.5 pt-1">
                    <input
                        type="checkbox"
                        id="confirm-deactivate"
                        checked={confirmed}
                        onChange={(e) => setConfirmed(e.target.checked)}
                        className="mt-0.5 rounded text-red-600 focus:ring-red-500"
                    />
                    <label htmlFor="confirm-deactivate" className="text-xs text-slate-600">
                        I understand that deactivating my account will immediately log me out and require an
                        administrator to restore access.
                    </label>
                </div>

                <div className="pt-3">
                    <button
                        type="submit"
                        disabled={loading || !confirmed}
                        className="w-full py-2.5 px-4 rounded-xl bg-red-600 hover:bg-red-700 text-white text-xs font-bold shadow-sm transition flex items-center justify-center gap-2 disabled:opacity-40"
                    >
                        {loading ? <Loader2 className="w-4 h-4 animate-spin"/> : <AlertTriangle className="w-4 h-4"/>}
                        <span>Request Account Deactivation</span>
                    </button>
                </div>
            </form>
        </div>
    );
}