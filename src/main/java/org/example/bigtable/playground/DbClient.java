package org.example.bigtable.playground;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class DbClient {
    // gcloud beta emulators bigtable start
    @Value("${bigtable.instanceId}")
    private String instanceId;

    @Value("${bigtable.tableId}")
    private String tableId;

    private final BigtableDataClient dataClient;
    private final BigtableTableAdminClient adminClient;

    public DbClient(BigtableTableAdminClient adminClient, BigtableDataClient dataClient) {
        this.adminClient = adminClient;
        this.dataClient = dataClient;
    }

    public void createTable() {
        if (adminClient.exists(this.tableId)) {
            log.info("Removing : {}", this.instanceId);
            this.adminClient.deleteTable(this.tableId);
        }

        List<String> columnFamilies = Lists.newArrayList("names", "contacts", "addresses");

        log.info("Creating table : {}", this.instanceId);
        CreateTableRequest createTableRequest = CreateTableRequest.of(tableId);
        for (String cf : columnFamilies) {
            createTableRequest.addFamily(cf);
        }
        adminClient.createTable(createTableRequest);
        log.info("Created table with the families: {}", columnFamilies);
    }

    public void writeData(final FakeData fakeData) {
        log.info("Writing data to {}", this.tableId);
        var rowMutation = RowMutation.create(this.tableId, fakeData.rowKey())
                .setCell("names", "lnam", fakeData.getLastName())
                .setCell("names", "fnam", fakeData.getFirstName());
        this.dataClient.mutateRow(rowMutation);
        log.info("Wrote : {}", fakeData);
    }

    public void readData(final FakeData fakeData) {
        var row = this.dataClient.readRow(this.tableId, fakeData.rowKey());
        row.getCells().forEach(r -> log.info("{} : {}", r.getQualifier().toStringUtf8(), r.getValue().toStringUtf8()));
    }
}
