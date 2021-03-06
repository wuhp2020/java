package com.spring.config;

import com.querydsl.core.types.Predicate;
import com.web.model.UserDO;
import com.web.repository.ResourceRepository;
import com.web.repository.RoleLinkedResourceRepository;
import com.web.repository.UserRepository;
import com.web.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RoleLinkedResourceRepository roleLinkedResourceRepository;

    /**
     * ????????????
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/**.html", "/**.js", "/**.css", "/v2/api-docs")
                .permitAll().anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl("/api/v1/login").permitAll()
                .failureHandler(loginFailureHandler()).successHandler(loginSuccessHandler())
                .and().logout().permitAll().invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler())
                .and().sessionManagement().maximumSessions(10).expiredUrl("/api/v1/login");
    }

    // ????????????
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
        auth.eraseCredentials(false);
    }

    /**
     * ????????????
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest,
                HttpServletResponse httpServletResponse, Authentication authentication)
                    throws IOException, ServletException {
                try {
                    UserDO user = (UserDO) authentication.getPrincipal();
                    log.info("USER : " + user.getUsername() + " LOGOUT SUCCESS !  ");
                } catch (Exception e) {
                    log.info("LOGOUT EXCEPTION , e : " + e.getMessage());
                }
                httpServletResponse.sendRedirect("/api/v1/login");
            }
        };
    }

    /**
     * ????????????
     * @return
     */
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                UserDO userDetails = (UserDO) authentication.getPrincipal();
                log.info("USER : " + userDetails.getUsername() + " LOGIN SUCCESS !  ");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write("{\"status\":\"200\", \"message\":\"????????????\", \"username\":\""+ userDetails.getUsername() +"\"}");
                out.flush();
                out.close();
            }
        };
    }

    /**
     * ????????????
     * @return
     */
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                HttpServletResponse httpServletResponse, AuthenticationException e)
                    throws IOException, ServletException {
                String message = "????????????";
                if (e instanceof BadCredentialsException ||
                        e instanceof UsernameNotFoundException) {
                    message = "?????????????????????????????????!";
                } else if (e instanceof LockedException) {
                    message = "????????????????????????????????????!";
                } else if (e instanceof CredentialsExpiredException) {
                    message = "?????????????????????????????????!";
                } else if (e instanceof AccountExpiredException) {
                    message = "?????????????????????????????????!";
                } else if (e instanceof DisabledException) {
                    message = "????????????????????????????????????!";
                } else {
                    message = "????????????!";
                }
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out = httpServletResponse.getWriter();
                out.write("{\"status\":\"500\", \"message\":\""+ message +"\"}");
                out.flush();
                out.close();
            }
        };
    }

    /**
     * ?????????????????????
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Autowired
            private UserRepository userRepository;

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserVO userVO = new UserVO();
                userVO.setUsername(username);
                Predicate predicate = userVO.buildUser();

                List<UserDO> userDOs =  (List<UserDO>)userRepository.findAll(predicate);
                if (CollectionUtils.isEmpty(userDOs)) {
                    throw new UsernameNotFoundException("username " + username + " not found");
                }

                return new SecurityUser(userDOs.get(0));
            }
        };
    }

    // ???????????????(???????????????ID)
    public class SecurityUser extends UserDO implements UserDetails {

        public SecurityUser(UserDO userDO) {
            if (userDO != null) {
                this.setId(userDO.getId());
                this.setUsername(userDO.getUsername());
                this.setPassword(userDO.getPassword());
                this.setUserLinkedRoles(userDO.getUserLinkedRoles());
            }
        }

        // ??????????????????????????????ID
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            String username = this.getUsername();
            if (username != null) {
//                List<UserLinkedRoleDO> userLinkedRoleDOs = this.getUserLinkedRoles();
//                if (!CollectionUtils.isEmpty(userLinkedRoleDOs)) {
//                    List<String> roleIds = userLinkedRoleDOs.stream().map(UserLinkedRoleDO::getRoleId).
//                            collect(Collectors.toList());
//                    return roleIds.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//                }
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(username);
                authorities.add(authority);
            }
            return authorities;
        }

        //?????????????????????,??????????????????
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        //????????????????????????,???????????????????????????????????????
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        //???????????????????????????????????????(??????),???????????????????????????
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        //???????????? ,?????????????????????????????????
        @Override
        public boolean isEnabled() {
            return true;
        }

    }

    /**
     * ???????????????????????????
     * @return
     */
    public static SecurityUser getLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //??????????????????????????????????????????????????????null
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                return (SecurityUser) authentication.getPrincipal();
            }
        }
        return null;
    }
}
