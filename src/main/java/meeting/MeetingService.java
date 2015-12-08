package meeting;

import ratpack.exec.Operation;
import ratpack.exec.Promise;

import java.util.List;

public interface MeetingService {
  Promise<List<Meeting>> getMeetings();
  Operation addMeeting(Meeting meeting);
  Operation rateMeeting(String id, String rating);
}
