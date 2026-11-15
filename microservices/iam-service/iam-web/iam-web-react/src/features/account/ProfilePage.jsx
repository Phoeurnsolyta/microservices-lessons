import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {userService} from '../../services/userService';
import {User, Mail, Phone, MapPin, CheckCircle2, AlertCircle, Save, Loader2} from 'lucide-react';

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
        zoneinfo: 'Phnom_Penh',
        locale: 'en',
        streetAddress: '',
        locality: '',
        region: '',
        postalCode: '',
        country: 'KH',
    });

    useEffect(() => {
        let isMounted = true;
        userService.getProfile()
            .then((data) => {
                if (!isMounted) return;
                setUser(data);

                // Derive given and family names from data or full name
                const fullName = data.name || '';
                const nameParts = fullName.trim().split(/\s+/);
                const derivedGiven = data.givenName || nameParts[0] || '';
                const derivedFamily = data.familyName || (nameParts.length > 1 ? nameParts.slice(1).join(' ') : '');

                setFormData({
                    givenName: derivedGiven,
                    familyName: derivedFamily,
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
            })
            .catch((err) => {
                if (!isMounted) return;
                setError(err.message || 'Failed to load profile.');
            })
            .finally(() => {
                if (isMounted) setFetching(false);
            });

        return () => {
            isMounted = false;
        };
    }, [setUser]);

    const handleChange = (e) => {
        setFormData((prev) => ({...prev, [e.target.name]: e.target.value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        // Prepare clean payload without empty strings that break DB constraints
        const hasAddress =
            formData.streetAddress.trim() ||
            formData.locality.trim() ||
            formData.region.trim();

        const payload = {
            givenName: formData.givenName.trim(),
            familyName: formData.familyName.trim(),
            middleName: formData.middleName.trim() || null,
            nickname: formData.nickname.trim() || null,
            gender: formData.gender || null,
            birthdate: formData.birthdate || null,
            phoneNumber: formData.phoneNumber.trim() || null,
            zoneinfo: formData.zoneinfo || 'Asia/Phnom_Penh',
            locale: formData.locale || 'en',
            address: hasAddress
                ? {
                    streetAddress: formData.streetAddress.trim() || null,
                    locality: formData.locality.trim() || null,
                    region: formData.region.trim() || null,
                    postalCode: formData.postalCode.trim() || null,
                    country: formData.country.trim() || 'KH',
                }
                : null,
        };

        try {
            const updated = await userService.updateProfile(payload);
            setUser(updated);
            setSuccess('Profile updated successfully in backend database!');
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

            {/* Account Info Banner */}
            <div
                className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div className="flex items-center gap-4">
                    <div
                        className="w-16 h-16 rounded-2xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-xl border border-primary-200 shadow-xs">
                        {formData.givenName?.[0] || user?.name?.[0] || 'U'}
                    </div>
                    <div>
                        <div className="flex items-center gap-2">
                            <h2 className="text-base font-bold text-slate-900">
                                {formData.givenName} {formData.familyName}
                            </h2>
                            <span
                                className="text-[10px] font-bold bg-primary-50 text-primary-700 px-2 py-0.5 rounded-full border border-primary-100 uppercase">
                {roles[0] || 'USER'}
              </span>
                        </div>
                        <div className="text-xs text-slate-500 font-mono mt-0.5">@{user?.username}</div>
                        <div className="flex items-center gap-3 text-xs text-slate-500 mt-2">
              <span className="flex items-center gap-1">
                <Mail className="w-3.5 h-3.5 text-slate-400"/>
                  {user?.email}
              </span>
                            {formData.phoneNumber && (
                                <span className="flex items-center gap-1">
                  <Phone className="w-3.5 h-3.5 text-slate-400"/>
                                    {formData.phoneNumber}
                </span>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {/* Alerts */}
            {success && (
                <div
                    className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs rounded-xl flex items-center gap-2">
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

            {/* Form Card */}
            <form onSubmit={handleSubmit}
                  className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-6">
                <div>
                    <h3 className="text-sm font-bold text-slate-800 mb-3 flex items-center gap-2">
                        <User className="w-4 h-4 text-primary-600"/>
                        <span>General Information</span>
                    </h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
                        <div>
                            <label className="block text-xs font-semibold text-slate-700 mb-1">First Name *</label>
                            <input
                                type="text"
                                required
                                name="givenName"
                                value={formData.givenName}
                                onChange={handleChange}
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-semibold text-slate-700 mb-1">Last Name *</label>
                            <input
                                type="text"
                                required
                                name="familyName"
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
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 bg-white focus:outline-none focus:ring-2 focus:ring-primary-500"
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
                                type="tel"
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
                                className="w-full px-3 py-2 text-sm rounded-xl border border-slate-200 bg-white focus:outline-none focus:ring-2 focus:ring-primary-500"
                            >
                                <option value="en">English (en)</option>
                                <option value="km">Khmer (km)</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div className="h-px bg-slate-100"/>

                <div>
                    <h3 className="text-sm font-bold text-slate-800 mb-3 flex items-center gap-2">
                        <MapPin className="w-4 h-4 text-primary-600"/>
                        <span>{t('account.address')}</span>
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