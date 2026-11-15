import {useState} from 'react';
import {useTranslation} from 'react-i18next';
import {adminService} from '../../services/adminService';
import {
    Clock,
    ShieldCheck,
    Lock,
    Save,
    CheckCircle2,
} from 'lucide-react';

export default function SystemSettingsPage() {
    const {t} = useTranslation();
    // Lazy state initializer directly reads saved or default settings
    const [settings, setSettings] = useState(() => adminService.getSystemSettings());
    const [saving, setSaving] = useState(false);
    const [savedSuccess, setSavedSuccess] = useState(false);

    const handleSave = (e) => {
        e.preventDefault();
        setSaving(true);
        adminService.saveSystemSettings(settings);
        setSaving(false);
        setSavedSuccess(true);
        setTimeout(() => setSavedSuccess(false), 3000);
    };

    return (
        <div className="max-w-4xl space-y-6">
            {/* Header */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-xl font-bold text-slate-900">{t('nav.settings')}</h1>
                    <p className="text-xs text-slate-500 mt-0.5">
                        Configure authentication lifetimes, password strength rules, and brute-force lockout thresholds
                    </p>
                </div>
                <button
                    onClick={handleSave}
                    disabled={saving}
                    className="flex items-center gap-1.5 px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white text-xs font-semibold rounded-xl shadow-sm transition disabled:opacity-50"
                >
                    <Save className="w-3.5 h-3.5"/>
                    <span>{saving ? 'Saving...' : 'Save Policies'}</span>
                </button>
            </div>

            {savedSuccess && (
                <div
                    className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs rounded-xl flex items-center gap-2">
                    <CheckCircle2 className="w-4 h-4 shrink-0"/>
                    <span>Security configuration policies persisted successfully.</span>
                </div>
            )}

            <form onSubmit={handleSave} className="space-y-6">
                {/* Card 1: Token Lifetimes */}
                <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-4">
                    <div className="flex items-center gap-2 border-b border-slate-100 pb-3">
                        <Clock className="w-4 h-4 text-primary-600"/>
                        <h2 className="text-sm font-bold text-slate-800">Token & Session Lifetimes</h2>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-xs">
                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">
                                Access Token TTL (minutes)
                            </label>
                            <input
                                type="number"
                                min={1}
                                max={1440}
                                value={settings.accessTokenTtlMinutes}
                                onChange={(e) =>
                                    setSettings({...settings, accessTokenTtlMinutes: Number(e.target.value)})
                                }
                                className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                            />
                            <span className="text-[11px] text-slate-400 mt-1 block">Default: 15 minutes</span>
                        </div>

                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">
                                Refresh Token TTL (days)
                            </label>
                            <input
                                type="number"
                                min={1}
                                max={365}
                                value={settings.refreshTokenTtlDays}
                                onChange={(e) =>
                                    setSettings({...settings, refreshTokenTtlDays: Number(e.target.value)})
                                }
                                className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                            />
                            <span className="text-[11px] text-slate-400 mt-1 block">Default: 30 days</span>
                        </div>

                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">
                                Authorization Code TTL (minutes)
                            </label>
                            <input
                                type="number"
                                min={1}
                                max={60}
                                value={settings.authCodeTtlMinutes}
                                onChange={(e) =>
                                    setSettings({...settings, authCodeTtlMinutes: Number(e.target.value)})
                                }
                                className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                            />
                            <span className="text-[11px] text-slate-400 mt-1 block">Default: 5 minutes</span>
                        </div>
                    </div>
                </div>

                {/* Card 2: Password Complexity Policies */}
                <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-4">
                    <div className="flex items-center gap-2 border-b border-slate-100 pb-3">
                        <ShieldCheck className="w-4 h-4 text-primary-600"/>
                        <h2 className="text-sm font-bold text-slate-800">Password Complexity Policies</h2>
                    </div>

                    <div className="space-y-3 text-xs">
                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">
                                Minimum Password Length: <strong>{settings.minPasswordLength}</strong> chars
                            </label>
                            <input
                                type="range"
                                min={8}
                                max={24}
                                value={settings.minPasswordLength}
                                onChange={(e) =>
                                    setSettings({...settings, minPasswordLength: Number(e.target.value)})
                                }
                                className="w-full accent-primary-600"
                            />
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 pt-2">
                            <label
                                className="flex items-center gap-2.5 p-3 rounded-xl border border-slate-200 cursor-pointer hover:bg-slate-50 transition">
                                <input
                                    type="checkbox"
                                    checked={settings.requireUppercase}
                                    onChange={(e) =>
                                        setSettings({...settings, requireUppercase: e.target.checked})
                                    }
                                    className="rounded text-primary-600 focus:ring-primary-500"
                                />
                                <span className="font-medium text-slate-700">Require Uppercase (A-Z)</span>
                            </label>

                            <label
                                className="flex items-center gap-2.5 p-3 rounded-xl border border-slate-200 cursor-pointer hover:bg-slate-50 transition">
                                <input
                                    type="checkbox"
                                    checked={settings.requireNumber}
                                    onChange={(e) =>
                                        setSettings({...settings, requireNumber: e.target.checked})
                                    }
                                    className="rounded text-primary-600 focus:ring-primary-500"
                                />
                                <span className="font-medium text-slate-700">Require Number (0-9)</span>
                            </label>

                            <label
                                className="flex items-center gap-2.5 p-3 rounded-xl border border-slate-200 cursor-pointer hover:bg-slate-50 transition">
                                <input
                                    type="checkbox"
                                    checked={settings.requireSpecialChar}
                                    onChange={(e) =>
                                        setSettings({...settings, requireSpecialChar: e.target.checked})
                                    }
                                    className="rounded text-primary-600 focus:ring-primary-500"
                                />
                                <span className="font-medium text-slate-700">Require Special Symbol</span>
                            </label>
                        </div>
                    </div>
                </div>

                {/* Card 3: Account Lockout & Registration */}
                <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-4">
                    <div className="flex items-center gap-2 border-b border-slate-100 pb-3">
                        <Lock className="w-4 h-4 text-primary-600"/>
                        <h2 className="text-sm font-bold text-slate-800">Account Lockout & Registration</h2>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-xs">
                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">
                                Max Consecutive Failed Attempts
                            </label>
                            <input
                                type="number"
                                min={3}
                                max={20}
                                value={settings.maxFailedAttempts}
                                onChange={(e) =>
                                    setSettings({...settings, maxFailedAttempts: Number(e.target.value)})
                                }
                                className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                            />
                            <span className="text-[11px] text-slate-400 mt-1 block">Account automatically locks upon threshold</span>
                        </div>

                        <div>
                            <label className="font-semibold text-slate-700 block mb-1">
                                Lockout Duration (minutes)
                            </label>
                            <input
                                type="number"
                                min={5}
                                max={1440}
                                value={settings.lockoutDurationMinutes}
                                onChange={(e) =>
                                    setSettings({...settings, lockoutDurationMinutes: Number(e.target.value)})
                                }
                                className="w-full px-3 py-2 rounded-xl border border-slate-200 focus:ring-2 focus:ring-primary-500"
                            />
                            <span
                                className="text-[11px] text-slate-400 mt-1 block">Duration before automated unlock</span>
                        </div>
                    </div>

                    <div className="pt-2 border-t border-slate-100">
                        <label
                            className="flex items-center gap-2.5 p-3 rounded-xl border border-slate-200 cursor-pointer hover:bg-slate-50 transition text-xs">
                            <input
                                type="checkbox"
                                checked={settings.allowRegistration}
                                onChange={(e) =>
                                    setSettings({...settings, allowRegistration: e.target.checked})
                                }
                                className="rounded text-primary-600 focus:ring-primary-500"
                            />
                            <div>
                                <span className="font-semibold text-slate-800 block">Allow Self-Service User Registration</span>
                                <span className="text-[11px] text-slate-400">
                  Allow prospective users to self-register via the <code>/register</code> portal
                </span>
                            </div>
                        </label>
                    </div>
                </div>
            </form>
        </div>
    );
}