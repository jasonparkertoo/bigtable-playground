package org.example.bigtable.playground;

import lombok.Data;

@Data
public class FakeData {
    private String firstName;
    private String lastName;
    private String systemId;
    private String userId;
    private String birthday;

    public String rowKey() {
        String format = "%s#%s#%s";
        return String.format(format, this.systemId, this.userId, this.birthday);
    }
}
