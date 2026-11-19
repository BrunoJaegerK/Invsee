# InvseePM1E

Plugin for **Nukkit PetteriM1 Edition (PM1E)** that allows authorized staff to inspect and edit the inventories and ender chests of online players.

This project is an adaptation of [LeonTenDev/Invsee](https://github.com/LeonTenDev/Invsee), updated for the PM1E server API. Original command names and configurable messages are retained.

## Features

- `/invsee <player>` — inspect and edit a player's inventory.
- `/inv <player>` — alias of `/invsee`.
- `/endersee <player>` — inspect and edit a player's ender chest.
- `/ecsee <player>` — alias of `/endersee`.
- Configurable prefix and messages in `plugins/InvseePM1E/config.yml`.
- Separate target reference for every opened menu, avoiding cross-player edits when multiple staff members use the plugin at once.
- Display-only filler slots in the inventory menu cannot be moved into a player's inventory.

## Requirements

- Nukkit PetteriM1 Edition.
- Java 17 or newer.
- FakeInventories **1.0.3-PM1E.2** compatible build.

Install FakeInventories on the server before installing InvseePM1E.

## Build

### 1. Install PM1E into your local Maven repository

Clone or open your PM1E source project and run this command from the directory containing its `pom.xml`:

```bash
mvn install -DskipTests
```

This project expects the Maven coordinates `cn.nukkit:Nukkit:PM1E`.

### 2. Install the PM1E-compatible FakeInventories jar

If version `1.0.3-PM1E.2` is not available from your configured Maven repositories, install the jar you use on your server into your local Maven repository:

```bash
mvn install:install-file -Dfile="libs/FakeInventories-1.0.3-PM1E.2.jar" -DgroupId=com.nukkitx -DartifactId=fakeinventories -Dversion=1.0.3-PM1E.2 -Dpackaging=jar
```

Adjust the `-Dfile` path if your jar is stored somewhere else.

### 3. Build InvseePM1E

Run from this project's root directory:

```bash
mvn clean package
```

The plugin jar will be generated at:

```
target/InvseePM1E.jar
```

Copy it and the compatible FakeInventories plugin jar into the server's `plugins` directory.

## Permissions

| Permission | Default | Description |
|---|---|---|
| `invsee.cmd` | op | Use `/invsee` and `/inv` |
| `endersee.cmd` | op | Use `/endersee` and `/ecsee` |

Operators can use both commands.

## Credits

- Original InvSee implementation: [LeonTenDev/Invsee](https://github.com/LeonTenDev/Invsee) — original author credited as Leon1917141 in the plugin metadata.
- PM1E adaptation and maintenance: BrunoJaegerK.

This repository did not contain a license file when the adaptation began. The original project's license terms should be checked with its author before redistributing modified source under a newly chosen license.
