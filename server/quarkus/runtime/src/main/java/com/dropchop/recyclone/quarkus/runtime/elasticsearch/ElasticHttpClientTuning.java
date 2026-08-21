package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import io.quarkus.elasticsearch.restclient.lowlevel.ElasticsearchClientConfig;
import io.quarkus.elasticsearch.restclient.lowlevel.ElasticsearchClientConfigConfigurer;
import jakarta.inject.Inject;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.core5.pool.ConnPoolControl;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 10. 2025.
 */
@ElasticsearchClientConfig
public class ElasticHttpClientTuning implements ElasticsearchClientConfigConfigurer {

  @Inject
  ElasticConnectionEvictor evictor;

  @Override
  public void accept(HttpAsyncClientBuilder httpClientBuilder) {
    // HttpComponents 5 enables SO_KEEPALIVE by default. Do not replace the
    // IOReactorConfig here, as that would discard Quarkus's configured I/O thread count.

    // Quarkus configures the pool before invoking custom configurers. Reuse it so
    // its connection limits, timeouts and TLS settings are retained.
    if (!(httpClientBuilder.getConnManager() instanceof ConnPoolControl<?> connPool)) {
      throw new IllegalStateException("Elasticsearch HTTP client is not using a connection pool");
    }
    evictor.start(connPool);
  }
}
