package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
@JsonIgnoreProperties({"name", "value", "analyzer", "slop"})
public class Phrase<T> extends PhraseParameters<T> {

  public Phrase() {
  }

  public Phrase(T name, T value) {
    super(name, value);
  }

  public Phrase(T name, T value, Integer slop) {
    super(name, value, slop);
  }

  public Phrase(T name, T value, String analyzer) {
    super(name, value, analyzer);
  }

  public Phrase(T name, T value, String analyzer, Integer slop) {
    super(name, value, analyzer, slop);
  }

  public PhraseParameters<T> get$phrase() {
    return new PhraseParameters<>(
      super.getName(),
      super.getValue(),
      super.getAnalyzer(),
      super.getSlop()
    );
  }

  public void set$phrase(PhraseParameters<T> phrase) {
    if (phrase != null) {
      setName(phrase.getName());
      setValue(phrase.getValue());
      setAnalyzer(phrase.getAnalyzer());
      setSlop(phrase.getSlop());
    }
  }
}
