# Nucleoid Backend

The Java rewrite of our [original Rust backend](https://github.com/NucleoidMC/nucleoid-backend), designed to incrementally replace our existing backend.

## Why are you going to Java, isn't Rust the Amazing New Thing

The main reason is that not many of us are Rust programmers, and since we're in the Minecraft modding community, Java is the go-to language, so we decided to switch to allow more people to work on it.

## Goals

- [ ] Shared library of types we can use with [nucleoid-extras](https://github.com/NucleoidMC/nucleoid-backend)
- [ ] New HTTP API version 2, with improvements from the original messy API
- [ ] Move the integrations connection to WebSocket
- [ ] Move some integration messages to use HTTP requests, which are more suitable for request-response flows like uploading statistics data.
- [ ] Move statistics into the Postgres database, so we only have one main database.
- [ ] Move performance counter tracking to something more suited and remove backend from being involved (Prometheus endpoint on each MC server?)
- [ ] Extra features on existing APIs (pagination)
- [ ] Implement some additional features that the current backend lacks

## Things to move over

- [ ] Leaderboards
  - [ ] Generation
  - [x] Listing
  - [ ] Player rankings
- [ ] Statistics
  - [ ] Move into postgres
  - [ ] Player-queries
  - [ ] Game-queries
  - [ ] Upload via http
  - [ ] Track info about games, even if no statistic values are recorded
- [ ] Integrations
  - [ ] Websocket 
  - [ ] Chat relay
  - [ ] Discord bot
  - [ ] Prometheus endpoint (more nucleoid-extras)

## New features

- [ ] "Stagedoor" (Internal admin pages/apis)
  - [ ] Log-in via GitHub?
  - [ ] Server-wide announcements for those not in the Discord
  - [ ] Leaving messages for specific users (private moderation warnings)
- [ ] Dockerise! 🐳
