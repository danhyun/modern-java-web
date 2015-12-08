package meeting;

import ratpack.exec.Operation;
import ratpack.exec.Promise;

import java.util.List;

public interface MeetingRepository {
  Promise<List<Meeting>> getMeetings();
  Operation addMeeting(Meeting meeting);
}
