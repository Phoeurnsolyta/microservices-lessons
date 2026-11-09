@param kh.edu.istad.platform.iam.web.dto.ChangePasswordForm form
@param String successMessage = null
@param String errorMessage = null
@param String activeTab = "changePassword"
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("changePassword.title")} - ${msg.get("common.appName")}</title>
    <style>
        :root {
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --bg: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #e2e8f0;
            --danger: #ef4444;
            --success: #22c55e;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: system-ui, -apple-system, sans-serif;
            background-color: var(--bg);
            color: var(--text-main);
            padding: 2rem 1rem;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid var(--border);
        }

        .nav-tabs {
            display: flex;
            gap: 0.5rem;
            margin-bottom: 2rem;
            border-bottom: 1px solid var(--border);
            padding-bottom: 0.5rem;
            flex-wrap: wrap;
        }

        .nav-tab {
            text-decoration: none;
            color: var(--text-muted);
            padding: 0.5rem 1rem;
            border-radius: 6px;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .nav-tab.active {
            background: var(--primary);
            color: white;
        }

        .card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 8px;
            padding: 2rem;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
        }

        .form-group {
            margin-bottom: 1.25rem;
        }

        label {
            display: block;
            font-size: 0.875rem;
            font-weight: 500;
            margin-bottom: 0.35rem;
        }

        input[type="password"] {
            width: 100%;
            padding: 0.65rem 0.85rem;
            border: 1px solid var(--border);
            border-radius: 6px;
            font-size: 0.95rem;
        }

        .btn-primary {
            width: 100%;
            background: var(--primary);
            color: white;
            border: none;
            padding: 0.75rem;
            font-size: 1rem;
            cursor: pointer;
            border-radius: 6px;
            font-weight: 600;
        }

        .btn-primary:hover {
            background: var(--primary-hover);
        }

        .alert {
            padding: 0.75rem 1rem;
            border-radius: 6px;
            font-size: 0.875rem;
            margin-bottom: 1.25rem;
        }

        .alert-success {
            background: #dcfce7;
            color: #166534;
        }

        .alert-danger {
            background: #fee2e2;
            color: #991b1b;
        }

        .lang-switch a {
            text-decoration: none;
            color: var(--text-muted);
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            border: 1px solid var(--border);
            font-size: 0.85rem;
        }

        .lang-switch a.active {
            background: var(--primary);
            color: white;
            border-color: var(--primary);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <div>
            <h1 style="color: var(--primary); font-size: 1.75rem;">${msg.get("changePassword.title")}</h1>
            <p style="color: var(--text-muted); font-size: 0.9rem;">${msg.get("changePassword.subtitle")}</p>
        </div>
        <div style="display: flex; gap: 1rem; align-items: center;">
            <div class="lang-switch">
                <a href="/profile/password?lang=en" class="${"en".equals(msg.getCurrentLang()) ? "active" : ""}">EN</a>
                <a href="/profile/password?lang=km" class="${"km".equals(msg.getCurrentLang()) ? "active" : ""}">KM</a>
            </div>
            <a href="/"
               style="text-decoration: none; color: var(--text-muted); font-size: 0.9rem;">${msg.get("common.back")}</a>
        </div>
    </div>

    <div class="nav-tabs">
        <a href="/" class="nav-tab">${msg.get("nav.overview")}</a>
        <a href="/profile/edit" class="nav-tab">${msg.get("nav.editProfile")}</a>
        <a href="/profile/password" class="nav-tab active">${msg.get("nav.changePassword")}</a>
        <a href="/profile/accounts" class="nav-tab">${msg.get("nav.linkedAccounts")}</a>
        <a href="/profile/apps" class="nav-tab">${msg.get("nav.connectedApps")}</a>
    </div>

    @if(successMessage != null)
        <div class="alert alert-success">${successMessage}</div>
    @endif
    @if(errorMessage != null)
        <div class="alert alert-danger">${errorMessage}</div>
    @endif

    <div class="card">
        <form action="/profile/password" method="post">
            @if(_csrf != null)
                <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
            @endif

            <div class="form-group">
                <label for="currentPassword">${msg.get("changePassword.currentPassword")}</label>
                <input type="password" id="currentPassword" name="currentPassword" required>
            </div>

            <div class="form-group">
                <label for="newPassword">${msg.get("changePassword.newPassword")}</label>
                <input type="password" id="newPassword" name="newPassword" required>
            </div>

            <div class="form-group">
                <label for="confirmPassword">${msg.get("changePassword.confirmNewPassword")}</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>

            <button type="submit" class="btn-primary"
                    style="margin-top: 0.5rem;">${msg.get("changePassword.submit")}</button>
        </form>
    </div>
</div>

</body>
</html>