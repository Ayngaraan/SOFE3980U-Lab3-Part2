# SOFE3980U - Lab 3: CI/CD with Jenkins, Docker, and GKE

## Student Information
- **Name:** Ayngaraan Chandramohan
- **Student ID:** 100825774
- **Course:** SOFE 3980U - Introduction to Artificial Intelligence / Digital Systems

---

## 🎥 Project Deliverables (Video Links)

| Deliverable | Video Link |
| :--- | :--- |
| **Part 1: CI Pipeline & Custom Design/Part 2: CD Pipeline & GKE Deployment** | https://drive.google.com/file/d/1QYbJ0zacgd-flcobo_RTPaEMKAygBp2v/view?usp=sharing |

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

## 🔒 The IAM Security Workaround (Deviation from Lab Manual)
During the implementation of this lab, I encountered a hard block regarding the generation of the `service_account.json` key file. 

**The Issue:**
Google Cloud recently updated its default security posture, enforcing an Organization Policy (`iam.disableServiceAccountKeyCreation`) that prevents the creation of JSON keys to stop accidental credential leaks. Because personal/student GCP accounts do not have an overarching "Organization" workspace, this policy cannot be disabled by the project owner.

**The Solution:**
Instead of relying on a downloaded JSON key for Jenkins to authenticate with GCP, I engineered a more secure, modern, "keyless" authentication method:
1.  **Native Node Identity:** Since Jenkins is deployed *inside* the GKE cluster, it inherits the identity of the cluster's default compute service account.
2.  **Direct IAM Binding:** I directly granted the necessary IAM roles (`artifactregistry.writer`, `container.admin`, `cloudbuild.builds.builder`, `storage.admin`, and `iam.serviceAccountUser`) to the GKE node's service account.
3.  **Infrastructure as Code Adjustment:** I modified the `Jenkinsfile_v2` pipeline script to remove all references to the `service_account` credential and the `gcloud auth activate-service-account` commands, allowing Jenkins to authenticate silently and securely via the node pool.

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

## 💻 Step-by-Step Deployment Commands
Below is the complete sequence of GCP Cloud Shell commands used to build the infrastructure, deploy Jenkins, apply the security workaround, and deploy the application.

### Phase 1: Infrastructure Provisioning
```bash
# 1. Create optimized GKE Cluster with required scopes
gcloud container clusters create sofe3980u \
  --num-nodes=2 \
  --machine-type=e2-standard-4 \
  --disk-size=50 \
  --zone=us-central1-a \
  --scopes=[https://www.googleapis.com/auth/cloud-platform](https://www.googleapis.com/auth/cloud-platform)

# 2. Create the Artifact Registry
gcloud artifacts repositories create sofe3980u \
  --repository-format=docker \
  --location=us-central1 \
  --description="Docker repository for CI/CD Pipeline"
# 3. Connect terminal to the cluster
gcloud container clusters get-credentials sofe3980u --zone=us-central1-a

# 4. Install Jenkins via Helm
helm repo add jenkinsci [https://charts.jenkins.io](https://charts.jenkins.io)
helm repo update
helm install cd-jenkins -f ~/SOFE3980U-Lab3-Part2/jenkins/values.yaml jenkinsci/jenkins --wait

# 5. Expose Jenkins directly on Port 80 to bypass local network restrictions
kubectl expose pod cd-jenkins-0 --type=LoadBalancer --name=jenkins-direct-access --port=80 --target-port=8080

# 6. Retrieve the Jenkins UI IP Address
kubectl get services jenkins-direct-access --watch
# 7. Enable Cloud Resource Manager API
gcloud services enable cloudresourcemanager.googleapis.com

# 8. Grant required permissions directly to the default GKE compute service account
PROJECT_ID=$(gcloud config get-value project)
NODE_SA="$(gcloud projects describe $PROJECT_ID --format='value(projectNumber)')-compute@developer.gserviceaccount.com"

gcloud projects add-iam-policy-binding $PROJECT_ID --member="serviceAccount:$NODE_SA" --role="roles/artifactregistry.writer"
gcloud projects add-iam-policy-binding $PROJECT_ID --member="serviceAccount:$NODE_SA" --role="roles/container.admin"
gcloud projects add-iam-policy-binding $PROJECT_ID --member="serviceAccount:$NODE_SA" --role="roles/cloudbuild.builds.builder"
gcloud projects add-iam-policy-binding $PROJECT_ID --member="serviceAccount:$NODE_SA" --role="roles/storage.admin"
gcloud projects add-iam-policy-binding $PROJECT_ID --member="serviceAccount:$NODE_SA" --role="roles/iam.serviceAccountUser"

# 9. Retrieve the live application IP address (Post-Jenkins Build)
kubectl get service binarycalculator-service --watch

# 10. Clean up resources to prevent unwanted billing
kubectl delete service binarycalculator-service
kubectl delete deployment binarycalculator-deployment
