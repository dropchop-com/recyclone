package com.dropchop.recyclone.base.api.model.cache;

import com.dropchop.recyclone.base.api.model.base.ModelWithCode;
import com.dropchop.recyclone.base.api.model.marker.HasLanguageCode;

/**
 * This interface describes a cache key used for writing to and searching from a cache structure.
 * The most important class filed is a constructed code which is used to identify the object in the cache structure.
 * <br />
 * In contrast, when a search is conducted, individual search code parts are concatenated together and used as a key.
 * Class fields used for key construction upon searching are prefixed with a search prefix.
 * <br />
 * If a cache key is language-specific, if it returns a non-null language code, otherwise an object under
 * the key is considered as language agnostic.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 04. 25.
 */
@SuppressWarnings("unused")
public interface CacheKey extends ModelWithCode, HasLanguageCode {

  String getSearchCode();

  void setSearchCode(String code);

  String getSearchLang();

  void setSearchLang(String lang);

  boolean hasSearchLang();

  boolean hasWildcard();
}
