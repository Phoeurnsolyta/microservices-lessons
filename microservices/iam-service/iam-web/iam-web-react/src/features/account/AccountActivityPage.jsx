import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {userService} from '../../services/userService';
import { ShieldCheck, UserCheck, Key, Loader2 } from 'lucide-react';

export default function AccountActivityPage() {
    const {t} = useTranslation();
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function load() {
            const data = await userService.getAccountActivity();
            setActivities(data);
            setLoading(false);
        }

        load();
    }, []);

    const getEventIcon = (type) => {
        switch (type) {
            case 'PASSWORD_CHANGED':
                return Key;
            case 'ROLE_ASSIGNED':
                return ShieldCheck;
            default:
                return UserCheck;
        }
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
        <div className="max-w-3xl space-y-6">
            <div>
                <h1 className="text-xl font-bold text-slate-900">{t('account.activityTitle')}</h1>
                <p className="text-xs text-slate-500 mt-0.5">{t('account.activitySubtitle')}</p>
            </div>

            <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                <div className="relative border-l-2 border-slate-100 ml-4 space-y-6 pl-6">
                    {activities.map((act) => {
                        const Icon = getEventIcon(act.type);
                        return (
                            <div key={act.id} className="relative">
                                <div
                                    className="absolute -left-[33px] top-1 w-6 h-6 rounded-full bg-primary-100 text-primary-700 flex items-center justify-center border-2 border-white">
                                    <Icon className="w-3 h-3"/>
                                </div>
                                <div>
                                    <div className="flex items-center gap-2">
                                        <span className="text-sm font-bold text-slate-800">{act.title}</span>
                                        <span
                                            className="text-[10px] bg-slate-100 text-slate-600 px-1.5 py-0.2 rounded font-medium">
                      By {act.actor}
                    </span>
                                    </div>
                                    <p className="text-xs text-slate-500 mt-0.5">{act.description}</p>
                                    <span className="text-[11px] text-slate-400 mt-1 block">
                    {new Date(act.timestamp).toLocaleString()}
                  </span>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>
        </div>
    );
}