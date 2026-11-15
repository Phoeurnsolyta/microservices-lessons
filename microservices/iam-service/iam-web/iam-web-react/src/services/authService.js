import apiClient from './apiClient';

export const authService = {

    async login({username, password}) {
        const response = await apiClient.post('/api/auth/login', {
            usernameOrEmail: username,
            password: password,
        });
        return response.data;
    },

    async checkSession() {
        const response = await apiClient.get('/api/auth/session');
        return response.data;
    },

    async logout() {
        try {
            await apiClient.post('/api/auth/logout');
        } catch {
            // ignore if session was already expired
        }
    },

    async register(data) {
        const payload = {
            ...data,
            confirmPassword: data.confirmPassword || data.password,
        };
        const response = await apiClient.post('/api/auth/register', payload);
        return response.data;
    },

    async getMe() {
        const response = await apiClient.get('/api/me');
        return response.data;
    },
};