import {useState, useEffect} from 'react';
import {Link} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {adminService} from '../../services/adminService';
import {
    Users,
    Key,
    Shield,
    Layers,
    Activity,
    ArrowUpRight,
    ShieldAlert,
    Server,
    Plus,
    Loader2,
} from 'lucide-react';

export default function AdminOverviewPage() {
    const {t} = useTranslation();
    const [loading, setLoading] = useState(true);
    const [stats, setStats] = useState({
        totalUsers: 0,
        activeUsers: 0,
        rolesCount: 0,
        permissionsCount: 0,
        clientsCount: 0,
        recentUsers: [],
    });

    useEffect(() => {
        let isMounted = true;
        Promise.all([
            adminService.getUsers({size: 100}),
            adminService.getRoles({size: 100}),
            adminService.getPermissions({size: 100}),
            adminService.getClients(),
        ])
            .then(([usersData, rolesData, permsData, clientsData]) => {
                if (!isMounted) return;
                const usersList = usersData.content || [];
                const activeCount = usersList.filter((u) => u.enabled).length;

                setStats({
                    totalUsers: usersData.page?.totalElements || usersList.length,
                    activeUsers: activeCount,
                    rolesCount: rolesData.page?.totalElements || rolesData.content?.length || 0,
                    permissionsCount: permsData.page?.totalElements || permsData.content?.length || 0,
                    clientsCount: clientsData?.length || 0,
                    recentUsers: usersList.slice(0, 5),
                });
            })
            .catch((err) => {
                console.error('Failed to load overview metrics:', err);
            })
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, []);

    if (loading) {
        return (
            <div className="flex items-center justify-center py-24 text-slate-400 gap-2">
                <Loader2 className="w-5 h-5 animate-spin"/>
                <span className="text-sm font-medium">{t('common.loading')}</span>
            </div>
        );
    }

    const statCards = [
        {
            title: t('management.totalUsers'),
            value: stats.totalUsers,
            sub: `${stats.activeUsers} active`,
            icon: Users,
            color: 'bg-emerald-50 text-emerald-700 border-emerald-100',
            to: '/admin/users',
        },
        {
            title: t('management.totalRoles'),
            value: stats.rolesCount,
            sub: 'RBAC system roles',
            icon: Key,
            color: 'bg-blue-50 text-blue-700 border-blue-100',
            to: '/admin/roles',
        },
        {
            title: t('management.totalPermissions'),
            value: stats.permissionsCount,
            sub: 'Resource authority rules',
            icon: Shield,
            color: 'bg-amber-50 text-amber-700 border-amber-100',
            to: '/admin/permissions',
        },
        {
            title: t('management.totalClients'),
            value: stats.clientsCount,
            sub: 'OAuth 2.1 / OIDC clients',
            icon: Layers,
            color: 'bg-indigo-50 text-indigo-700 border-indigo-100',
            to: '/admin/clients',
        },
    ];

    return (
        <div className="space-y-6">
            {/* Header Banner */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('management.overviewTitle')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Real-time status of ISTAD Identity Provider & Access Management Engine
                    </p>
                </div>
                <div className="flex items-center gap-2">
                    <Link
                        to="/admin/users"
                        className="flex items-center gap-1.5 px-3 py-2 bg-primary-600 hover:bg-primary-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
                    >
                        <Plus className="w-3.5 h-3.5"/>
                        <span>{t('management.createUser')}</span>
                    </Link>
                </div>
            </div>

            {/* Top Stat Cards */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                {statCards.map((card) => {
                    const Icon = card.icon;
                    return (
                        <Link
                            key={card.title}
                            to={card.to}
                            className="bg-white rounded-2xl border border-slate-200 p-5 shadow-sm hover:border-primary-300 transition group flex flex-col justify-between"
                        >
                            <div className="flex items-center justify-between">
                                <span className="text-xs font-semibold text-slate-500">{card.title}</span>
                                <div className={`p-2 rounded-xl border ${card.color}`}>
                                    <Icon className="w-4 h-4"/>
                                </div>
                            </div>
                            <div className="mt-4">
                                <div className="text-2xl font-bold text-slate-900">{card.value}</div>
                                <div className="text-xs text-slate-400 mt-1 flex items-center justify-between">
                                    <span>{card.sub}</span>
                                    <ArrowUpRight
                                        className="w-3.5 h-3.5 group-hover:translate-x-0.5 group-hover:-translate-y-0.5 transition"/>
                                </div>
                            </div>
                        </Link>
                    );
                })}
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <div className="lg:col-span-2 bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
                    <div className="flex items-center justify-between border-b border-slate-100 pb-3">
                        <div className="flex items-center gap-2">
                            <Activity className="w-4 h-4 text-primary-600"/>
                            <h2 className="text-sm font-bold text-slate-800">Registered Platform Users</h2>
                        </div>
                        <Link to="/admin/users" className="text-xs text-primary-600 hover:underline font-semibold">
                            Manage All Users ({stats.totalUsers})
                        </Link>
                    </div>

                    <div className="divide-y divide-slate-100">
                        {stats.recentUsers.length === 0 ? (
                            <div className="py-8 text-center text-xs text-slate-400">No users found.</div>
                        ) : (
                            stats.recentUsers.map((u) => (
                                <div key={u.uuid} className="py-3 flex items-center justify-between gap-4 text-xs">
                                    <div>
                                        <div className="font-semibold text-slate-900">{u.name || u.username}</div>
                                        <div
                                            className="text-[11px] text-slate-400 font-mono">@{u.username} • {u.email}</div>
                                    </div>
                                    <span
                                        className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${
                                            u.enabled
                                                ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                                                : 'bg-slate-100 text-slate-500 border border-slate-200'
                                        }`}
                                    >
                    {u.enabled ? 'ACTIVE' : 'DISABLED'}
                  </span>
                                </div>
                            ))
                        )}
                    </div>
                </div>

                {/* Engine Security Specifications (1 col) */}
                <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
                    <div className="flex items-center gap-2 border-b border-slate-100 pb-3">
                        <Server className="w-4 h-4 text-primary-600"/>
                        <h2 className="text-sm font-bold text-slate-800">IAM Platform Standards</h2>
                    </div>

                    <div className="space-y-3 text-xs">
                        <div
                            className="flex items-center justify-between p-2.5 rounded-xl bg-slate-50 border border-slate-100">
                            <span className="text-slate-600 font-medium">Protocol Standard</span>
                            <span className="font-mono font-bold text-slate-900">OAuth 2.1 / OIDC 1.0</span>
                        </div>
                        <div
                            className="flex items-center justify-between p-2.5 rounded-xl bg-slate-50 border border-slate-100">
                            <span className="text-slate-600 font-medium">Backend Framework</span>
                            <span className="font-mono font-bold text-slate-900">Spring Boot 3.4.0</span>
                        </div>
                        <div
                            className="flex items-center justify-between p-2.5 rounded-xl bg-slate-50 border border-slate-100">
                            <span className="text-slate-600 font-medium">Frontend Framework</span>
                            <span className="font-mono font-bold text-slate-900">React 19 + Tailwind v4</span>
                        </div>
                        <div
                            className="flex items-center justify-between p-2.5 rounded-xl bg-slate-50 border border-slate-100">
                            <span className="text-slate-600 font-medium">Token Format</span>
                            <span className="font-mono font-bold text-slate-900">RS256 Signed JWT</span>
                        </div>
                    </div>

                    <div className="pt-2 border-t border-slate-100">
                        <div className="p-3 bg-amber-50 rounded-xl border border-amber-200 flex items-start gap-2.5">
                            <ShieldAlert className="w-4 h-4 text-amber-600 shrink-0 mt-0.5"/>
                            <div className="text-xs text-amber-800 space-y-1">
                                <div className="font-semibold">Security Enforcement Active</div>
                                <div>Stateless RFC 9457 Problem Details error translation is active for all REST
                                    endpoints.
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}