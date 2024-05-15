package ara.main.Service;

import ara.main.Config.GeneratorId;
import ara.main.Dto.PersonsDto;
import ara.main.Dto.RegisterRequest;
import ara.main.Dto.ResetPasswordRequest;
import ara.main.Dto.UpdatedRegisterRequest;
import ara.main.Entity.persons;
import ara.main.Repositories.JDBCQuerys;
import ara.main.Repositories.PersonRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PersonasService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GeneratorId generatorId;
    @Autowired
    private JDBCQuerys jdbcQuerys;
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<String> register(RegisterRequest request){
        try {
            if (personRepository.existsByUsername(request.getUsername()) && personRepository.existsById(request.getIdentification()) || personRepository.existsByEmail(request.getEmail())){
                return ResponseEntity.status(450).body("Ya existe este usuario");
            }
            //Enconded password
            if(request.getPassword() !=null){
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                request.setPassword(encodedPassword);
            }
            //Contraseñas vacias
            /*else{
                return  ResponseEntity.badRequest().body("Contraseña Vacia");
            }*/
            var user = persons.builder()
                    .identification(generatorId.generatorNumericId())
                    .name(request.getName())
                    .secondName(request.getSecondLastname())
                    .lastname(request.getLastname())
                    .secondLastname(request.getSecondLastname())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .role(request.getRole())
                    .dni(request.getDni())
                    .build();
            personRepository.save(user);
            return ResponseEntity.ok("El usuario fue registrado con exito");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
    public ResponseEntity<List<persons>> getAll() {
        try{
            return ResponseEntity.ok(personRepository.findAll());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }
    public ResponseEntity<PersonsDto> getUser(String username){
        persons user= personRepository.findByUsername(username).orElse(null);
        if (user==null){
           return ResponseEntity.badRequest().body(new PersonsDto(null,null,null,null));
        }else{
            var profile = PersonsDto.builder()
                    .identification(user.getIdentification())
                    .name(user.getName())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .build();
            return ResponseEntity.ok(profile);
        }
    }
    public ResponseEntity<String> continueRegister(UpdatedRegisterRequest request){
        if (personRepository.existsById(request.getIdentification())){
            persons person=personRepository.findById(request.getIdentification()).orElse(null);
            assert person != null;
            var personRegister = persons.builder()
                    .identification(request.getIdentification())
                    .name(person.getName())
                    .secondName(person.getSecondLastname())
                    .lastname(person.getLastname())
                    .secondLastname(person.getSecondLastname())
                    .username(request.getUsername())
                    .email(person.getEmail())
                    .password(person.getPassword())
                    .role(person.getRole())
                    .dni(request.getDni())
                    .build();
            personRepository.save(personRegister);
            return ResponseEntity.ok("Modificado Correctamente");
        }
        return ResponseEntity.badRequest().body("No se encontro la identificacion");
    }
    public ResponseEntity<String> resetPassword(ResetPasswordRequest passwordRequest){
        String username = jwtService.extractUsername(passwordRequest.getToken());
        //declaracion de variables
        String identification= jwtService.extractID(passwordRequest.getToken());
        String nowPassword = jdbcQuerys.getPasswordByUsername(username);
        if (!personRepository.existsById(identification)){
            return ResponseEntity.badRequest().body("Identificacion no encontrada");
        }
        if (passwordEncoder.matches(passwordRequest.getPassword(), nowPassword)){
            if (!Objects.equals(passwordRequest.getNewPassword(), passwordRequest.getConfirmNewPassword())){
                return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
            }else{
                String encodedPassword = passwordEncoder.encode(passwordRequest.getConfirmNewPassword());
                passwordRequest.setConfirmNewPassword(encodedPassword);
                int valurResponse = jdbcQuerys.updatePassword(passwordRequest.getConfirmNewPassword(), identification);
                if (valurResponse>0){
                    return ResponseEntity.ok("Contraseña actualizada correctamente");
                }else{
                    return ResponseEntity.badRequest().body("La contraseña no pudo ser actualizada");
                }
            }
        }else{
            return ResponseEntity.badRequest().body("Contraseña antigua incorrecta");
        }
    }
}
