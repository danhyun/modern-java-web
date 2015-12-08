package meeting;

import ratpack.exec.Operation;
import ratpack.exec.Promise;
import redis.RatingRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultMeetingService implements MeetingService {

  private final MeetingRepository meetingRepository;
  private final RatingRepository ratingRepository;

  public DefaultMeetingService(MeetingRepository meetingRepository, RatingRepository ratingRepository) {
    this.meetingRepository = meetingRepository;
    this.ratingRepository = ratingRepository;
  }

  @Override
  public Promise<List<Meeting>> getMeetings() {
    return meetingRepository.getMeetings()
      .flatMap(meetings ->
        Promise.value(
          meetings.stream()
          .peek( meeting ->
            ratingRepository.getAverageRating(meeting.getId())
              .then(meeting::setRating)
          )
          .collect(Collectors.toList()))
      );
  }

  @Override
  public Operation addMeeting(Meeting meeting) {
    return meetingRepository.addMeeting(meeting);
  }

  @Override
  public Operation rateMeeting(String id, String rating) {
    return ratingRepository.rateMeeting(id, rating);
  }
}
