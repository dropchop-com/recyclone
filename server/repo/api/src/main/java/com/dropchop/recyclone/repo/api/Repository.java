package com.dropchop.recyclone.repo.api;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
public interface Repository<E> {

  Class<E> getRootClass();
}
