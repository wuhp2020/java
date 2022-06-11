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
     * 资源策略
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

    // 认证用户
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
        auth.eraseCredentials(false);
    }

    /**
     * 退出成功
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
     * 登录成功
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
                out.write("{\"status\":\"200\", \"message\":\"认证成功\", \"username\":\""+ userDetails.getUsername() +"\"}");
                out.flush();
                out.close();
            }
        };
    }

    /**
     * 登录失败
     * @return
     */
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                HttpServletResponse httpServletResponse, AuthenticationException e)
                    throws IOException, ServletException {
                String message = "认证失败";
                if (e instanceof BadCredentialsException ||
                        e instanceof UsernameNotFoundException) {
                    message = "账户名或者密码输入错误!";
                } else if (e instanceof LockedException) {
                    message = "账户被锁定，请联系管理员!";
                } else if (e instanceof CredentialsExpiredException) {
                    message = "密码过期，请联系管理员!";
                } else if (e instanceof AccountExpiredException) {
                    message = "账户过期，请联系管理员!";
                } else if (e instanceof DisabledException) {
                    message = "账户被禁用，请联系管理员!";
                } else {
                    message = "登录失败!";
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
     * 获取自定义用户
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

    // 自定义用户(关联了角色ID)
    public class SecurityUser extends UserDO implements UserDetails {

        public SecurityUser(UserDO userDO) {
            if (userDO != null) {
                this.setId(userDO.getId());
                this.setUsername(userDO.getUsername());
                this.setPassword(userDO.getPassword());
                this.setUserLinkedRoles(userDO.getUserLinkedRoles());
            }
        }

        // 自定义用户关联了角色ID
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

        //账户是否未过期,过期无法验证
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        //指定用户是否解锁,锁定的用户无法进行身份验证
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        //指示是否已过期的用户的凭据(密码),过期的凭据防止认证
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        //是否可用 ,禁用的用户不能身份验证
        @Override
        public boolean isEnabled() {
            return true;
        }

    }

    /**
     * 获取登录的用户信息
     * @return
     */
    public static SecurityUser getLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //有登陆用户就返回登录用户，没有就返回null
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
