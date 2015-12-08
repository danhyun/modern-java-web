package meeting;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import redis.DefaultRatingRepository;
import redis.RatingRepository;

import javax.inject.Singleton;

public class MeetingModule extends AbstractModule {
  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  public RatingRepository ratingRepository(RedisAsyncCommands<String, String> commands) {
    return new DefaultRatingRepository(commands);
  }

  @Provides
  @Singleton
  public MeetingService meetingService(MeetingRepository meetingRepository, RatingRepository ratingRepository) {
    return new DefaultMeetingService(meetingRepository, ratingRepository);
  }
}
