# SOFE3980U - Lab 3: CI/CD with Jenkins, Docker, and GKE

## Student Information
- **Name:** Ayngaraan Chandramohan
- **Student ID:** 100825774
- **Course:** SOFE 3980U - Introduction to Artificial Intelligence / Digital Systems

---

## 🎥 Project Deliverables (Video Links)

| Deliverable | Video Link |
| :--- | :--- |
| **Part 1: CI Pipeline & Custom Design** | [Insert YouTube/OneDrive Link Here] |
| **Part 2: CD Pipeline & GKE Deployment** | [Insert YouTube/OneDrive Link Here] |

---

## 🛠 Lab Overview
This project demonstrates a full **CI/CD Pipeline** for a Binary Calculator Web Application. The pipeline automates testing, building, containerizing, and deploying a Spring Boot application to a **Google Kubernetes Engine (GKE)** cluster using **Jenkins**.

### Technical Stack:
- **Java/Spring Boot:** Backend Logic
- **Maven:** Build Tool & Dependency Management
- **Docker:** Containerization
- **Google Artifact Registry:** Image Hosting
- **Google Kubernetes Engine (GKE):** Orchestration and Deployment
- **Jenkins:** Automation Server (CI/CD)

---

## 🚀 Discussion Questions & Answers

### 1. What are the benefits of using CI/CD in a cloud-native environment?
CI/CD provides rapid feedback loops. In a cloud-native setup (like GKE), it ensures that every code push is automatically validated through unit tests. By automating the "Containerize" and "Deployment" stages, we eliminate manual configuration errors and ensure that the version running in the cloud is always the latest "Green" build.

### 2. How does Kubernetes simplify the deployment process?
Kubernetes (GKE) acts as the orchestrator. Instead of manually managing Virtual Machines, we define a **Deployment** and a **Service**. GKE handles the scaling, health checks, and load balancing (via the `LoadBalancer` service type), providing a single External IP for users to access the application regardless of how many pods are running.

### 3. What is the role of the Jenkinsfile in this lab?
The `Jenkinsfile` (specifically `Jenkinsfile_v2`) serves as **Pipeline-as-Code**. It defines the exact sequence of events (Test -> Build -> Containerize -> Deploy). Because it is stored in the Git repository, the deployment logic is version-controlled, repeatable, and transparent to the entire development team.

### 4. Why did we use Google Artifact Registry?
Artifact Registry provides a secure, private location to store Docker images. By pushing our built images here, GKE can easily pull the latest version of our application using Google's internal high-speed network, ensuring fast and secure deployments.

---

## ⚙️ How to Run Locally
1. Clone the repository:
   ```bash
   git clone [https://github.com/Ayngaraan/SOFE3980U-Lab3-Part2.git](https://github.com/Ayngaraan/SOFE3980U-Lab3-Part2.git)
