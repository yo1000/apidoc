package com.yo1000.apidoc.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yo1000.apidoc.model.Document;
import com.yo1000.apidoc.model.Parameter;
import com.yo1000.apidoc.model.RequestHeader;
import com.yo1000.apidoc.model.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoichi.kikuchi on 2015/07/28.
 */
public class DocumentMapFormatter {
    public void format(Map<String, Document> documentMap) throws IOException {
        Map<String, StringBuilder> bodyBuilderMap = new HashMap<String, StringBuilder>();
        Map<String, StringBuilder> headBuilderMap = new HashMap<String, StringBuilder>();
        Map<String, String> titleMap = new HashMap<String, String>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        for (Map.Entry<String, Document> docEntry : documentMap.entrySet()) {
            String endpointUrl = docEntry.getKey();
            String[] names = endpointUrl.replaceAll("^/", "").replaceAll("\\?.*$", "").split("/");
            int firstVarIndex = 0;

            for (String n : names) {
                if (n.startsWith("{")) {
                    break;
                }

                firstVarIndex++;
            }

            int firstNameIndex = 0;

            if (firstVarIndex == 0) {
                firstNameIndex = 1;
            }
            else {
                firstNameIndex = firstVarIndex - 1;
            }

            StringBuilder filenameBuilder = new StringBuilder();
            StringBuilder endpointBuilder = new StringBuilder();
            StringBuilder subEndpointBuilder = new StringBuilder();

            for (int i = firstNameIndex; i < names.length; i++) {
                if (names[i].startsWith("{")) {
                    continue;
                }

                if (i <= 2 + firstNameIndex) {
                    if (filenameBuilder.length() > 0) {
                        filenameBuilder.append("-");
                        endpointBuilder.append(" ");
                    }

                    filenameBuilder.append(names[i]);
                    endpointBuilder.append(names[i]);
                }

                if (subEndpointBuilder.length() > 0) {
                    subEndpointBuilder.append(" ");
                }

                subEndpointBuilder.append(names[i]);
            }

            String filename = filenameBuilder.append(".html").toString().toLowerCase();

            String subEndpointTitle = subEndpointBuilder
                    .replace(0, 1, ((Character) (char) endpointBuilder.charAt(0)).toString().toUpperCase())
                    .append(" ").toString();

            titleMap.put(filename, endpointBuilder
                    .replace(0, 1, ((Character) (char) endpointBuilder.charAt(0)).toString().toUpperCase())
                    .append(" ").append("API").toString());

            StringBuilder headBuilder = headBuilderMap.containsKey(filename)
                    ? headBuilderMap.get(filename)
                    : new StringBuilder();

            headBuilderMap.put(filename, headBuilder);

            if (headBuilder.length() == 0) {
                headBuilder.append("<head>\n")
                        .append("<meta charset=\"utf-8\">\n")
                        .append("<title>").append(subEndpointTitle).append("</title>\n")
                        .append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n")
                        .append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\">\n")
                        .append("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n")
                        .append("</head>\n");
            }

            StringBuilder bodyBuilder = bodyBuilderMap.containsKey(filename)
                    ? bodyBuilderMap.get(filename)
                    : new StringBuilder();

            bodyBuilderMap.put(filename, bodyBuilder);

            bodyBuilder.append("<h2>").append(subEndpointTitle).append("</h2>\n");

            bodyBuilder.append("<h3>").append("Request").append("</h3>\n");

            bodyBuilder.append("<h4>").append("URL").append("</h4>\n");
            bodyBuilder.append("<p>")
                    .append(endpointUrl.replace("&", "&amp;").replace("{", "<code>").replace("}", "</code>"))
                    .append("</p>\n");

            bodyBuilder.append("<h4>").append("Methods").append("</h4>\n");
            StringBuilder methodsBuilder = new StringBuilder();
            for (String m : docEntry.getValue().getRequest().getMethods()) {
                if (methodsBuilder.length() > 0) {
                    methodsBuilder.append(", ");
                }

                methodsBuilder.append(m);
            }
            bodyBuilder.append("<p>").append(methodsBuilder.toString()).append("</p>\n");
            bodyBuilder.append("</p>\n");

            bodyBuilder.append("<h4>").append("Headers").append("</h4>\n");
            bodyBuilder.append("<div class=\"panel panel-default\">\n")
                    .append("<div class=\"panel-body\">\n");
            bodyBuilder.append("<table class=\"table table-hover table-condensed\">\n");
            bodyBuilder.append("<thead>\n")
                    .append("<tr>")
                    .append("<th>").append("Name").append("</th>")
                    .append("<th>").append("Value").append("</th>")
                    .append("<th>").append("Requires").append("</th>")
                    .append("</tr>\n")
                    .append("</thead>\n");
            bodyBuilder.append("<tbody>\n");
            for (RequestHeader header : docEntry.getValue().getRequest().getHeaders()) {
                bodyBuilder.append("<tr>");
                bodyBuilder.append("<td>").append("<code>").append(header.getName()).append("</code>").append("</td>");
                bodyBuilder.append("<td>").append(header.getValue()).append("</td>");
                bodyBuilder.append("<td>").append(header.isRequires()).append("</td>");
                bodyBuilder.append("</tr>\n");
            }
            bodyBuilder.append("</tbody>\n");
            bodyBuilder.append("</table>\n");
            bodyBuilder.append("</div>\n")
                    .append("</div>\n");

            bodyBuilder.append("<h4>").append("Parameters").append("</h4>\n");
            bodyBuilder.append("<div class=\"panel panel-default\">\n")
                    .append("<div class=\"panel-body\">\n");
            bodyBuilder.append("<table class=\"table table-hover table-condensed\">\n");
            bodyBuilder.append("<thead>\n")
                    .append("<tr>")
                    .append("<th>").append("Field").append("</th>")
                    .append("<th>").append("Type").append("</th>")
                    .append("<th>").append("Requires").append("</th>")
                    .append("</tr>\n")
                    .append("</thead>\n");
            bodyBuilder.append("<tbody>\n");
            for (Parameter parameter : docEntry.getValue().getRequest().getParameters()) {
                bodyBuilder.append("<tr>");
                bodyBuilder.append("<td>").append("<code>").append(parameter.getField()).append("</code>").append("</td>");
                bodyBuilder.append("<td>").append(parameter.getType()).append("</td>");
                bodyBuilder.append("<td>").append(parameter.isRequires()).append("</td>");
                bodyBuilder.append("</tr>\n");
            }
            bodyBuilder.append("</tbody>\n");
            bodyBuilder.append("</table>\n");
            bodyBuilder.append("</div>\n")
                    .append("</div>\n");

            for (int i = 0; i < docEntry.getValue().getResponses().size(); i++) {
                Response response = docEntry.getValue().getResponses().get(i);

                bodyBuilder.append("<h3>").append("Response example ").append((i + 1)).append("</h3>\n");
                bodyBuilder.append("<p>").append(response.getRequestUrl().replace("&", "&amp;")).append("</p>\n");

                bodyBuilder.append("<h4>").append("Status").append("</h4>\n");
                bodyBuilder.append("<p>").append(objectMapper.writeValueAsString(response.getStatus())).append("</p>\n");

                bodyBuilder.append("<h4>").append("Headers").append("</h4>\n");
                bodyBuilder.append("<pre>").append(objectMapper.writeValueAsString(response.getHeaders())).append("</pre>\n");

                bodyBuilder.append("<h4>").append("Body").append("</h4>\n");
                bodyBuilder.append("<pre>").append(objectMapper.writeValueAsString(response.getBody())).append("</pre>\n");
            }
        }

        for (Map.Entry<String, StringBuilder> headBuilderEntry : headBuilderMap.entrySet()) {
            final String filename = headBuilderEntry.getKey();

            StringBuilder headBuilder = headBuilderEntry.getValue();
            StringBuilder containerBuilder = bodyBuilderMap.get(filename);

            if (containerBuilder == null) {
                continue;
            }

            StringBuilder bodyBuilder = new StringBuilder();
            bodyBuilder.append("<body>\n")
                    .append("<div class=\"container\" role=\"main\">\n")
                    .append("<h1>").append(titleMap.get(filename)).append("</h1>")
                    .append("<p role=\"desc\">").append("</p>\n")
                    .append(containerBuilder.toString()).append("\n")
                    .append("</div>\n")
                    .append("</body>\n");

            final String doc = new StringBuilder("<!doctype html>\n")
                    .append("<html>\n")
                    .append(headBuilder.toString()).append("\n")
                    .append(bodyBuilder.toString()).append("\n")
                    .append("</html>\n").toString();

            this.handle(filename, doc);
        }
    }

    public void handle(String key, String html) {}}
