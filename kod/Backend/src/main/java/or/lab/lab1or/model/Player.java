package or.lab.lab1or.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor

@Entity
@Table(name="players")
public class Player {

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private Date dob;

    @Column
    private Integer weight;

    @Column
    private Integer height;

    @Column(name = "atp_rank")
    private Integer atpRank;

    @Column
    private String racket;

    @Column
    private String coach;

    @Column(name = "strong_hand")
    private String strongHand;

    @Column
    private String country;
}
