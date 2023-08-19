package org.example.InfluxDB;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;

public class InfluxDBWriter {
    InfluxDBClient influxDBClient;

    public InfluxDBWriter(InfluxDBConnector influxDBConnector) {
        this.influxDBClient = influxDBConnector.buildClient();
    }

    public void writePoint(Point point) {
        WriteApiBlocking writeApi = this.influxDBClient.getWriteApiBlocking();
        writeApi.writePoint(point);
    }
}
