import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {userService} from '../../services/userService';
import { User, Mail, Phone, MapPin, CheckCircle2, AlertCircle, Save, Loader2 } from 'lucide-react';

export default function ProfilePage() {
    const {t} = useTranslation();
    const user = useAuthStore((state) => state.user);
    const setUser = useAuthStore((state) => state.setUser);
    const roles = useAuthStore((state) => state.roles);

    const [loading, setLoading] = useState(false);
    const [fetching, setFetching] = useState(true);
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    const [formData, setFormData] = useState({
        givenName: '',
        familyName: '',
        middleName: '',
        nickname: '',
        gender: 'Other',
        birthdate: '',
        phoneNumber: '',
        zoneinfo: 'Asia/Phnom_Penh',
        locale: 'en',
        streetAddress: '',
        locality: '',
        region: '',
        postalCode: '',
        country: 'KH',
    });

    useEffect(() => {
        async function loadProfile() {
            try {
                const data = await userService.getProfile();
                setUser(data);
                setFormData({
                    givenName: data.givenName || '',
                    familyName: data.familyName || '',
                    middleName: data.middleName || '',
                    nickname: data.nickname || '',
                    gender: data.gender || 'Other',
                    birthdate: data.birthdate || '',
                    phoneNumber: data.phoneNumber || '',
                    zoneinfo: data.zoneinfo || 'Asia/Phnom_Penh',
                    locale: data.locale || 'en',
                    streetAddress: data.address?.streetAddress || '',
                    locality: data.address?.locality || '',
                    region: data.address?.region || '',
                    postalCode: data.address?.postalCode || '',
                    country: data.address?.country || 'KH',
                });
            } catch {
                if (user) {
                    setFormData((prev) => ({
                        ...prev,
                        givenName: user.givenName || '',
                        familyName: user.familyName || '',
                        phoneNumber: user.phoneNumber || '',
                    }));
                }
            } finally {
                setFetching(false);
            }
        }

        loadProfile();
    }, [setUser, user]);

    const handleChange = (e) => {
        setFormData((prev) => ({...prev, [e.target.name]: e.target.value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        try {
            const payload = {
                givenName: formData.givenName,
                familyName: formData.familyName,
                middleName: formData.middleName,
                nickname: formData.nickname,
                gender: formData.gender,
                birthdate: formData.birthdate || null,
                phoneNumber: formData.phoneNumber,
                zoneinfo: formData.zoneinfo,
                locale: formData.locale,
                address: {
                    streetAddress: formData.streetAddress,
                    locality: formData.locality,
                    region: formData.region,
                    postalCode: formData.postalCode,
                    country: formData.country,
                },
            };

            const updated = await userService.updateProfile(payload);
            setUser(updated);
            setSuccess('Profile updated successfully!');
            setTimeout(() => setSuccess(''), 4000);
        } catch (err) {
            setError(err.message || 'Failed to update profile.');
        } finally {
            setLoading(false);
        }
    };

    if (fetching) {
        return (
            <div className="flex items-center justify-center py-24 text-slate-400 gap-2">
                <Loader2 className="w-5 h-5 animate-spin"/>
                <span className="text-sm font-medium">{t('common.loading')}</span>
            </div>
        );
    }

    return (
        <div className="space-y-6">

            <div>
                <h1 className="text-xl font-bold text-slate-900">{t('account.profileTitle')}</h1>
                <p className="text-xs text-slate-500 mt-0.5">{t('account.profileSubtitle')}</p>
            </div>

            <div
                className="bg-white rounded-2xl border border-slate-200 p-6 flex flex-col sm:flex-row items-center gap-5 shadow-sm">
                <div
                    className="w-20 h-20 rounded-2xl bg-primary-100 border-2 border-primary-200 text-primary-700 flex items-center justify-center text-2xl font-bold shadow-sm">
                    {formData.givenName?.[0] || 'U'}
                </div>
                <div className="flex-1 text-center sm:text-left space-y-1">
                    <div className="flex flex-wrap items-center justify-center sm:justify-start gap-2">
                        <h2 className="text-lg font-bold text-slate-800">
                            {formData.givenName} {formData.familyName}
                        </h2>
                        <span
                            className="inline-flex items-center px-2 py-0.5 rounded text-[10px] font-bold bg-primary-50 text-primary-700 border border-primary-200">
              {roles.includes('ADMIN') ? 'ADMINISTRATOR' : 'USER'}
            </span>
                    </div>
                    <p className="text-xs text-slate-400">@{user?.username || 'user'}</p>
                    <div
                        className="flex flex-wrap items-center justify-center sm:justify-start gap-4 pt-1 text-xs text-slate-600">
            <span className="flex items-center gap-1.5">
              <Mail className="w-3.5 h-3.5 text-slate-400"/>
                {user?.email || 'N/A'}
            </span>
                        <span className="flex items-center gap-1.5">
              <Phone className="w-3.5 h-3.5 text-slate-400"/>
                            {formData.phoneNumber || 'No phone set'}
            </span>
                    </div>
                </div>
            </div>

            {success && (
                <div
                    className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs rounded-xl flex items-center gap-2">
                    <CheckCircle2 className="w-4 h-4 shrink-0"/>
                    <span>{success}</span>
                </div>
            )}

            {error && (
                <div
                    className="p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0"/>
                    <span>{error}</span>
                </div>
            )}


            <form onSubmit={handleSubmit}
                  className="bg-white rounded-2xl border border-slate-200 p-6 space-y-6 shadow-sm">
                <div>
                    <h3 className="text-sm font-bold text-slate-800 mb-3 flex items-center gap-2">
                        <User className="w-4 h-4 text-primary-600"/>
                        General Information
                    </h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('auth.givenName')}</label>
                            <input
                                type="text"
                                name="givenName"
                                required
                                value={formData.givenName}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('auth.familyName')}</label>
                            <input
                                type="text"
                                name="familyName"
                                required
                                value={formData.familyName}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.middleName')}</label>
                            <input
                                type="text"
                                name="middleName"
                                value={formData.middleName}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.nickname')}</label>
                            <input
                                type="text"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.gender')}</label>
                            <select
                                name="gender"
                                value={formData.gender}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500 bg-white"
                            >
                                <option value="Male">{t('account.male')}</option>
                                <option value="Female">{t('account.female')}</option>
                                <option value="Other">{t('account.other')}</option>
                            </select>
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.birthdate')}</label>
                            <input
                                type="date"
                                name="birthdate"
                                value={formData.birthdate}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.phone')}</label>
                            <input
                                type="text"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                                placeholder="+855 12 345 678"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.locale')}</label>
                            <select
                                name="locale"
                                value={formData.locale}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500 bg-white"
                            >
                                <option value="en">English (en)</option>
                                <option value="km">ភាសាខ្មែរ (km)</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div className="h-px bg-slate-100"/>

                <div>
                    <h3 className="text-sm font-bold text-slate-800 mb-3 flex items-center gap-2">
                        <MapPin className="w-4 h-4 text-primary-600"/>
                        {t('account.address')}
                    </h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                        <div className="sm:col-span-2">
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.street')}</label>
                            <input
                                type="text"
                                name="streetAddress"
                                value={formData.streetAddress}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                                placeholder="St. 562, Toul Kork"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.city')}</label>
                            <input
                                type="text"
                                name="locality"
                                value={formData.locality}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                                placeholder="Phnom Penh"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.region')}</label>
                            <input
                                type="text"
                                name="region"
                                value={formData.region}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                                placeholder="Phnom Penh"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.postalCode')}</label>
                            <input
                                type="text"
                                name="postalCode"
                                value={formData.postalCode}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                                placeholder="12000"
                            />
                        </div>

                        <div>
                            <label
                                className="block text-xs font-semibold text-slate-700 mb-1">{t('account.country')}</label>
                            <input
                                type="text"
                                name="country"
                                maxLength={2}
                                value={formData.country}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500 uppercase"
                                placeholder="KH"
                            />
                        </div>
                    </div>
                </div>

                <div className="flex justify-end pt-3">
                    <button
                        type="submit"
                        disabled={loading}
                        className="px-5 py-2.5 rounded-xl bg-primary-600 hover:bg-primary-700 text-white text-xs font-bold shadow-sm transition flex items-center gap-2 disabled:opacity-50"
                    >
                        {loading ? <Loader2 className="w-4 h-4 animate-spin"/> : <Save className="w-4 h-4"/>}
                        <span>{t('common.save')}</span>
                    </button>
                </div>
            </form>
        </div>
    );
}