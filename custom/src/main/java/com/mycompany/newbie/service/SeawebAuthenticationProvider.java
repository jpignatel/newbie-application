package com.mycompany.newbie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mycompany.newbie.domain.Authority;
import com.mycompany.newbie.domain.User;
import com.mycompany.newbie.repository.UserRepository;
import com.mycompany.newbie.security.AuthoritiesConstants;
import com.mycompany.newbie.service.dto.AdminUserDTO;

import vx.swxmlapi.facade.BaseFacade;
import vx.swxmlapi.facade.util.XDataFactory;
import vx.swxmlapi.xdata.LoginIN;
import vx.swxmlapi.xdata.LoginOUT;
import vx.swxmlapi.xdata.UserInfo;

@Service
@Configurable
public class SeawebAuthenticationProvider implements AuthenticationProvider {

	private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserInfo.Consumer consumer = new UserInfo.Consumer();
    	consumer.setUsername(username.toUpperCase().trim());
        consumer.setPassword(password.trim());
        log.debug("User name = " + consumer.getUsername());
        log.debug("Encoded password = " + consumer.getPassword());

        UserInfo userInfo = new UserInfo();
        userInfo.setConsumer(consumer);
        String country = consumer.getCountry();
        if (country == null || "".equals(country)) {
        	country = "FR";
        }
        /* Prepare request */
        LoginIN.TimeOut timeOut = new LoginIN.TimeOut();
        timeOut.setTotal(720);
        timeOut.setInactivity(20);
        		
        LoginIN loginIN = XDataFactory.createLoginIN(
                userInfo, null, country, "INT-CON", timeOut);
        LoginOUT _loginOUT = (LoginOUT) BaseFacade.processRequest(loginIN);
        
        if (_loginOUT.getErrors() != null) throw new BadCredentialsException("1000");
        
        AdminUserDTO userDto = new AdminUserDTO();
		userDto.setLogin(username);
        if(!userRepository.findOneByLogin(username).isPresent()) {
			userService.registerUser(userDto, _loginOUT.getMsgHeader().getSessionGUID());
		}
        User user = userRepository.findOneByLogin(username).get();

		userDto.setId(user.getId());
		userDto.setFirstName(_loginOUT.getMsgHeader().getSessionGUID());
		userDto.setLastName(_loginOUT.getUserInfoDetail().getClientID().toString());
		userDto.setAuthorities(StringUtils.commaDelimitedListToSet(AuthoritiesConstants.ADMIN));
		userService.updateUser(userDto);
		
    	// to add more logic
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
        return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
	}

	@Override
	public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
