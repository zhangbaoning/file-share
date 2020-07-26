package me.bn.fileshare.config;

import org.springframework.http.MediaType;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
@Component
public class FilePermission implements FilterInvocationSecurityMetadataSource {
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        ArrayList arrayList  = new ArrayList();
        FilterInvocation filterInvocation = (FilterInvocation) o;

        SecurityConfig securityConfig = new SecurityConfig("ROLE_" + "admin");
        arrayList.add(securityConfig);
        System.out.println(filterInvocation.getFullRequestUrl());
        System.out.println(o);
        return arrayList;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
