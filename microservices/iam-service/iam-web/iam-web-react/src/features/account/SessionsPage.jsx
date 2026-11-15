import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {userService} from '../../services/userService';
import {Laptop, Smartphone, Tablet, Globe, LogOut, CheckCircle2, Loader2, Shield} from 'lucide-react';

export default function SessionsPage() {
    const {t} = useTranslation();
    const [sessions, setSessions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [success, setSuccess] = useState('');

    const loadSessions = async () => {
        try {
            const data = await userService.getActiveSessions();
            setSessions(data);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadSessions();
    }, []);

    const handleTerminateOne = async (sessionId) => {
        await userService.terminateSession(sessionId);
        setSessions((prev) => prev.filter((s) => s.id !== sessionId));
        setSuccess('Session successfully terminated.');
        setTimeout(() => setSuccess(''), 3000);
    };

    const handleTerminateAll = async () => {
        if (!window.confirm('Are you sure you want to log out from all other devices?')) return;
        await userService.terminateAllOtherSessions();
        setSessions((prev) => prev.filter((s) => s.isCurrent));
        setSuccess('All other sessions terminated.');
        setTimeout(() => setSuccess(''), 3000);
    };

    const getDeviceIcon = (deviceStr) => {
        if (/iPhone|Android|Mobile/i.test(deviceStr)) return Smartphone;
        if (/iPad|Tablet/i.test(deviceStr)) return Tablet;
        return Laptop;
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center py-24 text-slate-400 gap-2">
                <Loader2 className="w-5 h-5 animate-spin"/>
                <span className="text-sm font-medium">{t('common.loading')}</span>
            </div>
        );
    }

    return (
        <div className="max-w-4xl space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('account.sessionsTitle')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">{t('account.sessionsSubtitle')}</p>
                </div>

                {sessions.length > 1 && (
                    <button
                        type="button"
                        onClick={handleTerminateAll}
                        className="px-3.5 py-2 rounded-xl text-xs font-bold text-red-600 border border-red-200 hover:bg-red-50 transition flex items-center gap-1.5 self-start"
                    >
                        <LogOut className="w-3.5 h-3.5"/>
                        <span>{t('account.terminateAll')}</span>
                    </button>
                )}
            </div>

            {success && (
                <div
                    className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs rounded-xl flex items-center gap-2">
                    <CheckCircle2 className="w-4 h-4 shrink-0"/>
                    <span>{success}</span>
                </div>
            )}

            {/* Sessions Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {sessions.map((sess) => {
                    const Icon = getDeviceIcon(sess.device);
                    return (
                        <div
                            key={sess.id}
                            className={`p-5 rounded-2xl border transition shadow-sm bg-white ${
                                sess.isCurrent ? 'border-primary-300 ring-1 ring-primary-300' : 'border-slate-200'
                            }`}
                        >
                            <div className="flex items-start justify-between">
                                <div className="flex items-center gap-3">
                                    <div
                                        className={`w-10 h-10 rounded-xl flex items-center justify-center ${
                                            sess.isCurrent ? 'bg-primary-100 text-primary-700' : 'bg-slate-100 text-slate-600'
                                        }`}
                                    >
                                        <Icon className="w-5 h-5"/>
                                    </div>
                                    <div>
                                        <div className="text-sm font-bold text-slate-800 flex items-center gap-2">
                                            <span>{sess.device}</span>
                                            {sess.isCurrent && (
                                                <span
                                                    className="text-[10px] bg-primary-100 text-primary-800 font-bold px-1.5 py-0.2 rounded">
                          {t('account.currentSession')}
                        </span>
                                            )}
                                        </div>
                                        <div className="text-xs text-slate-400">{sess.browser}</div>
                                    </div>
                                </div>

                                {!sess.isCurrent && (
                                    <button
                                        type="button"
                                        onClick={() => handleTerminateOne(sess.id)}
                                        className="p-1.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition"
                                        title={t('account.terminateSession')}
                                    >
                                        <LogOut className="w-4 h-4"/>
                                    </button>
                                )}
                            </div>

                            <div
                                className="mt-4 pt-3 border-t border-slate-100 grid grid-cols-2 gap-2 text-[11px] text-slate-500">
                                <div className="flex items-center gap-1.5">
                                    <Globe className="w-3.5 h-3.5 text-slate-400 shrink-0"/>
                                    <span className="truncate">{sess.location}</span>
                                </div>
                                <div className="flex items-center gap-1.5 justify-end">
                                    <Shield className="w-3.5 h-3.5 text-slate-400 shrink-0"/>
                                    <span>IP: {sess.ipAddress}</span>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}