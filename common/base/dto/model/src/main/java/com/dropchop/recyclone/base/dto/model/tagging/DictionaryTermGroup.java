package com.dropchop.recyclone.base.dto.model.tagging;

import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class DictionaryTermGroup extends GroupTag
  implements com.dropchop.recyclone.base.api.model.tagging.DictionaryTermGroup<Tag, TitleDescriptionTranslation> {

}
