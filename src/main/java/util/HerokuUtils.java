package util;

import com.google.common.base.Strings;
import ratpack.func.Pair;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface HerokuUtils {
  Function<String, List<String>> extractDbProperties = (url) -> {
    if (Strings.isNullOrEmpty(url)) return Collections.<String>emptyList();

    Pattern herokuDbPattern = Pattern
      .compile("postgres://(?<username>[^:]+):(?<password>[^:]+)@(?<serverName>[^:]+):(?<portNumber>[0-9]+)/(?<databaseName>.+)");

    Matcher matcher = herokuDbPattern.matcher(url);
    if (!matcher.matches()) return Collections.<String>emptyList();

    return Stream
      .of("username", "password", "databaseName", "serverName", "portNumber")
      .map(prop -> Pair.of(prop, matcher.group(prop)))
      .map(pair -> pair.left.equals(pair.left.toLowerCase()) ?
        pair : Pair.of("dataSourceProperties." + pair.left, pair.right)
      )
      .map(pair -> Pair.of("db." + pair.left, pair.right))
      .map(pair -> pair.left + "=" + pair.right)
      .collect(Collectors.toList());
  };
}
