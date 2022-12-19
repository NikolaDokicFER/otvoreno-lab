package or.lab.lab1or.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Component
public class Response {
    private String status;
    private String message;
    private List<Player> response;
}
