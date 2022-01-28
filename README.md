# Snake

A Snake-inspired multiplayer online video game for web browsers.

Play it now at https://snake.timoeberl.de/

The game was created as a project in the module [Software Develoment 3](https://www.hdm-stuttgart.de/studierende/stundenplan/studieninhalte/block?sgname=Medieninformatik+%28Bachelor%2C+7+Semester%29&sgblockID=2573372&sgang=550033&blockname=Software-Entwicklung+3).

## Team

@aw181 @fb098 @jg160 @lm154 @nh096 @tb166 @te038

## Supervisors

@nt030 @hahn

## Deploy with docker

> Read about [how to install docker](https://docs.docker.com/get-docker/).

### Compose file modification

Open `docker-compose.yaml` and set the password for the database. The password needs to be added in 2 playes, marked with a comment:
```yaml
# set database password
```

### Build, Create and Start

#### Linux

run the command

```bash
docker-compose up -d
```
