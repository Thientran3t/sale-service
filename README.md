# Setup Environment
### Apache Kafka
I was used docker compose to run the zookeeper and broker for Apache Kafka.
Docker compose file: `docker-compose.yml`

Start: move to folder include docker-compose.yml file and run

```bash
docker compose up -d
```
Stop:
```bash
docker compose down
```
Refer link: [Apache Kafka® Quick Start][]


## Services
Start the `ProducerApplication` first to create the `vnet` topic.

After that start `ConsumerApplication`.

## UI
After start all above services successfully. Please refer README.md in [this repository][] to start Angular application.

[Apache Kafka® Quick Start]: https://developer.confluent.io/quickstart/kafka-docker/
[this repository]: https://github.com/Thientran3t/sale-ui

## Config Properties Explanation:

### Producer Application
`origin.source.path`: path of folder origin files

`archive.source.path`: path of folder to archive files after read

`sequence.time.seconds`: sequence time to read each file.