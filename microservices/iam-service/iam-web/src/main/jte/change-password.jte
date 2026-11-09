@param kh.edu.istad.platform.iam.web.dto.ChangePasswordForm form = null
@param String successMessage = null
@param String errorMessage = null
@param String activeTab = "changePassword"
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("changePassword.title")} - ${msg.get("common.appName")}</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap" rel="stylesheet">
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

<div class="max-w-xl mx-auto space-y-6">

    <!-- Navigation Tabs -->
    <nav class="flex items-center gap-2 overflow-x-auto pb-2">
        <a href="/" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.overview")}
        </a>
        <a href="/profile/edit" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.editProfile")}
        </a>
        <a href="/profile/password" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#5e7c65] text-white shadow-sm">
            ${msg.get("nav.changePassword")}
        </a>
        <a href="/profile/accounts" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.linkedAccounts")}
        </a>
        <a href="/profile/apps" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.connectedApps")}
        </a>
    </nav>

    <div class="bg-[#fafbf9] border border-[#e5ebe4] rounded-3xl p-8 shadow-sm">
        <h2 class="text-xl font-bold text-[#2d3b31]">${msg.get("changePassword.title")}</h2>
        <p class="text-xs text-[#718575] mt-1 mb-6">${msg.get("changePassword.subtitle")}</p>

        @if(successMessage != null)
            <div class="mb-5 p-3.5 text-xs rounded-2xl bg-emerald-50 text-emerald-800 border border-emerald-200">
                ${successMessage}
            </div>
        @endif

        @if(errorMessage != null)
            <div class="mb-5 p-3.5 text-xs rounded-2xl bg-rose-50 text-rose-800 border border-rose-200">
                ${errorMessage}
            </div>
        @endif

        <form action="/profile/password" method="post" class="space-y-4">
            <div>
                <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("changePassword.currentPassword")}</label>
                <input type="password" name="currentPassword" required
                       class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3.5 px-4 outline-none"/>
            </div>
            <div>
                <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("changePassword.newPassword")}</label>
                <input type="password" name="newPassword" required
                       class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3.5 px-4 outline-none"/>
            </div>
            <div>
                <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("changePassword.confirmNewPassword")}</label>
                <input type="password" name="confirmPassword" required
                       class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3.5 px-4 outline-none"/>
            </div>

            <div class="pt-3">
                <button type="submit" class="w-full py-3.5 bg-[#5e7c65] hover:bg-[#4e6b55] text-white text-sm font-semibold rounded-full shadow-md transition-all">
                    ${msg.get("changePassword.submit")}
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>