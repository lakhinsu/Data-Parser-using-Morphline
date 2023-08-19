package org.example.InfluxDB;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

public class InfluxDBConnector {
    private final String token;
    private final String url;
    private final String bucket;
    private final String org;

    public InfluxDBConnector(String token, String url, String bucket, String org) {
        this.token = token;
        this.url = url;
        this.bucket = bucket;
        this.org = org;
    }

    public String getUrl() {
        return this.url;
    }

    public String getBucket() {
        return this.bucket;
    }

    public String getOrg() {
        return this.org;
    }

    public InfluxDBClient buildClient() {
        return InfluxDBClientFactory.create(this.url, this.token.toCharArray(), this.org, this.bucket);
    }
}
