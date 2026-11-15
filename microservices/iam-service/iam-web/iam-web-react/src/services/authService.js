import apiClient from './apiClient';

export const authService = {

    async login({username, password}) {
        const response = await apiClient.post('/api/auth/login', {
            usernameOrEmail: username,
            password: password,
        });
        return response.data; // { accessToken, tokenType, expiresIn }
    },

    async register(data) {
        const response = await apiClient.post('/api/auth/register', data);
        return response.data;
    },

    async getMe() {
        const response = await apiClient.get('/api/me');
        return response.data;
    },
};