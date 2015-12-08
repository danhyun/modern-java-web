package redis;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import ratpack.server.Service;
import ratpack.server.StopEvent;

import javax.inject.Singleton;

public class RedisModule extends AbstractModule {
  @Override
  protected void configure() { }

  @Provides
  @Singleton
  public RedisClient redisClient(RedisConfig config) {
    return RedisClient.create(config.getUrl());
  }

  @Provides
  @Singleton
  public StatefulRedisConnection<String, String> asyncCommands(RedisClient client) {
    return client.connect();
  }

  @Provides
  @Singleton
  public RedisAsyncCommands<String, String> asyncCommands(StatefulRedisConnection<String, String> connection) {
    return connection.async();
  }

  @Provides
  @Singleton
  public Service redisCleanup(RedisClient client, StatefulRedisConnection<String, String> connection) {
    return new Service() {
      @Override
      public void onStop(StopEvent event) throws Exception {
        connection.close();
        client.shutdown();
      }
    };
  }
}
