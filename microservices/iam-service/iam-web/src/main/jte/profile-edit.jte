@param kh.edu.istad.platform.iam.web.dto.ProfileEditForm form
@param kh.edu.istad.platform.iam.domain.dto.UserResult user
@param String successMessage = null
@param String errorMessage = null
@param String activeTab = "editProfile"
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("profileEdit.title")} - ${msg.get("common.appName")}</title>
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
            max-width: 900px;
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

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
            margin-bottom: 1rem;
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

        .section-title {
            font-size: 1.1rem;
            font-weight: 600;
            margin: 1.5rem 0 1rem 0;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid var(--border);
        }

        .btn-primary {
            background: var(--primary);
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 6px;
            font-size: 1rem;
            cursor: pointer;
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
            <h1 style="color: var(--primary); font-size: 1.75rem;">${msg.get("profileEdit.title")}</h1>
            <p style="color: var(--text-muted); font-size: 0.9rem;">${msg.get("profileEdit.subtitle")}</p>
        </div>
        <div style="display: flex; gap: 1rem; align-items: center;">
            <div class="lang-switch">
                <a href="/profile/edit?lang=en" class="${"en".equals(msg.getCurrentLang()) ? "active" : ""}">EN</a>
                <a href="/profile/edit?lang=km" class="${"km".equals(msg.getCurrentLang()) ? "active" : ""}">KM</a>
            </div>
            <a href="/"
               style="text-decoration: none; color: var(--text-muted); font-size: 0.9rem;">${msg.get("common.back")}</a>
        </div>
    </div>

    <div class="nav-tabs">
        <a href="/" class="nav-tab">${msg.get("nav.overview")}</a>
        <a href="/profile/edit" class="nav-tab active">${msg.get("nav.editProfile")}</a>
        <a href="/profile/password" class="nav-tab">${msg.get("nav.changePassword")}</a>
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
        <form action="/profile/edit" method="post">
            @if(_csrf != null)
                <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
            @endif

            <div class="form-row">
                <div class="form-group">
                    <label for="givenName">${msg.get("register.givenName")}</label>
                    <input type="text" id="givenName" name="givenName"
                           value="${form != null ? form.getGivenName() : ""}" required>
                </div>
                <div class="form-group">
                    <label for="familyName">${msg.get("register.familyName")}</label>
                    <input type="text" id="familyName" name="familyName"
                           value="${form != null ? form.getFamilyName() : ""}" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="nickname">${msg.get("profileEdit.nickname")}</label>
                    <input type="text" id="nickname" name="nickname" value="${form != null ? form.getNickname() : ""}">
                </div>
                <div class="form-group">
                    <label for="gender">${msg.get("profileEdit.gender")}</label>
                    <select id="gender" name="gender">
                        <option value="">Select Gender</option>
                        <option value="male"
                                selected="${"male".equalsIgnoreCase(form != null ? form.getGender() : "")}">Male
                        </option>
                        <option value="female"
                                selected="${"female".equalsIgnoreCase(form != null ? form.getGender() : "")}">Female
                        </option>
                        <option value="other"
                                selected="${"other".equalsIgnoreCase(form != null ? form.getGender() : "")}">Other
                        </option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="phoneNumber">${msg.get("profileEdit.phone")}</label>
                    <input type="text" id="phoneNumber" name="phoneNumber"
                           value="${form != null ? form.getPhoneNumber() : ""}" placeholder="+85512345678">
                </div>
                <div class="form-group">
                    <label for="locale">${msg.get("profileEdit.locale")}</label>
                    <select id="locale" name="locale">
                        <option value="en" selected="${"en".equalsIgnoreCase(form != null ? form.getLocale() : "en")}">
                            English (en)
                        </option>
                        <option value="km" selected="${"km".equalsIgnoreCase(form != null ? form.getLocale() : "")}">
                            ភាសាខ្មែរ (km)
                        </option>
                    </select>
                </div>
            </div>

            <div class="section-title">${msg.get("profileEdit.addressHeader")}</div>

            <div class="form-group">
                <label for="streetAddress">${msg.get("profileEdit.streetAddress")}</label>
                <input type="text" id="streetAddress" name="streetAddress"
                       value="${form != null ? form.getStreetAddress() : ""}">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="locality">${msg.get("profileEdit.locality")}</label>
                    <input type="text" id="locality" name="locality" value="${form != null ? form.getLocality() : ""}">
                </div>
                <div class="form-group">
                    <label for="region">${msg.get("profileEdit.region")}</label>
                    <input type="text" id="region" name="region" value="${form != null ? form.getRegion() : ""}">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="postalCode">${msg.get("profileEdit.postalCode")}</label>
                    <input type="text" id="postalCode" name="postalCode"
                           value="${form != null ? form.getPostalCode() : ""}">
                </div>
                <div class="form-group">
                    <label for="country">${msg.get("profileEdit.country")}</label>
                    <input type="text" id="country" name="country" maxlength="2"
                           value="${form != null ? form.getCountry() : ""}" placeholder="KH">
                </div>
            </div>

            <button type="submit" class="btn-primary" style="margin-top: 1rem;">${msg.get("common.save")}</button>
        </form>
    </div>
</div>

</body>
</html>