# Apply all resources
kubectl apply -f k8s-deployment.yaml
kubectl apply -f k8s-service.yaml
kubectl apply -f k8s-configmap.yaml
kubectl apply -f k8s-hpa.yaml
kubectl apply -f k8s-networkpolicy.yaml
kubectl apply -f k8s-pdb.yaml

# Or use kustomize
kubectl apply -k .

# Check deployment status
kubectl get pods -l app=wallet-service
kubectl get svc wallet-service
kubectl describe deployment wallet-service

# View logs
kubectl logs -l app=wallet-service -f

# Scale deployment
kubectl scale deployment wallet-service --replicas=5