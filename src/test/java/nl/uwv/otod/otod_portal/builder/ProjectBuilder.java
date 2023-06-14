package nl.uwv.otod.otod_portal.builder;

import nl.uwv.otod.otod_portal.model.Project;

public class ProjectBuilder {

	private String name;
	private String description;
	private String budgetOwner;
	private String status;
	
	public ProjectBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public ProjectBuilder setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public ProjectBuilder setBudgetOwner(String budgetOwner) {
		this.budgetOwner = budgetOwner;
		return this;
	}
	
	public ProjectBuilder setStatus(String status) {
		this.status = status;
		return this;
	}
	
	public Project build() {
		Project project = new Project();
		project.setName(name);
		project.setDescription(description);
		project.setBudgetOwner(budgetOwner);
		project.setStatus(status);
		return project;
	}
}
