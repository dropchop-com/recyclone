package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.base.State;
import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
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
public class Params implements Dto, CommonParams {

  @Override
  @JsonIgnore
  public List<String> getAvailableVersions() {
    return CommonParams.super.getAvailableVersions();
  }

  @Override
  @JsonIgnore
  public List<String> getAvailableLevelOfContentDetails() {
    return CommonParams.super.getAvailableLevelOfContentDetails();
  }

  @Override
  @JsonIgnore
  public Collection<State.Code> getHiddenStates() {
    return CommonParams.super.getHiddenStates();
  }

  @Override
  @JsonIgnore
  public String[] getSortFields() {
    return CommonParams.super.getSortFields();
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
