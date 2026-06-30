# outbreak-tracker

#### Build 
```shell
docker build -t health-hazard-tracker .
```

#### Run
```shell
docker run -d -p 8080:8080 --env-file .env health-hazard-tracker
```


#### Docker Hub 

Build & Push
```shell
docker build -t scot04100/health-hazard-tracker:latest .
```

```shell
docker push scot04100/health-hazard-tracker:latest
```


### Local Setup
We'll be running k8s locally, using minikube as the containerized cluster, 
and helm as the configuration manager. 

#### Start your local cluster
minikube start

#### Point your terminal's Docker client to minikube
eval $(minikube docker-env) && export DOCKER_API_VERSION="1.44"
alias minikube-env='eval $(minikube docker-env) && export DOCKER_API_VERSION="1.44"'
 
#### Build your image directly inside minikube
docker build -t health-hazard-tracker:latest .

#### Install Helm Chart

Spin up the pods using helm, in `local` namespace.
```shell
helm install health-hazard-tracker-local ./charts -n local --create-namespace
```
#### Kubectl Commands
View namespaces
`kubectl get namespaces`

View deployments within namespace
`kubectl get deployments -n=local`

View logs for the deployment 
`kubectl logs deployment/health-hazard-tracker-local-deployment --tail=20 -f -n=local`


kubectl logs deployment/health-hazard-tracker-local-deployment -n local -f

View all svcs within namespace 
`kubectl get svc -A`

View pods associated to a namespace
`kubectl get pods -n=local`

Port-forward requests from localhost:8080 to in pod port 80
`kubectl port-forward svc/health-hazard-tracker-local-service 8080:8080 -n=local`

### Helm 
To upgrade: 
`helm upgrade health-hazard-tracker-local ./charts -n local`

Check gateway health
`kubectl describe gateway/health-hazard-tracker-local-gateway -n local`

Check HTTPRoute health 
`kubectl describe httproute/health-hazard-tracker-local-route -n local`


https://doc.traefik.io/traefik/v3.7/getting-started/kubernetes/

Add CRDs

Add Traefik Helm Repo
helm repo add traefik https://traefik.github.io/charts
helm repo update

Add values yaml (check traefik-gateway-values.yaml)

Add Traefic Engine to k8s cluster ?
**helm install traefik traefik/traefik -f ./charts/traefik-gateway-values.yaml --wait**

kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.5.1/standard-install.yaml


# Step 1: Update your local Helm library to get the newest Traefik chart definitions
helm repo add traefik https://traefik.github.io/charts
helm repo update

# Step 2: Manually establish the Gateway API blueprints in your cluster
kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.5.1/standard-install.yaml
# Install Gateway API CRDs from the Standard channel.
kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.5.1/standard-install.yaml

# Step 3: Run your installation command
helm install traefik traefik/traefik -f ./charts/traefik-gateway-values.yaml --wait

### 2. Verify the Gateway API CRDs
Confirm that the required custom blueprints are fully registered with your cluster's API server:
```bash
kubectl get crds | grep 'gateway.networking.k8s.io'
```

**Expected Output:**
```text
gatewayclasses.gateway.networking.k8s.io
gateways.gateway.networking.k8s.io
httproutes.gateway.networking.k8s.io
referencegrants.gateway.networking.k8s.io
```

confirm traefik 

kubectl get gateway -n local


Apply and test code updates
helm upgrade health-hazard-tracker-local ./charts -n local
helm upgrade --install health-hazard-tracker-local ./charts -n local --set apiKeys.thirdPartyKey="$(grep FDA_API_KEY .env | cut -d '=' -f2)"
OR
helm upgrade health-hazard-tracker-local ./charts -n local --reuse-values

[ Laptop Browser / Curl ]
│  (Hits http://localhost:8080 or 127.0.0.1:8080)
▼
1. minikube tunnel (Funnels the host network traffic straight into the cluster)
   │
   ▼
2. Traefik LoadBalancer Service (Intercepts the packet at the cluster gate)
   │
   ▼
3. Traefik Engine / Gateway API (Evaluates your /api or /actuator path matches)
   │
   ▼
4. Your App's Kubernetes Service (Directs traffic to your internal application port)
   │
   ▼
5. Your Application Pods (Your code safely processes the incoming request!)


the Gateway API requires a Controller Engine (Traefik) to actually execute blueprints. 
You know that local clusters need a network tunnel to act as a cloud load balancer.


1. What are the "Blueprints"?In this scenario, the blueprints are the Custom Resource Definitions (CRDs) themselves.When you first install Kubernetes, it only natively understands basic core objects like a Pod, a Service, or a Deployment. It has absolutely no idea what a Gateway or an HTTPRoute is.By running the kubectl apply command from the official Kubernetes Gateway API repository, you are injecting the blueprints (CRDs) into the cluster. This extends the Kubernetes API, teaching it the structural definitions, schema rules, and fields for these new routing objects.Once those blueprints are installed, Kubernetes can successfully read, validate, and store your custom YAML files without throwing an "unrecognized resource type" error.

3. The Role of the Tunnel (Bridging the Host)Your understanding here is completely flawless.Even though you configured the Service, the HTTPRoute, the Gateway, and installed Traefik perfectly inside the cluster, the entire cluster is still trapped inside an isolated virtual machine sandbox (Minikube) on your laptop. Your host machine's browser or terminal has no native network route to get inside that sandbox.By running:bashminikube tunnel
   Use code with caution.You are explicitly telling Minikube to create a network bridge. It looks at your Traefik LoadBalancer service, allocates a local IP (like 127.0.0.1), and opens a routing highway. When you send a request to localhost or 127.0.0.1, the tunnel catches that packet on your laptop and safely drives it across the bridge into Traefik's front door inside the cluster.You have completely mapped out the entire cloud networking lifecycle from code to cluster.

You send a request to localhost:8080.Minikube Tunnel acts as the bridge, catching that request on your laptop and passing it across the network divide.Traefik is waiting at the gate on port 8080, intercepts the request, and reads your custom HTTPRoute paths.Your Configurations take over completely from there, routing the traffic directly to your running application pods.