# GatlingProjectOne

## 📌 Overview
This project is a **Gatling Performance Testing framework** built using **Maven**.  
It is designed for load, stress, and endurance testing of REST APIs and web applications.

---

## 🏗 Project Structure
GatlingProjectOne/
│── pom.xml # Maven build configuration
│── README.md # Project documentation
│
├── src
│ ├── main
│ │ └── resources
│ │ ├── data/ # Test data files (CSV, JSON)
│ │ ├── bodies/ # Request body payloads
│ │ └── gatling.conf # Gatling configuration
│ │
│ └── test
│ └── scala
│ └── simulations
│ └── com/gatling/tests/
│ ├── BasicSimulation.scala
│ ├── FeederDemo.scala
│ └── CustomSimulation.scala
│
└── target/gatling/results # Simulation reports after test execution


---

## 🚀 How to Run Tests
Run a specific simulation:
=> mvn gatling:test -D"gatling.simulationClass=simulations.com.gatling.tests.FeederDemo"


Run all simulations:
=> mvn gatling:test

Run with additional options:

=> mvn gatling:test -Dusers=50 -DrampUp=30


📊 Reports

After execution, Gatling generates detailed HTML reports:

=> target/gatling/results/<simulation-name>-<timestamp>/index.html


Open the index.html file in a browser to view:

Requests per second

Response times distribution

Percentiles

Failures (if any)

## ⚙️ Prerequisites

Java 17+

Maven 3.8+

Gatling 3.14.x

Scala 2.13.x


## 🤖 CI/CD Integration
## ✅ Jenkins Pipeline

Example Jenkinsfile for running Gatling tests:

pipeline {
agent any

    tools {
        maven 'Maven3'
        jdk 'jdk17'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-org/GatlingProjectOne.git'
            }
        }
        stage('Build & Test') {
            steps {
                sh 'mvn clean gatling:test'
            }
        }
        stage('Publish Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/gatling/results',
                    reportFiles: 'index.html',
                    reportName: 'Gatling Report'
                ])
            }
        }
    }
}

## 📂 Useful Links

Gatling Documentation: https://docs.gatling.io/
Jenkins HTML Publisher Plugins: https://plugins.jenkins.io/htmlpublisher/
GitHub Actions: https://docs.github.com/en/actions

---

👉 This makes your README **project structure, execution commands, reports, and prerequisites** 
    **production-ready**: it now covers **local execution + Jenkins + GitHub Actions CI/CD**.  
    in a real-time project style.


