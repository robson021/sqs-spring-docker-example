#How to run

```bash
docker-compose up -d
./gradlew clean bootRun
aws --endpoint-url http://localhost:9324 sqs send-message --queue-url http://localhost:9324/queue/default --message-body "Hello, queue"
```
or
```bash
./scripts/restart-queue.sh
./gradlew clean bootRun
```