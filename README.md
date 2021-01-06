### play-anorm-nano-precision

This is a minimal application to reproduce a bug linked to play-anorm and the java.sql.Timestap -> java.time.Instant transformation.

## How to reproduce

```shell
docker-compose -f docker-compose.test.yml up -d
sbt test
```
