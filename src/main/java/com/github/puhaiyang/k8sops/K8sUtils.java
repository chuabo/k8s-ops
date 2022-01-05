package com.github.puhaiyang.k8sops;

import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class K8sUtils {
    public static KubernetesClient getClient() {
        final KubernetesClient client = new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl("http://192.168.127.142:8081").build());
        return client;
    }
}
