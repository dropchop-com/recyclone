package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.State;
import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.Dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class Params implements Dto, com.dropchop.recyclone.model.api.invoke.Params {

  @Override
  @JsonIgnore
  public List<String> getAvailableVersions() {
    return com.dropchop.recyclone.model.api.invoke.Params.super.getAvailableVersions();
  }

  @Override
  @JsonIgnore
  public List<String> getAvailableLevelOfContentDetails() {
    return com.dropchop.recyclone.model.api.invoke.Params.super.getAvailableLevelOfContentDetails();
  }

  @Override
  @JsonIgnore
  public Collection<State.Code> getHiddenStates() {
    return com.dropchop.recyclone.model.api.invoke.Params.super.getHiddenStates();
  }

  @Override
  @JsonIgnore
  public String[] getSortFields() {
    return com.dropchop.recyclone.model.api.invoke.Params.super.getSortFields();
  }

  private String requestId;

  private String lang;

  @Builder.Default
  private String translationLang = null;

  @NonNull
  @Builder.Default
  private List<String> contentIncludes = new ArrayList<>();

  @NonNull
  @Builder.Default
  private List<String> contentExcludes = new ArrayList<>();

  @Builder.Default
  private Integer contentTreeLevel = null;

  @Builder.Default
  private String contentDetailLevel = null;

  @Builder.Default
  private String version = null;

  @Builder.Default
  private int size = 100;

  @Builder.Default
  private int from = 0;

  @Singular
  private List<String> states = new ArrayList<>();

  @Singular("sort")
  private List<String> sort = new ArrayList<>();

  @Singular
  private Set<Attribute<?>> attributes = new LinkedHashSet<>();
}
