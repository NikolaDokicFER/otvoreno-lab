package or.lab.lab1or.controller;

import lombok.AllArgsConstructor;
import or.lab.lab1or.model.Player;
import or.lab.lab1or.model.Response;
import or.lab.lab1or.repository.PlayerRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/players")
@CrossOrigin("*")
public class PlayerController {
    private final PlayerRepo playerRepo;
    private Response response;

    @GetMapping
    public  ResponseEntity<Response> getPlayers(){
        response.setStatus("OK");
        response.setMessage("Players fetched");
        response.setResponse(playerRepo.findAll());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public  ResponseEntity<Response> getPlayerById(@PathVariable Long id){
        if (playerRepo.existsById(id)) {
            response.setStatus("OK");
            response.setMessage("Player fetched");
            List<Player> list = new ArrayList<>();
            list.add(playerRepo.findById(id).orElseThrow());
            response.setResponse(list);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.setStatus("Not Found");
            response.setMessage("Player with this id does not exist");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public  ResponseEntity<Response> savePlayer(@RequestBody Player player){
        if (playerRepo.existsById(player.getPlayerId())) {
            response.setStatus("FORBIDDEN");
            response.setMessage("Player with this id already exists");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
        }else {
            response.setStatus("OK");
            response.setMessage("Player saved");
            List<Player> list = new ArrayList<>();
            list.add(playerRepo.save(player));
            response.setResponse(list);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @DeleteMapping("/id/{id}")
    public  ResponseEntity<Response> deletePlayer(@PathVariable Long id){
        if (playerRepo.existsById(id)) {
            playerRepo.deleteById(id);
            response.setStatus("OK");
            response.setMessage("Player deleted");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.setStatus("Not Found");
            response.setMessage("Player with this id does not exist");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public  ResponseEntity<Response> updatePlayer(@RequestBody Player player){
        if (playerRepo.existsById(player.getPlayerId())) {
            response.setStatus("OK");
            response.setMessage("Player updated");
            List<Player> list = new ArrayList<>();
            list.add(playerRepo.save(player));
            response.setResponse(list);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.setStatus("Not Found");
            response.setMessage("Player with this id does not exist");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{firstName}")
    public  ResponseEntity<Response> getPlayerByFirstName(@PathVariable String firstName){
        if (playerRepo.existsByFirstName(firstName)) {
            response.setStatus("OK");
            response.setMessage("Players fetched");
            List<Player> list = new ArrayList<>();
            list.add(playerRepo.findByFirstName(firstName));
            response.setResponse(list);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.setStatus("Not Found");
            response.setMessage("Players with this first name do not exist");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<Response> getPlayersByCountry(@PathVariable String country){
        if (playerRepo.existsByCountry(country)) {
            response.setStatus("OK");
            response.setMessage("Players fetched");
            response.setResponse(playerRepo.findByCountry(country));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.setStatus("Not Found");
            response.setMessage("Players from this country do not exist");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/racket/{racket}")
    public  ResponseEntity<Response> getPlayerByRacket(@PathVariable String racket){
        if (playerRepo.existsByRacket(racket)) {
            response.setStatus("OK");
            response.setMessage("Players fetched");
            response.setResponse(playerRepo.findByRacket(racket));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.setStatus("Not Found");
            response.setMessage("Players who use this racket do not exist");
            response.setResponse(null);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "**")
    public  ResponseEntity<Response> redirect() {
        response.setStatus("Not Implemented");
        response.setMessage("Method not implemented for requested resource");
        response.setResponse(null);
        return new ResponseEntity<>(response,HttpStatus.NOT_IMPLEMENTED);
    }
}
