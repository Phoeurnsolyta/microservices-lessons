@param boolean hasError = false
@param boolean isLogout = false
@param boolean isRegistered = false
@param String telegramBotUsername = ""
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("login.title")} - ${msg.get("common.appName")}</title>
    <style>
        :root {
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --bg: #f1f5f9;
            --card-bg: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #cbd5e1;
            --danger: #ef4444;
            --success: #22c55e;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: system-ui, -apple-system, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
            background: var(--bg);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 1rem;
        }

        .auth-card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 2.5rem;
            width: 100%;
            max-width: 440px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -2px rgba(0, 0, 0, 0.05);
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .header h1 {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--primary);
        }

        .lang-switch a {
            text-decoration: none;
            color: var(--text-muted);
            font-size: 0.8rem;
            padding: 0.2rem 0.4rem;
            border: 1px solid var(--border);
            border-radius: 4px;
        }

        .lang-switch a.active {
            background: var(--primary);
            color: #fff;
            border-color: var(--primary);
        }

        .alert {
            padding: 0.75rem 1rem;
            border-radius: 6px;
            font-size: 0.875rem;
            margin-bottom: 1.25rem;
        }

        .alert-danger {
            background: #fee2e2;
            color: #991b1b;
        }

        .alert-success {
            background: #dcfce7;
            color: #166534;
        }

        .form-group {
            margin-bottom: 1rem;
        }

        label {
            display: block;
            font-size: 0.875rem;
            font-weight: 500;
            margin-bottom: 0.35rem;
            color: var(--text-main);
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 0.65rem 0.85rem;
            border: 1px solid var(--border);
            border-radius: 6px;
            font-size: 0.95rem;
        }

        input:focus {
            outline: 2px solid var(--primary);
            border-color: transparent;
        }

        .btn-primary {
            width: 100%;
            background: var(--primary);
            color: white;
            border: none;
            padding: 0.75rem;
            font-size: 1rem;
            font-weight: 600;
            border-radius: 6px;
            cursor: pointer;
            margin-top: 0.5rem;
        }

        .btn-primary:hover {
            background: var(--primary-hover);
        }

        .divider {
            display: flex;
            align-items: center;
            text-align: center;
            margin: 1.5rem 0;
            color: var(--text-muted);
            font-size: 0.8rem;
        }

        .divider::before, .divider::after {
            content: '';
            flex: 1;
            border-bottom: 1px solid var(--border);
        }

        .divider span {
            padding: 0 0.5rem;
        }

        .social-buttons {
            display: flex;
            flex-direction: column;
            gap: 0.6rem;
        }

        .btn-social {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            padding: 0.65rem;
            border: 1px solid var(--border);
            border-radius: 6px;
            text-decoration: none;
            color: var(--text-main);
            font-size: 0.9rem;
            font-weight: 500;
            background: #fff;
            transition: background 0.15s;
        }

        .btn-social:hover {
            background: #f8fafc;
        }

        .footer {
            margin-top: 1.5rem;
            text-align: center;
            font-size: 0.875rem;
            color: var(--text-muted);
        }

        .footer a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 600;
        }
    </style>
</head>
<body>
<div class="auth-card">
    <div class="header">
        <h1>${msg.get("login.title")}</h1>
        <div class="lang-switch">
            <a href="/login?lang=en" class="${"en".equals(msg.getCurrentLang()) ? "active" : ""}">EN</a>
            <a href="/login?lang=km" class="${"km".equals(msg.getCurrentLang()) ? "active" : ""}">KM</a>
        </div>
    </div>
    @if(hasError)
        <div class="alert alert-danger">${msg.get("login.invalidCredentials")}</div>
    @endif
    @if(isLogout)
        <div class="alert alert-success">${msg.get("login.loggedOut")}</div>
    @endif
    @if(isRegistered)
        <div class="alert alert-success">Registration successful! Please sign in with your credentials.</div>
    @endif
    <form action="/login" method="post">
        @if(_csrf != null)
            <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
        @endif
        <div class="form-group">
            <label for="username">${msg.get("login.usernameOrEmail")}</label>
            <input type="text" id="username" name="username" required autofocus autocomplete="username">
        </div>
        <div class="form-group">
            <label for="password">${msg.get("login.password")}</label>
            <input type="password" id="password" name="password" required autocomplete="current-password">
        </div>
        <button type="submit" class="btn-primary">${msg.get("login.submit")}</button>
    </form>
    <div class="divider">
        <span>${msg.get("login.or")}</span>
    </div>
    <div class="social-buttons">
        <a href="/oauth2/authorization/google" class="btn-social">
            <span>🌐</span> ${msg.get("login.continueWith", "Google")}
        </a>
        <a href="/oauth2/authorization/github" class="btn-social">
            <span>🐙</span> ${msg.get("login.continueWith", "GitHub")}
        </a>
        <a href="/oauth2/authorization/facebook" class="btn-social">
            <span>📘</span> ${msg.get("login.continueWith", "Facebook")}
        </a>

        <!-- Telegram Social Login: official widget if username set, otherwise fallback button -->
        @if(telegramBotUsername != null && !telegramBotUsername.isBlank())
            <div style="display: flex; justify-content: center; margin-top: 0.25rem;">
                <script async src="https://telegram.org/js/telegram-widget.js?22"
                        data-telegram-login="${telegramBotUsername}"
                        data-size="large"
                        data-auth-url="/login/telegram/callback"
                        data-request-access="write">
                </script>
            </div>
        @else
            <a href="/oauth2/authorization/telegram" class="btn-social">
                <span>✈️</span> ${msg.get("login.continueWith", "Telegram")}
            </a>
        @endif
    </div>
    <div class="footer">
        ${msg.get("login.noAccount")} <a href="/register">${msg.get("login.registerLink")}</a>
    </div>
</div>
</body>
</html>