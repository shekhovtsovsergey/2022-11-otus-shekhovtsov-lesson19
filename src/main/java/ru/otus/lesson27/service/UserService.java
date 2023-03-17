package ru.otus.lesson27.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.otus.lesson27.dto.UserDto;
import ru.otus.lesson27.model.Role;
import ru.otus.lesson27.model.User;
import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {

    public User findByUsername(String username);
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles);
    public String registration(UserDto userDto, BindingResult result, Model model);
    public String newPass();

}
