package ru.obelisk.monitor.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.obelisk.monitor.database.models.entity.UserRole;
import ru.obelisk.monitor.database.models.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Qualifier("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        ru.obelisk.monitor.database.models.entity.User user = userRepository.findByUsername(username);
        
        List<GrantedAuthority> authorities = buildUserAuthority(new HashSet<UserRole>(user.getRoles()));
        return buildUserForAuthentication(user, authorities);
    }

    private User buildUserForAuthentication(ru.obelisk.monitor.database.models.entity.User user,
                                            List<GrantedAuthority> authorities) {
    	User authUser = new User(user.getLogin(), user.getPass(), authorities);
    	return authUser; 
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // Build user's authorities
        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority("ROLE_"+userRole.getRoleName()));
            System.out.println("RETURN ROLE: ROLE_"+userRole.getRoleName());
        }

        return new ArrayList<GrantedAuthority>(setAuths);
    }
}