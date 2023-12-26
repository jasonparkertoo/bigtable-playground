package org.example.bigtable.playground;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private final DbClient dbClient;

    public Application(DbClient dbClient) {
        this.dbClient = dbClient;
    }

	@Override
	public void run(String... args) throws Exception {

		dbClient.createTable();

		var fakeData = new FakeData();
		fakeData.setFirstName("John");
		fakeData.setLastName("Doe");
		fakeData.setBirthday("11111995");
		fakeData.setUserId("1234ABCD");
		fakeData.setSystemId("EFGH5678");

		dbClient.writeData(fakeData);
		dbClient.readData(fakeData);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
