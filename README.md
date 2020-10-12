![Publish][publish badge]
![Build and validate][build badge]

[publish badge]: https://github.com/ministryofjustice/laa-architecture-as-code/workflows/Publish/badge.svg
[build badge]: https://github.com/ministryofjustice/laa-architecture-as-code/workflows/Build%20and%20validate/badge.svg

# laa-architecture-as-code

Modelling architecture in Legal Aid Agency (LAA) with the [C4 model][c4] and [Structurizr][structurizr].

## Running locally

Requires `plantuml` and `wget` to be installed (e.g. with `brew install plantuml wget`)

```
script/generate_images.sh && open exports
```

This command will install all further dependencies locally and generate all defined workspace diagrams without using the
Structurizr web API.

[c4]: https://c4model.com/
[c4-abstractions]: https://c4model.com/#Abstractions
[structurizr]: https://structurizr.com/

## Running with gradle

The project is built with `gradle`.

| Action                                         | Command                                                         |
| ---------------------------------------------- | --------------------------------------------------------------- |
| Build the project                              | `./gradlew build`                                               |
| Create a local Structurizr workspace JSON file | `./gradlew run`                                                 |
| Push to the remote Structurizr workspace       | `./gradlew run --args='--push'`<br/>(see secrets section below) |

### :rotating_light: Remote-only changes will be lost

The remote workspace's content is _replaced_ with the content in this repository. Remote-only changes will be **lost**.

### Secrets

The `--push` command can be configured with these environment variables:

| Environment variable       | Meaning                                              |
| -------------------------- | ---------------------------------------------------- |
| `STRUCTURIZR_API_KEY`      | **Required** The API key for the Structurizr API.    |
| `STRUCTURIZR_API_SECRET`   | **Required** The API secret for the Structurizr API. |
| `STRUCTURIZR_WORKSPACE_ID` | Overrides the default workspace ID.                  |

You can sign up for a free account with Structurizr to create an empty workspace to test on. If you set up the
environment variables with they key, secret, and workspace ID from your account you can test on there, see the example
below.

Bear in mind you are restricted to 10 diagrams on a free account, you can get around this by commenting out systems in
`defineWorkspace.kt` that aren't relevant to what you're testing.

Example:
```
STRUCTURIZR_WORKSPACE_ID=12345 \
  STRUCTURIZR_API_KEY=key \
  STRUCTURIZR_API_SECRET=secret \
  ./gradlew run --args='--push'
```

You can view these secrets on the [dashboard](https://structurizr.com/dashboard), after clicking *Show more...* next to
the desired workspace.
