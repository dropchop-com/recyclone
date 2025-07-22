package com.dropchop.recyclone.base.jpa.model.localization;

import com.dropchop.recyclone.base.api.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.api.model.marker.HasTags;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.jpa.model.base.JpaCode;
import com.dropchop.recyclone.base.jpa.model.base.JpaTitleTranslationHelper;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 21. 07. 25.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dictionary_term")
public class JpaDictionaryTerm extends JpaCode
    implements DictionaryTerm<JpaTag, JpaTitleTranslation, JpaTitleDescriptionTranslation>,
    JpaTitleTranslationHelper, HasCreated, HasModified,
    HasTags<JpaTag, JpaTitleDescriptionTranslation> {


  private ZonedDateTime created;
  private ZonedDateTime modified;

  @ElementCollection
  @CollectionTable(
      name="dictionary_term_l",
      uniqueConstraints = @UniqueConstraint(
          name = "uq_dictionary_term_l_fk_language_code_lang", columnNames = {"fk_dict_term_code", "lang"}
      ),
      foreignKey = @ForeignKey(name = "dictionary_term_l_fk_dict_term_code"),
      joinColumns = @JoinColumn(name="fk_dict_term_code")
  )
  private Set<JpaTitleTranslation> translations;

  @ManyToMany
  @JoinTable(
      name="dictionary_term_t",
      uniqueConstraints = @UniqueConstraint(
          name = "uq_dictionary_term_t_fk_dict_term_code_fk_tag_uuid", columnNames = {"fk_dict_term_code", "fk_tag_uuid"}),
      joinColumns = @JoinColumn( name="fk_dict_term_code", foreignKey = @ForeignKey(name = "dictionary_term_t_fk_dict_term_code")),
      inverseJoinColumns = @JoinColumn( name="fk_tag_uuid", foreignKey = @ForeignKey(name = "dictionary_term_t_fk_tag_uuid"))
  )
  @OrderColumn(name = "idx")
  private List<JpaTag> tags;

}
