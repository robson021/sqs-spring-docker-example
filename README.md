## How to run

Requirements:

- AWS CLI
- Docker

Run manually:

```bash
docker-compose up -d
./gradlew clean bootRun
```

Run with scripts:

```bash
./scripts/restart-queue.sh
./gradlew clean bootRun
```

Publish messages with AWS CLI:

```bash
aws --endpoint-url http://localhost:9324 sqs send-message --queue-url http://localhost:9324/queue/default --message-body "Hello, queue"
```

Queue implementation used:

- https://hub.docker.com/r/roribio16/alpine-sqs/
- https://github.com/roribio/alpine-sqs