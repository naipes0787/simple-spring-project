package wolox.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wolox.training.repositories.UserRepository;
import wolox.training.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @SuppressWarnings("unused")
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return new CustomUserDetails(userRepository.findFirstByUsername(username).
            orElseThrow(() -> new UsernameNotFoundException(username)));
    }
}