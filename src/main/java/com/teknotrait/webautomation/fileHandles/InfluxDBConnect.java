package com.teknotrait.webautomation.fileHandles;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;

public class InfluxDBConnect {

    private static final String token = "YOUR-TOKEN-HERE";
    private static final String org = "Teknotrait";
    private static final String bucket = "Teknobucket";
    private static final String url = "http://localhost:8086"; // Local InfluxDB URL

    public static void main(String[] args) {
        InfluxDBClient client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);

        Point point = Point
                .measurement("temperature")
                .addTag("location", "server-room")
                .addField("value", 28.7)
                .time(Instant.now(), WritePrecision.NS);

        try {
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writePoint(point);
            System.out.println("✅ Data written to InfluxDB successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error writing data to InfluxDB: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
