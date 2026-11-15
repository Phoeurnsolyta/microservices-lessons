import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {adminService} from '../../services/adminService';
import {
    Search,
    Activity,
    CheckCircle2,
    XCircle,
    Loader2,
    RefreshCw,
} from 'lucide-react';

export default function AuditLogsPage() {
    const {t} = useTranslation();
    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [eventTypeFilter, setEventTypeFilter] = useState('');

    const eventTypes = [
        'ALL',
        'USER_LOGIN_SUCCESS',
        'LOGIN_FAILURE',
        'ROLE_ASSIGNED',
        'PASSWORD_RESET',
    ];

    const refreshLogs = () => {
        adminService.getAuditLogs({eventType: eventTypeFilter === 'ALL' ? '' : eventTypeFilter, search})
            .then((data) => setLogs(data || []))
            .catch((err) => console.error(err));
    };

    useEffect(() => {
        let isMounted = true;
        adminService.getAuditLogs({eventType: eventTypeFilter === 'ALL' ? '' : eventTypeFilter, search})
            .then((data) => {
                if (isMounted) setLogs(data || []);
            })
            .catch((err) => console.error(err))
            .finally(() => {
                if (isMounted) setLoading(false);
            });

        return () => {
            isMounted = false;
        };
    }, [eventTypeFilter, search]);

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('nav.audit')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Real-time audit inspection of security events, authentication logs, and role modifications
                    </p>
                </div>
                <button
                    onClick={refreshLogs}
                    className="flex items-center gap-1.5 px-3 py-2 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 text-xs font-semibold rounded-xl shadow-xs transition"
                >
                    <RefreshCw className="w-3.5 h-3.5"/>
                    <span>Refresh Logs</span>
                </button>
            </div>

            {/* Filter Tabs */}
            <div className="flex items-center gap-2 overflow-x-auto pb-1">
                {eventTypes.map((et) => {
                    const isSelected = et === 'ALL' ? eventTypeFilter === '' : eventTypeFilter === et;
                    return (
                        <button
                            key={et}
                            onClick={() => setEventTypeFilter(et === 'ALL' ? '' : et)}
                            className={`px-3 py-1.5 rounded-xl text-xs font-semibold transition ${
                                isSelected
                                    ? 'bg-primary-600 text-white shadow-xs'
                                    : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'
                            }`}
                        >
                            {et}
                        </button>
                    );
                })}
            </div>

            {/* Search Bar */}
            <div className="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm">
                <div className="relative">
                    <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5"/>
                    <input
                        type="text"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        placeholder="Search by actor username, target resource, or details..."
                        className="w-full pl-9 pr-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                </div>
            </div>

            {/* Logs Table */}
            <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full text-left text-xs">
                        <thead
                            className="bg-slate-50 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
                        <tr>
                            <th className="py-3 px-4">Event Type</th>
                            <th className="py-3 px-4">Actor</th>
                            <th className="py-3 px-4">Target Resource</th>
                            <th className="py-3 px-4">Details</th>
                            <th className="py-3 px-4">IP Address</th>
                            <th className="py-3 px-4">Status</th>
                            <th className="py-3 px-4 text-right">Timestamp</th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-100 text-slate-700">
                        {loading ? (
                            <tr>
                                <td colSpan="7" className="py-12 text-center text-slate-400">
                                    <Loader2 className="w-5 h-5 animate-spin mx-auto mb-2"/>
                                    <span>{t('common.loading')}</span>
                                </td>
                            </tr>
                        ) : logs.length === 0 ? (
                            <tr>
                                <td colSpan="7" className="py-12 text-center text-slate-400">
                                    No audit records found matching your filters.
                                </td>
                            </tr>
                        ) : (
                            logs.map((log) => (
                                <tr key={log.id} className="hover:bg-slate-50/70 transition">
                                    <td className="py-3 px-4">
                                        <div
                                            className="flex items-center gap-1.5 font-bold font-mono text-[11px] text-slate-900">
                                            <Activity className="w-3.5 h-3.5 text-primary-600"/>
                                            <span>{log.eventType}</span>
                                        </div>
                                    </td>
                                    <td className="py-3 px-4 font-mono font-semibold text-slate-800">
                                        @{log.actor}
                                    </td>
                                    <td className="py-3 px-4 font-medium text-slate-600">
                                        {log.target}
                                    </td>
                                    <td className="py-3 px-4 text-slate-500 max-w-xs truncate">
                                        {log.details}
                                    </td>
                                    <td className="py-3 px-4 font-mono text-slate-500 text-[11px]">
                                        {log.ipAddress}
                                    </td>
                                    <td className="py-3 px-4">
                      <span
                          className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold ${
                              log.status === 'SUCCESS'
                                  ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                                  : 'bg-red-50 text-red-700 border border-red-200'
                          }`}
                      >
                        {log.status === 'SUCCESS' ? (
                            <CheckCircle2 className="w-3 h-3"/>
                        ) : (
                            <XCircle className="w-3 h-3"/>
                        )}
                          {log.status}
                      </span>
                                    </td>
                                    <td className="py-3 px-4 text-right text-slate-400 text-[11px]">
                                        {new Date(log.timestamp).toLocaleString()}
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}