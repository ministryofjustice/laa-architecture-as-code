![Publish](https://github.com/ministryofjustice/laa-architecture-as-code/workflows/Publish/badge.svg)
![Build and validate](https://github.com/ministryofjustice/laa-architecture-as-code/workflows/Build%20and%20validate/badge.svg)

# laa-architecture-as-code

Modelling architecture in Legal Aid Agency (LAA) with the [C4 model][c4] and [Structurizr][structurizr].

## Running

The project is built with `gradle`.

| Action | Command |
| --- | --- |
| Build the project | `./gradlew build` |
| Create a local Structurizr workspace JSON file | `./gradlew run` |
| Push to the remote Structurizr workspace | `./gradlew run --args='--push'`<br/>(please see **"Secrets"** section below) |

### :rotating_light: Remote-only changes will be lost

The remote workspace's content is _replaced_ with the content in this repository. Remote-only changes will be **lost**.

### Secrets

The `--push` command can be configured with these environment variables:

| Environment variable | Meaning |
| --- | --- |
| `STRUCTURIZR_API_KEY` | **Required** The API key for the Structurizr API. |
| `STRUCTURIZR_API_SECRET` | **Required** The API secret for the Structurizr API. |
| `STRUCTURIZR_WORKSPACE_ID` | Overrides the default workspace ID. |

Example:
```
STRUCTURIZR_WORKSPACE_ID=12345 \
  STRUCTURIZR_API_KEY=key \
  STRUCTURIZR_API_SECRET=secret \
  ./gradlew run --args='--push'
```

You can view these secrets on the [dashboard](https://structurizr.com/dashboard), after clicking *Show more...* next to
the desired workspace.

## Generating images locally

Requires `plantuml` and `wget` to be installed (e.g. with `brew install plantuml wget`)

```
script/generate_images.sh
```

This command will locally generate all defined workspace diagrams without using the Structurizr web API.


[c4]: https://c4model.com/
[c4-abstractions]: https://c4model.com/#Abstractions
[structurizr]: https://structurizr.com/
