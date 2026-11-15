import {useState} from 'react';
import {Link, useNavigate, useLocation} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {authService} from '../../services/authService';
import {Lock, User, AlertCircle, ArrowRight, Sparkles} from 'lucide-react';

export default function LoginPage() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const location = useLocation();
    const setAuth = useAuthStore((state) => state.setAuth);

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const from = location.state?.from?.pathname || '/';

    const handleQuickFillAdmin = () => {
        setUsername('phoeurnsolyta');
        setPassword('secret');
        setError('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const tokenData = await authService.login({username, password});
            const realJwt = tokenData.accessToken || tokenData.access_token;
            setAuth({
                accessToken: realJwt,
                refreshToken: tokenData.refreshToken,
            });

            try {
                const liveProfile = await authService.getMe();
                useAuthStore.getState().setUser(liveProfile);
            } catch (err) {
                console.warn("Could not load user profile:", err);
            }

            navigate('/', {replace: true});
        } catch (err) {
            setError(err.message || 'Invalid username or password.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <div className="mb-6 text-center">
                <h2 className="text-xl font-bold text-slate-800">{t('auth.signIn')}</h2>
                <p className="text-xs text-slate-500 mt-1">{t('auth.signInSubtitle')}</p>
            </div>

            {/* Quick Test Demo Helper Button */}
            <div
                className="mb-5 p-3 bg-primary-50 rounded-xl border border-primary-100 flex items-center justify-between">
                <div className="flex items-center gap-2 text-xs text-primary-800">
                    <Sparkles className="w-4 h-4 text-primary-600"/>
                    <span>Demo Admin: <strong>phoeurnsolyta</strong></span>
                </div>
                <button
                    type="button"
                    onClick={handleQuickFillAdmin}
                    className="text-xs font-semibold text-primary-700 hover:text-primary-800 hover:underline"
                >
                    Auto Fill
                </button>
            </div>

            {error && (
                <div
                    className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0"/>
                    <span>{error}</span>
                </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.usernameOrEmail')}
                    </label>
                    <div className="relative">
                        <User className="w-4 h-4 text-slate-400 absolute left-3 top-3"/>
                        <input
                            type="text"
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full pl-9 pr-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition"
                            placeholder="Username or email"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-xs font-semibold text-slate-700 mb-1">
                        {t('auth.password')}
                    </label>
                    <div className="relative">
                        <Lock className="w-4 h-4 text-slate-400 absolute left-3 top-3"/>
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full pl-9 pr-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition"
                            placeholder="••••••••"
                        />
                    </div>
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full mt-2 py-2.5 px-4 rounded-xl bg-primary-600 hover:bg-primary-700 text-white text-sm font-semibold shadow-sm transition flex items-center justify-center gap-2 disabled:opacity-50"
                >
                    {loading ? t('common.loading') : (
                        <>
                            <span>{t('auth.signIn')}</span>
                            <ArrowRight className="w-4 h-4"/>
                        </>
                    )}
                </button>
            </form>

            <div className="mt-6">
                <div className="relative">
                    <div className="absolute inset-0 flex items-center">
                        <div className="w-full border-t border-slate-200"/>
                    </div>
                    <div className="relative flex justify-center text-xs">
            <span className="px-2 bg-white text-slate-400 font-medium">
              {t('auth.signInWith')}
            </span>
                    </div>
                </div>

                <div className="mt-4 grid grid-cols-3 gap-2">
                    <a
                        href="http://localhost:9000/oauth2/authorization/google"
                        className="flex items-center justify-center py-2 px-3 border border-slate-200 rounded-xl text-xs font-medium text-slate-700 hover:bg-slate-50 transition"
                    >
                        Google
                    </a>
                    <a
                        href="http://localhost:9000/oauth2/authorization/github"
                        className="flex items-center justify-center py-2 px-3 border border-slate-200 rounded-xl text-xs font-medium text-slate-700 hover:bg-slate-50 transition"
                    >
                        GitHub
                    </a>
                    <a
                        href="http://localhost:9000/oauth2/authorization/facebook"
                        className="flex items-center justify-center py-2 px-3 border border-slate-200 rounded-xl text-xs font-medium text-slate-700 hover:bg-slate-50 transition"
                    >
                        Facebook
                    </a>
                </div>
            </div>

            <div className="mt-6 text-center text-xs text-slate-500">
                <span>{t('auth.dontHaveAccount')} </span>
                <Link to="/register" className="font-semibold text-primary-600 hover:underline">
                    {t('auth.registerNow')}
                </Link>
            </div>
        </div>
    );
}