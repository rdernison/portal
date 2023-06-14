package nl.uwv.otod.otod_portal.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.model.Project;

public class ProjectBuilderTest {

	@Test
	public void testBuildProject() {
		Project project = new ProjectBuilder().setBudgetOwner("John Doe")
				.setDescription("Description")
				.setName("Project 101")
				.build();
		
		assertEquals("Project 101", project.getName());
		assertEquals("Description", project.getDescription());
		assertEquals("John Doe", project.getBudgetOwner());
	}
}
