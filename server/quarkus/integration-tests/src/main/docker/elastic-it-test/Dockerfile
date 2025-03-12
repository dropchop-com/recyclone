FROM docker.elastic.co/elasticsearch/elasticsearch:8.15.3

USER 0:0

COPY dummy-index-tpl.json /dummy-index-tpl.json
COPY event-index-tpl.json /event-index-tpl.json

COPY init-index.sh /init-index.sh
COPY init-script.sh /init-script.sh

RUN chmod +x /init-index.sh
RUN chmod +x /init-script.sh

ENTRYPOINT ["/init-script.sh"]
CMD ["/bin/tini", "--", "/usr/local/bin/docker-entrypoint.sh"]

# for development run:
# docker build --progress plain -t tmp-elastic .
# docker run -e "discovery.type=single-node" -e "xpack.security.enabled=false" tmp-elastic
#

USER 1000:0
