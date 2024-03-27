package com.dropchop.recyclone.repo.api;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
public interface Repository<E, ID> {

  Class<E> getRootClass();
}
