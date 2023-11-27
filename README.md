<div align="center">
<img src="https://github.com/CoJaques/Pongly/assets/91125307/a0c16e8b-e511-4e3a-815d-0627d94de3ea" width="100" />
<h1 align="center">

<br>PONGLY</h1>
<h3>◦ Level up the fun with Pongly!</h3>

<img src="https://img.shields.io/github/last-commit/CoJaques/Pongly?style=flat-square&color=5D6D7E" alt="git-last-commit" />
<img src="https://img.shields.io/github/commit-activity/m/CoJaques/Pongly?style=flat-square&color=5D6D7E" alt="GitHub commit activity" />
<img src="https://img.shields.io/github/languages/top/CoJaques/Pongly?style=flat-square&color=5D6D7E" alt="GitHub top language" />
</div>

---

## 📖 Table of Contents
- [📖 Table of Contents](#-table-of-contents)
- [📍 Overview](#-overview)
- [🚀 Getting Started](#-getting-started)
    - [🔧 Installation](#-installation)
    - [🤖 Running Pongly](#-running-Pongly)
- [🤝 Contributing](#-contributing)

---


## 📍 Overview

Pongly is a project that enables multiplayer Pong game. It includes both client and server components. The server component listens for client connections and assigns them to game parties. Each party consists of two players. The server uses multi-threading to handle multiple clients concurrently. The client component connects to the server and allows players to control their paddles in the game. It handles user input, updates the display, and communicates with the server to send/receive game data. The project provides a complete game solution for multiplayer Pong enthusiasts.

The protocol definition can be found here : https://github.com/CoJaques/Pongly/tree/main/ApplicationProtocolDiagram

## 🚀 Getting Started

### 🔧 Packaging

You can package the jar file directly by using the maven wrapper packaged into the project. If you use Intellij IDE, the packaging configuration is embedded into the project.

1. Clone the Pongly repository:
```sh
git clone https://github.com/CoJaques/Pongly
```

2. Change to the project directory:
```sh
cd Pongly
```

3. Build the client and the server:
```sh
# Download the dependencies
./mvnw dependency:resolve

# Package the application
./mvnw package
```

### 🤖 Running Pongly

To play the game, you need, at least, one server and 2 client. Go to the folder where the server and client jar are and use these command :

#### Note for windows user, use *javaw* in place of java

Options de la ligne de commande :

-H, --hostname : Specify hostname or ip adress of the server, default is localhost

-P, --port : specify the used port number, default is 1313

-T, --thread : Define max thread number for poolThread of the server

#### Server
```sh
java -jar server-1.0.jar [-P port] [-T nbThread]
```

#### Client
```sh
java -jar pongly-1.0.jar [-H hostname] [-P port]
```
---

#### Example

To run a server on the port 1300 with 12 threads use :

```sh
java -jar server-1.0.jar -P 1300 -T 12
```

To run a client which connect to the localhost adress using port 1300 :
```sh
java -jar pongly-1.0.jar -H localhost -P 1300
```

## 🤝 Contributing

Contributions are welcome! Here are several ways you can contribute:

- **[Join the Discussions](https://github.com/CoJaques/Pongly/discussions)**: Share your insights, provide feedback, or ask questions.
- **[Report Issues](https://github.com/CoJaques/Pongly/issues)**: Submit bugs found or log feature requests for COJAQUES.

