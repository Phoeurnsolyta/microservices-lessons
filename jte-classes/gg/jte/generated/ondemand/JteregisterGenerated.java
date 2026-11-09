package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JteregisterGenerated {
	public static final String JTE_NAME = "register.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,6,6,6,6,6,6,6,6,6,10,10,10,10,10,10,152,152,152,154,154,154,154,154,154,154,154,154,155,155,155,155,155,155,155,155,155,159,159,160,160,160,161,161,164,164,165,165,165,165,165,165,165,165,165,165,165,165,165,165,165,165,165,166,166,170,170,170,171,171,171,171,171,171,171,171,171,175,175,175,176,176,176,176,176,176,176,176,176,182,182,182,183,183,183,183,183,183,183,183,183,188,188,188,189,189,189,189,189,189,189,189,189,195,195,195,199,199,199,205,205,205,212,212,212,216,216,216,216,216,216,221,0,0,0,1,2,3,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, kh.edu.istad.platform.iam.web.dto.RegisterForm form, String errorMessage, org.springframework.security.web.csrf.CsrfToken _csrf, kh.edu.istad.platform.iam.web.i18n.I18n msg) {
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
		jteOutput.writeUserContent(msg.get("register.title"));
		jteOutput.writeContent(" - ");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("common.appName"));
		jteOutput.writeContent("</title>\r\n    <style>\r\n        :root {\r\n            --primary: #4f46e5;\r\n            --primary-hover: #4338ca;\r\n            --bg: #f1f5f9;\r\n            --card-bg: #ffffff;\r\n            --text-main: #0f172a;\r\n            --text-muted: #64748b;\r\n            --border: #cbd5e1;\r\n            --danger: #ef4444;\r\n        }\r\n\r\n        * {\r\n            box-sizing: border-box;\r\n            margin: 0;\r\n            padding: 0;\r\n        }\r\n\r\n        body {\r\n            font-family: system-ui, -apple-system, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;\r\n            background: var(--bg);\r\n            color: var(--text-main);\r\n            min-height: 100vh;\r\n            display: flex;\r\n            align-items: center;\r\n            justify-content: center;\r\n            padding: 1.5rem 1rem;\r\n        }\r\n\r\n        .card {\r\n            background: var(--card-bg);\r\n            border: 1px solid var(--border);\r\n            border-radius: 12px;\r\n            padding: 2.5rem;\r\n            width: 100%;\r\n            max-width: 500px;\r\n            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);\r\n        }\r\n\r\n        .header {\r\n            display: flex;\r\n            justify-content: space-between;\r\n            align-items: center;\r\n            margin-bottom: 1.5rem;\r\n        }\r\n\r\n        .header h1 {\r\n            font-size: 1.5rem;\r\n            font-weight: 700;\r\n            color: var(--primary);\r\n        }\r\n\r\n        .lang-switch a {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            font-size: 0.8rem;\r\n            padding: 0.2rem 0.4rem;\r\n            border: 1px solid var(--border);\r\n            border-radius: 4px;\r\n        }\r\n\r\n        .lang-switch a.active {\r\n            background: var(--primary);\r\n            color: #fff;\r\n            border-color: var(--primary);\r\n        }\r\n\r\n        .alert-danger {\r\n            background: #fee2e2;\r\n            color: #991b1b;\r\n            padding: 0.75rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.875rem;\r\n            margin-bottom: 1.25rem;\r\n        }\r\n\r\n        .form-row {\r\n            display: grid;\r\n            grid-template-columns: 1fr 1fr;\r\n            gap: 1rem;\r\n        }\r\n\r\n        .form-group {\r\n            margin-bottom: 1rem;\r\n        }\r\n\r\n        label {\r\n            display: block;\r\n            font-size: 0.875rem;\r\n            font-weight: 500;\r\n            margin-bottom: 0.35rem;\r\n        }\r\n\r\n        input, select {\r\n            width: 100%;\r\n            padding: 0.65rem 0.85rem;\r\n            border: 1px solid var(--border);\r\n            border-radius: 6px;\r\n            font-size: 0.95rem;\r\n        }\r\n\r\n        input:focus, select:focus {\r\n            outline: 2px solid var(--primary);\r\n            border-color: transparent;\r\n        }\r\n\r\n        .btn-primary {\r\n            width: 100%;\r\n            background: var(--primary);\r\n            color: white;\r\n            border: none;\r\n            padding: 0.75rem;\r\n            font-size: 1rem;\r\n            font-weight: 600;\r\n            border-radius: 6px;\r\n            cursor: pointer;\r\n            margin-top: 0.5rem;\r\n        }\r\n\r\n        .btn-primary:hover {\r\n            background: var(--primary-hover);\r\n        }\r\n\r\n        .footer {\r\n            margin-top: 1.5rem;\r\n            text-align: center;\r\n            font-size: 0.875rem;\r\n            color: var(--text-muted);\r\n        }\r\n\r\n        .footer a {\r\n            color: var(--primary);\r\n            text-decoration: none;\r\n            font-weight: 600;\r\n        }\r\n    </style>\r\n</head>\r\n<body>\r\n\r\n<div class=\"card\">\r\n    <div class=\"header\">\r\n        <h1>");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(msg.get("register.title"));
		jteOutput.writeContent("</h1>\r\n        <div class=\"lang-switch\">\r\n            <a href=\"/register?lang=en\"");
		var __jte_html_attribute_1 = "en".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">EN</a>\r\n            <a href=\"/register?lang=km\"");
		var __jte_html_attribute_2 = "km".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">KM</a>\r\n        </div>\r\n    </div>\r\n\r\n    ");
		if (errorMessage != null) {
			jteOutput.writeContent("\r\n        <div class=\"alert-danger\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(errorMessage);
			jteOutput.writeContent("</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n\r\n    <form action=\"/register\" method=\"post\">\r\n        ");
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
		jteOutput.writeContent("\r\n\r\n        <div class=\"form-row\">\r\n            <div class=\"form-group\">\r\n                <label for=\"givenName\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.givenName"));
		jteOutput.writeContent("</label>\r\n                <input type=\"text\" id=\"givenName\" name=\"givenName\"");
		var __jte_html_attribute_5 = form != null ? form.getGivenName() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_5);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                       required>\r\n            </div>\r\n            <div class=\"form-group\">\r\n                <label for=\"familyName\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.familyName"));
		jteOutput.writeContent("</label>\r\n                <input type=\"text\" id=\"familyName\" name=\"familyName\"");
		var __jte_html_attribute_6 = form != null ? form.getFamilyName() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_6);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                       required>\r\n            </div>\r\n        </div>\r\n\r\n        <div class=\"form-group\">\r\n            <label for=\"username\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.username"));
		jteOutput.writeContent("</label>\r\n            <input type=\"text\" id=\"username\" name=\"username\"");
		var __jte_html_attribute_7 = form != null ? form.getUsername() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_7)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_7);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" required\r\n                   autocomplete=\"username\">\r\n        </div>\r\n\r\n        <div class=\"form-group\">\r\n            <label for=\"email\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.email"));
		jteOutput.writeContent("</label>\r\n            <input type=\"email\" id=\"email\" name=\"email\"");
		var __jte_html_attribute_8 = form != null ? form.getEmail() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_8)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_8);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" required\r\n                   autocomplete=\"email\">\r\n        </div>\r\n\r\n        <div class=\"form-row\">\r\n            <div class=\"form-group\">\r\n                <label for=\"password\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.password"));
		jteOutput.writeContent("</label>\r\n                <input type=\"password\" id=\"password\" name=\"password\" required autocomplete=\"new-password\">\r\n            </div>\r\n            <div class=\"form-group\">\r\n                <label for=\"confirmPassword\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.confirmPassword"));
		jteOutput.writeContent("</label>\r\n                <input type=\"password\" id=\"confirmPassword\" name=\"confirmPassword\" required autocomplete=\"new-password\">\r\n            </div>\r\n        </div>\r\n\r\n        <div class=\"form-group\">\r\n            <label for=\"locale\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.locale"));
		jteOutput.writeContent("</label>\r\n            <select id=\"locale\" name=\"locale\">\r\n                <option value=\"en\" selected>English (en)</option>\r\n                <option value=\"km\">ខ្មែរ (km)</option>\r\n            </select>\r\n        </div>\r\n\r\n        <button type=\"submit\" class=\"btn-primary\">");
		jteOutput.setContext("button", null);
		jteOutput.writeUserContent(msg.get("register.submit"));
		jteOutput.writeContent("</button>\r\n    </form>\r\n\r\n    <div class=\"footer\">\r\n        ");
		jteOutput.setContext("div", null);
		jteOutput.writeUserContent(msg.get("register.alreadyHaveAccount"));
		jteOutput.writeContent(" <a href=\"/login\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("register.loginLink"));
		jteOutput.writeContent("</a>\r\n    </div>\r\n</div>\r\n\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		kh.edu.istad.platform.iam.web.dto.RegisterForm form = (kh.edu.istad.platform.iam.web.dto.RegisterForm)params.get("form");
		String errorMessage = (String)params.getOrDefault("errorMessage",  null);
		org.springframework.security.web.csrf.CsrfToken _csrf = (org.springframework.security.web.csrf.CsrfToken)params.getOrDefault("_csrf",  null);
		kh.edu.istad.platform.iam.web.i18n.I18n msg = (kh.edu.istad.platform.iam.web.i18n.I18n)params.get("msg");
		render(jteOutput, jteHtmlInterceptor, form, errorMessage, _csrf, msg);
	}
}
