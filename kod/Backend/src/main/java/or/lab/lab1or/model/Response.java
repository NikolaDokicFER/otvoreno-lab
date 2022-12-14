package or.lab.lab1or.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonProperty("@context")
    private Context context = new Context();
    private List<Player> response;
}
