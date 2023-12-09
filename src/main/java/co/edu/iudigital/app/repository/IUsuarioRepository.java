package co.edu.iudigital.app.repository;

import co.edu.iudigital.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository
        extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}
