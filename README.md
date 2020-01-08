## Bus Routes Per Day For Each Line

1. Launch docker containers `docker-compose up -d`
2. Import ES index mapping [mapping.http](docs/mapping.http)
3. Import Grafana dashboard [dashboard-single-line.json](docs/dashboard-single-line.json)

```bash
clear;    
mvn compile exec:java \
    -Dexec.mainClass=it.kruczek.ztm.Processing.Pipelines.ImportLocalFilesWatcher \
    -Dexec.args="--inputDirectory=${PWD}/data/**.xml --elasticSearchUrl=http://localhost:9200 --elasticSearchUsername=elastic --elasticSearchPassword=changeme --elasticSearchIndex=buses" \
    -Pdirect-runner
```

## Data

All gathered data can be found [here](https://michal.kruczek.it/data/)

## Troubleshooting

> max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

```
sudo sysctl -w vm.max_map_count=262144
```
