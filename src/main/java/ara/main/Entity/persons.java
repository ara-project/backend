package ara.main.Entity;


import ara.main.Dto.util.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class persons implements UserDetails {
    @Id
    private String identification;
    private String name;
    @Column(name = "second_name")
    private String secondName;
    private String lastname;
    @Column(name = "second_lastname")
    private String secondLastname;
    private String username;
    private String email; // Nuevo campo agregado
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String dni;
    @Column(name = "url_photo")
    private String urlPhoto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
