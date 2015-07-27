package com.yo1000.apidoc.component;

import com.yo1000.apidoc.model.Document;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/28.
 */
public abstract class ApidocFormatHandlerInterceptor extends HandlerInterceptorAdapter {
    public abstract ConcurrentHashMap<String, Document> getDocumentMap();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);

        Map<String, Document> docMap = this.getDocumentMap();

        new DocumentMapFormatter() {
            @Override
            public void handle(final String key, final String html) {
                super.handle(key, html);

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            File f = new File("apidoc");
                            if (!f.exists()) {
                                f.mkdir();
                            }
                            Writer w = new FileWriter(new File("apidoc", key));
                            w.write(html);
                            w.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                //System.out.println(html);
            }
        }.format(docMap);
    }
}
