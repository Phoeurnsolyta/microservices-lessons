@param kh.edu.istad.platform.iam.domain.dto.UserResult user = null
@param kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult authorities = null
@param String activeTab = "overview"
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("profile.title")} - ${msg.get("common.appName")}</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: {
                        sans: ['"Kantumruy Pro"', 'Inter', 'sans-serif'],
                    }
                }
            }
        }
    </script>
</head>
<body class="bg-[#f0f3ee] text-[#2d3b31] min-h-screen p-4 sm:p-8">

<div class="max-w-4xl mx-auto space-y-6">

    <!-- Top App Bar -->
    <header class="bg-[#fafbf9] border border-[#e5ebe4] rounded-3xl p-6 flex flex-col sm:flex-row items-center justify-between gap-4 shadow-sm">
        <div class="flex items-center gap-4">
            <div class="w-12 h-12 rounded-full bg-[#dde3dc] flex items-center justify-center text-[#4c6552] font-bold text-lg">
                ${user != null && user.givenName() != null ? user.givenName().substring(0, 1) : "U"}
            </div>
            <div>
                <h2 class="text-xl font-bold text-[#2d3b31]">
                    ${user != null ? (user.givenName() + " " + user.familyName()) : "User"}
                </h2>
                <p class="text-xs text-[#718575]">${user != null ? user.email() : ""}</p>
            </div>
        </div>

        <div class="flex items-center gap-3">
            <!-- Language Switcher -->
            <div class="inline-flex items-center bg-[#dde3dc]/60 p-1 rounded-full text-xs font-semibold">
                <a href="?lang=en"
                   class="px-3 py-1 rounded-full ${"en".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]"}">EN</a>
                <a href="?lang=km"
                   class="px-3 py-1 rounded-full ${"km".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]"}">KM</a>
            </div>
            <!-- Logout Form with CSRF Token -->
            <form action="/logout" method="post" class="inline">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                @endif
                <button type="submit"
                        class="px-4 py-1.5 rounded-full bg-[#dde3dc] hover:bg-rose-100 hover:text-rose-700 text-xs font-medium transition-colors">
                    ${msg.get("common.logout")}
                </button>
            </form>
        </div>
    </header>

    <!-- Navigation Tabs (Pill style) -->
    <nav class="flex items-center gap-2 overflow-x-auto pb-2">
        <a href="/"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all ${"overview".equals(activeTab) ? "bg-[#5e7c65] text-white shadow-sm" : "bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]"}">
            ${msg.get("nav.overview")}
        </a>
        <a href="/profile/edit"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.editProfile")}
        </a>
        <a href="/profile/password"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.changePassword")}
        </a>
        <a href="/profile/accounts"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.linkedAccounts")}
        </a>
        <a href="/profile/apps"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.connectedApps")}
        </a>
    </nav>

    <!-- Main Card -->
    <div class="bg-[#fafbf9] border border-[#e5ebe4] rounded-3xl p-8 shadow-sm space-y-8">

        <!-- Standard Claims Section -->
        <div>
            <h3 class="text-base font-bold text-[#2d3b31] mb-4">${msg.get("profile.claims")}</h3>
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                <div class="p-4 bg-[#dde3dc]/40 rounded-2xl">
                    <span class="text-xs text-[#718575] block">Username</span>
                    <span class="text-sm font-semibold text-[#2d3b31]">${user != null ? user.username() : "-"}</span>
                </div>
                <div class="p-4 bg-[#dde3dc]/40 rounded-2xl">
                    <span class="text-xs text-[#718575] block">Email</span>
                    <span class="text-sm font-semibold text-[#2d3b31]">${user != null ? user.email() : "-"}</span>
                </div>
                <div class="p-4 bg-[#dde3dc]/40 rounded-2xl">
                    <span class="text-xs text-[#718575] block">Subject (UUID)</span>
                    <span class="text-xs font-mono text-[#2d3b31] truncate block">${user != null ? String.valueOf(user.uuid()) : "-"}</span>
                </div>
                <div class="p-4 bg-[#dde3dc]/40 rounded-2xl">
                    <span class="text-xs text-[#718575] block">Email Verified</span>
                    <span class="text-sm font-semibold text-[#2d3b31]">${user != null && user.emailVerified() ? "Verified" : "Unverified"}</span>
                </div>
                <div class="p-4 bg-[#dde3dc]/40 rounded-2xl">
                    <span class="text-xs text-[#718575] block">Preferred Locale</span>
                    <span class="text-sm font-semibold uppercase text-[#2d3b31]">${user != null ? user.locale() : "en"}</span>
                </div>
                <div class="p-4 bg-[#dde3dc]/40 rounded-2xl">
                    <span class="text-xs text-[#718575] block">Status</span>
                    <span class="inline-block mt-0.5 px-2.5 py-0.5 bg-emerald-100 text-emerald-800 rounded-full text-xs font-semibold">
                            ${user != null && user.enabled() ? "Active" : "Disabled"}
                        </span>
                </div>
            </div>
        </div>

        <!-- Assigned Roles -->
        <div>
            <h3 class="text-base font-bold text-[#2d3b31] mb-3">${msg.get("profile.roles")}</h3>
            <div class="flex flex-wrap gap-2">
                @if(authorities != null && authorities.roles() != null && !authorities.roles().isEmpty())
                    @for(kh.edu.istad.platform.iam.domain.dto.RoleAuthorityResult role : authorities.roles())
                        <span class="px-4 py-1.5 rounded-full bg-[#dde3dc] text-[#3d4f42] text-xs font-semibold">
                                ${role.name()}
                            </span>
                    @endfor
                @else
                    <span class="text-xs text-[#718575]">No roles assigned.</span>
                @endif
            </div>
        </div>

        <!-- Effective Permissions -->
        <div>
            <h3 class="text-base font-bold text-[#2d3b31] mb-3">${msg.get("profile.permissions")}</h3>
            <div class="flex flex-wrap gap-2">
                @if(authorities != null && authorities.effectivePermissions() != null && !authorities.effectivePermissions().isEmpty())
                    @for(String perm : authorities.effectivePermissions())
                        <span class="px-3.5 py-1.5 rounded-full bg-[#e8eee7] border border-[#d8e0d7] text-[#4c6552] text-xs font-mono font-medium">
                                ${perm}
                            </span>
                    @endfor
                @else
                    <span class="text-xs text-[#718575]">No direct permissions.</span>
                @endif
            </div>
        </div>

    </div>
</div>
</body>
</html>