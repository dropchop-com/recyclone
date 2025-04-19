package com.dropchop.recyclone.base.jpa.repo.tagging;

import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import com.dropchop.recyclone.base.jpa.repo.ByTagsCriteriaDecorator;
import com.dropchop.recyclone.base.jpa.repo.LikeTypeCriteriaDecorator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 06. 22.
 */
@Getter
@ApplicationScoped
public class TagRepository extends BlazeRepository<JpaTag, UUID> {

  Class<JpaTag> rootClass = JpaTag.class;

  @Override
  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    Collection<CriteriaDecorator> commonDecorators = super.getCommonCriteriaDecorators();
    ArrayList<CriteriaDecorator> tagDecorators = new ArrayList<>(commonDecorators);
    tagDecorators.add(new LikeTypeCriteriaDecorator());
    tagDecorators.add(new ByTagsCriteriaDecorator());
    return tagDecorators;
  }
}
