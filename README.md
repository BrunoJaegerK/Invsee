# InvseePM1E

Plugin for **Nukkit PetteriM1 Edition (PM1E)** that allows authorized staff to inspect and edit the inventories and ender chests of online players.

Adapted from [LeonTenDev/Invsee](https://github.com/LeonTenDev/Invsee) for PM1E.

## Features

- `/invsee <player>` — inspect and edit a player's inventory.
- `/inv <player>` — alias of `/invsee`.
- `/endersee <player>` — inspect and edit a player's ender chest.
- `/ecsee <player>` — alias of `/endersee`.
- Configurable prefix and messages in `plugins/InvseePM1E/config.yml`.
- Each opened menu retains its own target, preventing cross-player edits.
- Decorative slots in the inventory menu cannot be taken.

## Requirements

- Nukkit PetteriM1 Edition.
- Java 17 or newer.
- **PetteriM1/FakeInventories-1**, release `1.1.8-PM1E-IWareQ`.

Download the exact PM1E build from [FakeInventories-1 Releases](https://github.com/PetteriM1/FakeInventories-1/releases) and install it on the server.

## Build

### 1. Install PM1E locally

From the NukkitPetteriM1Edition project root:

```bash
mvn install -DskipTests
```

### 2. Install FakeInventories-1 locally

Download `FakeInventories-1.1.8-PM1E-IWareQ.jar` from the linked releases page. From the Invsee project root, run:

```bash
mvn install:install-file -Dfile="libs/FakeInventories-1.1.8-PM1E-IWareQ.jar" -DgroupId=me.iwareq -DartifactId=fakeinventories-pm1e -Dversion=1.1.8 -Dpackaging=jar
```

Change the file path if you saved the JAR elsewhere.

### 3. Compile Invsee

```bash
mvn clean package
```

Output:

```
target/InvseePM1E.jar
```

Place both `InvseePM1E.jar` and `FakeInventories-1.1.8-PM1E-IWareQ.jar` in the server's `plugins` directory.

## Permissions

| Permission | Default | Description |
|---|---|---|
| `invsee.cmd` | op | Use `/invsee` and `/inv` |
| `endersee.cmd` | op | Use `/endersee` and `/ecsee` |

## Credits

- Original plugin: [LeonTenDev/Invsee](https://github.com/LeonTenDev/Invsee), credited as Leon1917141 in its original metadata.
- PM1E adaptation: BrunoJaegerK.
