package com.yo1000.apidoc.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yo1000.apidoc.model.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public abstract class ApidocAdvice {
    public abstract ConcurrentHashMap<String, Document> getDocumentMap();

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

        RequestMapping classReqMap = (RequestMapping) methodSignature.getDeclaringType().getAnnotation(RequestMapping.class);

        Document apidoc = new Document();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Request apiReq = new Request();
        apidoc.setRequest(apiReq);
        apiReq.setEndpoint(this.makeEndpoint(classReqMap, methodReqMap));
        apiReq.setParameters(this.mekeParameters(methodSignature));
        apiReq.setHeaders(this.mekeHeaders(methodSignature));

        Response apiResp = new Response();
        apidoc.setResponse(apiResp);
        apiResp.setBody(objectMapper.writeValueAsString(o));
        apiResp.setHeaders(new ArrayList<Header>());

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();

        HttpServletRequest request = attrs.getRequest();
        String key = request.getRequestURL().toString();

        this.getDocumentMap().put(key, apidoc);

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

    protected List<Parameter> mekeParameters(MethodSignature methodSignature) {
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

    protected List<Header> mekeHeaders(MethodSignature methodSignature) {
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        Annotation[][] argsAnnos = methodSignature.getMethod().getParameterAnnotations();

        List<Header> apidocHeaders = new ArrayList<Header>();

        for (int i = 0; i < argsAnnos.length; i++) {
            Annotation[] argAnnos = argsAnnos[i];
            Header apidocHeader = new Header();

            for (Annotation anno : argAnnos) {
                if (!(anno instanceof RequestHeader)) {
                    continue;
                }

                RequestHeader requestHeader = (RequestHeader) anno;

                apidocHeader.setName(requestHeader.value() != null && !requestHeader.value().isEmpty() ?
                        requestHeader.value() : parameterNames[i]);
                apidocHeader.setValue(parameterTypes[i].getSimpleName().toLowerCase());
                apidocHeader.setRequires(requestHeader.required());

                apidocHeaders.add(apidocHeader);
            }
        }

        return apidocHeaders;
    }
}
