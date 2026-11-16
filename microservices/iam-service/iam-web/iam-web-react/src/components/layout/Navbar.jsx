import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {useNavigate} from 'react-router-dom';
import {LogOut} from 'lucide-react';
import LanguageSwitcher from '../common/LanguageSwitcher';
import {authService} from '../../services/authService';

export default function Navbar() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const user = useAuthStore((state) => state.user);
    const roles = useAuthStore((state) => state.roles);
    const logout = useAuthStore((state) => state.logout);

    const handleLogout = async () => {
        await authService.logout();
        logout();
        navigate('/login');
    };

    const displayName = user
        ? `${user.givenName || ''} ${user.familyName || ''}`.trim() || user.username
        : 'User';

    return (
        <header className="sticky top-0 z-30 bg-white/90 backdrop-blur-md border-b border-slate-200/80 shadow-2xs">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16 items-center">
                    {/* Brand Logo with Official ISTAD Emblem */}
                    <div className="flex items-center gap-3">
                        <img
                            src="/istad-logo.png"
                            alt="ISTAD Emblem"
                            className="w-10 h-10 object-contain rounded-full shadow-xs hover:scale-105 transition"
                        />
                        <div>
                            <span className="text-base font-bold tracking-tight text-slate-900 block leading-tight">
                                ISTAD IAM
                            </span>
                            <span className="text-[11px] text-[#4E6B56] font-semibold">
                                Identity & Access Management
                            </span>
                        </div>
                    </div>

                    {/* Right Header Actions */}
                    <div className="flex items-center gap-3">
                        <div className="rounded-full bg-slate-100 p-0.5">
                            <LanguageSwitcher/>
                        </div>

                        <div className="h-6 w-px bg-slate-200 mx-1"/>

                        {/* User Profile Pill */}
                        <div className="flex items-center gap-3 pl-1">
                            <div
                                className="w-9 h-9 rounded-full bg-[#EBF3EE] text-[#4E6B56] font-bold flex items-center justify-center text-sm border border-[#D0E2D6] overflow-hidden shadow-2xs">
                                {user?.pictureUrl ? (
                                    <img
                                        src={user.pictureUrl}
                                        alt={displayName}
                                        className="w-full h-full rounded-full object-cover"
                                    />
                                ) : (
                                    <span>{(displayName || 'U')[0].toUpperCase()}</span>
                                )}
                            </div>

                            <div className="hidden sm:block text-left">
                                <div className="text-xs font-bold text-slate-800 leading-tight">
                                    {displayName}
                                </div>
                                <div className="flex items-center gap-1.5 mt-0.5">
                                    <span
                                        className="inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-bold bg-[#EBF3EE] text-[#4E6B56] border border-[#D0E2D6]">
                                        {roles.includes('ADMIN') ? 'ADMIN' : 'USER'}
                                    </span>
                                    <span className="text-xs text-slate-400 font-mono">
                                        @{user?.username || 'user'}
                                    </span>
                                </div>
                            </div>

                            <button
                                type="button"
                                onClick={handleLogout}
                                className="ml-2 p-2 rounded-full text-slate-500 hover:text-red-600 hover:bg-red-50 transition cursor-pointer"
                                title={t('common.logout')}
                            >
                                <LogOut className="w-4 h-4"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </header>
    );
}