# INFO925_TP1_Dilisio_Caillet
Doodle-like with RabbitMQ<br>
jdk version : 1.8

##Dependances

Install rabbitMQ :
```bash
➜  ~ brew update && brew install rabbit-mq
```
(more details @ https://www.rabbitmq.com/download.html)

Install Maven :
```bash
➜  ~ brew install maven
```
(more details @ https://maven.apache.org/install.html)

## Installation

```bash
➜  ~ cd path/to/INFO925_T1_Dilisio_Caillet  
```

Create runnable jar:
```bash
➜  ~ mvn install
```
 
## Run

Before run rabbit-mq:
```bash
➜  ~ rabbitmq-server
```

Start agents :
```bash
➜  ~ java -jar target/INFO925_T1_Dilisio_Caillet-agent  
```
Start System Doodle:
```bash
➜  ~ java -jar target/INFO925_T1_Dilisio_Caillet-doodle  
```
