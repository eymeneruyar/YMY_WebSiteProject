package YMY.services;

import YMY.entities.Role;
import YMY.entities.User;
import YMY.repositories.UserRepository;
import YMY.utils.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService extends SimpleUrlLogoutSuccessHandler implements UserDetailsService, LogoutSuccessHandler {

    final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Security Login
    @Override
    public UserDetails loadUserByUsername(String email) {
        //System.out.println("Email: " + email);
        UserDetails userDetails = null;
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        if(userOptional.isPresent()){
            User us = userOptional.get();
            userDetails = new org.springframework.security.core.userdetails.User(
                    us.getEmail(),
                    us.getPassword(),
                    us.isEnabled(),
                    us.isTokenExpired(),
                    true,
                    true,
                    getAuthorities(us.getRoles()) );
        }
        else{
            throw new UsernameNotFoundException("Kullanıcı adı ya da şifre hatalı!");
        }
        return userDetails;
    }

    private static List<GrantedAuthority> getAuthorities (List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    public User register(User us) throws AuthenticationException {

        if(!Util.isEmail(us.getEmail())){
            throw new AuthenticationException("Bu mail formatı hatalı!");
        }

        //Kullanıcılar bölümünden güncelleme işlemi yapabilmek için kapattım.
        /*Optional<User> uOpt = uRepo.findByuEmailEqualsIgnoreCase(us.getUEmail());
        if(uOpt.isPresent()){
            throw new AuthenticationException("Bu kullanıcı daha önce kayıtlı");
        }*/

        us.setPassword(encoder().encode(us.getPassword()));

        return userRepository.saveAndFlush(us);
    }

    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public User userInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

}
