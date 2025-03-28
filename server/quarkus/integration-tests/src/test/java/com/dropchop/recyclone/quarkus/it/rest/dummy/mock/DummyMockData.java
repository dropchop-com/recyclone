package com.dropchop.recyclone.quarkus.it.rest.dummy.mock;

import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import io.quarkus.test.Mock;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mock
@SuppressWarnings("unused")
public class DummyMockData {

  private TitleDescriptionTranslation createTranslations(
    String lang, String title, String description, Boolean base) {
    TitleDescriptionTranslation translation = new TitleDescriptionTranslation();
    translation.setBase(true);
    translation.setTitleDescription(lang, title, description);
    return translation;
  }

  private Dummy createDummy(String code, String title, String description, String lang,
                            ZonedDateTime created, ZonedDateTime modified, ZonedDateTime deactivated,
                            Set<TitleDescriptionTranslation> translations) {
    Dummy dummy = new Dummy();
    dummy.setCode(code);
    dummy.setTitle(title);
    dummy.setDescription(description);
    dummy.setLang(lang);
    dummy.setCreated(created);
    dummy.setModified(modified);
    dummy.setDeactivated(deactivated);
    dummy.setTranslations(translations);
    return dummy;
  }

  public List<Dummy> createMockDummies() {
    List<Dummy> dummies = new ArrayList<>();

    Set<TitleDescriptionTranslation> translations1 = new HashSet<>();
    translations1.add(createTranslations("en", "Title 1", "Description 1", true));
    dummies.add(createDummy("sad15s1a21sa21a51a","Dummy 1",
      "Description 1", "en", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(1), null, translations1));

    Set<TitleDescriptionTranslation> translations2 = new HashSet<>();
    translations2.add(createTranslations("fr", "Titre 2", "Description 2", true));
    dummies.add(createDummy("asdlasdadsa4dsds4d","Dummy 2",
      "Description 2", "fr", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(2), null, translations2));

    Set<TitleDescriptionTranslation> translations3 = new HashSet<>();
    translations3.add(createTranslations("de", "Titel 3", "Beschreibung 3", false));
    dummies.add(createDummy("4d5as45s1ds4d5ss8sd6s","Dummy 3",
      "Beschreibung 3", "de", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(3), null, translations3));

    Set<TitleDescriptionTranslation> translations4 = new HashSet<>();
    translations4.add(createTranslations("es", "Título 4", "Descripción 4", false));
    dummies.add(createDummy("48s4d51sa5s45s121s","Dummy 4",
      "Descripción 4", "es", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(4), null, translations4));

    Set<TitleDescriptionTranslation> translations5 = new HashSet<>();
    translations5.add(createTranslations("it", "Titolo 5", "Descrizione 5", true));
    dummies.add(createDummy("asdasdsadassa1s11s1ss","Dummy 5",
      "Descrizione 5", "it", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(5), ZonedDateTime.now().plusDays(1), translations5));

    Set<TitleDescriptionTranslation> translations6 = new HashSet<>();
    translations6.add(createTranslations("pt", "Título 6", "Descrição 6", false));
    dummies.add(createDummy("sdas1s1s1s1s11s1s1s1s1","Dummy 6",
      "Descrição 6", "pt", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(6), null, translations6));

    Set<TitleDescriptionTranslation> translations7 = new HashSet<>();
    translations7.add(createTranslations("ru", "Заголовок 7", "Описание 7", true));
    dummies.add(createDummy("8s1s5s45aw1g45f4g","Dummy 7",
      "Описание 7", "ru", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(7), null, translations7));

    Set<TitleDescriptionTranslation> translations8 = new HashSet<>();
    translations8.add(createTranslations("zh", "标题8", "描述8", true));
    dummies.add(createDummy("wh48u2f4v8h4t2r6","Dummy 8",
      "描述8", "zh", ZonedDateTime.now(),
      ZonedDateTime.now().plusHours(8), null, translations8));

    return dummies;
  }
}
