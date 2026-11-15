import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
      react(),
      tailwindcss(),
  ],
  server: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      },

      '/oauth2': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      },

      '/connect': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      },

      '/.well-known': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
