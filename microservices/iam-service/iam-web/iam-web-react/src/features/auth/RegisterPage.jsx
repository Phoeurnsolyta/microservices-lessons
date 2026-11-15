import {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {authService} from '../../services/authService';
import {AlertCircle, CheckCircle, ArrowRight} from 'lucide-react';

export default function RegisterPage() {
    const {t} = useTranslation();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        givenName: '',
        familyName: '',
        locale: 'en',
    });

    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData((prev) => ({...prev, [e.target.name]: e.target.value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await authService.register(formData);
            setSuccess(true);
            setTimeout(() => {
                navigate('/login');
            }, 2000);
        } catch (err) {
            setError(err.message || 'Registration failed. Please check your information.');
        } finally {
            setLoading(false);
        }
    };

    if (success) {
        return (
            <div className="text-center py-6">
                <CheckCircle className="w-12 h-12 text-green-500 mx-auto mb-3"/>
                <h3 className="text-lg font-bold text-slate-800">Account Created!</h3>
                <p className="text-xs text-slate-500 mt-1">Redirecting you to sign in...</p>
            </div>
        );
    }

    return (
        <div>
            <div className="mb-6 text-center">
                <h2 className="text-xl font-bold text-slate-800">{t('auth.registerTitle')}</h2>
                <p className="text-xs text-slate-500 mt-1">{t('auth.registerSubtitle')}</p>
            </div>

            {error && (
                <div
                    className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0"/>
                    <span>{error}</span>
                </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-3">
                <div className="grid grid-cols-2 gap-2">
                    <div>
                        <label className="block text-xs font-semibold text-slate-700 mb-1">
                            {t('auth.givenName')}
                        </label>
                        <input
                            type="text"
                            name="givenName"
                            required
                            value={formData.givenName}
                            onChange={handleChange}
                            className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            placeholder="Solyta"
                        />
                    </div>
                    <div>
                        <label className="block text-xs font-semibold text-slate-700 mb-1">
                            {t('auth.familyName')}
                        </label>
                        <input
                            type="text"
                            name="familyName"
                            required
                            value={formData.familyName}
                            onChange={handleChange}
                            className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            placeholder="Phoeurn"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.username')}
                    </label>
                    <input
                        type="text"
                        name="username"
                        required
                        value={formData.username}
                        onChange={handleChange}
                        className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                        placeholder="username"
                    />
                </div>

                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.email')}
                    </label>
                    <input
                        type="email"
                        name="email"
                        required
                        value={formData.email}
                        onChange={handleChange}
                        className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                        placeholder="you@example.com"
                    />
                </div>

                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.password')}
                    </label>
                    <input
                        type="password"
                        name="password"
                        required
                        value={formData.password}
                        onChange={handleChange}
                        className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                        placeholder="••••••••"
                    />
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full mt-4 py-2.5 px-4 rounded-xl bg-primary-600 hover:bg-primary-700 text-white text-sm font-semibold shadow-sm transition flex items-center justify-center gap-2 disabled:opacity-50"
                >
                    {loading ? t('common.loading') : (
                        <>
                            <span>{t('auth.registerNow')}</span>
                            <ArrowRight className="w-4 h-4"/>
                        </>
                    )}
                </button>
            </form>

            <div className="mt-6 text-center text-xs text-slate-500">
                <span>{t('auth.alreadyHaveAccount')} </span>
                <Link to="/login" className="font-semibold text-primary-600 hover:underline">
                    {t('auth.signIn')}
                </Link>
            </div>
        </div>
    );
}