package jooq;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import meeting.DefaultMeetingRepository;
import meeting.MeetingRepository;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import javax.inject.Singleton;
import javax.sql.DataSource;

public class JooqModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(MeetingRepository.class).to(DefaultMeetingRepository.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  public DSLContext dslContext(DataSource dataSource) {
    return DSL.using(new DefaultConfiguration().derive(dataSource));
  }
}
