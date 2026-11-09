package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JteloginGenerated {
	public static final String JTE_NAME = "login.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,8,12,12,12,12,12,12,202,202,202,204,204,204,204,204,204,204,204,204,205,205,205,205,205,205,205,205,205,208,208,209,209,209,210,210,211,211,212,212,212,213,213,214,214,216,216,218,218,219,219,219,219,219,219,219,219,219,219,219,219,219,219,219,219,219,220,220,222,222,222,226,226,226,229,229,229,232,232,232,236,236,236,239,239,239,242,242,242,245,246,246,249,249,249,249,249,249,249,249,249,255,255,257,257,257,259,259,262,262,262,262,262,262,266,0,0,0,1,2,3,4,5,0,0,0};
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
		jteOutput.writeContent("</title>\r\n    <style>\r\n        :root {\r\n            --primary: #4f46e5;\r\n            --primary-hover: #4338ca;\r\n            --bg: #f1f5f9;\r\n            --card-bg: #ffffff;\r\n            --text-main: #0f172a;\r\n            --text-muted: #64748b;\r\n            --border: #cbd5e1;\r\n            --danger: #ef4444;\r\n            --success: #22c55e;\r\n        }\r\n\r\n        * {\r\n            box-sizing: border-box;\r\n            margin: 0;\r\n            padding: 0;\r\n        }\r\n\r\n        body {\r\n            font-family: system-ui, -apple-system, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;\r\n            background: var(--bg);\r\n            color: var(--text-main);\r\n            min-height: 100vh;\r\n            display: flex;\r\n            align-items: center;\r\n            justify-content: center;\r\n            padding: 1rem;\r\n        }\r\n\r\n        .auth-card {\r\n            background: var(--card-bg);\r\n            border: 1px solid var(--border);\r\n            border-radius: 12px;\r\n            padding: 2.5rem;\r\n            width: 100%;\r\n            max-width: 440px;\r\n            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -2px rgba(0, 0, 0, 0.05);\r\n        }\r\n\r\n        .header {\r\n            display: flex;\r\n            justify-content: space-between;\r\n            align-items: center;\r\n            margin-bottom: 1.5rem;\r\n        }\r\n\r\n        .header h1 {\r\n            font-size: 1.5rem;\r\n            font-weight: 700;\r\n            color: var(--primary);\r\n        }\r\n\r\n        .lang-switch a {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            font-size: 0.8rem;\r\n            padding: 0.2rem 0.4rem;\r\n            border: 1px solid var(--border);\r\n            border-radius: 4px;\r\n        }\r\n\r\n        .lang-switch a.active {\r\n            background: var(--primary);\r\n            color: #fff;\r\n            border-color: var(--primary);\r\n        }\r\n\r\n        .alert {\r\n            padding: 0.75rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.875rem;\r\n            margin-bottom: 1.25rem;\r\n        }\r\n\r\n        .alert-danger {\r\n            background: #fee2e2;\r\n            color: #991b1b;\r\n        }\r\n\r\n        .alert-success {\r\n            background: #dcfce7;\r\n            color: #166534;\r\n        }\r\n\r\n        .form-group {\r\n            margin-bottom: 1rem;\r\n        }\r\n\r\n        label {\r\n            display: block;\r\n            font-size: 0.875rem;\r\n            font-weight: 500;\r\n            margin-bottom: 0.35rem;\r\n            color: var(--text-main);\r\n        }\r\n\r\n        input[type=\"text\"], input[type=\"password\"] {\r\n            width: 100%;\r\n            padding: 0.65rem 0.85rem;\r\n            border: 1px solid var(--border);\r\n            border-radius: 6px;\r\n            font-size: 0.95rem;\r\n        }\r\n\r\n        input:focus {\r\n            outline: 2px solid var(--primary);\r\n            border-color: transparent;\r\n        }\r\n\r\n        .btn-primary {\r\n            width: 100%;\r\n            background: var(--primary);\r\n            color: white;\r\n            border: none;\r\n            padding: 0.75rem;\r\n            font-size: 1rem;\r\n            font-weight: 600;\r\n            border-radius: 6px;\r\n            cursor: pointer;\r\n            margin-top: 0.5rem;\r\n        }\r\n\r\n        .btn-primary:hover {\r\n            background: var(--primary-hover);\r\n        }\r\n\r\n        .divider {\r\n            display: flex;\r\n            align-items: center;\r\n            text-align: center;\r\n            margin: 1.5rem 0;\r\n            color: var(--text-muted);\r\n            font-size: 0.8rem;\r\n        }\r\n\r\n        .divider::before, .divider::after {\r\n            content: '';\r\n            flex: 1;\r\n            border-bottom: 1px solid var(--border);\r\n        }\r\n\r\n        .divider span {\r\n            padding: 0 0.5rem;\r\n        }\r\n\r\n        .social-buttons {\r\n            display: flex;\r\n            flex-direction: column;\r\n            gap: 0.6rem;\r\n        }\r\n\r\n        .btn-social {\r\n            display: flex;\r\n            align-items: center;\r\n            justify-content: center;\r\n            gap: 0.5rem;\r\n            padding: 0.65rem;\r\n            border: 1px solid var(--border);\r\n            border-radius: 6px;\r\n            text-decoration: none;\r\n            color: var(--text-main);\r\n            font-size: 0.9rem;\r\n            font-weight: 500;\r\n            background: #fff;\r\n            transition: background 0.15s;\r\n        }\r\n\r\n        .btn-social:hover {\r\n            background: #f8fafc;\r\n        }\r\n\r\n        .footer {\r\n            margin-top: 1.5rem;\r\n            text-align: center;\r\n            font-size: 0.875rem;\r\n            color: var(--text-muted);\r\n        }\r\n\r\n        .footer a {\r\n            color: var(--primary);\r\n            text-decoration: none;\r\n            font-weight: 600;\r\n        }\r\n    </style>\r\n</head>\r\n<body>\r\n<div class=\"auth-card\">\r\n    <div class=\"header\">\r\n        <h1>");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(msg.get("login.title"));
		jteOutput.writeContent("</h1>\r\n        <div class=\"lang-switch\">\r\n            <a href=\"/login?lang=en\"");
		var __jte_html_attribute_1 = "en".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">EN</a>\r\n            <a href=\"/login?lang=km\"");
		var __jte_html_attribute_2 = "km".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">KM</a>\r\n        </div>\r\n    </div>\r\n    ");
		if (hasError) {
			jteOutput.writeContent("\r\n        <div class=\"alert alert-danger\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(msg.get("login.invalidCredentials"));
			jteOutput.writeContent("</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n    ");
		if (isLogout) {
			jteOutput.writeContent("\r\n        <div class=\"alert alert-success\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(msg.get("login.loggedOut"));
			jteOutput.writeContent("</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n    ");
		if (isRegistered) {
			jteOutput.writeContent("\r\n        <div class=\"alert alert-success\">Registration successful! Please sign in with your credentials.</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n    <form action=\"/login\" method=\"post\">\r\n        ");
		if (_csrf != null) {
			jteOutput.writeContent("\r\n            <input type=\"hidden\"");
			var __jte_html_attribute_3 = _csrf.getParameterName();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
				jteOutput.writeContent(" name=\"");
				jteOutput.setContext("input", "name");
				jteOutput.writeUserContent(__jte_html_attribute_3);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			var __jte_html_attribute_4 = _csrf.getToken();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_4);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\r\n        ");
		}
		jteOutput.writeContent("\r\n        <div class=\"form-group\">\r\n            <label for=\"username\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("login.usernameOrEmail"));
		jteOutput.writeContent("</label>\r\n            <input type=\"text\" id=\"username\" name=\"username\" required autofocus autocomplete=\"username\">\r\n        </div>\r\n        <div class=\"form-group\">\r\n            <label for=\"password\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("login.password"));
		jteOutput.writeContent("</label>\r\n            <input type=\"password\" id=\"password\" name=\"password\" required autocomplete=\"current-password\">\r\n        </div>\r\n        <button type=\"submit\" class=\"btn-primary\">");
		jteOutput.setContext("button", null);
		jteOutput.writeUserContent(msg.get("login.submit"));
		jteOutput.writeContent("</button>\r\n    </form>\r\n    <div class=\"divider\">\r\n        <span>");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(msg.get("login.or"));
		jteOutput.writeContent("</span>\r\n    </div>\r\n    <div class=\"social-buttons\">\r\n        <a href=\"/oauth2/authorization/google\" class=\"btn-social\">\r\n            <span>🌐</span> ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("login.continueWith", "Google"));
		jteOutput.writeContent("\r\n        </a>\r\n        <a href=\"/oauth2/authorization/github\" class=\"btn-social\">\r\n            <span>🐙</span> ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("login.continueWith", "GitHub"));
		jteOutput.writeContent("\r\n        </a>\r\n        <a href=\"/oauth2/authorization/facebook\" class=\"btn-social\">\r\n            <span>📘</span> ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("login.continueWith", "Facebook"));
		jteOutput.writeContent("\r\n        </a>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        ");
		if (telegramBotUsername != null && !telegramBotUsername.isBlank()) {
			jteOutput.writeContent("\r\n            <div style=\"display: flex; justify-content: center; margin-top: 0.25rem;\">\r\n                <script async src=\"https://telegram.org/js/telegram-widget.js?22\"\r\n                       ");
			var __jte_html_attribute_5 = telegramBotUsername;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
				jteOutput.writeContent(" data-telegram-login=\"");
				jteOutput.setContext("script", "data-telegram-login");
				jteOutput.writeUserContent(__jte_html_attribute_5);
				jteOutput.setContext("script", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent("\r\n                        data-size=\"large\"\r\n                        data-auth-url=\"/login/telegram/callback\"\r\n                        data-request-access=\"write\">\r\n                </script>\r\n            </div>\r\n        ");
		} else {
			jteOutput.writeContent("\r\n            <a href=\"/oauth2/authorization/telegram\" class=\"btn-social\">\r\n                <span>✈️</span> ");
			jteOutput.setContext("a", null);
			jteOutput.writeUserContent(msg.get("login.continueWith", "Telegram"));
			jteOutput.writeContent("\r\n            </a>\r\n        ");
		}
		jteOutput.writeContent("\r\n    </div>\r\n    <div class=\"footer\">\r\n        ");
		jteOutput.setContext("div", null);
		jteOutput.writeUserContent(msg.get("login.noAccount"));
		jteOutput.writeContent(" <a href=\"/register\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("login.registerLink"));
		jteOutput.writeContent("</a>\r\n    </div>\r\n</div>\r\n</body>\r\n</html>");
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
