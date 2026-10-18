# Java Smart Chat Application

A console-based multi-client chat application built using Core Java.

## Technologies

- Java 21
- OOP
- Socket Programming
- Multithreading
- JDBC
- MySQL
- Collections
- Exception Handling

## Features

- Multiple users can connect
- Username-based login
- Real-time messaging
- Online users list
- Private messaging
- Join/leave notifications
- Message history stored in MySQL
- User information stored in MySQL

## Commands

/users

Shows currently online users.

Example:

/users


/msg username message

Sends a private message.

Example:

/msg Rahul Hello Rahul!


/quit

Disconnects from the chat.

## How to Run

### 1. Start MySQL

Make sure MySQL is running.

### 2. Create database

Run:

database.sql

### 3. Add MySQL JDBC Driver

Add MySQL Connector/J to the project.

### 4. Configure Database

Open:

src/database/DatabaseConnection.java

Change:

private static final String USER = "root";
private static final String PASSWORD = "root";

according to your MySQL setup.

### 5. Compile

Compile all Java files.

### 6. Start Server

Run:

server.Server

### 7. Start Client

Open another terminal and run:

client.Client

Multiple clients can be started from multiple terminals.

## Architecture

Client
   |
   | Socket
   |
Server
   |
ClientHandler
   |
ChatManager
   |
MySQL Database
