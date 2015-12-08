package redis;

import ratpack.exec.Operation;
import ratpack.exec.Promise;
import ratpack.func.Pair;

import java.util.Map;
import java.util.stream.IntStream;

public interface RatingRepository {
  Promise<Map<String, String>> getRatings(Long meetingId);

  default Promise<Double> getAverageRating(Long meetingId) {
    return getRatings(meetingId)
      .map(m -> m.entrySet()
        .stream()
        .map(e -> Pair.of(Integer.valueOf(e.getKey()), Integer.valueOf(e.getValue())))
        .flatMapToInt(pair -> IntStream.range(0, pair.right).map(i -> pair.left))
        .average().orElse(0d)
    );
  }

  Operation rateMeeting(String meetingId, String rating);
}
