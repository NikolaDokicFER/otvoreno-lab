package or.lab.lab1or.repository;

import or.lab.lab1or.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepo extends JpaRepository<Player, Long> {
    Player findByFirstName(String firstName);
    List<Player> findByCountry(String country);
    List<Player> findByRacket(String racket);
    boolean existsByFirstName(String firstName);
    boolean existsByCountry(String country);
    boolean existsByRacket(String racket);
}
