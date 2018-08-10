/**
 * 
 */
package de.haproxy.test;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Johannes Hiemer.
 *
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
public abstract class MockMvcTest {
	
	protected MockMvc mockMvc;
	
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Value("${security.token.default}")
	protected String token;
	
	@Value("${security.token.name}")
	protected String tokenName;
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	@Before
    public final void initMockMvc() throws Exception {
		 mockMvc = webAppContextSetup(webApplicationContext)
	        		.apply(documentationConfiguration(this.restDocumentation)).build();
    }

}
