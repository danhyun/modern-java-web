package redis;

import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;

public class DefaultRatingRepository implements RatingRepository {
  private final RedisAsyncCommands<String, String> commands;

  @Inject
  public DefaultRatingRepository(RedisAsyncCommands<String, String> commands) {
    this.commands = commands;
  }

  Function<Long, String> getKeyForMeeting = (id) -> "meeting:" + id + ":rating";

  @Override
  public Promise<Map<String, String>> getRatings(Long meetingId) {
    return Promise.of(downstream ->
      commands
        .hgetall(getKeyForMeeting.apply(meetingId))
        .thenAccept(downstream::success)
    );
  }

  @Override
  public Operation rateMeeting(String meetingId, String rating) {
    return Promise.of(downstream ->
      commands.hincrby(
        getKeyForMeeting.apply(Long.valueOf(meetingId)),
        String.valueOf(rating), 1
      ).thenAccept(downstream::success)
    ).operation();
  }
}
