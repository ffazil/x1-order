package com.tracebucket.x1.order;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Base class to implement transactional integration tests using the root application configuration.
 *
 * @author ffazil
 * @since 04/03/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = X1OrderApplication.class)
@Transactional
public abstract class AbstractIntegrationTest {
}
