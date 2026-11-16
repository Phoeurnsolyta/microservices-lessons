import {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {authService} from '../../services/authService';
import {AlertCircle, CheckCircle, Loader2} from 'lucide-react';

export default function RegisterPage() {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
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

        if (formData.password !== formData.confirmPassword) {
            setError(i18n.language === 'km' ? 'ពាក្យសម្ងាត់ផ្ទៀងផ្ទាត់មិនត្រូវគ្នាទេ' : 'Passwords do not match');
            return;
        }

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
            <div
                className="bg-white rounded-3xl sm:rounded-[32px] border border-slate-200/80 p-8 sm:p-10 shadow-xl text-center py-10 space-y-3">
                <CheckCircle className="w-12 h-12 text-[#4E6B56] mx-auto mb-2"/>
                <h3 className="text-lg font-bold text-slate-800">
                    {i18n.language === 'km' ? 'គណនីត្រូវបានបង្កើតដោយជោគជ័យ!' : 'Account Created Successfully!'}
                </h3>
                <p className="text-xs text-slate-500">
                    {i18n.language === 'km' ? 'កំពុងនាំអ្នកទៅទំព័រចូលប្រើ...' : 'Redirecting you to sign in...'}
                </p>
            </div>
        );
    }

    return (
        <div
            className="bg-white rounded-3xl sm:rounded-[32px] border border-slate-200/80 p-8 sm:p-10 shadow-xl shadow-slate-200/50 relative">
            {/* Header */}
            <div className="flex flex-col items-center text-center mb-6">
                <div className="w-14 h-14 rounded-full bg-[#EBF3EE] flex items-center justify-center mb-3 shadow-inner">
                    <svg className="w-8 h-8 text-[#4E6B56]" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12 2C11.5 4 9 7 9 10C9 12 10.5 13.5 12 13.5C13.5 13.5 15 12 15 10C15 7 12.5 4 12 2Z"
                              opacity="0.85"/>
                        <path d="M7 6C6.5 8 5 11 5 13.5C5 17 8 20 12 20C8.5 19.5 7 17 7 14.5C7 12 8 9.5 8.5 8L7 6Z"/>
                        <path
                            d="M17 6C17.5 8 19 11 19 13.5C19 17 16 20 12 20C15.5 19.5 17 17 17 14.5C17 12 16 9.5 15.5 8L17 6Z"/>
                    </svg>
                </div>
                <h2 className="text-xl font-bold text-slate-800">
                    {i18n.language === 'km' ? 'បង្កើតគណនីថ្មី' : 'Create New Account'}
                </h2>
                <p className="text-xs text-slate-500 mt-1">
                    {i18n.language === 'km' ? 'សូមបំពេញព័ត៌មានរបស់អ្នកដើម្បីចុះឈ្មោះ' : 'Sign up to access ISTAD IAM services'}
                </p>
            </div>

            {error && (
                <div
                    className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-2xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0"/>
                    <span>{error}</span>
                </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-3.5">
                <div className="grid grid-cols-2 gap-3">
                    <input
                        type="text"
                        name="givenName"
                        required
                        value={formData.givenName}
                        onChange={handleChange}
                        placeholder={i18n.language === 'km' ? 'នាមខ្លួន' : 'First Name'}
                        className="w-full px-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                    />
                    <input
                        type="text"
                        name="familyName"
                        required
                        value={formData.familyName}
                        onChange={handleChange}
                        placeholder={i18n.language === 'km' ? 'គោត្តនាម' : 'Last Name'}
                        className="w-full px-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                    />
                </div>

                <input
                    type="text"
                    name="username"
                    required
                    value={formData.username}
                    onChange={handleChange}
                    placeholder={i18n.language === 'km' ? 'ឈ្មោះអ្នកប្រើប្រាស់' : 'Username'}
                    className="w-full px-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                />

                <input
                    type="email"
                    name="email"
                    required
                    value={formData.email}
                    onChange={handleChange}
                    placeholder={i18n.language === 'km' ? 'អាសយដ្ឋានអ៊ីមែល' : 'Email Address'}
                    className="w-full px-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                />

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                    <input
                        type="password"
                        name="password"
                        required
                        minLength={8}
                        value={formData.password}
                        onChange={handleChange}
                        placeholder={i18n.language === 'km' ? 'ពាក្យសម្ងាត់' : 'Password (8+ chars)'}
                        className="w-full px-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                    />
                    <input
                        type="password"
                        name="confirmPassword"
                        required
                        minLength={8}
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        placeholder={i18n.language === 'km' ? 'ផ្ទៀងផ្ទាត់ពាក្យសម្ងាត់' : 'Confirm Password'}
                        className="w-full px-4 py-3 bg-[#EEF2F6] rounded-2xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[#4E6B56] transition"
                    />
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full py-3.5 bg-[#4E6B56] hover:bg-[#415a48] text-white text-xs font-bold rounded-2xl shadow-md shadow-[#4E6B56]/25 transition flex items-center justify-center gap-2 disabled:opacity-50 mt-4"
                >
                    {loading ? (
                        <>
                            <Loader2 className="w-4 h-4 animate-spin"/>
                            <span>{t('common.loading')}</span>
                        </>
                    ) : (
                        <span>{i18n.language === 'km' ? 'ចុះឈ្មោះគណនី' : 'Create Account'}</span>
                    )}
                </button>
            </form>

            <div className="mt-6 text-center text-xs text-slate-500">
                <span>{i18n.language === 'km' ? 'មានគណនីរួចហើយ?' : 'Already have an account?'}{' '}</span>
                <Link to="/login" className="text-[#4E6B56] hover:text-[#3e5645] font-bold hover:underline">
                    {i18n.language === 'km' ? 'ចូលប្រើនៅទីនេះ' : 'Sign in here'}
                </Link>
            </div>
        </div>
    );
}