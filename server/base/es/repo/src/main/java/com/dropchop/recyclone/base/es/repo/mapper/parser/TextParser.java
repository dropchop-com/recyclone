package com.dropchop.recyclone.base.es.repo.mapper.parser;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TextParser {
  private final String input;
  private int position;
  private final StringBuilder currentToken = new StringBuilder();
  private boolean inPhrase;

  public TextParser(String input) {
    this.input = input.trim();
  }

  public List<String> parse() {
    List<String> tokens = new ArrayList<>();
    while (position < input.length()) {
      char c = input.charAt(position++);
      handleCharacter(c, tokens);
    }
    if (!currentToken.isEmpty()) {
      tokens.add(currentToken.toString());
    }
    return tokens;
  }

  private void handleCharacter(char c, List<String> tokens) {
    if (c == '"') {
      handleQuote(tokens);
    } else if (Character.isWhitespace(c) && inPhrase) {
      completeToken(tokens);
    } else {
      currentToken.append(c);
    }
  }

  private void handleQuote(List<String> tokens) {
    if (inPhrase) {
      completeToken(tokens);
      inPhrase = false;
    } else {
      inPhrase = true;
      if (!currentToken.isEmpty()) {
        completeToken(tokens);
      }
    }
  }

  private void completeToken(List<String> tokens) {
    if (!currentToken.isEmpty()) {
      tokens.add(currentToken.toString());
      currentToken.setLength(0);
    }
  }
}
