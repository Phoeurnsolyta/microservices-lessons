import {useTranslation} from 'react-i18next';
import {useAuthStore} from '../../store/useAuthStore';
import {Globe} from 'lucide-react';

export default function LanguageSwitcher() {
    const {i18n} = useTranslation();
    const language = useAuthStore((state) => state.language);
    const setLanguage = useAuthStore((state) => state.setLanguage);

    const toggleLanguage = () => {
        const nextLang = language === 'en' ? 'km' : 'en';
        i18n.changeLanguage(nextLang);
        setLanguage(nextLang);
    };

    return (
        <button
            type="button"
            onClick={toggleLanguage}
            className="inline-flex items-center gap-2 px-3 py-1.5 text-xs font-medium rounded-full border border-slate-200 bg-white hover:bg-slate-100 text-slate-700 transition shadow-sm"
            title="Switch Language / ប្តូរភាសា"
        >
            <Globe className="w-3.5 h-3.5 text-primary-600"/>
            <span>{language === 'en' ? 'ភាសាខ្មែរ (KM)' : 'English (EN)'}</span>
        </button>
    );
}