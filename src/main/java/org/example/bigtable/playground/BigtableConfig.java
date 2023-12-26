package org.example.bigtable.playground;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
public class BigtableConfig {

    @Value("${bigtable.projectId}")
    private String projectId;

    @Value("${bigtable.instanceId}")
    private String instanceId;

    @Value("${bigtable.emulatorPort}")
    private int emulatorPort;

    @Bean
    @Profile("local")
    public BigtableDataClient dataClient() throws IOException {
        return BigtableDataClient.create(
                BigtableDataSettings.newBuilderForEmulator(emulatorPort)
                        .setProjectId(this.projectId)
                        .setInstanceId(this.instanceId)
                        .build()
        );
    }

    @Bean
    @Profile("local")
    public BigtableTableAdminClient adinClient() throws IOException {
        return BigtableTableAdminClient.create(
                BigtableTableAdminSettings.newBuilderForEmulator(this.emulatorPort)
                        .setProjectId(this.projectId)
                        .setInstanceId(this.instanceId)
                        .build()
        );
    }
}
