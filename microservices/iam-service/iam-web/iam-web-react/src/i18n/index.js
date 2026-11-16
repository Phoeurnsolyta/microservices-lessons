import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import en from './locales/en.json';
import km from './locales/km.json';

const savedLanguage = localStorage.getItem('i18nextLng') || 'en';
document.documentElement.lang = savedLanguage;

i18n
    .use(initReactI18next)
    .init({
        resources: {
            en: {translation: en},
            km: {translation: km},
        },
        lng: savedLanguage,
        fallbackLng: 'en',
        interpolation: {
            escapeValue: false,
        },
    });

i18n.on('languageChanged', (lng) => {
    document.documentElement.lang = lng;
    localStorage.setItem('i18nextLng', lng);
});

export default i18n;