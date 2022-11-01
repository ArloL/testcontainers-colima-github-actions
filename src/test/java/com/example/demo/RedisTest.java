package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import io.lettuce.core.RedisClient;

@Testcontainers
class RedisTest {

	@Container
	public GenericContainer<?> redis = new GenericContainer<>(
			DockerImageName.parse("redis:5.0.3-alpine")
	).withExposedPorts(6379);

	@Test
	void works() {
		RedisClient client = RedisClient.create(
				String.format(
						"redis://%s:%d/0",
						redis.getHost(),
						redis.getFirstMappedPort()
				)
		);
		var connection = client.connect().sync();
		String key = "test";
		String input = "example";
		connection.set(key, input);
		String actual = connection.get(key);
		assertEquals(input, actual);
	}

}
