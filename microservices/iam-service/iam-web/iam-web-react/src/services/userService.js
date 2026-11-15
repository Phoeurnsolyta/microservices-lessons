import apiClient from './apiClient';

export const userService = {
    async getProfile() {
        const response = await apiClient.get('/api/me');
        return response.data;
    },

    async updateProfile(profileData) {
        const response = await apiClient.patch('/api/me', profileData);
        return response.data;
    },

    async changePassword({currentPassword, newPassword, confirmPassword}) {
        const response = await apiClient.put('/api/me/password', {
            currentPassword,
            newPassword,
            confirmPassword,
        });
        return response.data;
    },

    async getIdentities() {
        const response = await apiClient.get('/api/me/identities');
        return response.data;
    },

    async unlinkIdentity(identityUuid) {
        const response = await apiClient.delete(`/api/me/identities/${identityUuid}`);
        return response.data;
    },

    async getActiveSessions() {
        const response = await apiClient.get('/api/me/sessions');
        return response.data;
    },

    async terminateSession(sessionId) {
        await apiClient.delete(`/api/me/sessions/${sessionId}`);
        return true;
    },

    async terminateAllOtherSessions() {
        await apiClient.delete('/api/me/sessions');
        return true;
    },

    async getLoginHistory() {
        const response = await apiClient.get('/api/me/login-history');
        return response.data;
    },

    async getAccountActivity() {
        const response = await apiClient.get('/api/me/activity');
        return response.data;
    },

    async deactivateAccount(password) {
        const response = await apiClient.post('/api/me/deactivate', {password});
        return response.data;
    },
};