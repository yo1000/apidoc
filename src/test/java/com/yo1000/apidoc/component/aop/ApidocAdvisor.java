package com.yo1000.apidoc.component.aop;

import com.yo1000.apidoc.component.ApidocAdvice;
import com.yo1000.apidoc.model.DocumentBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yoichi.kikuchi on 2015/07/17.
 */
@Aspect
@Component
public class ApidocAdvisor extends ApidocAdvice {
    @Autowired
    private DocumentBuilder documentBuilder;

    @Override
    public DocumentBuilder getDocumentBuilder() {
        return this.documentBuilder;
    }

    @Around("execution(* com.yo1000.apidoc.controller.api.MockResource.*(..)) && " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Override
    public Object aroundResource(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.aroundResource(joinPoint);
    }
}
