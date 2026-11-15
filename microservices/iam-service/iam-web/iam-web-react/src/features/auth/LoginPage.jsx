import {useState, useEffect} from 'react';
import {Link, useNavigate, useLocation} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {authService} from '../../services/authService';
import {Lock, User, AlertCircle, ArrowRight, Loader2} from 'lucide-react';

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

    useEffect(() => {
        if (!useAuthStore.getState().accessToken) {
            authService.checkSession()
                .then(async (tokenData) => {
                    if (tokenData?.accessToken) {
                        setAuth({
                            accessToken: tokenData.accessToken,
                            refreshToken: tokenData.refreshToken,
                        });
                        try {
                            const liveProfile = await authService.getMe();
                            useAuthStore.getState().setUser(liveProfile);
                        } catch {
                            // ignore profile error
                        }
                        navigate(from, {replace: true});
                    }
                })
                .catch(() => {
                });
        }
    }, [from, navigate, setAuth]);

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
                console.warn('Could not load user profile:', err);
            }

            navigate(from, {replace: true});
        } catch (err) {
            setError(err.message || 'Invalid username or password.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="relative">
            {/* Top Navy Blue Star Medallion */}
            <div className="flex justify-center -mb-8 relative z-20">
                <div
                    className="w-16 h-16 rounded-full bg-[#0E2042] border-2 border-[#1E3A68] shadow-lg shadow-[#0E2042]/25 flex items-center justify-center relative">
                    <div className="absolute inset-1 rounded-full border border-dashed border-blue-300/30"/>
                    <div className="text-blue-100 text-2xl font-serif drop-shadow-[0_0_8px_rgba(255,255,255,0.6)]">
                        ★
                    </div>
                    <div className="absolute -left-3 text-[#0E2042]/60 text-xs">༺</div>
                    <div className="absolute -right-3 text-[#0E2042]/60 text-xs">༻</div>
                </div>
            </div>

            {/* White Form Card with Navy Accents */}
            <div
                className="bg-white border border-slate-200/90 rounded-3xl p-6 sm:p-8 pt-12 shadow-xl shadow-slate-300/40 relative overflow-hidden">
                {/* Navy Decorative Corner Filigrees */}
                <div
                    className="absolute top-2 left-2 text-[#0E2042]/25 text-xs font-serif select-none pointer-events-none">
                    ╭─✦
                </div>
                <div
                    className="absolute top-2 right-2 text-[#0E2042]/25 text-xs font-serif select-none pointer-events-none">
                    ✦─╮
                </div>
                <div
                    className="absolute bottom-2 left-2 text-[#0E2042]/25 text-xs font-serif select-none pointer-events-none">
                    ╰─✦
                </div>
                <div
                    className="absolute bottom-2 right-2 text-[#0E2042]/25 text-xs font-serif select-none pointer-events-none">
                    ✦─╯
                </div>

                {/* Heading & Subtitle in Navy Blue */}
                <div className="text-center mb-6">
                    <div className="flex items-center justify-center gap-2 text-[#0E2042]/30 text-xs mb-1">
                        <span>───</span>
                        <span className="text-[#0E2042] text-sm">✦</span>
                        <span>───</span>
                    </div>
                    <h2 className="text-lg font-bold text-[#0E2042] tracking-wider uppercase font-serif">
                        {t('auth.signIn')}
                    </h2>
                    <p className="text-[11px] text-slate-500 mt-0.5">
                        {t('auth.signInSubtitle')}
                    </p>
                </div>

                {/* Error Alert */}
                {error && (
                    <div
                        className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
                        <AlertCircle className="w-4 h-4 shrink-0 text-red-500"/>
                        <span>{error}</span>
                    </div>
                )}

                {/* Form */}
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label
                            className="block text-xs font-semibold text-[#0E2042] mb-1.5 flex items-center justify-between">
                            <span>{t('auth.usernameOrEmail')}</span>
                            <span className="text-[#0E2042]/40 text-[10px]">✦</span>
                        </label>
                        <div className="relative">
                            <User className="w-4 h-4 text-[#0E2042]/50 absolute left-3.5 top-3"/>
                            <input
                                type="text"
                                required
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="w-full pl-10 pr-3 py-2.5 text-xs rounded-xl bg-[#F8FAFC] border border-slate-300 text-[#0E2042] placeholder:text-slate-400 focus:outline-none focus:border-[#0E2042] focus:ring-2 focus:ring-[#0E2042]/10 transition shadow-inner"
                                placeholder="Username or email"
                            />
                        </div>
                    </div>

                    <div>
                        <label
                            className="block text-xs font-semibold text-[#0E2042] mb-1.5 flex items-center justify-between">
                            <span>{t('auth.password')}</span>
                            <span className="text-[#0E2042]/40 text-[10px]">✦</span>
                        </label>
                        <div className="relative">
                            <Lock className="w-4 h-4 text-[#0E2042]/50 absolute left-3.5 top-3"/>
                            <input
                                type="password"
                                required
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full pl-10 pr-3 py-2.5 text-xs rounded-xl bg-[#F8FAFC] border border-slate-300 text-[#0E2042] placeholder:text-slate-400 focus:outline-none focus:border-[#0E2042] focus:ring-2 focus:ring-[#0E2042]/10 transition shadow-inner"
                                placeholder="••••••••"
                            />
                        </div>
                    </div>

                    {/* Navy Blue Submit Button */}
                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full mt-3 py-3 px-4 rounded-xl bg-[#0E2042] hover:bg-[#162E5C] text-white text-xs font-bold tracking-wider uppercase shadow-md shadow-[#0E2042]/20 transition flex items-center justify-center gap-2 disabled:opacity-50"
                    >
                        {loading ? (
                            <Loader2 className="w-4 h-4 animate-spin text-white"/>
                        ) : (
                            <>
                                <span>{t('auth.signIn')}</span>
                                <ArrowRight className="w-3.5 h-3.5"/>
                            </>
                        )}
                    </button>
                </form>

                {/* Divider with Center Star */}
                <div className="mt-6 relative">
                    <div className="absolute inset-0 flex items-center">
                        <div className="w-full border-t border-slate-200"/>
                    </div>
                    <div className="relative flex justify-center text-[10px]">
            <span className="px-3 bg-white text-[#0E2042]/60 uppercase tracking-widest font-mono">
              ✦ {t('auth.signInWith')} ✦
            </span>
                    </div>
                </div>

                {/* Borderless Social Icons: Google, GitHub, Facebook, Telegram */}
                <div className="mt-5 flex items-center justify-center gap-6">
                    {/* Google */}
                    <a
                        href="http://localhost:9000/oauth2/authorization/google"
                        title="Sign in with Google"
                        className="p-1 hover:scale-120 transition-transform duration-200"
                    >
                        <svg className="w-7 h-7 drop-shadow-sm" viewBox="0 0 24 24">
                            <path
                                fill="#4285F4"
                                d="M23.745 12.27c0-.7-.06-1.4-.19-2.07H12v4.51h6.6c-.29 1.52-1.14 2.82-2.4 3.68v3.05h3.88c2.27-2.09 3.66-5.17 3.66-9.17z"
                            />
                            <path
                                fill="#34A853"
                                d="M12 24c3.24 0 5.95-1.08 7.93-2.91l-3.88-3.05c-1.08.72-2.45 1.16-4.05 1.16-3.12 0-5.77-2.1-6.72-4.93H1.25v3.15C3.26 21.36 7.33 24 12 24z"
                            />
                            <path
                                fill="#FBBC05"
                                d="M5.28 14.27c-.25-.72-.38-1.49-.38-2.27s.13-1.55.38-2.27V6.58H1.25C.45 8.18 0 10.04 0 12s.45 3.82 1.25 5.42l4.03-3.15z"
                            />
                            <path
                                fill="#EA4335"
                                d="M12 4.75c1.77 0 3.35.61 4.6 1.8l3.42-3.42C17.95 1.19 15.24 0 12 0 7.33 0 3.26 2.64 1.25 6.58l4.03 3.15c.95-2.83 3.6-4.98 6.72-4.98z"
                            />
                        </svg>
                    </a>

                    {/* GitHub in Navy */}
                    <a
                        href="http://localhost:9000/oauth2/authorization/github"
                        title="Sign in with GitHub"
                        className="p-1 hover:scale-120 transition-transform duration-200"
                    >
                        <svg className="w-7 h-7 fill-[#0E2042] hover:fill-[#162E5C] transition-colors drop-shadow-sm"
                             viewBox="0 0 24 24">
                            <path
                                fillRule="evenodd"
                                clipRule="evenodd"
                                d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                            />
                        </svg>
                    </a>

                    {/* Facebook */}
                    <a
                        href="http://localhost:9000/oauth2/authorization/facebook"
                        title="Sign in with Facebook"
                        className="p-1 hover:scale-120 transition-transform duration-200"
                    >
                        <svg className="w-7 h-7 fill-[#1877F2] drop-shadow-sm" viewBox="0 0 24 24">
                            <path
                                d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                        </svg>
                    </a>

                    {/* Telegram */}
                    <a
                        href="http://localhost:9000/login/telegram"
                        title="Sign in with Telegram"
                        className="p-1 hover:scale-120 transition-transform duration-200"
                    >
                        <svg className="w-7 h-7 fill-[#229ED9] drop-shadow-sm" viewBox="0 0 24 24">
                            <path
                                d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm5.894 8.221l-1.97 9.28c-.145.658-.537.818-1.084.508l-3-2.21-1.446 1.394c-.16.16-.295.295-.605.295l.213-3.053 5.56-5.023c.242-.213-.054-.333-.373-.121l-6.871 4.326-2.962-.924c-.643-.204-.657-.643.136-.953l11.57-4.458c.538-.196 1.006.128.832.945z"/>
                        </svg>
                    </a>
                </div>

                {/* Footer Link */}
                <div className="mt-6 text-center text-xs text-slate-500">
                    <span>{t('auth.dontHaveAccount')} </span>
                    <Link to="/register" className="font-bold text-[#0E2042] hover:underline">
                        {t('auth.registerNow')}
                    </Link>
                </div>
            </div>
        </div>
    );
}