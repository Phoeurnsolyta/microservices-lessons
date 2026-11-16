import {useState, useEffect} from 'react';
import {Link} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {adminService} from '../../services/adminService';
import {
    Users,
    Key,
    Shield,
    Layers,
    Activity,
    ArrowUpRight,
    Plus,
    Loader2,
} from 'lucide-react';

export default function AdminOverviewPage() {
    const {t} = useTranslation();
    const currentUser = useAuthStore((state) => state.user);
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
                    recentUsers: usersList.slice(0, 8),
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
            sub: `${stats.activeUsers} active accounts`,
            icon: Users,
            color: 'bg-[#EBF3EE] text-[#4E6B56] border-[#D0E2D6]',
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
            sub: 'Resource access rules',
            icon: Shield,
            color: 'bg-amber-50 text-amber-700 border-amber-100',
            to: '/admin/permissions',
        },
        {
            title: t('management.totalClients'),
            value: stats.clientsCount,
            sub: 'OAuth 2.1 / OIDC clients',
            icon: Layers,
            color: 'bg-purple-50 text-purple-700 border-purple-100',
            to: '/admin/clients',
        },
    ];

    const greetingName = currentUser?.givenName || currentUser?.name || currentUser?.username || 'Admin';

    return (
        <div className="space-y-6">
            {/* Header Greeting Banner (Image 1 Style) */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-bold text-slate-900 tracking-tight">
                        Welcome back, {greetingName}
                    </h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Real-time status of ISTAD Identity Provider & Access Management Engine
                    </p>
                </div>
                <div className="flex items-center gap-2">
                    <Link
                        to="/admin/users"
                        className="flex items-center gap-1.5 px-4 py-2.5 bg-[#4E6B56] hover:bg-[#415a48] text-white text-xs font-bold rounded-2xl shadow-md shadow-[#4E6B56]/25 transition"
                    >
                        <Plus className="w-3.5 h-3.5"/>
                        <span>{t('management.createUser')}</span>
                    </Link>
                </div>
            </div>

            {/* Stat Cards (Ultra-rounded 3xl with subtle hover) */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                {statCards.map((card) => {
                    const Icon = card.icon;
                    return (
                        <Link
                            key={card.title}
                            to={card.to}
                            className="bg-white rounded-3xl border border-slate-200/80 p-5 shadow-sm hover:border-[#4E6B56]/40 hover:shadow-md transition group flex flex-col justify-between"
                        >
                            <div className="flex items-center justify-between">
                                <span className="text-xs font-semibold text-slate-500">{card.title}</span>
                                <div className={`p-2.5 rounded-2xl border ${card.color}`}>
                                    <Icon className="w-4 h-4"/>
                                </div>
                            </div>
                            <div className="mt-4">
                                <div
                                    className="text-3xl font-extrabold text-slate-900 tracking-tight">{card.value}</div>
                                <div className="text-xs text-slate-400 mt-1 flex items-center justify-between">
                                    <span>{card.sub}</span>
                                    <ArrowUpRight
                                        className="w-3.5 h-3.5 group-hover:translate-x-0.5 group-hover:-translate-y-0.5 text-slate-400 group-hover:text-[#4E6B56] transition"/>
                                </div>
                            </div>
                        </Link>
                    );
                })}
            </div>

            {/* Registered Platform Users - Clean Full Width with Avatars */}
            <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm p-6 space-y-4">
                <div className="flex items-center justify-between border-b border-slate-100 pb-3">
                    <div className="flex items-center gap-2">
                        <Activity className="w-4 h-4 text-[#4E6B56]"/>
                        <h2 className="text-sm font-bold text-slate-800">Registered Platform Users</h2>
                    </div>
                    <Link to="/admin/users" className="text-xs text-[#4E6B56] hover:underline font-bold">
                        Manage All Users ({stats.totalUsers})
                    </Link>
                </div>

                <div className="divide-y divide-slate-100">
                    {stats.recentUsers.length === 0 ? (
                        <div className="py-8 text-center text-xs text-slate-400">No users found.</div>
                    ) : (
                        stats.recentUsers.map((u) => (
                            <div key={u.uuid} className="py-3 flex items-center justify-between gap-4 text-xs">
                                <div className="flex items-center gap-3">
                                    <div
                                        className="w-9 h-9 rounded-2xl bg-[#EBF3EE] text-[#4E6B56] flex items-center justify-center font-bold text-xs border border-[#D0E2D6] shrink-0 overflow-hidden shadow-2xs">
                                        {u.pictureUrl ? (
                                            <img
                                                src={u.pictureUrl}
                                                alt={u.name || u.username}
                                                className="w-full h-full object-cover"
                                            />
                                        ) : (
                                            <span>{(u.name || u.username || 'U')[0].toUpperCase()}</span>
                                        )}
                                    </div>
                                    <div>
                                        <div className="font-semibold text-slate-900 leading-tight">
                                            {u.name || u.username}
                                        </div>
                                        <div className="text-[11px] text-slate-400 font-mono mt-0.5">
                                            @{u.username} • {u.email}
                                        </div>
                                    </div>
                                </div>
                                <span
                                    className={`text-[10px] font-bold px-2.5 py-0.5 rounded-full ${
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
        </div>
    );
}