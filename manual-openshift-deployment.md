# Manual OpenShift Deployment

1. Build the image locally while setting the right group+image+tag (update tag version as desired):

```sh
export APP_IMAGE_TAG=0.0.0-alpha.0
mvn package -Dquarkus.container-image.group=artifactory.karolinska.se/kar-vdp-karda-events-images-snapshot -Dquarkus.container-image.name=composition-generator-app -Dquarkus.container-image.tag=${APP_IMAGE_TAG}
```

2. Test that it seems to be working:

```sh
docker run --rm -p 8080:8080 artifactory.karolinska.se/kar-vdp-karda-events-images-snapshot/composition-generator-app:${APP_IMAGE_TAG}
```

Then open a browser to <http://localhost:8080>.

3. Push the locally-built image to Artifactory:

```sh
docker push artifactory.karolinska.se/kar-vdp-karda-events-images-snapshot/composition-generator-app:${APP_IMAGE_TAG}
```

4. Deploy the following resources to OpenShift in the desired namespace:

```yaml
---
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: composition-generator-app
  name: composition-generator-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: composition-generator-app
  template:
    metadata:
      labels:
        app: composition-generator-app
    spec:
      containers:
        - name: composition-generator-app
          image: artifactory.karolinska.se/kar-vdp-karda-events-images-snapshot/composition-generator-app:0.0.0-alpha.0
          ports:
            - containerPort: 8080
              name: http
              protocol: TCP
          livenessProbe:
            httpGet:
              path: /
              port: 8080
          readinessProbe:
            httpGet:
              path: /
              port: 8080
          startupProbe:
            httpGet:
              path: /
              port: 8080
          resources:
            limits:
              cpu: 500m
              memory: 1Gi
            requests:
              cpu: 50m
              memory: 256Mi
---
apiVersion: v1
kind: Service
metadata:
  name: composition-generator-app
spec:
  selector:
    app: composition-generator-app
  ports:
    - name: http
      protocol: TCP
      port: 80
      targetPort: 8080
---
apiVersion: route.openshift.io/v1
kind: Route
metadata:
  name: composition-generator-app
spec:
  host: openehr-composition-generator-dev.apps.tamarin.mta.karolinska.se
  port:
    targetPort: http
  tls:
    termination: edge
  to:
    kind: Service
    name: composition-generator-app
---
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: temp-allow-incoming-ingress-traffic
spec:
  podSelector: {}
  ingress:
    - from:
      - namespaceSelector:
          matchLabels:
            network.openshift.io/policy-group: ingress
  policyTypes:
    - Ingress
```
