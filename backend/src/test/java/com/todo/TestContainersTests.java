package com.todo;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class TestContainersTests extends AbstractTestcontainersUnitTests
{
	@Test
	public void canStartPostgresDB()
	{
		 //postgreSQLContainer.start();

		assertThat(postgreSQLContainer.isRunning()).isTrue();
		assertThat(postgreSQLContainer.isCreated()).isTrue();
	}
	

}