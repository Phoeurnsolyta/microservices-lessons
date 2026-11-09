package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JteloginGenerated {
	public static final String JTE_NAME = "login.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,8,12,12,12,12,12,12,14,19,35,38,41,41,41,41,42,42,42,42,46,49,54,54,54,55,55,55,58,59,59,61,61,61,63,63,65,65,67,67,67,69,69,71,73,73,74,74,74,74,74,74,74,74,74,74,74,74,74,74,74,74,74,75,75,77,86,86,86,86,86,86,86,86,86,89,98,98,98,98,98,98,98,98,98,101,104,104,104,108,111,111,111,114,116,127,135,143,152,154,154,154,155,155,155,159,161,161,161,169,0,0,0,1,2,3,4,5,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, boolean hasError, boolean isLogout, boolean isRegistered, String telegramBotUsername, org.springframework.security.web.csrf.CsrfToken _csrf, kh.edu.istad.platform.iam.web.i18n.I18n msg) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html");
		var __jte_html_attribute_0 = msg.getCurrentLang();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" lang=\"");
			jteOutput.setContext("html", "lang");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("html", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("login.title"));
		jteOutput.writeContent(" - ");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("common.appName"));
		jteOutput.writeContent("</title>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n    <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap\" rel=\"stylesheet\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n    <script>\r\n        tailwind.config = {\r\n            theme: {\r\n                extend: {\r\n                    fontFamily: {\r\n                        sans: ['\"Kantumruy Pro\"', 'Inter', 'sans-serif'],\r\n                    }\r\n                }\r\n            }\r\n        }\r\n    </script>\r\n</head>\r\n<body class=\"bg-[#f0f3ee] text-[#2d3b31] min-h-screen flex items-center justify-center p-4\">\r\n\r\n");
		jteOutput.writeContent("\r\n<div class=\"w-full max-w-[420px] bg-[#fafbf9] rounded-[36px] shadow-2xl shadow-slate-300/40 p-8 sm:p-10 border border-[#e5ebe4]\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <div class=\"flex justify-end mb-2\">\r\n        <div class=\"inline-flex items-center bg-[#dde3dc]/60 p-1 rounded-full text-xs font-semibold\">\r\n            <a href=\"?lang=en\" class=\"px-3 py-1 rounded-full transition-all ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent("en".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31] hover:text-black");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">EN</a>\r\n            <a href=\"?lang=km\" class=\"px-3 py-1 rounded-full transition-all ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent("km".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31] hover:text-black");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">KM</a>\r\n        </div>\r\n    </div>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <div class=\"text-center mb-8\">\r\n        <div class=\"w-14 h-14 mx-auto mb-3 flex items-center justify-center text-[#4c6552]\">\r\n            ");
		jteOutput.writeContent("\r\n            <svg class=\"w-11 h-11\" viewBox=\"0 0 24 24\" fill=\"currentColor\">\r\n                <path fill-rule=\"evenodd\" d=\"M12 1.5a5.25 5.25 0 00-5.25 5.25v3a3 3 0 00-3 3v6.75a3 3 0 003 3h10.5a3 3 0 003-3v-6.75a3 3 0 00-3-3v-3c0-2.9-2.35-5.25-5.25-5.25zm3.75 8.25v-3a3.75 3.75 0 00-7.5 0v3h7.5z\" clip-rule=\"evenodd\"/>\r\n            </svg>\r\n        </div>\r\n        <h1 class=\"text-2xl font-bold tracking-tight text-[#2d3b31]\">");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(msg.get("login.title"));
		jteOutput.writeContent("</h1>\r\n        <p class=\"text-sm text-[#667a6c] mt-1.5\">");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(msg.get("login.subtitle"));
		jteOutput.writeContent("</p>\r\n    </div>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    ");
		if (hasError) {
			jteOutput.writeContent("\r\n        <div class=\"mb-5 p-3.5 text-xs rounded-2xl bg-rose-50 text-rose-700 border border-rose-200 text-center font-medium\">\r\n            ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(msg.get("login.invalidCredentials"));
			jteOutput.writeContent("\r\n        </div>\r\n    ");
		}
		jteOutput.writeContent("\r\n\r\n    ");
		if (isLogout) {
			jteOutput.writeContent("\r\n        <div class=\"mb-5 p-3.5 text-xs rounded-2xl bg-emerald-50 text-emerald-700 border border-emerald-200 text-center font-medium\">\r\n            ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(msg.get("login.loggedOut"));
			jteOutput.writeContent("\r\n        </div>\r\n    ");
		}
		jteOutput.writeContent("\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <form action=\"/login\" method=\"post\" class=\"space-y-4\">\r\n        ");
		if (_csrf != null) {
			jteOutput.writeContent("\r\n            <input type=\"hidden\"");
			var __jte_html_attribute_1 = _csrf.getParameterName();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
				jteOutput.writeContent(" name=\"");
				jteOutput.setContext("input", "name");
				jteOutput.writeUserContent(__jte_html_attribute_1);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			var __jte_html_attribute_2 = _csrf.getToken();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_2);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent("/>\r\n        ");
		}
		jteOutput.writeContent("\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <div class=\"relative\">\r\n                <span class=\"absolute inset-y-0 left-0 flex items-center pl-4 pointer-events-none text-[#718575]\">\r\n                    <svg class=\"w-5 h-5\" fill=\"none\" stroke=\"currentColor\" viewBox=\"0 0 24 24\">\r\n                        <path stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"2\" d=\"M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z\"/>\r\n                    </svg>\r\n                </span>\r\n            <input type=\"text\" id=\"username\" name=\"username\" required autofocus\r\n                   class=\"w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3.5 pl-12 pr-4 outline-none transition-all\"\r\n                  ");
		var __jte_html_attribute_3 = msg.get("login.usernameOrEmail");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
			jteOutput.writeContent(" placeholder=\"");
			jteOutput.setContext("input", "placeholder");
			jteOutput.writeUserContent(__jte_html_attribute_3);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("/>\r\n        </div>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <div class=\"relative\">\r\n                <span class=\"absolute inset-y-0 left-0 flex items-center pl-4 pointer-events-none text-[#718575]\">\r\n                    <svg class=\"w-5 h-5\" fill=\"none\" stroke=\"currentColor\" viewBox=\"0 0 24 24\">\r\n                        <path stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"2\" d=\"M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z\"/>\r\n                    </svg>\r\n                </span>\r\n            <input type=\"password\" id=\"password\" name=\"password\" required\r\n                   class=\"w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] placeholder-[#798e7e] text-sm rounded-2xl py-3.5 pl-12 pr-4 outline-none transition-all\"\r\n                  ");
		var __jte_html_attribute_4 = msg.get("login.password");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
			jteOutput.writeContent(" placeholder=\"");
			jteOutput.setContext("input", "placeholder");
			jteOutput.writeUserContent(__jte_html_attribute_4);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("/>\r\n        </div>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <button type=\"submit\"\r\n                class=\"w-full mt-2 py-3.5 bg-[#5e7c65] hover:bg-[#4e6b55] active:scale-[0.99] text-white font-medium text-base rounded-full shadow-md shadow-[#5e7c65]/30 transition-all\">\r\n            ");
		jteOutput.setContext("button", null);
		jteOutput.writeUserContent(msg.get("login.submit"));
		jteOutput.writeContent("\r\n        </button>\r\n    </form>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <div class=\"relative my-7\">\r\n        <div class=\"absolute inset-0 flex items-center\"><div class=\"w-full border-t border-[#d8e0d7]\"></div></div>\r\n        <div class=\"relative flex justify-center text-xs text-[#718575]\"><span class=\"bg-[#fafbf9] px-3 font-medium\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(msg.get("login.or"));
		jteOutput.writeContent("</span></div>\r\n    </div>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <div class=\"flex items-center justify-center gap-5\">\r\n        ");
		jteOutput.writeContent("\r\n        <a href=\"/oauth2/authorization/google\" title=\"Google\"\r\n           class=\"w-12 h-12 rounded-full bg-[#dde3dc]/60 hover:bg-[#dde3dc] flex items-center justify-center transition-all hover:scale-105 active:scale-95\">\r\n            <svg class=\"w-5 h-5\" viewBox=\"0 0 24 24\">\r\n                <path fill=\"#4285F4\" d=\"M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z\"/>\r\n                <path fill=\"#34A853\" d=\"M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z\"/>\r\n                <path fill=\"#FBBC05\" d=\"M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z\"/>\r\n                <path fill=\"#EA4335\" d=\"M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z\"/>\r\n            </svg>\r\n        </a>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <a href=\"/oauth2/authorization/github\" title=\"GitHub\"\r\n           class=\"w-12 h-12 rounded-full bg-[#dde3dc]/60 hover:bg-[#dde3dc] flex items-center justify-center transition-all hover:scale-105 active:scale-95 text-[#2d3b31]\">\r\n            <svg class=\"w-5 h-5 fill-current\" viewBox=\"0 0 16 16\">\r\n                <path d=\"M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z\"/>\r\n            </svg>\r\n        </a>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <a href=\"/oauth2/authorization/facebook\" title=\"Facebook\"\r\n           class=\"w-12 h-12 rounded-full bg-[#dde3dc]/60 hover:bg-[#dde3dc] flex items-center justify-center transition-all hover:scale-105 active:scale-95 text-[#1877F2]\">\r\n            <svg class=\"w-5 h-5 fill-current\" viewBox=\"0 0 24 24\">\r\n                <path d=\"M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z\"/>\r\n            </svg>\r\n        </a>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <a href=\"javascript:void(0)\" onclick=\"triggerTelegramLogin()\" title=\"Telegram\"\r\n           class=\"w-12 h-12 rounded-full bg-[#dde3dc]/60 hover:bg-[#dde3dc] flex items-center justify-center transition-all hover:scale-105 active:scale-95 text-[#2AABEE]\">\r\n            <svg class=\"w-5 h-5 fill-current\" viewBox=\"0 0 24 24\">\r\n                <path d=\"M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm4.64 6.8c-.15 1.58-.8 5.42-1.13 7.19-.14.75-.42 1-.68 1.03-.58.05-1.02-.38-1.58-.75-.88-.58-1.38-.94-2.23-1.5-.99-.65-.35-1.01.22-1.59.15-.15 2.71-2.48 2.76-2.69a.2.2 0 00-.05-.18c-.06-.05-.14-.03-.21-.02-.09.02-1.49.95-4.22 2.79-.4.27-.76.41-1.08.4-.36-.01-1.04-.2-1.55-.37-.63-.2-1.12-.31-1.08-.66.02-.18.27-.36.75-.55 2.92-1.27 4.86-2.11 5.83-2.51 2.78-1.16 3.35-1.36 3.73-1.36.08 0 .27.02.39.12.1.08.13.19.14.27-.01.06.01.24 0 .38z\"/>\r\n            </svg>\r\n        </a>\r\n    </div>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <div class=\"mt-8 text-center text-xs text-[#718575]\">\r\n        ");
		jteOutput.setContext("div", null);
		jteOutput.writeUserContent(msg.get("login.noAccount"));
		jteOutput.writeContent("\r\n        <a href=\"/register\" class=\"text-[#4c6552] font-bold hover:underline ml-1\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("login.registerLink"));
		jteOutput.writeContent("</a>\r\n    </div>\r\n</div>\r\n\r\n");
		jteOutput.writeContent("\r\n<script>\r\n    const botName = \"");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(telegramBotUsername);
		jteOutput.writeContent("\";\r\n    function triggerTelegramLogin() {\r\n        if (botName) {\r\n            window.open(\"https://t.me/\" + botName, \"_blank\");\r\n        }\r\n    }\r\n</script>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		boolean hasError = (boolean)params.getOrDefault("hasError",  false);
		boolean isLogout = (boolean)params.getOrDefault("isLogout",  false);
		boolean isRegistered = (boolean)params.getOrDefault("isRegistered",  false);
		String telegramBotUsername = (String)params.getOrDefault("telegramBotUsername",  "");
		org.springframework.security.web.csrf.CsrfToken _csrf = (org.springframework.security.web.csrf.CsrfToken)params.getOrDefault("_csrf",  null);
		kh.edu.istad.platform.iam.web.i18n.I18n msg = (kh.edu.istad.platform.iam.web.i18n.I18n)params.get("msg");
		render(jteOutput, jteHtmlInterceptor, hasError, isLogout, isRegistered, telegramBotUsername, _csrf, msg);
	}
}
