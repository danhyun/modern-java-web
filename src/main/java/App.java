import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariConfig;
import jooq.JooqModule;
import meeting.MeetingChainAction;
import meeting.MeetingModule;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import redis.RedisConfig;
import redis.RedisModule;
import util.HerokuUtils;

import java.util.List;


public class App {
  public static void main(String[] args) throws Exception {
    List<String> programArgs = Lists.newArrayList(args);
    programArgs.addAll(
      HerokuUtils.extractDbProperties
        .apply(System.getenv("DATABASE_URL"))
    );

    RatpackServer.start(serverSpec -> serverSpec
      .serverConfig(config -> config
        .baseDir(BaseDir.find())
        .yaml("postgres.yaml")
        .yaml("redis.yaml")
        .env()
        .sysProps()
        .args(programArgs.stream().toArray(String[]::new))
        .require("/db", HikariConfig.class)
        .require("/redis", RedisConfig.class)
      )
      .registry(Guice.registry(bindings -> bindings
        .module(HikariModule.class)
        .module(JooqModule.class)
        .module(RedisModule.class)
        .module(MeetingModule.class)
        .bind(MeetingChainAction.class)
      ))
      .handlers(chain -> chain.
        prefix("meeting", MeetingChainAction.class)
      )
    );
  }
}
