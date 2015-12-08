package meeting;

import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;
import ratpack.path.PathTokens;


public class MeetingChainAction implements Action<Chain> {
  @Override
  public void execute(Chain chain) throws Exception {
    chain
      .path(ctx -> {
        MeetingService service = ctx.get(MeetingService.class);
        ctx
          .byMethod(method -> method
            .get(() -> service
                .getMeetings()
                .map(Jackson::json)
                .then(ctx::render)
            )
            .post(() -> ctx
              .parse(Jackson.fromJson(Meeting.class))
              .nextOp(service::addMeeting)
              .map(m -> "Added meeting for " + m.getOrganizer())
              .then(ctx::render)
            )
          );
      })
      .get(":id:\\d+/rate/:rating:[1-5]", ctx -> {
        MeetingService service = ctx.get(MeetingService.class);
        PathTokens pathTokens = ctx.getPathTokens();
        service
          .rateMeeting(pathTokens.get("id"), pathTokens.get("rating"))
          .then(() -> ctx.redirect("/meeting"));
      });
  }
}
