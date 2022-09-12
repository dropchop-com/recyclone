package com.dropchop.recyclone.model.entity.jpa.test;

import com.dropchop.recyclone.model.api.test.Node;
import com.dropchop.recyclone.model.entity.jpa.attr.EAttribute;
import com.dropchop.recyclone.model.entity.jpa.marker.HasEAttributes;
import com.dropchop.recyclone.model.entity.jpa.base.ECode;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 05. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "test_node")
@SuppressWarnings("JpaDataSourceORMInspection")
public class ENode extends ECode implements Node<ETitleTranslation, ENode>, HasEAttributes {
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, targetEntity = ENode.class)
  @JoinTable(name = "test_node_test_node",
    joinColumns = {
      @JoinColumn(name = "fk_test_node_code",
        foreignKey = @ForeignKey(name = "test_node_code_fk_test_node_code")
      )
    },
    inverseJoinColumns = {
      @JoinColumn(name = "fk_test_node_code",
        foreignKey = @ForeignKey(name = "test_node_code_fk_test_node_code")
      )
    }
  )
  @OrderBy("code ASC")
  private SortedSet<ENode> children;

  @ElementCollection
  @CollectionTable(
    name="test_node_a",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_test_node_a_name",
      columnNames = {"fk_test_node_code", "name"}
    ),
    foreignKey = @ForeignKey(name = "test_node_a_fk_test_node_code"),
    joinColumns = @JoinColumn(name="fk_test_node_code")
  )
  private Set<EAttribute<?>> eAttributes;

  @Column(name="title")
  private String title;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "test_node_fk_language_code"))
  private ELanguage language;

  @ElementCollection
  @CollectionTable(
    name="test_node_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_test_node_l_fk_language_code_lang",
      columnNames = {"fk_test_node_code", "lang"}
    ),
    foreignKey = @ForeignKey(name = "test_node_l_fk_test_node_code"),
    joinColumns = @JoinColumn(name="fk_test_node_code")
  )
  private Set<ETitleTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  @Transient
  private EMiki miki;
}
