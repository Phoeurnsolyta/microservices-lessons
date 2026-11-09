package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JtechangepasswordGenerated {
	public static final String JTE_NAME = "change-password.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,8,12,12,12,12,12,12,157,157,157,158,158,158,162,162,162,162,162,162,162,162,162,163,163,163,163,163,163,163,163,163,166,166,166,171,171,171,172,172,172,173,173,173,174,174,174,175,175,175,178,178,179,179,179,180,180,181,181,182,182,182,183,183,187,187,188,188,188,188,188,188,188,188,188,188,188,188,188,188,188,188,188,189,189,192,192,192,197,197,197,202,202,202,207,207,207,213,0,0,0,1,2,3,4,5,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, kh.edu.istad.platform.iam.web.dto.ChangePasswordForm form, String successMessage, String errorMessage, String activeTab, org.springframework.security.web.csrf.CsrfToken _csrf, kh.edu.istad.platform.iam.web.i18n.I18n msg) {
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
		jteOutput.writeUserContent(msg.get("changePassword.title"));
		jteOutput.writeContent(" - ");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("common.appName"));
		jteOutput.writeContent("</title>\r\n    <style>\r\n        :root {\r\n            --primary: #4f46e5;\r\n            --primary-hover: #4338ca;\r\n            --bg: #f8fafc;\r\n            --card-bg: #ffffff;\r\n            --text-main: #0f172a;\r\n            --text-muted: #64748b;\r\n            --border: #e2e8f0;\r\n            --danger: #ef4444;\r\n            --success: #22c55e;\r\n        }\r\n\r\n        * {\r\n            box-sizing: border-box;\r\n            margin: 0;\r\n            padding: 0;\r\n        }\r\n\r\n        body {\r\n            font-family: system-ui, -apple-system, sans-serif;\r\n            background-color: var(--bg);\r\n            color: var(--text-main);\r\n            padding: 2rem 1rem;\r\n        }\r\n\r\n        .container {\r\n            max-width: 600px;\r\n            margin: 0 auto;\r\n        }\r\n\r\n        .header {\r\n            display: flex;\r\n            justify-content: space-between;\r\n            align-items: center;\r\n            margin-bottom: 1.5rem;\r\n            padding-bottom: 1rem;\r\n            border-bottom: 1px solid var(--border);\r\n        }\r\n\r\n        .nav-tabs {\r\n            display: flex;\r\n            gap: 0.5rem;\r\n            margin-bottom: 2rem;\r\n            border-bottom: 1px solid var(--border);\r\n            padding-bottom: 0.5rem;\r\n            flex-wrap: wrap;\r\n        }\r\n\r\n        .nav-tab {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.5rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.9rem;\r\n            font-weight: 500;\r\n        }\r\n\r\n        .nav-tab.active {\r\n            background: var(--primary);\r\n            color: white;\r\n        }\r\n\r\n        .card {\r\n            background: var(--card-bg);\r\n            border: 1px solid var(--border);\r\n            border-radius: 8px;\r\n            padding: 2rem;\r\n            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);\r\n        }\r\n\r\n        .form-group {\r\n            margin-bottom: 1.25rem;\r\n        }\r\n\r\n        label {\r\n            display: block;\r\n            font-size: 0.875rem;\r\n            font-weight: 500;\r\n            margin-bottom: 0.35rem;\r\n        }\r\n\r\n        input[type=\"password\"] {\r\n            width: 100%;\r\n            padding: 0.65rem 0.85rem;\r\n            border: 1px solid var(--border);\r\n            border-radius: 6px;\r\n            font-size: 0.95rem;\r\n        }\r\n\r\n        .btn-primary {\r\n            width: 100%;\r\n            background: var(--primary);\r\n            color: white;\r\n            border: none;\r\n            padding: 0.75rem;\r\n            font-size: 1rem;\r\n            cursor: pointer;\r\n            border-radius: 6px;\r\n            font-weight: 600;\r\n        }\r\n\r\n        .btn-primary:hover {\r\n            background: var(--primary-hover);\r\n        }\r\n\r\n        .alert {\r\n            padding: 0.75rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.875rem;\r\n            margin-bottom: 1.25rem;\r\n        }\r\n\r\n        .alert-success {\r\n            background: #dcfce7;\r\n            color: #166534;\r\n        }\r\n\r\n        .alert-danger {\r\n            background: #fee2e2;\r\n            color: #991b1b;\r\n        }\r\n\r\n        .lang-switch a {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.25rem 0.5rem;\r\n            border-radius: 4px;\r\n            border: 1px solid var(--border);\r\n            font-size: 0.85rem;\r\n        }\r\n\r\n        .lang-switch a.active {\r\n            background: var(--primary);\r\n            color: white;\r\n            border-color: var(--primary);\r\n        }\r\n    </style>\r\n</head>\r\n<body>\r\n\r\n<div class=\"container\">\r\n    <div class=\"header\">\r\n        <div>\r\n            <h1 style=\"color: var(--primary); font-size: 1.75rem;\">");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(msg.get("changePassword.title"));
		jteOutput.writeContent("</h1>\r\n            <p style=\"color: var(--text-muted); font-size: 0.9rem;\">");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(msg.get("changePassword.subtitle"));
		jteOutput.writeContent("</p>\r\n        </div>\r\n        <div style=\"display: flex; gap: 1rem; align-items: center;\">\r\n            <div class=\"lang-switch\">\r\n                <a href=\"/profile/password?lang=en\"");
		var __jte_html_attribute_1 = "en".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">EN</a>\r\n                <a href=\"/profile/password?lang=km\"");
		var __jte_html_attribute_2 = "km".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">KM</a>\r\n            </div>\r\n            <a href=\"/\"\r\n               style=\"text-decoration: none; color: var(--text-muted); font-size: 0.9rem;\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("common.back"));
		jteOutput.writeContent("</a>\r\n        </div>\r\n    </div>\r\n\r\n    <div class=\"nav-tabs\">\r\n        <a href=\"/\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.overview"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/edit\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.editProfile"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/password\" class=\"nav-tab active\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.changePassword"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/accounts\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.linkedAccounts"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/apps\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.connectedApps"));
		jteOutput.writeContent("</a>\r\n    </div>\r\n\r\n    ");
		if (successMessage != null) {
			jteOutput.writeContent("\r\n        <div class=\"alert alert-success\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(successMessage);
			jteOutput.writeContent("</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n    ");
		if (errorMessage != null) {
			jteOutput.writeContent("\r\n        <div class=\"alert alert-danger\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(errorMessage);
			jteOutput.writeContent("</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n\r\n    <div class=\"card\">\r\n        <form action=\"/profile/password\" method=\"post\">\r\n            ");
		if (_csrf != null) {
			jteOutput.writeContent("\r\n                <input type=\"hidden\"");
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
			jteOutput.writeContent(">\r\n            ");
		}
		jteOutput.writeContent("\r\n\r\n            <div class=\"form-group\">\r\n                <label for=\"currentPassword\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("changePassword.currentPassword"));
		jteOutput.writeContent("</label>\r\n                <input type=\"password\" id=\"currentPassword\" name=\"currentPassword\" required>\r\n            </div>\r\n\r\n            <div class=\"form-group\">\r\n                <label for=\"newPassword\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("changePassword.newPassword"));
		jteOutput.writeContent("</label>\r\n                <input type=\"password\" id=\"newPassword\" name=\"newPassword\" required>\r\n            </div>\r\n\r\n            <div class=\"form-group\">\r\n                <label for=\"confirmPassword\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("changePassword.confirmNewPassword"));
		jteOutput.writeContent("</label>\r\n                <input type=\"password\" id=\"confirmPassword\" name=\"confirmPassword\" required>\r\n            </div>\r\n\r\n            <button type=\"submit\" class=\"btn-primary\"\r\n                    style=\"margin-top: 0.5rem;\">");
		jteOutput.setContext("button", null);
		jteOutput.writeUserContent(msg.get("changePassword.submit"));
		jteOutput.writeContent("</button>\r\n        </form>\r\n    </div>\r\n</div>\r\n\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		kh.edu.istad.platform.iam.web.dto.ChangePasswordForm form = (kh.edu.istad.platform.iam.web.dto.ChangePasswordForm)params.get("form");
		String successMessage = (String)params.getOrDefault("successMessage",  null);
		String errorMessage = (String)params.getOrDefault("errorMessage",  null);
		String activeTab = (String)params.getOrDefault("activeTab",  "changePassword");
		org.springframework.security.web.csrf.CsrfToken _csrf = (org.springframework.security.web.csrf.CsrfToken)params.getOrDefault("_csrf",  null);
		kh.edu.istad.platform.iam.web.i18n.I18n msg = (kh.edu.istad.platform.iam.web.i18n.I18n)params.get("msg");
		render(jteOutput, jteHtmlInterceptor, form, successMessage, errorMessage, activeTab, _csrf, msg);
	}
}
