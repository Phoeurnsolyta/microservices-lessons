import {useState} from 'react';
import {useTranslation} from 'react-i18next';
import {userService} from '../../services/userService';
import {ShieldCheck, Lock, CheckCircle2, AlertCircle, Key, Loader2} from 'lucide-react';

export default function SecurityPage() {
    const {t} = useTranslation();

    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    // Password Strength Calculation
    const getStrength = (pwd) => {
        let score = 0;
        if (pwd.length >= 8) score += 25;
        if (/[A-Z]/.test(pwd)) score += 25;
        if (/[0-9]/.test(pwd)) score += 25;
        if (/[^A-Za-z0-9]/.test(pwd)) score += 25;
        return score;
    };

    const strength = getStrength(newPassword);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (newPassword !== confirmPassword) {
            setError('New passwords do not match.');
            return;
        }

        if (newPassword.length < 8) {
            setError('Password must contain at least 8 characters.');
            return;
        }

        setLoading(true);
        try {
            await userService.changePassword({currentPassword, newPassword, confirmPassword});
            setSuccess('Your password has been changed successfully!');
            setCurrentPassword('');
            setNewPassword('');
            setConfirmPassword('');
            setTimeout(() => setSuccess(''), 4000);
        } catch (err) {
            setError(err.message || 'Failed to update password.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl space-y-6">
            <div>
                <h1 className="text-xl font-bold text-slate-900">{t('account.securityTitle')}</h1>
                <p className="text-xs text-slate-500 mt-0.5">{t('account.securitySubtitle')}</p>
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

            <form onSubmit={handleSubmit}
                  className="bg-white rounded-2xl border border-slate-200 p-6 space-y-5 shadow-sm">
                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.currentPassword')}
                    </label>
                    <div className="relative">
                        <Lock className="w-4 h-4 text-slate-400 absolute left-3 top-3"/>
                        <input
                            type="password"
                            required
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            className="w-full pl-9 pr-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            placeholder="••••••••"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.newPassword')}
                    </label>
                    <div className="relative">
                        <Key className="w-4 h-4 text-slate-400 absolute left-3 top-3"/>
                        <input
                            type="password"
                            required
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            className="w-full pl-9 pr-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            placeholder="••••••••"
                        />
                    </div>

                    {newPassword && (
                        <div className="mt-2 space-y-1">
                            <div className="h-1.5 w-full bg-slate-100 rounded-full overflow-hidden">
                                <div
                                    className={`h-full transition-all duration-300 ${
                                        strength <= 25
                                            ? 'bg-red-500 w-1/4'
                                            : strength <= 50
                                                ? 'bg-amber-500 w-2/4'
                                                : strength <= 75
                                                    ? 'bg-blue-500 w-3/4'
                                                    : 'bg-emerald-500 w-full'
                                    }`}
                                />
                            </div>
                            <span className="text-[10px] text-slate-400">
                {strength <= 25 ? 'Weak' : strength <= 50 ? 'Fair' : strength <= 75 ? 'Good' : 'Strong Password'}
              </span>
                        </div>
                    )}
                </div>

                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.confirmPassword')}
                    </label>
                    <div className="relative">
                        <Key className="w-4 h-4 text-slate-400 absolute left-3 top-3"/>
                        <input
                            type="password"
                            required
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            className="w-full pl-9 pr-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            placeholder="••••••••"
                        />
                    </div>
                </div>

                <div className="pt-2">
                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full py-2.5 px-4 rounded-xl bg-primary-600 hover:bg-primary-700 text-white text-xs font-bold shadow-sm transition flex items-center justify-center gap-2 disabled:opacity-50"
                    >
                        {loading ? <Loader2 className="w-4 h-4 animate-spin"/> : <ShieldCheck className="w-4 h-4"/>}
                        <span>{t('account.changePassword')}</span>
                    </button>
                </div>
            </form>
        </div>
    );
}