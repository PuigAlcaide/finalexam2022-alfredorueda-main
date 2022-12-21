package cat.tecnocampus.apollofy.application;

import cat.tecnocampus.apollofy.application.dto.DJListTrackDTO;
import cat.tecnocampus.apollofy.application.exceptions.ElementNotFoundInBBDD;
import cat.tecnocampus.apollofy.application.exceptions.NotAuthorizedToDoAction;
import cat.tecnocampus.apollofy.domain.DJList;
import cat.tecnocampus.apollofy.domain.DJListTrackFragment;
import cat.tecnocampus.apollofy.domain.Track;
import cat.tecnocampus.apollofy.domain.UserFy;
import cat.tecnocampus.apollofy.persistence.DJListRepository;
import cat.tecnocampus.apollofy.persistence.DJListTrackFragmentRepository;
import cat.tecnocampus.apollofy.persistence.TrackRepository;
import cat.tecnocampus.apollofy.persistence.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DJListController {

    private DJListRepository djListRepository;
    private UserRepository userRepository;
    private TrackRepository trackRepository;
    private DJListTrackFragmentRepository djListTrackFragmentRepository;

    public DJListController(DJListRepository djListRepository, UserRepository userRepository,
                            TrackRepository trackRepository,
                            DJListTrackFragmentRepository djListTrackFragmentRepository) {
        this.djListRepository = djListRepository;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
        this.djListTrackFragmentRepository = djListTrackFragmentRepository;
    }

    //You'll need to uncomment lines 15, 19, and 23. Also rearrange line 18
    /* TODO 4.2
        Implement a method (or as many as you need) to add track fragments to existing DJLists. For example:
        public void addTracksToDJlistWithTimeRange(String userEmail, Long DJListId, List<DJListTrackDTO> tracksDTO)
        */
    @Transactional
    public List<DJListTrackDTO> addTracksToDJlistWithTimeRange(String userEmail, Long DJListId,
                                                               List<DJListTrackDTO> tracksDTO) {
        UserFy user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ElementNotFoundInBBDD(userEmail));
        DJList djList = djListRepository.findById(DJListId).orElseThrow(
                () -> new ElementNotFoundInBBDD(DJListId.toString()));

        if(!djList.getOwner().equals(user)) throw new NotAuthorizedToDoAction();

        List<DJListTrackFragment> updatedLists = new ArrayList<>();

        for (DJListTrackDTO djListTrackDTO : tracksDTO) {

            Track track = trackRepository.findById(djListTrackDTO.trackId()).orElseThrow(
                    () -> new ElementNotFoundInBBDD(djListTrackDTO.trackId().toString()));

            DJListTrackFragment djListTrackFragment = getDJListTrackFragmentIfExists(djList, track,
                    djListTrackDTO.startTimeMillis(), djListTrackDTO.endTimeMillis());
            updatedLists.add(djListTrackFragment);
        }

        djListTrackFragmentRepository.saveAll(updatedLists);
        return tracksDTO;
    }


    public DJListTrackFragment getDJListTrackFragmentIfExists(DJList djList, Track track, Long startTimeMillis,
                                                              Long endTimeMillis) {

        DJListTrackFragment djListTrackFragment = djListTrackFragmentRepository.findByDjListAndTrack(djList,
                track).orElse(new DJListTrackFragment());
        djListTrackFragment.setTrack(track);
        djListTrackFragment.setDjList(djList);
        djListTrackFragment.setStartTimeMillis(startTimeMillis);
        djListTrackFragment.setEndTimeMillis(endTimeMillis);
        return djListTrackFragment;
    }
        /*
        Also you may want to receive a list of objects with the following attributes:
        Long trackId, Long startTimeMillis, Long endTimeMillis.
        You can use "application/dto/DJListTrackDTO for this matter (see _TODO 4.3)

        You'll need to implement a method in the DJListTrackFragmentRepository to get the DJListTrackFragments that point
        to a given DJList and Track (see _TODO 4.5)

        This method has to verify the following constraints and implement the specified behaviour:
        - The DJList with the provided identifier exists.
        - The user with the provided identifier exists.
        - The provided user is the owner of the DJList.
        - For each of the DJListTrackDTO in "trackDTO" parameter:
              -you must verify that the track exists.
              -in the case that the track is already associated to that DJList, then startTimeMillis and endTimeMillis
               are updated in its corresponding DJListTrackFragment object.
              -in case the track is not associated to the DJList yet, then you must create a new DJListTrackFragment object,
               linking the track to the DJList and specifying startTimeMillis and endTimeMillis accordingly.
       */
}
