package com.example.associado;

import com.example.associado.constants.DatasetConstantsTest;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
public class ExampleTest extends Assertions {

	@BeforeEach
	void setup() {
		DatabaseHelperTest.getInstance()
			.execute(DatasetConstantsTest.ASSOCIADO_DATASET, DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	void batataTest() {
		assertTrue(true);
	}
}
