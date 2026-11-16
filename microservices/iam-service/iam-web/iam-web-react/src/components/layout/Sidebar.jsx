import {NavLink} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {
    User,
    ShieldCheck,
    Link as LinkIcon,
    Laptop,
    History,
    Activity,
    AlertTriangle,
    LayoutDashboard,
    Users,
    Key,
    Shield,
    Layers,
    FileText,
    Settings,
} from 'lucide-react';

export default function Sidebar() {
    const {t} = useTranslation();
    const hasRole = useAuthStore((state) => state.hasRole);
    const isAdmin = hasRole('ADMIN');

    const accountNav = [
        {to: '/', label: t('nav.profile'), icon: User, end: true},
        {to: '/security', label: t('nav.security'), icon: ShieldCheck},
        {to: '/linked-accounts', label: t('nav.linkedAccounts'), icon: LinkIcon},
        {to: '/sessions', label: t('nav.sessions'), icon: Laptop},
        {to: '/login-history', label: t('nav.loginHistory'), icon: History},
        {to: '/activity', label: t('nav.activity'), icon: Activity},
        {to: '/deactivate', label: t('nav.deactivate'), icon: AlertTriangle, danger: true},
    ];

    const adminNav = [
        {to: '/admin', label: t('nav.overview'), icon: LayoutDashboard, end: true},
        {to: '/admin/users', label: t('nav.users'), icon: Users},
        {to: '/admin/roles', label: t('nav.roles'), icon: Key},
        {to: '/admin/permissions', label: t('nav.permissions'), icon: Shield},
        {to: '/admin/clients', label: t('nav.clients'), icon: Layers},
        {to: '/admin/audit', label: t('nav.audit'), icon: FileText},
        {to: '/admin/settings', label: t('nav.settings'), icon: Settings},
    ];

    return (
        <aside
            className="w-64 shrink-0 bg-[#181B20] text-slate-300 h-[calc(100vh-5.5rem)] sticky top-20 self-start p-4 flex flex-col rounded-3xl my-3 shadow-xl overflow-hidden">

            {/* Scrollable Navigation Area with Ultra-Thin Scrollbar */}
            <div className="space-y-6 flex-1 overflow-y-auto pr-1 sidebar-scroll">
                {/* Account Section */}
                <div>
                    <div className="px-3 mb-2 text-[11px] font-bold text-slate-400 uppercase tracking-wider">
                        {t('nav.account')}
                    </div>
                    <nav className="space-y-1">
                        {accountNav.map((item) => {
                            const Icon = item.icon;
                            return (
                                <NavLink
                                    key={item.to}
                                    to={item.to}
                                    end={item.end}
                                    className={({isActive}) =>
                                        `flex items-center gap-3 px-3.5 py-2.5 rounded-2xl text-xs font-semibold transition ${
                                            isActive
                                                ? item.danger
                                                    ? 'bg-red-500/20 text-red-300 font-bold'
                                                    : 'bg-[#4E6B56] text-white shadow-md shadow-[#4E6B56]/30'
                                                : item.danger
                                                    ? 'text-slate-400 hover:text-red-400 hover:bg-white/5'
                                                    : 'text-slate-400 hover:text-white hover:bg-white/5'
                                        }`
                                    }
                                >
                                    <Icon className="w-4 h-4 shrink-0"/>
                                    <span>{item.label}</span>
                                </NavLink>
                            );
                        })}
                    </nav>
                </div>

                {/* Administration Section */}
                {isAdmin && (
                    <div>
                        <div
                            className="px-3 mb-2 text-[11px] font-bold text-[#A8D1B6] uppercase tracking-wider flex items-center justify-between">
                            <span>{t('nav.management')}</span>
                            <span
                                className="text-[10px] bg-[#4E6B56]/40 text-[#C9E5D4] px-2 py-0.5 rounded-full font-bold border border-[#4E6B56]/60">
                                ADMIN
                            </span>
                        </div>
                        <nav className="space-y-1">
                            {adminNav.map((item) => {
                                const Icon = item.icon;
                                return (
                                    <NavLink
                                        key={item.to}
                                        to={item.to}
                                        end={item.end}
                                        className={({isActive}) =>
                                            `flex items-center gap-3 px-3.5 py-2.5 rounded-2xl text-xs font-semibold transition ${
                                                isActive
                                                    ? 'bg-[#4E6B56] text-white shadow-md shadow-[#4E6B56]/30'
                                                    : 'text-slate-400 hover:text-white hover:bg-white/5'
                                            }`
                                        }
                                    >
                                        <Icon className="w-4 h-4 shrink-0"/>
                                        <span>{item.label}</span>
                                    </NavLink>
                                );
                            })}
                        </nav>
                    </div>
                )}
            </div>

            {/* Bottom Card */}
            <div className="pt-3 shrink-0">
                <div
                    className="p-3.5 bg-gradient-to-br from-[#242930] to-[#1C2025] rounded-2xl border border-white/5 text-xs text-slate-300 space-y-1.5 shadow-sm">
                    <div className="flex items-center justify-between font-bold text-white text-xs">
                        <span>ISTAD IAM Platform</span>
                        <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
                    </div>
                </div>
            </div>
        </aside>
    );
}