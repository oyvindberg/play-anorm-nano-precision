### play-anorm-nano-precision

This is a minimal application to reproduce a bug linked to play-anorm and the java.sql.Timestap -> java.time.ZonedDateTime transformation.
This application was strongly inspired by https://github.com/FXHibon/play-anorm-nano-precision

It' related to the issue https://github.com/playframework/anorm/issues/408

## How to reproduce

```shell
sbt test
```
