package com.dropchop.recyclone.base.es.repo.config;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/26/25.
 */
@Getter
@SuperBuilder
public class IngestPipelineIndexConfig extends DefaultIndexConfig implements HasIngestPipeline {
  private final String ingestPipeline;
}
