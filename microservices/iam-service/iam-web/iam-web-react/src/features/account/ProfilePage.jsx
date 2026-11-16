import {useState, useEffect, useRef} from 'react';
import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {userService} from '../../services/userService';
import ConfirmModal from '../../components/common/ConfirmModal';
import {
    User,
    Mail,
    Phone,
    MapPin,
    CheckCircle2,
    AlertCircle,
    Save,
    Loader2,
    Camera,
    Trash2,
} from 'lucide-react';

export default function ProfilePage() {
    const {t} = useTranslation();
    const user = useAuthStore((state) => state.user);
    const setUser = useAuthStore((state) => state.setUser);
    const roles = useAuthStore((state) => state.roles);

    const fileInputRef = useRef(null);

    const [loading, setLoading] = useState(false);
    const [uploadingAvatar, setUploadingAvatar] = useState(false);
    const [fetching, setFetching] = useState(true);
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');
    const [avatarUrl, setAvatarUrl] = useState('');
    const [showDeleteModal, setShowDeleteModal] = useState(false);

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
        let isMounted = true;
        userService.getProfile()
            .then((data) => {
                if (!isMounted) return;
                setUser(data);
                if (data.pictureUrl) {
                    setAvatarUrl(data.pictureUrl);
                }

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

    const handleAvatarSelect = async (e) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (!file.type.startsWith('image/')) {
            setError('Please select a valid image file (PNG, JPG, WEBP).');
            return;
        }

        if (file.size > 5 * 1024 * 1024) {
            setError('Image file size must be less than 5MB.');
            return;
        }

        setUploadingAvatar(true);
        setError('');
        setSuccess('');

        try {
            const res = await userService.uploadAvatar(file);
            setAvatarUrl(res.pictureUrl);
            setUser({...user, pictureUrl: res.pictureUrl});
            setSuccess('Profile picture updated and saved to Redis!');
            setTimeout(() => setSuccess(''), 4000);
        } catch (err) {
            setError(err.response?.data?.detail || err.message || 'Failed to upload photo.');
        } finally {
            setUploadingAvatar(false);
        }
    };

    // Confirmed removal via custom modal
    const handleConfirmRemoveAvatar = async () => {
        setUploadingAvatar(true);
        setError('');
        setSuccess('');

        try {
            await userService.deleteAvatar();
            setAvatarUrl('');
            setUser({...user, pictureUrl: null});
            setShowDeleteModal(false);
            setSuccess('Profile picture removed.');
            setTimeout(() => setSuccess(''), 4000);
        } catch (err) {
            setError(err.message || 'Failed to delete avatar.');
        } finally {
            setUploadingAvatar(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

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

            {/* Account Info Banner with Avatar Upload */}
            <div
                className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm flex flex-col sm:flex-row items-start sm:items-center justify-between gap-6">
                <div className="flex items-center gap-5">
                    {/* Avatar Container with Upload Icon */}
                    <div className="relative group shrink-0">
                        <div
                            className="w-20 h-20 rounded-2xl bg-primary-100 text-primary-800 flex items-center justify-center font-bold text-2xl border border-primary-200 shadow-sm overflow-hidden">
                            {avatarUrl ? (
                                <img
                                    src={avatarUrl}
                                    alt="Profile"
                                    className="w-full h-full object-cover"
                                />
                            ) : (
                                <span>{formData.givenName?.[0] || user?.name?.[0] || 'U'}</span>
                            )}
                        </div>

                        {/* Hidden File Input */}
                        <input
                            ref={fileInputRef}
                            type="file"
                            accept="image/png, image/jpeg, image/webp"
                            className="hidden"
                            onChange={handleAvatarSelect}
                        />

                        {/* Camera Button Trigger */}
                        <button
                            type="button"
                            onClick={() => fileInputRef.current?.click()}
                            disabled={uploadingAvatar}
                            title="Upload Avatar (Stored in Redis)"
                            className="absolute -bottom-1 -right-1 p-2 bg-primary-600 hover:bg-primary-700 text-white rounded-xl shadow-md border-2 border-white transition flex items-center justify-center"
                        >
                            {uploadingAvatar ? (
                                <Loader2 className="w-3.5 h-3.5 animate-spin"/>
                            ) : (
                                <Camera className="w-3.5 h-3.5"/>
                            )}
                        </button>
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

                        <div className="flex items-center gap-4 text-xs text-slate-500 mt-2">
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

                        {avatarUrl && (
                            <button
                                type="button"
                                onClick={() => setShowDeleteModal(true)}
                                disabled={uploadingAvatar}
                                className="mt-2 text-[11px] text-red-600 hover:text-red-700 font-medium flex items-center gap-1 transition"
                            >
                                <Trash2 className="w-3 h-3"/>
                                <span>Remove Photo</span>
                            </button>
                        )}
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
                            <label className="block text-xs font-semibold text-slate-600 mb-1">
                                First Name <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="text"
                                name="givenName"
                                value={formData.givenName}
                                onChange={handleChange}
                                required
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">
                                Last Name <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="text"
                                name="familyName"
                                value={formData.familyName}
                                onChange={handleChange}
                                required
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Middle Name</label>
                            <input
                                type="text"
                                name="middleName"
                                value={formData.middleName}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Nickname</label>
                            <input
                                type="text"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Gender</label>
                            <select
                                name="gender"
                                value={formData.gender}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none bg-white transition"
                            >
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Date of Birth</label>
                            <input
                                type="date"
                                name="birthdate"
                                value={formData.birthdate}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Phone Number</label>
                            <input
                                type="tel"
                                name="phoneNumber"
                                placeholder="+855 12 345 678"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Preferred
                                Language</label>
                            <select
                                name="locale"
                                value={formData.locale}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none bg-white transition"
                            >
                                <option value="en">English (en)</option>
                                <option value="km">ខ្មែរ (km)</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div className="pt-4 border-t border-slate-100">
                    <h3 className="text-sm font-bold text-slate-800 mb-3 flex items-center gap-2">
                        <MapPin className="w-4 h-4 text-primary-600"/>
                        <span>Address Information</span>
                    </h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div className="sm:col-span-2">
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Street Address</label>
                            <input
                                type="text"
                                name="streetAddress"
                                placeholder="St. 562, Toul Kork"
                                value={formData.streetAddress}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">City / Khan</label>
                            <input
                                type="text"
                                name="locality"
                                placeholder="Phnom Penh"
                                value={formData.locality}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Province / Region</label>
                            <input
                                type="text"
                                name="region"
                                placeholder="Phnom Penh"
                                value={formData.region}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Postal Code</label>
                            <input
                                type="text"
                                name="postalCode"
                                placeholder="12000"
                                value={formData.postalCode}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                        <div>
                            <label className="block text-xs font-semibold text-slate-600 mb-1">Country (e.g. KH)</label>
                            <input
                                type="text"
                                name="country"
                                maxLength="2"
                                placeholder="KH"
                                value={formData.country}
                                onChange={handleChange}
                                className="w-full text-xs px-3 py-2 border border-slate-300 rounded-xl uppercase focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition"
                            />
                        </div>
                    </div>
                </div>

                <div className="flex justify-end pt-4 border-t border-slate-100">
                    <button
                        type="submit"
                        disabled={loading}
                        className="flex items-center gap-2 px-5 py-2.5 bg-primary-600 hover:bg-primary-700 text-white text-xs font-semibold rounded-xl shadow-sm transition disabled:opacity-50"
                    >
                        {loading ? <Loader2 className="w-4 h-4 animate-spin"/> : <Save className="w-4 h-4"/>}
                        <span>{loading ? t('common.saving') : t('common.save')}</span>
                    </button>
                </div>
            </form>

            {/* Custom Modal Confirmation */}
            <ConfirmModal
                isOpen={showDeleteModal}
                onClose={() => setShowDeleteModal(false)}
                onConfirm={handleConfirmRemoveAvatar}
                title="Remove Profile Photo"
                message="Are you sure you want to remove your profile photo? Your avatar will revert to your name initials."
                confirmText="Remove Photo"
                cancelText="Cancel"
                variant="danger"
                loading={uploadingAvatar}
            />
        </div>
    );
}