# Apply all resources
kubectl apply -k services/user-services/k8s/ -n microservices

# Check deployment status
kubectl rollout status deployment/user-services -n microservices

# Verify pods are running
kubectl get pods -n microservices -l app=user-services