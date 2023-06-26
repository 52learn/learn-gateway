package com.study.gateway.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebFilter(filterName = "myWebFilter",urlPatterns = "/**")
@Slf4j
public class MyWebFilter implements Filter {
    ObjectMapper objectMapper;
    public MyWebFilter(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        Map<String,String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String headerValue = httpRequest.getHeader(headerName);
            headerMap.put(headerName,headerValue);
        }
        String headerMapString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(headerMap);
        log.info("request headers: {}",headerMapString);
        String requestParameterString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request.getParameterMap());
        log.info("request parameters: {}",requestParameterString);
        BodyCachingHttpServletRequestWrapper requestWrapper =
            new BodyCachingHttpServletRequestWrapper((HttpServletRequest) request);
        byte[] requestBody = requestWrapper.getBody();
        String requestBodyStr = new String(requestBody);
        log.info("request body: {}",requestBodyStr);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-Response-color","webFilterColor");
        httpResponse.setHeader( "secret-info","user=ford&password=omg!what&flag=true");

        if(headerMap.containsKey("x-output")){
            StringBuffer sb = new StringBuffer();
            sb.append("request headers: \n");
            sb.append(headerMapString+" \n");
            sb.append("request parameters: \n");
            sb.append(requestParameterString+" \n");
            response.getWriter().write(sb.toString());
            response.getWriter().close();
        }else{
            filterChain.doFilter(request,response);
        }

    }

    @Override
    public void destroy() {

    }
}
