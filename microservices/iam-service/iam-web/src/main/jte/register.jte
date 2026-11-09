@param kh.edu.istad.platform.iam.web.dto.RegisterForm form
@param String errorMessage = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("register.title")} - ${msg.get("common.appName")}</title>
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
            padding: 1.5rem 1rem;
        }

        .card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 2.5rem;
            width: 100%;
            max-width: 500px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
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

        .alert-danger {
            background: #fee2e2;
            color: #991b1b;
            padding: 0.75rem 1rem;
            border-radius: 6px;
            font-size: 0.875rem;
            margin-bottom: 1.25rem;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }

        .form-group {
            margin-bottom: 1rem;
        }

        label {
            display: block;
            font-size: 0.875rem;
            font-weight: 500;
            margin-bottom: 0.35rem;
        }

        input, select {
            width: 100%;
            padding: 0.65rem 0.85rem;
            border: 1px solid var(--border);
            border-radius: 6px;
            font-size: 0.95rem;
        }

        input:focus, select:focus {
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

<div class="card">
    <div class="header">
        <h1>${msg.get("register.title")}</h1>
        <div class="lang-switch">
            <a href="/register?lang=en" class="${"en".equals(msg.getCurrentLang()) ? "active" : ""}">EN</a>
            <a href="/register?lang=km" class="${"km".equals(msg.getCurrentLang()) ? "active" : ""}">KM</a>
        </div>
    </div>

    @if(errorMessage != null)
        <div class="alert-danger">${errorMessage}</div>
    @endif

    <form action="/register" method="post">
        @if(_csrf != null)
            <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
        @endif

        <div class="form-row">
            <div class="form-group">
                <label for="givenName">${msg.get("register.givenName")}</label>
                <input type="text" id="givenName" name="givenName" value="${form != null ? form.getGivenName() : ""}"
                       required>
            </div>
            <div class="form-group">
                <label for="familyName">${msg.get("register.familyName")}</label>
                <input type="text" id="familyName" name="familyName" value="${form != null ? form.getFamilyName() : ""}"
                       required>
            </div>
        </div>

        <div class="form-group">
            <label for="username">${msg.get("register.username")}</label>
            <input type="text" id="username" name="username" value="${form != null ? form.getUsername() : ""}" required
                   autocomplete="username">
        </div>

        <div class="form-group">
            <label for="email">${msg.get("register.email")}</label>
            <input type="email" id="email" name="email" value="${form != null ? form.getEmail() : ""}" required
                   autocomplete="email">
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="password">${msg.get("register.password")}</label>
                <input type="password" id="password" name="password" required autocomplete="new-password">
            </div>
            <div class="form-group">
                <label for="confirmPassword">${msg.get("register.confirmPassword")}</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required autocomplete="new-password">
            </div>
        </div>

        <div class="form-group">
            <label for="locale">${msg.get("register.locale")}</label>
            <select id="locale" name="locale">
                <option value="en" selected>English (en)</option>
                <option value="km">ខ្មែរ (km)</option>
            </select>
        </div>

        <button type="submit" class="btn-primary">${msg.get("register.submit")}</button>
    </form>

    <div class="footer">
        ${msg.get("register.alreadyHaveAccount")} <a href="/login">${msg.get("register.loginLink")}</a>
    </div>
</div>

</body>
</html>