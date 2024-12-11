package com.dropchop.recyclone.model.api.localization;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 05. 22.
 */
public interface TranslationHelper<TR extends Translation> {

  TR getTranslationInstance(String lngCode);
}
