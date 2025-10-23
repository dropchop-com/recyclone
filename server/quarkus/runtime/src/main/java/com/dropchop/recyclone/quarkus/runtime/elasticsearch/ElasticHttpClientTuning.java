package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import io.quarkus.elasticsearch.restclient.lowlevel.ElasticsearchClientConfig;
import jakarta.inject.Inject;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.elasticsearch.client.RestClientBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 10. 2025.
 */
@ElasticsearchClientConfig
public class ElasticHttpClientTuning implements RestClientBuilder.HttpClientConfigCallback {

  @Inject
  ElasticConnectionEvictor evictor;

  @Override
  public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
    // Enable TCP keepalive
    IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
        .setSoKeepAlive(true)
        .build();

    // Create a connection manager with proper cleanup
    PoolingNHttpClientConnectionManager connManager;
    try {
      connManager = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(ioReactorConfig));
    } catch (IOReactorException e) {
      throw new RuntimeException(e);
    }
    evictor.start(connManager);
    return httpClientBuilder.setConnectionManager(connManager);
  }
}
