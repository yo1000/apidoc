package com.yo1000.apidoc.component;

import com.yo1000.apidoc.model.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public abstract class ApidocAdvice {
    public abstract DocumentBuilder getDocumentBuilder();

    public Object aroundResource(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();

        if (!(signature instanceof MethodSignature)) {
            return o;
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        RequestMapping methodReqMap = methodSignature.getMethod().getAnnotation(RequestMapping.class);

        if (methodReqMap == null || methodReqMap.value().length <= 0) {
            return o;
        }

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attrs.getRequest();

        String url = request.getRequestURL().toString();
        String query = request.getQueryString();
        String key = url + (query != null ? ("?" + query) : "");

        DocumentBuilder documentBuilder = this.getDocumentBuilder();
        Request docReq = new Request();
        Response docResp = new Response();

        documentBuilder.put(key, docReq);
        documentBuilder.put(key, docResp);

        RequestMapping classReqMap = (RequestMapping) methodSignature.getDeclaringType().getAnnotation(RequestMapping.class);

        docReq.setEndpoint(this.makeEndpoint(classReqMap, methodReqMap) + this.makeQueryParameter(methodSignature));
        docReq.setMethods(this.makeMethods(classReqMap, methodReqMap));
        docReq.setParameters(this.makeParameters(methodSignature));
        docReq.setHeaders(this.makeHeaders(methodSignature));

        docResp.setRequestUrl(key);
        docResp.setBody((o instanceof ResponseEntity) ? ((ResponseEntity) o).getBody() : o);
        docResp.setHeaders(new ArrayList<ResponseHeader>());

        return o;
    }

    protected String makeEndpoint(RequestMapping typeRequestMapping, RequestMapping methodRequestMapping) {
        StringBuilder endpoint = new StringBuilder("/");

        if (typeRequestMapping != null && typeRequestMapping.value().length > 0) {
            String classResource = typeRequestMapping.value()[0];
            classResource = classResource.replaceAll("^[/]+", "");
            classResource = classResource.replaceAll("[/]+$", "");

            endpoint.append(classResource);
        }

        String methodResource = methodRequestMapping.value()[0];
        methodResource = methodResource.replaceAll("^[/]+", "");
        methodResource = methodResource.replaceAll("[/]+$", "");

        if (!methodResource.isEmpty()){
            endpoint.append("/").append(methodResource);
        }

        return endpoint.toString();
    }

    protected List<String> makeMethods(RequestMapping typeRequestMapping, RequestMapping methodRequestMapping) {
        List<String> methods = new ArrayList<String>();

        for (RequestMethod method : typeRequestMapping.method()) {
            if (methods.contains(method.name())) {
                continue;
            }

            methods.add(method.name());
        }

        for (RequestMethod method : methodRequestMapping.method()) {
            if (methods.contains(method.name())) {
                continue;
            }

            methods.add(method.name());
        }

        if (methods.size() == 0) {
            methods.add(RequestMethod.GET.name());
        }

        return methods;
    }

    protected String makeQueryParameter(MethodSignature methodSignature) {
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        Annotation[][] argsAnnos = methodSignature.getMethod().getParameterAnnotations();

        StringBuilder b = new StringBuilder();

        for (int i = 0; i < argsAnnos.length; i++) {
            Annotation[] argAnnos = argsAnnos[i];

            for (Annotation anno : argAnnos) {
                if (!(anno instanceof RequestParam)) {
                    continue;
                }

                RequestParam requestParam = (RequestParam) anno;

                String name = requestParam.value() != null && !requestParam.value().isEmpty() ?
                        requestParam.value() : parameterNames[i];

                b.append(b.length() == 0 ? "?" : "&")
                        .append(name).append("={").append(name).append("}");
            }
        }

        return b.toString();
    }

    protected List<Parameter> makeParameters(MethodSignature methodSignature) {
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        Annotation[][] argsAnnos = methodSignature.getMethod().getParameterAnnotations();

        List<Parameter> apidocParameters = new ArrayList<Parameter>();

        for (int i = 0; i < argsAnnos.length; i++) {
            Annotation[] argAnnos = argsAnnos[i];
            Parameter apidocParameter = new Parameter();

            for (Annotation anno : argAnnos) {
                if (anno instanceof PathVariable) {
                    PathVariable pathVariable = (PathVariable) anno;

                    apidocParameter.setField(pathVariable.value() != null && !pathVariable.value().isEmpty() ?
                            pathVariable.value() : parameterNames[i]);
                    apidocParameter.setType(parameterTypes[i].getSimpleName().toLowerCase());
                    apidocParameter.setRequires(true);

                    apidocParameters.add(apidocParameter);
                    continue;
                }

                if (!(anno instanceof RequestParam)) {
                    continue;
                }

                RequestParam requestParam = (RequestParam) anno;

                apidocParameter.setField(requestParam.value() != null && !requestParam.value().isEmpty() ?
                        requestParam.value() : parameterNames[i]);
                apidocParameter.setType(parameterTypes[i].getSimpleName().toLowerCase());
                apidocParameter.setRequires(requestParam.required());

                apidocParameters.add(apidocParameter);
            }
        }

        return apidocParameters;
    }

    protected List<RequestHeader> makeHeaders(MethodSignature methodSignature) {
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        Annotation[][] argsAnnos = methodSignature.getMethod().getParameterAnnotations();

        List<RequestHeader> apidocRequestHeaders = new ArrayList<RequestHeader>();

        for (int i = 0; i < argsAnnos.length; i++) {
            Annotation[] argAnnos = argsAnnos[i];
            RequestHeader apidocRequestHeader = new RequestHeader();

            for (Annotation anno : argAnnos) {
                if (!(anno instanceof org.springframework.web.bind.annotation.RequestHeader)) {
                    continue;
                }

                org.springframework.web.bind.annotation.RequestHeader requestRequestHeader = (org.springframework.web.bind.annotation.RequestHeader) anno;

                apidocRequestHeader.setName(requestRequestHeader.value() != null && !requestRequestHeader.value().isEmpty() ?
                        requestRequestHeader.value() : parameterNames[i]);
                apidocRequestHeader.setValue(parameterTypes[i].getSimpleName().toLowerCase());
                apidocRequestHeader.setRequires(requestRequestHeader.required());

                apidocRequestHeaders.add(apidocRequestHeader);
            }
        }

        return apidocRequestHeaders;
    }
}
