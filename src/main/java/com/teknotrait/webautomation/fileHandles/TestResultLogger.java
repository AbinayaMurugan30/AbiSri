package com.teknotrait.webautomation.fileHandles;


import com.influxdb.client.*;
import com.influxdb.client.write.Point;
import com.influxdb.client.domain.WritePrecision;

import java.time.Instant;

public class TestResultLogger {

    private static final String url = "http://localhost:8086";
    private static final String token = "8OT8Yk0KvP9fyN_p9ykP_Sojid8AkmzTOA7jiC3PbnXvZI-kOLK-H70ryVlUz9IsKmjpEr9b6pwbeSjbYYqfWA==";
    private static final String org = "Teknotrait";
    private static final String bucket = "Teknobucket";

    private static final InfluxDBClient client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);

    public static void logTestResult(String testName, String status, long duration, String errorMessage, String feature) {
        Point point = Point.measurement("test_results")
                .addTag("feature", feature)
                .addTag("status", status)
                .addField("testName", testName)
                .addField("duration", duration)
                .addField("errorMessage", errorMessage == null ? "" : errorMessage)
                .time(Instant.now(), WritePrecision.MS);
        System.out.println(" Line Protocol: " + point.toLineProtocol());


        try (WriteApi writeApi = client.getWriteApi()) {
            writeApi.writePoint(point);
            System.out.println("✅ Point sent to InfluxDB successfully.");
        }
    }

    public static void close() {
        client.close();
    }
}
