package com.helioplis.accounting.security.jwt.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;

@Service
public class UserService implements  UserDetailsService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptEncoder;

    public Integer saveUser(UserHelioplis user) {

        //Encode password before saving to DB
        user.setPassword(bCryptEncoder.encode(user.getPassword()));
        return userRepo.save(user).getId();
    }

    //find user by username
    public Optional<UserHelioplis> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserHelioplis> opt = userRepo.findByUsername(username);

        org.springframework.security.core.userdetails.User springUser=null;

        if(opt.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " +username +" not found");
        }else {
            UserHelioplis user =opt.get();	//retrieving user from DB
            Set<String> roles = user.getRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
            for(String role:roles) {
                ga.add(new SimpleGrantedAuthority(role));
            }

            springUser = new org.springframework.security.core.userdetails.User(
                    username,
                    user.getPassword(),
                    ga );
        }

        return springUser;
    }

}