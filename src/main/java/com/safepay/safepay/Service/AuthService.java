package com.safepay.safepay.Service;



import com.safepay.safepay.Auth.AuthUser;
import com.safepay.safepay.Auth.JwtService;
import com.safepay.safepay.Dto.RegisterRequest;
import com.safepay.safepay.Model.User;
import com.safepay.safepay.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository,AuthenticationManager authenticationManager,JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }


   public RegisterRequest.UserSummaryResponse register(RegisterRequest request){
     String name=request.fullname;
        String email=request.email.trim().toLowerCase();
   String hash=passwordEncoder.encode(request.password);
        User user=new User(email,hash,name);

       try {
             User savedUSer=userRepository.saveAndFlush(user);
             return new RegisterRequest.UserSummaryResponse(
                     savedUSer.getId(),savedUSer.getName(),savedUSer.getEmail()
             );
       }catch (DataIntegrityViolationException d){
       throw new RuntimeException("Registration Failed: UserExists");
       }

  }


  public RegisterRequest.AuthResponse login(RegisterRequest.LoginRequest loginRequest){
      Authentication auth = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
      AuthUser principal = (AuthUser) auth.getPrincipal();            // the same AuthUser our service returned
      String token = jwtService.generateToken(principal.getId(), principal.getRole());   // role from the loaded user
      return new RegisterRequest.AuthResponse(token, "Bearer", jwtService.getExpirationSeconds());
  }


}
