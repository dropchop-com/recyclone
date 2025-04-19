package com.dropchop.recyclone.base.jpa.repo;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.invoke.TagParams;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;

import java.util.List;

public class ByTagsCriteriaDecorator extends BlazeCriteriaDecorator {


  @Override
  public void decorate() {
    String alias = getContext().getRootAlias();
    Params params = getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    if (params instanceof TagParams tagParams) {
      List<Tag> tags = tagParams.getTags();
      if (tags != null && !tags.isEmpty()) {
        cb.where(alias + DELIM + "tags.uuid").in(tags.stream().map(Tag::getId).toList());
      }
    }
  }
}
