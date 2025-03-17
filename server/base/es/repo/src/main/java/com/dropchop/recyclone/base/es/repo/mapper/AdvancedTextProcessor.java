package com.dropchop.recyclone.base.es.repo.mapper;

import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.mapper.parser.ClauseFactory;
import com.dropchop.recyclone.base.es.repo.mapper.parser.SpanNearBuilder;
import com.dropchop.recyclone.base.es.repo.mapper.parser.TextParser;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AdvancedTextProcessor {
  private final String name;
  private final String value;
  private final Boolean inOrder;
  private final Integer slop;

  public QueryNodeObject process() {
    List<String> tokens = new TextParser(value).parse();

    SpanNearBuilder builder = new SpanNearBuilder()
      .withInOrder(inOrder)
      .withSlop(slop);

    ClauseFactory factory = new ClauseFactory(name);

    tokens.stream()
      .map(factory::createClause)
      .forEach(builder::addClause);

    return builder.build();
  }
}
