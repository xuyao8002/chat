package com.xuyao.chat.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;

@Component
public class UrlInterceptor implements HandlerInterceptor {

    @Value("#{'${exclude.path.patterns:}'.split(',')}")
    private Set<String> excludePaths;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String servletPath = request.getServletPath();
        if (Objects.equals(servletPath, "/error")) {
            return true;
        }
        if (!excludePaths.contains(servletPath)) {
            String first = servletPath.split("/")[1];
            boolean exclude = false;
            for (String excludePath : excludePaths) {
                if (excludePath.startsWith("/" + first)) {
                    exclude = true;
                }
            }
            if(!exclude){
                throw new RuntimeException("无访问权限");
            }
        }
        return true;
    }
}
