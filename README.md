## Bus Routes Per Day For Each Line

```bash
clear;    
mvn compile exec:java \
    -Dexec.mainClass=it.kruczek.ztm.Processing.ImportDataPipeline \
    -Dexec.args="--inputDirectory=${PWD}/data/**.xml --elasticSearchUrl=http://localhost:9200 --elasticSearchUsername=elastic --elasticSearchPassword=changeme --elasticSearchIndex=buses" \
    -Pdirect-runner
```

## Run Docker Elastic Search

```bash

docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.2.3

```

## Troubleshooting

> max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

```
sudo sysctl -w vm.max_map_count=262144
```

> ElasticHQ URL 

`http://elastic:changeme@localhost:9200`