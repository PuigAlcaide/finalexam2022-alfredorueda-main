package cat.tecnocampus.apollofy.api;

import cat.tecnocampus.apollofy.application.DJListController;
import cat.tecnocampus.apollofy.application.dto.DJListTrackDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/* TODO 4.4
    Implement RestController method that handles @PostMapping("/djlist/{id}/tracks") API calls to register new track
    associations with the DJList specified as parameter. You will need to get the user somehow (maybe from the security).
    You want to receive a list of DJListTrackDTO in the request's body.

    Note that all annotations are missing
*/
@RestController
@RequestMapping("/api/")
public class DJListRestController {

    private final DJListController djListController;

    public DJListRestController(DJListController djListController) {this.djListController = djListController;}

    @PostMapping("/djlist/{id}/tracks")
    public List<DJListTrackDTO> addTracksToDJ(Principal principal, @PathVariable Long id, @Valid @RequestBody List<DJListTrackDTO> djListTrackDTOList) {
        return djListController.addTracksToDJlistWithTimeRange(principal.getName(), id, djListTrackDTOList);
    }
}
