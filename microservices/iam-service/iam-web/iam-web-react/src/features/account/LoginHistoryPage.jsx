import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {userService} from '../../services/userService';
import {CheckCircle2, XCircle, Loader2} from 'lucide-react';

export default function LoginHistoryPage() {
    const {t} = useTranslation();
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function load() {
            const data = await userService.getLoginHistory();
            setHistory(data);
            setLoading(false);
        }

        load();
    }, []);

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
            <div>
                <h1 className="text-xl font-bold text-slate-900">{t('account.loginHistoryTitle')}</h1>
                <p className="text-xs text-slate-500 mt-0.5">{t('account.loginHistorySubtitle')}</p>
            </div>

            <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm">
                <div className="overflow-x-auto">
                    <table className="w-full text-left text-xs text-slate-600">
                        <thead
                            className="bg-slate-50 border-b border-slate-200 text-slate-400 font-semibold uppercase tracking-wider text-[10px]">
                        <tr>
                            <th className="px-5 py-3.5">Status</th>
                            <th className="px-5 py-3.5">Device & Browser</th>
                            <th className="px-5 py-3.5">IP Address</th>
                            <th className="px-5 py-3.5">Location</th>
                            <th className="px-5 py-3.5">Timestamp</th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-100">
                        {history.map((item) => (
                            <tr key={item.id} className="hover:bg-slate-50/50 transition">
                                <td className="px-5 py-3.5">
                                    {item.status === 'SUCCESS' ? (
                                        <span
                                            className="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full text-[10px] font-bold bg-emerald-50 text-emerald-700 border border-emerald-200">
                        <CheckCircle2 className="w-3 h-3 text-emerald-600"/>
                        SUCCESS
                      </span>
                                    ) : (
                                        <span
                                            className="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full text-[10px] font-bold bg-red-50 text-red-700 border border-red-200">
                        <XCircle className="w-3 h-3 text-red-600"/>
                        FAILED
                      </span>
                                    )}
                                </td>
                                <td className="px-5 py-3.5 font-medium text-slate-800">{item.device}</td>
                                <td className="px-5 py-3.5 text-slate-500 font-mono">{item.ipAddress}</td>
                                <td className="px-5 py-3.5 text-slate-500">{item.location}</td>
                                <td className="px-5 py-3.5 text-slate-400">
                                    {new Date(item.timestamp).toLocaleString()}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}