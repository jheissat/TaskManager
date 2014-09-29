
package fr.julienheissat.modelController;

/**
 * Created by juju on 15/09/2014.
 */
public class Project
{

    private String projectID;
    private String projectName;
    private int projectColor;
    private String projectDescription;
    // protected List<Users> ProjectUsers;

    public Project(String name)
    {
        this.projectName = projectName;
    }

    public Project()
    {
    }

    @Override
    public String toString()
    {
        return projectName;
    }

    public String getProjectID()
    {
        return projectID;
    }

    public void setProjectID(String projectID)
    {
        this.projectID = projectID;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public int getProjectColor()
    {
        return projectColor;
    }

    public void setProjectColor(int projectColor)
    {
        this.projectColor = projectColor;
    }

    public String getProjectDescription()
    {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription)
    {
        this.projectDescription = projectDescription;
    }
}