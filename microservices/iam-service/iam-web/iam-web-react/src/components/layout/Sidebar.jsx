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
            className="w-64 shrink-0 bg-white border-r border-slate-200 min-h-[calc(100vh-4rem)] p-4 flex flex-col justify-between">
            <div className="space-y-6">
                {/* Account Section */}
                <div>
                    <div className="px-3 mb-2 text-xs font-semibold text-slate-400 uppercase tracking-wider">
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
                                        `flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition ${
                                            isActive
                                                ? item.danger
                                                    ? 'bg-red-50 text-red-700'
                                                    : 'bg-primary-50 text-primary-700 font-semibold'
                                                : item.danger
                                                    ? 'text-slate-600 hover:text-red-600 hover:bg-red-50/50'
                                                    : 'text-slate-600 hover:text-slate-900 hover:bg-slate-50'
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
                            className="px-3 mb-2 text-xs font-semibold text-primary-700 uppercase tracking-wider flex items-center justify-between">
                            <span>{t('nav.management')}</span>
                            <span
                                className="text-[10px] bg-primary-100 text-primary-800 px-1.5 py-0.5 rounded font-bold">
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
                                            `flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition ${
                                                isActive
                                                    ? 'bg-primary-600 text-white shadow-sm'
                                                    : 'text-slate-600 hover:text-slate-900 hover:bg-slate-50'
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

            <div className="p-3 bg-slate-50 rounded-xl border border-slate-200 text-xs text-slate-500">
                <div className="font-semibold text-slate-700">ISTAD IAM v2.0</div>
                <div>OAuth 2.1 / OIDC 1.0</div>
            </div>
        </aside>
    );
}