import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {useNavigate} from 'react-router-dom';
import {Shield, LogOut, User as UserIcon} from 'lucide-react';
import LanguageSwitcher from '../common/LanguageSwitcher';
import { authService } from '../../services/authService';

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
        <header className="sticky top-0 z-30 bg-white border-b border-slate-200">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    {/* Brand Logo */}
                    <div className="flex items-center gap-3">
                        <div
                            className="w-10 h-10 rounded-xl bg-primary-600 flex items-center justify-center text-white shadow-sm">
                            <Shield className="w-6 h-6"/>
                        </div>
                        <div>
              <span className="text-base font-bold tracking-tight text-slate-900 block leading-tight">
                ISTAD IAM
              </span>
                            <span className="text-xs text-primary-700 font-medium">
                Identity & Access Management
              </span>
                        </div>
                    </div>

                    {/* Right Header Actions */}
                    <div className="flex items-center gap-3">
                        <LanguageSwitcher/>

                        <div className="h-6 w-px bg-slate-200 mx-1"/>

                        {/* User Profile Pill */}
                        <div className="flex items-center gap-3 pl-1">
                            <div
                                className="w-9 h-9 rounded-full bg-primary-100 text-primary-700 font-semibold flex items-center justify-center text-sm border border-primary-200">
                                {user?.pictureUrl ? (
                                    <img
                                        src={user.pictureUrl}
                                        alt={displayName}
                                        className="w-full h-full rounded-full object-cover"
                                    />
                                ) : (
                                    <UserIcon className="w-5 h-5 text-primary-700"/>
                                )}
                            </div>

                            <div className="hidden sm:block text-left">
                                <div className="text-sm font-semibold text-slate-800 leading-tight">
                                    {displayName}
                                </div>
                                <div className="flex items-center gap-1.5 mt-0.5">
                  <span
                      className="inline-flex items-center px-1.5 py-0.2 rounded text-[10px] font-semibold bg-primary-50 text-primary-700 border border-primary-200">
                    {roles.includes('ADMIN') ? 'ADMIN' : 'USER'}
                  </span>
                                    <span className="text-xs text-slate-400">
                    @{user?.username || 'user'}
                  </span>
                                </div>
                            </div>

                            <button
                                type="button"
                                onClick={handleLogout}
                                className="ml-2 p-2 rounded-lg text-slate-500 hover:text-red-600 hover:bg-red-50 transition"
                                title={t('common.logout')}
                            >
                                <LogOut className="w-5 h-5"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </header>
    );
}