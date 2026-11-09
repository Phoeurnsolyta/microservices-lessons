@param String errorMessage = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("register.title")} - ${msg.get("common.appName")}</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Tailwind CSS -->
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
<body class="bg-[#f0f3ee] text-[#2d3b31] min-h-screen flex items-center justify-center p-4">

<div class="w-full max-w-[440px] bg-[#fafbf9] rounded-[36px] shadow-2xl shadow-slate-300/40 p-8 sm:p-10 border border-[#e5ebe4]">

    <!-- Language Switcher -->
    <div class="flex justify-end mb-2">
        <div class="inline-flex items-center bg-[#dde3dc]/60 p-1 rounded-full text-xs font-semibold">
            <a href="?lang=en" class="px-3 py-1 rounded-full transition-all ${"en".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]"}">EN</a>
            <a href="?lang=km" class="px-3 py-1 rounded-full transition-all ${"km".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]"}">KM</a>
        </div>
    </div>

    <!-- Header -->
    <div class="text-center mb-6">
        <div class="w-14 h-14 mx-auto mb-3 flex items-center justify-center text-[#4c6552]">
            <svg class="w-12 h-12" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 3c-1.5 3-2 5.5-2 8 0 2.5 1 4 2 5 1-1 2-2.5 2-5 0-2.5-.5-5-2-8zm-3.5 3c-1.2 2.2-2.8 4.6-2.5 7 .3 2.4 2 4.2 4 5-.8-1.5-1.2-3.3-1.2-5.2 0-2.3.8-4.5 1.7-6.8zm7 0c.9 2.3 1.7 4.5 1.7 6.8 0 1.9-.4 3.7-1.2 5.2 2-.8 3.7-2.6 4-5 .3-2.4-1.3-4.8-2.5-7zm-10 4.5C4.2 12.2 3 14 3 16c0 2 2 3.5 4.5 3.8-1-1.3-1.6-3-1.5-4.8.1-1.6.8-3.1 1.5-4.5zm13 0c.7 1.4 1.4 2.9 1.5 4.5.1 1.8-.5 3.5-1.5 4.8 2.5-.3 4.5-1.8 4.5-3.8 0-2-1.2-3.8-2.5-5.5z"/>
            </svg>
        </div>
        <h1 class="text-2xl font-bold tracking-tight text-[#2d3b31]">${msg.get("register.title")}</h1>
        <p class="text-sm text-[#667a6c] mt-1">${msg.get("register.subtitle")}</p>
    </div>

    @if(errorMessage != null)
        <div class="mb-4 p-3 text-xs rounded-2xl bg-rose-50 text-rose-700 border border-rose-200 text-center font-medium">
            ${errorMessage}
        </div>
    @endif

    <form action="/register" method="post" class="space-y-3.5">
        @if(_csrf != null)
            <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
        @endif

        <!-- Given Name & Family Name in 2 columns -->
        <div class="grid grid-cols-2 gap-3">
            <input type="text" name="givenName" required
                   class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3 px-4 outline-none transition-all"
                   placeholder="${msg.get("register.givenName")}"/>
            <input type="text" name="familyName" required
                   class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3 px-4 outline-none transition-all"
                   placeholder="${msg.get("register.familyName")}"/>
        </div>

        <!-- Email -->
        <div class="relative">
                <span class="absolute inset-y-0 left-0 flex items-center pl-4 pointer-events-none text-[#718575]">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207"/></svg>
                </span>
            <input type="email" name="email" required
                   class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3 pl-12 pr-4 outline-none transition-all"
                   placeholder="${msg.get("register.email")}"/>
        </div>

        <!-- Username -->
        <div class="relative">
                <span class="absolute inset-y-0 left-0 flex items-center pl-4 pointer-events-none text-[#718575]">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
                </span>
            <input type="text" name="username" required
                   class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3 pl-12 pr-4 outline-none transition-all"
                   placeholder="${msg.get("register.username")}"/>
        </div>

        <!-- Password -->
        <div class="relative">
                <span class="absolute inset-y-0 left-0 flex items-center pl-4 pointer-events-none text-[#718575]">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/></svg>
                </span>
            <input type="password" name="password" required
                   class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3 pl-12 pr-4 outline-none transition-all"
                   placeholder="${msg.get("register.password")}"/>
        </div>

        <!-- Confirm Password -->
        <div class="relative">
                <span class="absolute inset-y-0 left-0 flex items-center pl-4 pointer-events-none text-[#718575]">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>
                </span>
            <input type="password" name="confirmPassword" required
                   class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3 pl-12 pr-4 outline-none transition-all"
                   placeholder="${msg.get("register.confirmPassword")}"/>
        </div>

        <!-- Preferred Locale Hidden or Selected -->
        <input type="hidden" name="locale" value="${msg.getCurrentLang()}"/>

        <!-- Submit Button (Pill) -->
        <button type="submit"
                class="w-full mt-3 py-3.5 bg-[#5e7c65] hover:bg-[#4e6b55] active:scale-[0.99] text-white font-medium text-base rounded-full shadow-md shadow-[#5e7c65]/30 transition-all">
            ${msg.get("register.submit")}
        </button>
    </form>

    <div class="mt-6 text-center text-xs text-[#718575]">
        ${msg.get("register.alreadyHaveAccount")}
        <a href="/login" class="text-[#4c6552] font-bold hover:underline ml-1">${msg.get("register.loginLink")}</a>
    </div>
</div>
</body>
</html>