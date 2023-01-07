package or.lab.lab1or.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Component
public class Context {
    @JsonProperty("@vocab")
    private String vocab = "\"http://schema.org/\"";
    private String first_name = "givenName";
    private String last_name = "familyName";
    private String dob = "birthPlace";
    private String country = "nationality";
}
