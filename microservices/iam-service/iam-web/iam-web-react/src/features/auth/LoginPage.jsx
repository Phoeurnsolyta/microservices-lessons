import {useState, useEffect} from 'react';
import {Link, useNavigate, useLocation} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {authService} from '../../services/authService';
import {Lock, User, AlertCircle, Loader2} from 'lucide-react';

export default function LoginPage() {
    const {t, i18n} = useTranslation();
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
        <div
            className="bg-white rounded-3xl sm:rounded-[32px] border border-slate-200/80 p-8 sm:p-10 shadow-xl shadow-slate-200/50 relative">
            {/* Language Toggle Pill in top-right */}
            <div className="absolute top-6 right-6">
                <div className="flex bg-[#EEF2F6] rounded-full p-1 text-[11px] font-bold text-slate-600">
                    <button
                        type="button"
                        onClick={() => i18n.changeLanguage('en')}
                        className={`px-2.5 py-1 rounded-full transition ${i18n.language === 'en' ? 'bg-white text-[#4E6B56] shadow-2xs font-extrabold' : 'text-slate-500 hover:text-slate-800'}`}
                    >
                        EN
                    </button>
                    <button
                        type="button"
                        onClick={() => i18n.changeLanguage('km')}
                        className={`px-2.5 py-1 rounded-full transition ${i18n.language === 'km' ? 'bg-white text-[#4E6B56] shadow-2xs font-extrabold' : 'text-slate-500 hover:text-slate-800'}`}
                    >
                        KM
                    </button>
                </div>
            </div>

            {/* Top Lock Icon Badge */}
            <div className="flex flex-col items-center text-center mt-1 mb-6">
                <div className="w-18 h-18 rounded-full bg-[#EBF3EE] flex items-center justify-center mb-3 shadow-inner text-[#4E6B56]">
                    <Lock className="w-8 h-8" strokeWidth={2.2}/>
                </div>
                <h2 className="text-2xl font-bold text-slate-800">
                    {i18n.language === 'km' ? 'ចូលគណនី' : 'Sign In'}
                </h2>
                <p className="text-sm text-slate-500 mt-1.5">
                    {i18n.language === 'km' ? 'ចូលទៅកាន់គណនីរបស់អ្នក' : 'Access your personal account'}
                </p>
            </div>

            {/* Alerts */}
            {error && (
                <div
                    className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-2xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0"/>
                    <span>{error}</span>
                </div>
            )}

            {/* Form */}
            <form onSubmit={handleSubmit} className="space-y-4">
                {/* Username Input */}
                <div className="relative">
                    <div
                        className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-slate-400">
                        <User className="w-4 h-4 text-slate-500"/>
                    </div>
                    <input
                        type="text"
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder={i18n.language === 'km' ? 'ឈ្មោះអ្នកប្រើប្រាស់ ឬ អ៊ីមែល' : 'Username or Email'}
                        className="w-full pl-11 pr-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                    />
                </div>

                {/* Password Input */}
                <div className="relative">
                    <div
                        className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-slate-400">
                        <Lock className="w-4 h-4 text-slate-500"/>
                    </div>
                    <input
                        type="password"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder={i18n.language === 'km' ? 'ពាក្យសម្ងាត់' : 'Password'}
                        className="w-full pl-11 pr-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                    />
                </div>

                {/* Submit Button (Image 2 Green Pill) */}
                <button
                    type="submit"
                    disabled={loading}
                    className="w-full py-3.5 bg-[#4E6B56] hover:bg-[#415a48] text-white text-xs font-bold rounded-2xl shadow-md shadow-[#4E6B56]/25 transition flex items-center justify-center gap-2 disabled:opacity-50 mt-2"
                >
                    {loading ? (
                        <>
                            <Loader2 className="w-4 h-4 animate-spin"/>
                            <span>{t('common.loading')}</span>
                        </>
                    ) : (
                        <span>{i18n.language === 'km' ? 'ចូលប្រើ' : 'Sign In'}</span>
                    )}
                </button>
            </form>

            {/* Divider */}
            <div className="relative my-6 text-center">
                <div className="absolute inset-0 flex items-center">
                    <div className="w-full border-t border-slate-200"></div>
                </div>
                <span className="relative bg-white px-3 text-xs text-slate-400 font-medium">
                    {i18n.language === 'km' ? 'ឬ' : 'or'}
                </span>
            </div>

            {/* Social Authentication Round Buttons */}
            <div className="flex justify-center items-center gap-4">
                {/* Google */}
                <a
                    href="http://localhost:9000/oauth2/authorization/google"
                    title="Google"
                    className="w-11 h-11 rounded-full bg-[#F1F3F5] hover:bg-slate-200 flex items-center justify-center transition shadow-2xs"
                >
                    <svg className="w-5 h-5" viewBox="0 0 24 24">
                        <path fill="#4285F4"
                              d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                        <path fill="#34A853"
                              d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                        <path fill="#FBBC05"
                              d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
                        <path fill="#EA4335"
                              d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
                    </svg>
                </a>

                {/* GitHub */}
                <a
                    href="http://localhost:9000/oauth2/authorization/github"
                    title="GitHub"
                    className="w-11 h-11 rounded-full bg-[#F1F3F5] hover:bg-slate-200 flex items-center justify-center transition shadow-2xs text-slate-800"
                >
                    <svg className="w-5 h-5 fill-current" viewBox="0 0 24 24">
                        <path fillRule="evenodd" clipRule="evenodd"
                              d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"/>
                    </svg>
                </a>

                {/* Facebook */}
                <a
                    href="http://localhost:9000/oauth2/authorization/facebook"
                    title="Facebook"
                    className="w-11 h-11 rounded-full bg-[#F1F3F5] hover:bg-slate-200 flex items-center justify-center transition shadow-2xs text-[#1877F2]"
                >
                    <svg className="w-5 h-5 fill-current" viewBox="0 0 24 24">
                        <path
                            d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                    </svg>
                </a>

                {/* Telegram */}
                <a
                    href="http://localhost:9000/login/telegram"
                    title="Telegram"
                    className="w-11 h-11 rounded-full bg-[#F1F3F5] hover:bg-slate-200 flex items-center justify-center transition shadow-2xs text-[#229ED9]"
                >
                    <svg className="w-5 h-5 fill-current" viewBox="0 0 24 24">
                        <path
                            d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm4.64 6.8c-.15 1.58-.8 5.42-1.13 7.19-.14.75-.42 1-.68 1.03-.58.05-1.02-.38-1.58-.75-.88-.58-1.38-.94-2.23-1.5-.99-.65-.35-1.01.22-1.59.15-.15 2.71-2.48 2.76-2.69a.2.2 0 00-.05-.18c-.06-.05-.14-.03-.21-.02-.09.02-1.49.95-4.22 2.79-.4.27-.76.41-1.08.4-.36-.01-1.04-.2-1.55-.37-.63-.2-1.12-.31-1.08-.66.02-.18.27-.36.74-.55 2.92-1.27 4.86-2.11 5.83-2.51 2.78-1.16 3.35-1.36 3.73-1.36.08 0 .27.02.39.12.1.08.13.19.14.27-.01.06.01.24 0 .38z"/>
                    </svg>
                </a>
            </div>

            {/* Bottom Sign-Up Link */}
            <div className="mt-8 text-center text-xs text-slate-500">
                <span>{i18n.language === 'km' ? 'មិនទាន់មានគណនីមែនទេ?' : "Don't have an account?"}{' '}</span>
                <Link
                    to="/register"
                    className="text-[#4E6B56] hover:text-[#3e5645] font-bold hover:underline"
                >
                    {i18n.language === 'km' ? 'បង្កើតគណនីថ្មី' : 'Create Account'}
                </Link>
            </div>
        </div>
    );
}