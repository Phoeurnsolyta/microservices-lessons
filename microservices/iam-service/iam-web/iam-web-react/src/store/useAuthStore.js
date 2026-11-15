import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const parseJwt = (token) => {
    if (!token) return null;
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch {
        return null;
    }
};

export const useAuthStore = create(
    persist(
        (set, get) => ({
            accessToken: null,
            refreshToken: null,
            user: null,
            roles: [],
            permissions: [],
            language: 'en',

            setAuth: ({ accessToken, refreshToken, user = null }) => {
                const decoded = parseJwt(accessToken);
                const roles =
                    decoded?.roles ||
                    (decoded?.authorities
                        ?.filter((a) => a.startsWith('ROLE_'))
                        .map((r) => r.replace('ROLE_', '')) || []);

                const permissions =
                    decoded?.permissions ||
                    (decoded?.authorities?.filter((a) => !a.startsWith('ROLE_')) || []);

                set({
                    accessToken,
                    refreshToken: refreshToken || get().refreshToken,
                    roles,
                    permissions,
                    user: user || get().user,
                });
            },
            setUser: (user) => set({ user }),
            setLanguage: (lang) => {
                set({ language: lang });
                localStorage.setItem('i18nextLng', lang);
                document.documentElement.lang = lang;
            },

            hasRole: (roleName) => {
                const { roles } = get();
                return roles.includes(roleName);
            },

            hasPermission: (permName) => {
                const { permissions, roles } = get();
                if (roles.includes('ADMIN')) return true; // Super administrator bypass
                return permissions.includes(permName);
            },

            logout: () => {
                set({
                    accessToken: null,
                    refreshToken: null,
                    user: null,
                    roles: [],
                    permissions: [],
                });
            },

            isAuthenticated: () => !!get().accessToken,
        }),
        {
            name: 'istad-iam-auth-store',
            partialize: (state) => ({
                accessToken: state.accessToken,
                refreshToken: state.refreshToken,
                user: state.user,
                roles: state.roles,
                permissions: state.permissions,
                language: state.language,
            }),
        }
    )
);