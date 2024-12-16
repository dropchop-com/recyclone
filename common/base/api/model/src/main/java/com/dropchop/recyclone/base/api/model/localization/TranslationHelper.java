package com.dropchop.recyclone.base.api.model.localization;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 05. 22.
 */
public interface TranslationHelper<TR extends Translation> {

  TR getTranslationInstance(String lngCode);
}
