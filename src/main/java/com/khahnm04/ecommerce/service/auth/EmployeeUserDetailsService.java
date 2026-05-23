package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmailAndDeletedAtIsNull(email)
            .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND.getMessage()));
    }

}
