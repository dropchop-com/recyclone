package com.dropchop.recyclone.model.dto.localization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
public class TitleTranslation implements com.dropchop.recyclone.model.api.localization.TitleTranslation {

  @NonNull
  private String lang;

  @NonNull
  @EqualsAndHashCode.Exclude
  private String title;

  private Boolean base;
}
