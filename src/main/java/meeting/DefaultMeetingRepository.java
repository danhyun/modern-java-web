package meeting;

import org.jooq.DSLContext;
import ratpack.exec.Blocking;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import javax.inject.Inject;
import java.util.List;

import static jooq.tables.Meeting.MEETING;

public class DefaultMeetingRepository implements MeetingRepository {
  private final DSLContext context;

  @Inject
  public DefaultMeetingRepository(DSLContext context) {
    this.context = context;
  }

  @Override
  public Promise<List<Meeting>> getMeetings() {
    return Blocking.get(() ->
      context
        .select()
        .from(MEETING)
        .fetchInto(Meeting.class)
    );
  }

  @Override
  public Operation addMeeting(Meeting meeting) {
    return Blocking.op(() ->
      context.newRecord(MEETING, meeting).store()
    );
  }
}
