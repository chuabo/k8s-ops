package com.github.puhaiyang.k8sops;

import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class K8sUtils {
    @Value("${k8s.url}")
    private String url;

    private static String url_;

    @PostConstruct
    public void K8sUtils() {
        this.url_ = url;
    }

    public static KubernetesClient getClient() {
        final KubernetesClient client = new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl(url_).build());
        return client;
    }
}
