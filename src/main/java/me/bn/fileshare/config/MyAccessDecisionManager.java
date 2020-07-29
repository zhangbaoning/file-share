package me.bn.fileshare.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        boolean flag = false;
        FilterInvocation filterInvocation = (FilterInvocation) o;
        if ((filterInvocation.getRequestUrl().endsWith("png")||filterInvocation.getRequestUrl().endsWith("jpg")||filterInvocation.getRequestUrl().endsWith("jpeg"))) {

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (filterInvocation.getRequestUrl().contains(authority.getAuthority())) {
                    flag = true;
                }
            }
            if (!flag){
                throw new AccessDeniedException(filterInvocation.getRequestUrl()+"failure");
            }
        }
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
