package ru.otus.lesson27.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.otus.lesson27.dao.RoleDao;
import ru.otus.lesson27.dao.UserDao;
import ru.otus.lesson27.dto.UserDto;
import ru.otus.lesson27.model.Role;
import ru.otus.lesson27.model.Status;
import ru.otus.lesson27.model.User;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleDao roleDao;
    private final MailService mailService;


    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    @Override
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public String registration(UserDto userDto, BindingResult result, Model model){
        if (userDao.findByUsername(userDto.getUsername()) != null) {
            result.rejectValue("username", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) return "register";

        String password = newPass();
        Role role = roleDao.findByName("ROLE_USER");
        List roles = new ArrayList<>();
        roles.add(role);
        User user = new User(null,null,null,Status.valueOf("ACTIVE"),userDto.getUsername(),userDto.getFirstName(),userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(password),roles);
        userDao.save(user);
        mailService.sendMessage(user.getEmail(),"Registration", "Hello " + user.getUsername() + ", your password is "+ password);
        return "redirect:/login/register?success";
    }


    @Override
    public String newPass() {
        int len = 10;
        final String chars = "!" + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"+"_+";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
