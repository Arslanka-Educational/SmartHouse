# SmartHouse Design Document

## Functional Requirements

### RU

* ___Функциональные требования безопасности системы___
    | №  | Требования                          | Приоритет  |  Трудоемкость  |
    |----|-------------------------------------|------------|----------------|
    | 1  |Система пожарной сигнализации должна  автоматически вызывать пожарных и отправлять уведомление пользователю (+ включать сигнализацию) при возгорании или задымлении в помещении (повышении температуры или уровня дыма)|    must   |    8 члвк/часов    |
    | 2  | Система должна обнаруживать и регистрировать движения в зоне видимости камер, сохраняя данные для удаленного доступа | must       |     6 члвк/часов   |
    | 3  | Система  интеграции с дверьми и окнами должна регистрировать (+ включать сигнализацию) несанкционированное проникновение и отправлять уведомление пользователю и/или вызывать полицию  | must       |     8 члвк/часов  |
    | 4  | Система должна предоставлять удаленный просмотр видео с камер наблюдения через приложение  | must       |     3 члвк/часа  |
    | 5  | Система должна предоставлять защиту от протечек с возможностью автоматического перекрытия подачи воды и уведомлением пользователя при обнаружении утечки   | must |     6 члвк/часов              |
    | 6  | Система должна предоставлять контролaь доступа с использованием персональных кодов для безопасного и удобного входа и выхода из дома | must |     6 члвк/часов  |
    | 7  | Система может предоставлять автоматическое резервное копирования и восстановление для сохранения настроек системы безопасности и важной информации в случае сбоев  | could       |     6 члвк/часов     |


## System Design

```mermaid
graph TB

subgraph smarthouse-backend

api-gateway <-- JSON / HTTP --> core-temperature-handler
core-temperature-handler --> core-temperature-PostgreSQL[(PostrgeSQL)]
core-temperature-queue[/core-temperature-redis-queue/]
core-temperature-handler --> core-temperature-queue --> core-temperature-sensore-adapter
core-temperature-sensore-adapter -- JSON / HTTP callback --> core-temperature-handler

api-gateway <-- JSON / HTTP --> core-smoke-handler
core-smoke-handler --> core-smoke-PostgreSQL[(PostrgeSQL)]
core-smoke-queue[/core-smoke-redis-queue/]
core-smoke-handler --> core-smoke-queue --> core-smoke-sensore-adapter
core-smoke-sensore-adapter -- JSON / HTTP callback--> core-smoke-handler


api-gateway <-- JSON / HTTP --> core-video-handler
core-video-handler --> core-video-PostgreSQL[(PostrgeSQL)]
core-video-queue[/core-video-redis-queue/]
core-video-handler --> core-video-queue --> core-video-sensore-adapter
core-video-sensore-adapter -- JSON / HTTP callback --> core-video-handler


core-notification --> core-notification-postgres[(PostrgeSQL)]
core-video-handler --> core-video-notification-queue[/core-video-notificationredis-queue/] --> core-notification
core-smoke-handler -->  core-smoke-nofitication-queue[/core-smoke-nofitication-queue/]
--> core-notification
core-temperature-handler --> core-temperature-notification-queue[/core-temperature-notification-redis-queue/]
--> core-notification


core-video-handler --> core-logging-queue
core-smoke-handler --> core-logging-queue
core-temperature-handler --> core-logging-queue
core-logging-queue[/core-logging-queue/] --> logging-consumer -->  core-logging-ClickHouse[(ClickHouse)]



end

subgraph mobile
application -- JSON/ HTTPS --> api-gateway
end

subgraph sensore-client

core-video-sensore-adapter --> sensore
core-smoke-sensore-adapter --> sensore
core-temperature-sensore-adapter --> sensore

end
```
